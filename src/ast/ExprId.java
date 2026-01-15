package ast;
import ast.simbolo.SimboloNome;
import parser.Token;

public class ExprId extends Expr
{
	public Posicao     posicao;
	public String 	   nome;
	public SimboloNome simbolo;

	public ExprId(Token token)
	{
		super(token.beginLine, token.beginColumn, Tipo.Indeterminado);
		this.posicao = new Posicao(token);
		this.nome 	 = token.image;
		this.simbolo = null;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprId");
		Print.campoComObjeto("simbolo", this.simbolo);
		Print.fechaObjeto();
	}
}
