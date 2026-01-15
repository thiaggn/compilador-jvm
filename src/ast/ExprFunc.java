package ast;

import java.util.ArrayList;

public class ExprFunc extends Expr
{
	public Id 			   identificador;
	public ArrayList<Expr> argumentos;

	public ExprFunc(Id nome, ArrayList<Expr> args)
	{
		super(nome.linha, nome.coluna, Tipo.Indeterminado);
		this.identificador = nome;
		this.argumentos = args;
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
}