

let classdir = ref ""

let directory package =
  if !classdir = "" 
  then package
  else !classdir ^ "/" ^ package

let arguments = Arg.align [
  ("-classdir", Arg.Set_string classdir,
   "dir same as -classdir for the converter");
]

let anon_fun _ = raise (Arg.Bad "invalid argument")

let no_error_completion = ref false;

;;

Arg.parse arguments anon_fun "usage: converter ... | converter_errors ...";
try
  while(true) do
    let l = read_line()
    in
      if (String.sub l 0 (min (String.length l) 34)) 
	                           = "conversion completed with 0 errors"
      then
	no_error_completion := true;
      try
	Scanf.sscanf l "error: line %d: %s@.%s@: %n"
	  (fun line_number package file count ->
	     Printf.printf "%s/%s.java:%d: %s\n"
	       (directory package) file line_number
	       (String.sub l (count -1) (String.length l - count + 1)));
      with
	| _ -> Printf.printf "%s\n" l
  done
with
  | End_of_file -> ()
;;
if !no_error_completion then exit 0 else exit 1
