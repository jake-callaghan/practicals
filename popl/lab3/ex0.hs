rec f(n) =
  let val x = n in
  loop (
    x := x+2;
    if x > 3 then exit else nil;
    x := x+3
  );
  x;;