package ast;

public class CmdIf extends BlocoOuIf
{
	public Expr      exprCondicao;
	public Bloco     blocoEntao;
	public BlocoOuIf senao;

	public CmdIf(Expr condicao, Bloco blocoEntao, BlocoOuIf senao, parser.Token tokIf)
	{
		super(tokIf.beginLine, tokIf.beginColumn);
		this.exprCondicao = condicao;
		this.blocoEntao   = blocoEntao;
		this.senao        = senao;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdIf");
		Print.campoComObjeto("condição", this.exprCondicao);
		Print.campoComObjeto("bloco_entao", this.blocoEntao);
		if (this.senao != null) {


			Print.campoComObjeto("bloco_senao", this.senao);
		}
		Print.fechaObjeto();
	}
}
