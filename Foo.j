.source Foo.java
.class public Foo
.super java/lang/Object

.method public <init>()V
	.limit stack 1
	.limit locals 1
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 4

	ldc 10.0
	fstore_1

	bipush 11
	istore_2

	fload_1
	ldc 10.0
	fadd

	iload_2
	i2f

	fconst_1
	fload_1
	fdiv
	fmul
	fadd
	fstore_3

	getstatic java/lang/System/out Ljava/io/PrintStream;
	fload_3
	invokevirtual java/io/PrintStream/println(F)V
	return
.end method


# com apelidos
%a -> 1
%b -> 2
%c -> 3

pushf    10.0
storef   %a

pushi    1
storei   %b

loadi    %a
pushi    10.0
addi

loadi    %b
i2f

pushf    1.0
loadf    %a
divf
mulf
addf
storef   %c

get      stream
load     %c
call     println