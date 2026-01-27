package ir;


public class InstrEmpilhar extends Instrucao
{
	public Formato formato;
	public float   valorFloat;
	public int     valorInt;
	public String  valorStr;

	public InstrEmpilhar(float valor)
	{
		this.formato = Formato.Float;
		this.valorFloat = valor;
	}

	public InstrEmpilhar(int valor)
	{
		this.formato = Formato.Int;
		this.valorInt = valor;
	}

	public InstrEmpilhar(boolean valor)
	{
		this.formato = Formato.Int;
		this.valorInt = valor ? 1 : 0;
	}

	public InstrEmpilhar(String string)
	{
		this.formato = Formato.Referencia;
		this.valorStr = string;
	}

	public void print()
	{
		switch (this.formato)
		{
			case Formato.Int        -> Print.instrucao("push", this.valorInt);
			case Formato.Float      -> Print.instrucao("push", this.valorFloat);
			case Formato.Referencia -> Print.instrucao("push", String.format("\"%s\"", this.valorStr));
			default                 -> throw new Error();
		}
	}
}
