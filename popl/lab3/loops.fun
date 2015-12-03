rec loop0() = let rec f() = exit in loop f();;
rec loop1() = loop (let rec f() = exit in f());;
rec loop2() = loop (let rec f() = exit in loop f());;
