package ast;

public class CmdExibe implements Cmd
{
	public Expr valor;

	public CmdExibe(Expr valor)
	{
		this.valor = valor;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdExibe");
		Print.campoComObjeto("expr", this.valor);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return null;
	}
}