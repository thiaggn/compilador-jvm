### Uso
O uso é `./compilar <nome_do_arquivo> [--arvore]`. A flag `--arvore` vai exibir a árvore sintática para facilitar a depuração.
```bash
./compilar Entrada.txt --arvore
```

Se o PowerShell não deixar executar o script, é só habilitar a execução para o usuário local (você) no terminal:
```bash
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
```
Ou temporariamente:
```bash
Set-ExecutionPolicy Bypass -Scope Process
```
### Declaração de variáveis:

São formas válidas de declarar variáveis:
```javascript
// Variável não-redeclarável
float a := 10.0;

// Variável redeclarável (o tipo é inferido)
x := "nome";

int tamanho = tam(x);
exibe x + a; // 14.0
```

Variáveis redeclaráveis podem ser declaradas novamente com outro tipo, desde que a redeclaração ocorra no mesmo escopo.

```javascript
x := 10;
x := "dez";
exibe x;  // "dez"
```
Caso a declaração aconteça em um escopo diferente, trata-se de uma nova declaração local.
```javascript
x := 10;
if (x < 10) then {
	x := "dez";
	exibe x; // "dez"
}
exibe x; // 10
```
Variáveis primitivas não são tratadas como tipos distintos entre si; nesses casos, ocorre uma conversão implícita. Na prática, apenas `strings` e os tipos primitivos são considerados tipos diferentes.

```javascript
y := 10;
if (y < 5) then {
	y := 5.0; // 5.0 é rebaixado para inteiro
}
exibe y; // 5
```
Variáveis explicitamente declaradas com seu tipo não podem ser redeclaradas.
```javascript
int idade := 22;
idade := "50"; // erro semântico: 'idade' não permite redeclaração com outro tipo.
```

### Laço for
A inicialização deve, obrigatoriamente, ser uma declaração de variável. Existem algumas peculiaridades:
- a declaração tem a mesma forma de uma declaração dinâmica, mas é estática;
- é declarada no escopo do laço (não redeclara ou modifica variáveis externas com o mesmo nome);
- o valor inicial pode ser uma variável no escopo externo.

A expressão condicional deve, obrigatoriamente, avaliar para um tipo primitivo.

A expressão iterativa não possui restrições adicionais, além de ser uma expressão. 

```javascript
for (i := 0; i < 10; i++) {
	// itera 10 vezes
}
```
```javascript
i := 10;
for (i := 6; i < 10; i++) {
	// itera 4 vezes
}
exibe i; // 10
```
```javascript
k := 5;
for (i := k; i < 6; i++) {
	// itera 1 vez
}
```
```javascript
i := 4;
for (i := i; i < 6; i++) { // 'i < 6' e 'i++' se referem ao 'i' no escopo do laço.
	// itera 2 vezes
}
exibe i; // 4
```
```javascript
i := 5;
for (i := 0; i < 10; i++) {
	i++; // incrementa o 'i' no escopo do laço
}
exibe i; // 5
```
```javascript
for (i := 10; 0; 0) { 
	// não itera nenhuma vez
}
```
```javascript
for (i := 10; 100; i--) { 
	// loop infinito
}
```

### Laço while
A expressão condicional do laço deve resolver para um primitivo (`int`, `float`, `double`, etc.);
```javascript
while (10) do {
	// loop infinito
}

while("ok") do { // erro

}
```

### Condicionais
A expressão condicional dos `if` deve resolver para um tipo primitivo.
```javascript
int ovos := 1;

if (ovos == 1) then {
	exibe "darling, you shall take 2 eggs";
	ovos := 2;
} 
else if (ovos > 1) then {
	exibe "fatso";
	ovos := 0;
} 
else {
	ovos := 0;
}
```

### Coerção de tipos
Expressões são promovidas ou rebaixadas automaticamente. A prioridade é `double`, `float`, `long`, `int`, `short`, nessa ordem. A coerção ocorre da esquerda pra direita. O número de conversões pode variar a depender da posição das expressões. 

Não é possível fazer coerção explícita. A notação a seguir é apenas um indicador visual.
```javascript
// a = float(10)
float a := 10; 

// b = int(10.0)
int b := 10.0;

// c = 1 + 2 + 3 + 4.0
// c = (((1 + 2) + 3) + 4.0)
// c = (float((1 + 2) + 3)) + 4.0 
// c = 10.0
float c := 1 + 2 + 3 + 4.0;

// d = 4.0 + 1 + 2 + 3
// d = (((4.0 + 1) + 2) + 3)
// d = (((4.0 + float(1)) + float(2)) + float(3)) 
// d = 10.0
float d := 4.0 + 1 + 2 + 3;

// e = 4.0 + 1 + 2 + 3
// e = (((4.0 + 1) + 2) + 3)
// e = double((((4.0 + float(1)) + float(2)) + float(3))) 
// e = 10.0
double e := 4.0 + 1 + 2 + 3;

k := 10.0;
recebe_um_inteiro(k);
```

### Tipos
`bool` é um apelido para `int` e `char` é um apelido para `short`. Os demais tipos realmente são oq são.