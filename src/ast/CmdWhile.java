package ast;

public class CmdWhile implements Cmd {
	public Expr exprCondicao;
	public Bloco bloco;

	public CmdWhile(Expr condicao, Bloco bloco) {
		this.exprCondicao = condicao;
		this.bloco = bloco;
	}

	@Override
	public void print() {
		Print.abreObjeto("CmdWhile");
		Print.campoComObjeto("expr_cond", this.exprCondicao);
		Print.campoComObjeto("bloco", this.bloco);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return null;
	}
}
