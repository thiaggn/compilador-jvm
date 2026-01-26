package ir;

public class InstrZeroJumpIf extends Instrucao
{
	public Condicao condicao;
	public Label marcador;
	public Formato formato;

	public InstrZeroJumpIf(Condicao condicao, Label marcador, Formato formato)
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
			case Condicao.Maior     -> Print.instrucao("jgtz" + formato.sufixo, m);
			case Condicao.MaiorIg   -> Print.instrucao("jgez" + formato.sufixo, m);
			case Condicao.Menor     -> Print.instrucao("jltz" + formato.sufixo, m);
			case Condicao.MenorIg   -> Print.instrucao("jlez" + formato.sufixo, m);
			case Condicao.Igual     -> Print.instrucao("jeqz" + formato.sufixo, m);
			case Condicao.Diferente -> Print.instrucao("jnez" + formato.sufixo, m);
		}
	}
	
}
