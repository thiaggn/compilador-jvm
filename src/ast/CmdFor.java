package ast;

public final class CmdFor extends Cmd
{
	public Id    varInicial;  
	public Expr  exprInicial;
	public Expr  exprTeste;
	public Expr  exprIterativa;
	public Bloco bloco;

	public CmdFor (Id varInicial, Expr inicial, Expr teste, Expr iterativa, Bloco bloco, parser.Token tokFor)
	{
		super(tokFor.beginLine, tokFor.beginColumn);
		this.varInicial    = varInicial;
		this.exprInicial   = inicial;
		this.exprTeste 	   = teste;
		this.exprIterativa = iterativa;
		this.bloco         = bloco;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdFor");
		Print.campoComObjeto("expr_inicial", this.exprInicial);
		Print.campoComObjeto("expr_condicional", this.exprTeste);
		Print.campoComObjeto("expr_update", this.exprIterativa);
		Print.campoComObjeto("bloco", this.bloco);
		Print.fechaObjeto();
	}
}