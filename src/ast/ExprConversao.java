package ast;

public class ExprConversao extends Expr
{
	public Expr alvo;
	
	public ExprConversao(Expr alvo, Tipo tipo)
	{
		super(alvo.linha, alvo.coluna, tipo);
		this.alvo = alvo;
	}

	@Override
	public void print()
	{
		Print.abreObjeto(String.format("ExprConversão<%s, %s>", this.alvo.tipo.nome, this.tipo.nome));
		Print.campoComObjeto("expr_alvo", alvo);
		Print.fechaObjeto();
	}
}
