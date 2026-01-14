package ast;

public class CmdBreak implements Cmd {

	@Override
	public void print()
	{
		Print.abreObjeto("CmdBreak");
		
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao() {
		return null;
	}
	
}
