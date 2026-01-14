package ast;
import ast.simbolo.SimboloNome;
import parser.Token;

public class ExprId implements Expr
{
	public Posicao     posicao;
	public String 	   nome;
	public SimboloNome simbolo;

	public ExprId(Token token)
	{
		this.posicao = new Posicao(token);
		this.nome = token.image;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprId");
		Print.campoComObjeto("simbolo", this.simbolo);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return this.posicao;
	}
}
