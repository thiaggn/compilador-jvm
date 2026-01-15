package ast;
import parser.Token;

public class ExprId extends Expr
{
	public String 	   nome;
	public int 		   referencia;

	public ExprId(Token token)
	{
		super(token.beginLine, token.beginColumn, Tipo.Indeterminado);
		this.nome 	 = token.image;
		this.referencia = -1;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprId");
		Print.campoComValor("nome", this.nome);
		Print.campoComValor("referencia", String.format("%d", this.referencia));
		Print.fechaObjeto();
	}
}
