module U = Unix

let rec print_bytes_rec buf len index line_count =
  if index = len then ()
  else begin
    Printf.printf "%02X " (int_of_char buf.[index]);
    if line_count > 23 then 
      begin
	print_char '\n';
	print_bytes_rec buf len (index+1) 0
      end
    else
      print_bytes_rec buf len (index+1) (line_count+1)
  end

let print_bytes buf len = 
  print_bytes_rec buf len 0 0;
  print_char '\n';
  flush stdout

let buf_length = 16000

let dump_connection client_addr inc outc =
  let _ = Printf.printf "PID %d new connection\n%!" (U.getpid()) in
  let inc_descr = U.descr_of_in_channel inc in
  let (oinc, ooutc) = match client_addr with
    | None -> (None, None)
    | Some addr -> 
	Printf.printf "Connect to other server...%!";
	let (i, o) = U.open_connection addr
	in
	  Printf.printf "connected\n%!";
	  (Some i, Some o)
  in
  let inc_tuple = (inc_descr, (inc, ooutc, "Client")) in
  let oinc_tuple = match oinc with
    | None -> (U.stdout, (stdin, None, "XXX"))
    | Some oinc -> (U.descr_of_in_channel oinc, (oinc, Some outc, "Server"))
  in
  let descr_assoc = match oinc with
    | None -> [inc_tuple] 
    | Some _ -> [inc_tuple; oinc_tuple] 
  in
  let read_selectors = List.map fst descr_assoc in
  let buf = String.create buf_length in
  let connected = ref true
  in
    while !connected do
      let (ready, _, _) = U.select read_selectors [] [] (-1.0) 
      in
	List.iter
	  (fun fd ->
	     let (fd_in, fd_out_opt, name) = List.assoc fd descr_assoc in
	     let len = input fd_in buf 0 buf_length
	     in
	       if len = buf_length then
		 Printf.printf "%s input filled buffer!!!\n%!" name;
	       if len > 0 then 
		 begin
		   Printf.printf "%s %d bytes\n" name len;
		   print_bytes buf len;
		   match fd_out_opt with
		     | None -> ()
		     | Some oc ->
			 output oc buf 0 len;
			 flush oc
		 end
	       else begin
		 Printf.printf "%s connection closed\n%!" name;
		 connected := false
	       end
	  )
	  ready
    done;
    close_in inc; close_out outc;
    (match oinc with
       | None -> () 
       | Some ic -> close_in ic
    );
    (match ooutc with
       | None -> ()
       | Some oc -> close_out oc
    )


let default_server_port = 9025
let server_port = ref default_server_port
let client_port = ref 0


let arguments = Arg.align
  [("-server-port", Arg.Set_int server_port,
    (Printf.sprintf "p set port to listen on [default %d]" 
       default_server_port));
   ("-port", Arg.Set_int client_port,
    "p set port to forward traffic to");
  ]

let anon_fun x =
  raise (Arg.Bad (Printf.sprintf "Unexpected argument %s" x))

let main() =
  Arg.parse arguments anon_fun "port_dump";
  let server_addr = U.ADDR_INET(U.inet_addr_any, !server_port) in
  let client_addr = 
    if !client_port = 0 then None
    else Some(U.ADDR_INET(U.inet_addr_loopback, !client_port))
  in
    Printf.printf "port_dump: starting server at port %d\n%!" 
      !server_port;
    Unix.establish_server (dump_connection client_addr) server_addr
;;

main()
