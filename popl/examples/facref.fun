rec fac(n) =
  let val k = n in
  let val r = 1 in
  while k > 0 do
    (r := r * k; k := k - 1);
  r;;
