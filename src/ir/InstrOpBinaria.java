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
			case Operacao.Subtracao     -> Print.instrucao("sub" + this.formato.sufixo);
			case Operacao.Adicao        -> Print.instrucao("add" + this.formato.sufixo);
			case Operacao.Divisao       -> Print.instrucao("div" + this.formato.sufixo);
			case Operacao.Resto         -> Print.instrucao("rem" + this.formato.sufixo);
			case Operacao.Multiplicacao -> Print.instrucao("mul" + this.formato.sufixo);
		}
	}
}
