package ast;

public class CmdFor implements Cmd
{
	public Expr exprInicial;
	public Expr exprCond;
	public Expr exprUpdate;
	public Bloco bloco;

	public CmdFor (Expr inicio, Expr condicao, Expr atualizacao, Bloco bloco)
	{
		this.exprInicial = inicio;
		this.exprCond = condicao;
		this.exprUpdate = atualizacao;
		this.bloco = bloco;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdFor");
		Print.campoComObjeto("expr_inicial", this.exprInicial);
		Print.campoComObjeto("expr_condicional", this.exprCond);
		Print.campoComObjeto("expr_update", this.exprUpdate);
		Print.campoComObjeto("bloco", this.bloco);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao() {
		return null;
	}
}