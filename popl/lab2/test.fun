val x = new();; x := 1;;
val doubleX() = x := 2 * !x; !x;;
val foo1(f) = !x;;
val foo2(f) = f; f; f; !x;;

-- foo1(doubleX()) will return 1 as the argument doubleX is not evaluated at all.
-- foo2(doubleX()) will return 8, as doubleX is evaluated each time the argument is mentioned.

-- Jensen's device

val sum(i, a, b, f) =
  let val s = new() in
  i := a; s := 0;
  while !i < b do (s := !s + f(); i := !i + 1);
  !s;;

val go() =
  let val i = new() in
  sum(i, 0, 10, lambda() !i * !i);;

-- go() returns 285
