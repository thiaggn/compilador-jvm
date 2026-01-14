package ast;

public class CmdIf implements Cmd
{
	public Expr exprCondicao;
	public Bloco blocoEntao, blocoSenao;

	public CmdIf(Expr condicao, Bloco blocoEntao, Bloco blocoSenao)
	{
		this.exprCondicao = condicao;
		this.blocoEntao = blocoEntao;
		this.blocoSenao = blocoSenao;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdIf");
		Print.campoComObjeto("condição", this.exprCondicao);
		Print.campoComObjeto("bloco_entao", this.blocoEntao);
		if (this.blocoSenao != null) {
			Print.campoComObjeto("bloco_senao", this.blocoSenao);
		}
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return null;
	}
}
