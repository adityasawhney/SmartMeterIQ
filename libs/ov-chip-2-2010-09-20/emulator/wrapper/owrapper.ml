(* 
 * OV-chip 2.0 project
 * 
 * Digital Security (DS) group at Radboud Universiteit Nijmegen
 * 
 * Copyright (C) 2008, 2009
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License in file COPYING in this or one of the
 * parent directories for more details.
 * 
 * Created 9.10.08 by Hendrik
 * 
 * wrap jcre/jcwde emulators to keep them alive over several apdutool sessions
 * 
 * $Id: owrapper.ml,v 1.4 2009-02-20 15:29:23 tews Exp $
 *)


open Jckit_path

module U = Unix


type emulator_type =
  | CREF
  | JCWDE

let emulator_executable = function
  | CREF -> "cref"
  | JCWDE -> "jcwde"


let verbose = ref false

let default_emulator = JCWDE
let emulator = ref default_emulator

let default_server_port = 9025
let default_emulator_port = 9010

let server_port = ref default_server_port
let emulator_port = ref default_emulator_port

let emulator_classpath = ref ""

let eeprom_input = ref ""
let eeprom_output = ref ""

let jcwde_config = ref ""


let the = function
  | None -> assert false
  | Some x -> x


let rec print_bytes_rec buf len index line_count =
  if index = len then ()
  else begin
    Printf.printf "%02X " (int_of_char buf.[index]);
    if line_count > 73 && index + 1 < len then 
      begin
	print_string "\n    ";
	print_bytes_rec buf len (index+1) 4
      end
    else
      print_bytes_rec buf len (index+1) (line_count+3)
  end

let print_bytes id buf len = 
  print_string id;
  print_string ": ";
  print_bytes_rec buf len 0 (String.length id + 2);
  print_char '\n';
  flush stdout


let emulator_command() =
  let command = 
    Filename.concat 
      (Filename.concat !jckit_path "bin") 
      (emulator_executable !emulator)
  in
  let command = command ^ " -p " ^ (string_of_int !emulator_port) in
  let command = match !emulator with
    | JCWDE -> command
    | CREF ->
	command ^
	  (if !eeprom_input = "" then "" else
	     " -i " ^ !eeprom_input) ^
	  (if !eeprom_output = "" then "" else
	     " -o " ^ !eeprom_output)
  in
  let command = match !emulator with
    | CREF -> command
    | JCWDE -> command ^ " " ^ !jcwde_config
  in
    command


let emulator_environment() = match !emulator with
  | CREF -> U.environment()
  | JCWDE ->
      if !emulator_classpath = "" then U.environment()
      else
	let env = U.environment() in
	let len = Array.length env in
	let new_env = Array.create (len + 1) ""
	in
	  Array.blit env 0 new_env 0 len;
	  new_env.(len) <- "CLASSPATH=" ^ !emulator_classpath;
	  new_env


let start_emulator() = 
  let command = emulator_command() in
  let env = emulator_environment() 
  in
    Printf.printf "Start emulator\n  %s\n" command;
    U.open_process_full command env


let copy_len = 4096
let copy_buf = String.create copy_len

exception Copy_eof
exception Copy_source_empty

exception Cleanup

let copy_channel id inc ouc eof_flag print_raw_bytes =
  try
    (* 
     * if id <> None then
     *   Printf.printf "Data ready on %s\n%!" (the id);
     *)
    while true do
      let len = 
	try
	  input inc copy_buf 0 copy_len
	with
	  | Sys_blocked_io -> raise Copy_source_empty
	  | Sys_error("Connection reset by peer") -> 
	      Printf.printf 
		"%s connection reset by peer\n%!"
		(match id with
		   | None -> "??"
		   | Some x -> x);
	      raise Copy_eof
      in
	if len = 0 then begin
	  if id <> None && !verbose then
	    Printf.printf "%s EOF\n%!" (the id);
	  raise Copy_eof;
	end;
	if id <> None && !verbose then
	  if print_raw_bytes then
	    print_bytes (the id) copy_buf len
	  else
	    Printf.printf "%s pipe %d bytes\n%!" (the id) len;
	output ouc copy_buf 0 len;
	flush ouc
    done
  with
    | Copy_eof -> eof_flag := true
    | Copy_source_empty -> ()


let powerup_command = "\xF0\x00\x00\xF0"

let power_up_emulator emulator_addr emulator_port =
  let _ = U.select [] [] [] 0.2 in
  let _ = 
    if !verbose then 
      Printf.printf "connect to emulator\n%!" 
  in
  let (emulator_out, emulator_in) = 
    try
      U.open_connection emulator_addr 
    with
       | U.Unix_error(error, msg1, msg2) ->
	   Printf.eprintf 
	     "Connecting to emulator at port %d failed: %s %s: %s\n" 
	     emulator_port
	     msg1 msg2 (U.error_message error);
	   raise Cleanup
  in
  let atr_max_len = 200 in
  let atr = String.create atr_max_len
  in
    if !verbose then
      Printf.printf "send powerup to emulator\n%!";
    output emulator_in powerup_command 0 (String.length powerup_command);
    flush emulator_in;
    let len = input emulator_out atr 0 atr_max_len
    in
      if len = atr_max_len then begin
	Printf.eprintf "Received %d bytes ATR, something is wrong\n%!" len;
	exit 2;
      end;
      if !verbose then
	print_bytes "Received ATR" atr len;
      (emulator_out, emulator_in, String.sub atr 0 len)
    

