package ir;

public class InstrJumpIf extends Instrucao {
	public Condicao condicao;
	public Label marcador;
	public Formato formato;

	public InstrJumpIf(Condicao condicao, Label marcador, Formato formato)
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
			case Condicao.Maior     -> Print.instrucao("jgt" + formato.sufixo, m);
			case Condicao.MaiorIg   -> Print.instrucao("jge" + formato.sufixo, m);
			case Condicao.Menor     -> Print.instrucao("jlt" + formato.sufixo, m);
			case Condicao.MenorIg   -> Print.instrucao("jle" + formato.sufixo, m);
			case Condicao.Igual     -> Print.instrucao("jeq" + formato.sufixo, m);
			case Condicao.Diferente -> Print.instrucao("jne" + formato.sufixo, m);
		}
	}
}
