val main = new();;
val current = new();;

val yield(v) =
  callcc(lambda (k) 
    !current := k; throw(!main, v));;

val resume(p) =
  callcc(lambda (k)
    main := k; current := p; throw(!p, nil));;

val cocall(f) =
  let val p = new() in
  callcc(lambda (k1)
    callcc(lambda (k2) p := k2; throw(k1, nil)); f());
  p;;

val flatten(t0) =
  let rec flat(t) =
    if integer(t) then yield(t)
    else if t = nil then nil
    else (flat(head(t)); flat(tail(t))) in
  flat(t0); yield(nil);;

val fringe(t) =
  let val p = cocall(lambda () flatten(t)) in
  let rec loop() =
    let val v = resume(p) in
    if v = nil then nil
    else v : loop() in
  loop();;

val samefringe(t1, t2) =
  let val p1 = cocall(lambda () flatten(t1)) in
  let val p2 = cocall(lambda () flatten(t2)) in
  let rec loop() =
    let val v1 = resume(p1) in
    let val v2 = resume(p2) in
    print(list(v1, v2));
    if v1 <> v2 then false
    else if v1 = nil then true
    else loop() in
  loop();;

val xxx = list(3, list(1, 4), list(1), 5, list(9, list(2), 6));;
val yyy = list(list(3, 1), list(4, 1), 5, 9, list(2, 6));;
val zzz = list(list(3), list(1, 4), list(5, list(1)), list(9, 2, list(6, 5)));;
