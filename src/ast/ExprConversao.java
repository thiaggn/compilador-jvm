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
		Print.abreObjeto("ExprConversão");
		Print.campoComValor("tipo", this.tipo.nome);
		Print.campoComValor("tipo_id", String.format("%d", this.tipo.id));
		Print.campoComObjeto("expr_alvo", alvo);
		Print.fechaObjeto();
	}
}
