package ir;

public class InstrZeroSaltarSe extends Instrucao
{
	public Condicao condicao;
	public Marcador marcador;
	public Formato formato;

	public InstrZeroSaltarSe(Condicao condicao, Marcador marcador, Formato formato)
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
			case Condicao.Maior     -> Print.instrucao("ifgtz", m);
			case Condicao.MaiorIg   -> Print.instrucao("ifgez", m);
			case Condicao.Menor     -> Print.instrucao("ifltz", m);
			case Condicao.MenorIg   -> Print.instrucao("iflez", m);
			case Condicao.Igual     -> Print.instrucao("ifeqz", m);
			case Condicao.Diferente -> Print.instrucao("ifnez", m);
		}
	}
	
}
