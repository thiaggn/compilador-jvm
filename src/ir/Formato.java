package ir;

public enum Formato 
{
	Short,
	Float,
	Int,
	Double ,
	Long,
	Referencia;

	public int tamanho()
	{
		return switch (this)
		{
			case Short      -> 1;
			case Float      -> 1;
			case Int        -> 1;
			case Double     -> 2;
			case Long       -> 2;
			case Referencia -> 2;
			default -> throw new Error();
		};
	}

}