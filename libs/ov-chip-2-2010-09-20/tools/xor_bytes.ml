
while true do
  let line = read_line() in
  let akku = ref 0 in
  let i = ref 0
  in
    while !i + 2 <= String.length line do
      let x = int_of_string("0x" ^ (String.sub line !i 2)) 
      in
	(* Printf.printf "got 0x%02X\n" x; *)
	akku := !akku lxor x;
	i := !i + 3	  
    done;
    Printf.printf "XOR 0x%02X\n" !akku;
done

	
