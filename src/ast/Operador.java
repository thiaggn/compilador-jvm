package ast;

public enum Operador {
	Mais("+"),
	Menos("-"),
	Mul("*"),
	Div("/"),
	Maior(">"),
	Resto("%"),

	Nao("!"),
	IncPos("++"),
	DecPos("--"),

	MaiorIg(">="),
	Menor("<"),
	MenorIg("<="),
	Igual("=="),
	Dif("!="),
	EE("&&"),
	OuOu("||");

	private final String simbolo;

	Operador(String simbolo) {
		this.simbolo = simbolo;
	}

	@Override
	public String toString() {
		return simbolo;
	}
}