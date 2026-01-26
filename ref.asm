int k := 10;

bool a := k < 2 || (k < 3 || k < 4);

; Compilador java
main:
	push	10
	storei	%k

	loadi	%k
	push	2
	iflt    .L0

	loadi	%k
	push	3
	iflt    .L0

	loadi	%k
	push	4
	ifge	.L2

.L0:
	push	1
	goto   .L3

.L2:
	push	0

.L3:
	storei	%a
	return