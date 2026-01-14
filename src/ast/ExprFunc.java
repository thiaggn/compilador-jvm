package ast;

import java.util.ArrayList;

public class ExprFunc implements Expr
{
	public Id identificador;
	public ArrayList<Expr> argumentos;
	public Posicao posicao;

	public ExprFunc(Id nome, ArrayList<Expr> args)
	{
		this.identificador = nome;
		this.argumentos = args;
		this.posicao = nome.obterPosicao();
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ChamadaFunção");
		Print.campoComValor("nome", this.identificador.nome);
		Print.abreArray("argumentos");
		for (No arg : this.argumentos) {
			Print.itemDeArray(arg);
		}
		Print.fechaArray();
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return this.posicao;
	}
}