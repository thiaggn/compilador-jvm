package ir;

public class InstrSaltarSe extends Instrucao {
	public Condicao condicao;
	public Marcador marcador;
	public Formato formato;

	public InstrSaltarSe(Condicao condicao, Marcador marcador, Formato formato)
	{
		this.condicao = condicao;
		this.marcador = marcador;
		this.formato = formato;
	} 

	@Override
	public void print() {
		String m = String.format("\u001B[4m%s\u001B[0m", marcador.id);

		switch (this.condicao) 
		{
			case Condicao.Maior         -> Print.instrucao("jgt" , m);
			case Condicao.MaiorIg       -> Print.instrucao("jge" , m);
			case Condicao.Menor         -> Print.instrucao("jlt" , m);
			case Condicao.MenorIg       -> Print.instrucao("jle" , m);
			case Condicao.Igual         -> Print.instrucao("jeq" , m);
			case Condicao.Diferente     -> Print.instrucao("jne" , m);
			case Condicao.MaiorZero     -> Print.instrucao("jgtz", m);
			case Condicao.MaiorIgZero   -> Print.instrucao("jgez", m);
			case Condicao.MenorZero     -> Print.instrucao("jltz", m);
			case Condicao.MenorIgZero   -> Print.instrucao("jlez", m);
			case Condicao.IgualZero     -> Print.instrucao("jeqz", m);
			case Condicao.DiferenteZero -> Print.instrucao("jnez", m);
		}
	}
}
