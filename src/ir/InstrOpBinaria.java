package ir;

public class InstrOpBinaria extends Instrucao
{
	public Operacao op;
	public Formato      formato;

	public InstrOpBinaria(Operacao op, Formato formato)
	{
		this.op = op;
		this.formato = formato;
	}

	@Override
	void print()
	{

	}
}
