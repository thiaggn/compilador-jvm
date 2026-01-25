bool k := 5 < 4;

main:
	push	5
	push	4
	iflt	.L0:
	push	0

.L0:
	push	1
	goto	.L1

.L1:
	store	%k