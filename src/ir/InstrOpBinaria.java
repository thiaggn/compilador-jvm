package ir;

public class InstrOpBinaria extends Instrucao
{
	public Operacao op;
	public Formato  formato;

	public InstrOpBinaria(Operacao op, Formato formato)
	{
		this.op = op;
		this.formato = formato;
	}

	@Override
	public void print()
	{
		switch (op)
		{
			case Operacao.Subtracao     -> Print.instrucao("sub");
			case Operacao.Adicao        -> Print.instrucao("add");
			case Operacao.Divisao       -> Print.instrucao("div" );
			case Operacao.Resto         -> Print.instrucao("rem");
			case Operacao.Multiplicacao -> Print.instrucao("mul");
		}
	}
}