let client_powerup client_out client_in atr =
  let buf_len = (String.length powerup_command + 1) in
  let buf = String.create buf_len in
  let _ = 
    if !verbose then 
      Printf.printf "Wait for powerup from client\n%!" 
  in
  let len = input client_out buf 0 buf_len
  in
    if len != buf_len -1 then begin
      Printf.eprintf "Client powerup with wrong length (%d)\n%!" len;
      false
    end
    else if String.sub buf 0 (buf_len -1) = powerup_command 
    then 
      begin
	output client_in atr 0 (String.length atr);
	flush client_in;
	if !verbose then
	  Printf.printf "Client powerup successful\n%!";
	true;
      end
    else begin
      Printf.eprintf "Powerup command not recognized\n%!";
      false
    end


let client_loop emulator_stdout emulator_stdout_fd emulator_stdout_eof
    emulator_stderr emulator_stderr_fd emulator_stderr_eof
    server_socket_fd
    emulator_out emulator_in emulator_socket_fd emulator_out_eof
    atr =
  let _ = assert(not !emulator_out_eof) in
  let _ = Printf.printf "Connecting to new client\n%!" in
  let (client_socket_fd, _) = U.accept server_socket_fd in
  let client_out = U.in_channel_of_descr client_socket_fd in
  let client_in = U.out_channel_of_descr client_socket_fd in
  let client_out_eof = ref false in
  let read_descriptors = 
    [emulator_stdout_fd; emulator_stderr_fd; 
     client_socket_fd; emulator_socket_fd] 
  in
  let stay_in_loop = ref true
  in
    if client_powerup client_out client_in atr 
    then 
      begin
	U.set_nonblock client_socket_fd;
	while !stay_in_loop do 
	  let (in_ready, _, _) = U.select read_descriptors [] [] (-1.0)
	  in
    	    if List.mem emulator_stdout_fd in_ready 
    	    then 
    	      copy_channel None emulator_stdout stdout emulator_stdout_eof 
		false;

    	    if List.mem emulator_stderr_fd in_ready
    	    then
    	      copy_channel None emulator_stderr stderr emulator_stderr_eof
		false;

	    if List.mem emulator_socket_fd in_ready
	    then begin
	      copy_channel (Some "EMULATOR") emulator_out client_in 
		emulator_out_eof true;
	      if !emulator_out_eof && !verbose then
		Printf.printf "\nEmulator socket closed\n";
	    end;

	    if (List.mem client_socket_fd in_ready) &&
	      (not !emulator_stdout_eof) && (not !emulator_stderr_eof) &&
	      (not !emulator_out_eof)
	    then
	      copy_channel (Some "CLIENT") client_out emulator_in 
		client_out_eof true;

	    if !emulator_stdout_eof && !emulator_stderr_eof &&
	      !emulator_out_eof
	    then begin
	      if !verbose then
		Printf.printf "\nEmulator died\n%!";
	      stay_in_loop := false;
	    end;

	    if !client_out_eof then begin
	      Printf.printf "Client disconnected\n%!";
	      stay_in_loop := false;
	    end
	done;
      end;
    if !verbose then
      Printf.printf "Close client connection\n%!";
    (try
       U.shutdown client_socket_fd U.SHUTDOWN_ALL;
     with
	 (* 59: Transport endpoint is not connected, apparently happens if
	  * the other side dies.
	  *)
       | U.Unix_error(U.ENOTCONN,_,_) -> ()
    );
    U.close client_socket_fd
	


(* wrapper_loop runs in an infinite recursion. 
 * Make sure it is tail-recursive
 *)
