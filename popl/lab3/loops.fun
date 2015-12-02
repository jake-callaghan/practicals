rec f(n) = 
	let val x = n in 
	loop (
		x := x+2;
		if x > 3 then exit else Nil;
		x := x+3;
	);
	x;;

rec loop0() = let rec f() = exit in loop f();;
rec loop1() = loop (let rec f() = exit in f());;
rec loop2() = loop (let rec f() = exit in loop f());;