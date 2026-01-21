package ir;


public class InstrPush extends Instrucao
{
	public Formato formato;
	public float   valorFloat;
	public int     valorInt;

	public InstrPush(float valor)
	{
		this.formato = Formato.Float;
		this.valorFloat = valor;
	}

	public InstrPush(int valor)
	{
		this.formato = Formato.Int;
		this.valorInt = valor;
	}

	public InstrPush(boolean valor)
	{
		this.formato = Formato.Int;
		this.valorInt = valor ? 1 : 0;
	}

	public void print()
	{
		if (formato == Formato.Float)    Print.instrucao("pushf", this.valorFloat);
		else if (formato == Formato.Int) Print.instrucao("pushi", this.valorInt);
	}
}
