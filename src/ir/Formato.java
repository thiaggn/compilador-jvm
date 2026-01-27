package ir;

public enum Formato 
{
	Short,
	Float,
	Int,
	Double,
	Char,
	Long,
	String,
	Referencia;

	public int tamanho()
	{
		return switch (this)
		{
			case Short      -> 1;
			case Float      -> 1;
			case Char       -> 1;
			case Int        -> 1;
			case Double     -> 2;
			case Long       -> 2;
			case String     -> 1;
			case Referencia -> 1;
		};
	}

}