let rec wrapper_loop server_socket_fd emulator_addr emulator_port =
  let (emulator_stdout, emulator_stdin, emulator_stderr) as emulator_channels = 
    start_emulator() 
  in
  let (emulator_out, emulator_in, atr) = 
    try
      power_up_emulator emulator_addr emulator_port
    with
      | Cleanup as ex -> 
	  ignore(U.close_process_full emulator_channels);
	  raise ex
  in
  let emulator_socket_fd = U.descr_of_in_channel emulator_out in
  let _ = assert((U.descr_of_out_channel emulator_in) = emulator_socket_fd) in
  let emulator_out_eof = ref false in
  let restart_emulator() =
    ignore(U.close_process_full emulator_channels);
    U.shutdown_connection emulator_out;
    (* U.shutdown emulator_socket_fd U.SHUTDOWN_ALL; *)
    U.close emulator_socket_fd;
    Printf.printf "\n\nEmulator terminated\n%!";
    wrapper_loop server_socket_fd emulator_addr emulator_port
  in
  let emulator_stdout_fd = U.descr_of_in_channel emulator_stdout in
  let emulator_stderr_fd = U.descr_of_in_channel emulator_stderr in
  let emulator_stdout_eof = ref false in
  let emulator_stderr_eof = ref false in
  let read_descriptors = 
    [emulator_stdout_fd; emulator_stderr_fd; server_socket_fd] 
  in
    U.set_nonblock emulator_stdout_fd;
    U.set_nonblock emulator_stderr_fd;
    U.set_nonblock emulator_socket_fd;
    while true do 
      Printf.printf "\nWaiting for clients\n%!";
      let (in_ready, _, _) = U.select read_descriptors [] [] (-1.0)
      in
    	if List.mem emulator_stdout_fd in_ready 
    	then 
    	  copy_channel None emulator_stdout stdout emulator_stdout_eof false;
    	if List.mem emulator_stderr_fd in_ready
    	then
    	  copy_channel None emulator_stderr stderr emulator_stderr_eof false;
	if !emulator_stdout_eof && !emulator_stderr_eof 
	then 
	  restart_emulator()
	else begin
    	  if (List.mem server_socket_fd in_ready) && (not !emulator_out_eof)
	  then begin
    	    client_loop 
	      emulator_stdout emulator_stdout_fd emulator_stdout_eof
	      emulator_stderr emulator_stderr_fd emulator_stderr_eof
	      server_socket_fd
	      emulator_out emulator_in emulator_socket_fd emulator_out_eof
	      atr;
	    if !verbose then
	      Printf.printf "Client loop finished\n%!";
	  end;
	  if !emulator_stdout_eof || !emulator_stderr_eof || !emulator_out_eof
	  then begin
	    if !verbose then
	      Printf.printf "EOF on one emulator channel, begin restart\n%!";
	    if not !emulator_stdout_eof then
    	      copy_channel (Some "STDOUT") emulator_stdout stdout 
		emulator_stdout_eof false;
	    if not !emulator_stderr_eof then
    	      copy_channel (Some "STDERR") emulator_stderr stderr 
		emulator_stderr_eof false;
	    restart_emulator()
	  end;
	end	
    done




let setup_server_socket server_port =
  let server_addr = U.ADDR_INET(U.inet_addr_any, server_port) in
  let socket_fd = U.socket (U.domain_of_sockaddr server_addr) U.SOCK_STREAM 0 
  in
    (try
       U.setsockopt socket_fd U.SO_REUSEADDR true;
       U.bind socket_fd server_addr;
       U.listen socket_fd 1;
     with
       | U.Unix_error(error, msg1, msg2) ->
	   Printf.eprintf "Starting server at port %d failed: %s %s: %s\n" 
	     server_port
	     msg1 msg2 (U.error_message error);
	   exit 3;
    );
    socket_fd



let arguments = Arg.align
  [("-s", Arg.Set_int server_port,
    (Printf.sprintf "p set server port to listen on [default %d]" 
       default_server_port));
   ("-e", Arg.Set_int emulator_port,
    (Printf.sprintf "p set emulator port to forward to [default %d]"
       default_emulator_port));
   ("-cp", Arg.Set_string emulator_classpath,
    "classpath set CLASSPATH environment variable for emulator");
   ("-cref", Arg.Unit (fun () -> emulator := CREF),
    (" use cref" ^
       if default_emulator = CREF then " [default]" else ""));
   ("-jcwde", Arg.Unit (fun () -> emulator := JCWDE),
    (" use jcwde" ^
       if default_emulator = JCWDE then " [default]" else ""));
   ("-i", Arg.Set_string eeprom_input,
    "eeprom_in set input eeprom for cref");
   ("-o", Arg.Set_string eeprom_output,
    "eeprom_out set output eeprom for cref");
   ("-jckit", Arg.Set_string jckit_path,
    "path set the path for the jckit (such that path/bin/jcwde exists)");
   ("-v", Arg.Set verbose,
    " more verbose");
  ]

let anon_fun x =
  if !jcwde_config = "" then
    jcwde_config := x
  else
    raise (Arg.Bad (Printf.sprintf "Unexpected argument %s" x))


let main() =
  Arg.parse arguments anon_fun "owrapper [options] [jcwde_config]";
  if !emulator = JCWDE && !jcwde_config = "" then begin
    prerr_endline "Emulator jcwde requires a config file.";
    exit 1;
  end;
  let emulator_addr = U.ADDR_INET(U.inet_addr_loopback, !emulator_port) in
  let server_socket_fd = setup_server_socket !server_port
  in
    Printf.printf 
      ("\nowrapper (pid %d): listening on port %d running emulator " ^^
	 "at port %d\n%!")
      (U.getpid())
      !server_port !emulator_port;
    if !verbose then
      Printf.printf "Using java card kit at %s\n" !jckit_path;
    wrapper_loop server_socket_fd emulator_addr !emulator_port   
;;

try
  main()
with
  | Cleanup -> 
      exit 3
  | U.Unix_error(error, msg1, msg2) as ex ->
      Printf.eprintf 
	"Unix error: %s %s: %s\n" 
	msg1 msg2 (U.error_message error);
      raise ex

