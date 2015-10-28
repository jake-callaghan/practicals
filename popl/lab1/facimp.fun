-- Factorial implemented imperatively
val fac(n) =
  let val k = new() in
  let val r = new() in
  k := n; r := 1;
  while !k > 0 do
    (r := !r * !k; k := !k - 1);
  !r;;
