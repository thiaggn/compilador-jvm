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
			case Condicao.Maior     -> Print.instrucao("ifgt", m);
			case Condicao.MaiorIg   -> Print.instrucao("ifge", m);
			case Condicao.Menor     -> Print.instrucao("iflt", m);
			case Condicao.MenorIg   -> Print.instrucao("ifle", m);
			case Condicao.Igual     -> Print.instrucao("ifeq", m);
			case Condicao.Diferente -> Print.instrucao("ifne", m);
		}
	}
}
