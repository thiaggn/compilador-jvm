package ir;

public enum Condicao {
	Igual,
	Diferente,
	Maior,
	MaiorIg,
	Menor,
	MenorIg,
	
	IgualZero,
	DiferenteZero,
	MaiorZero,
	MaiorIgZero,
	MenorZero,
	MenorIgZero;

	public Condicao inverso()
	{
		return switch (this)
		{
			case Igual     -> Diferente;
			case Diferente -> Igual;
			case Maior     -> MenorIg;
			case MaiorIg   -> Menor;
			case Menor     -> MaiorIg;
			case MenorIg   -> Maior;

			case IgualZero     -> DiferenteZero;
			case DiferenteZero -> IgualZero;
			case MaiorZero     -> MenorIgZero;
			case MaiorIgZero   -> MenorZero;
			case MenorZero     -> MaiorIgZero;
			case MenorIgZero   -> MaiorZero;
		};
	}
}