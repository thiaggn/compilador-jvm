package ast;

import parser.Token;

public class CmdIf extends Cmd
{
	public Expr exprCondicao;
	public Bloco blocoEntao, blocoSenao;

	public CmdIf(Expr condicao, Bloco blocoEntao, Bloco blocoSenao, Token tokIf)
	{
		super(tokIf.beginLine, tokIf.beginColumn);
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
}
