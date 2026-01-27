package ir;

public class InstrExibe extends Instrucao
{
	public Formato formato;

	public InstrExibe(Formato formato)
	{
		this.formato = formato;
	}

	@Override
	public void print()
	{
		Print.instrucao("print");
	}
}
