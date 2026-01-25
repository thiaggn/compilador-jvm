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

	public boolean ehBooleano()
	{
		switch(this)
		{
			case MaiorIg:
			case Maior:
			case Menor:
			case MenorIg:
			case Igual:
			case Dif:
			case EE:
			case OuOu:
				return true;
			
			default:
				return false;
		}
	}

	public boolean ehRelacional()
	{
		switch (this)
		{
			case Maior:
			case MaiorIg:
			case Menor:
			case MenorIg:
			case Igual:
			case Dif:
				return true;
			default:
				return false;
		}
	}

	public boolean ehLogico()
	{
		return this == EE || this == OuOu;
	}
}