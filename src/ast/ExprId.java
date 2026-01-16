package ast;
import ast.simbolo.Simbolo;
import parser.Token;

public class ExprId extends Expr
{
	public String  nome;
	public Simbolo simbolo;

	public ExprId(Token token)
	{
		super(token.beginLine, token.beginColumn, Tipo.Indeterminado);
		this.nome 	 = token.image;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprId");
		Print.campoComValor("nome", String.format("\u001B[96m%s\u001B[0m (%d)", this.simbolo.nome, this.simbolo.ref));
		Print.campoComValor("tipo", this.simbolo.tipo.nome);
		Print.fechaObjeto();
	}
}
