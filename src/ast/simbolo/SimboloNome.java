package ast.simbolo;

import ast.Print;
import ast.Printavel;

public class SimboloNome implements Printavel {
	public final String nome;
	public final int id;
	public final Tipo tipo;

	static int proximoId = 1;
	public SimboloNome(String nome, Tipo tipo) {
		this.id = proximoId++;
		this.nome = nome;
		this.tipo = tipo;
	}
	@Override
	public void print() {
		Print.abreObjeto("SimboloNome");
		Print.campoComValor("nome", nome);
		Print.campoComValor("referencia", String.format("%d", this.id));
		Print.fechaObjeto();
	}
}
