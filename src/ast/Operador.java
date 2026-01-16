package ast;

public enum Operador {
	Mais("+"),
	Menos("-"),
	Mul("*"),
	Div("/"),
	Maior(">"),
	MaiorIg(">="),
	Menor("<"),
	MenorIg("<="),
	Igual("=="),
	Dif("!="),
	Nao("!"),
	E("&&"),
	Ou("||"),
	IncPos("++"),
	DecPos("--");

	private final String simbolo;

	Operador(String simbolo) {
		this.simbolo = simbolo;
	}

	@Override
	public String toString() {
		return simbolo;
	}
}