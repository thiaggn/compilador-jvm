package ir;

public enum Condicao {
	Igual,
	Diferente,
	Maior,
	MaiorIg,
	Menor,
	MenorIg;

	public Condicao negado()
	{
		return switch (this)
		{
			case Igual     -> Diferente;
			case Diferente -> Igual;
			case Maior     -> MenorIg;
			case MaiorIg   -> Menor;
			case Menor     -> MaiorIg;
			case MenorIg   -> Maior;
		};
	}
}