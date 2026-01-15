package ast.simbolo;

import ast.Print;
import ast.Printavel;
import ast.Tipo;

public class Simbolo implements Printavel {
	public final String  nome;
	public final int 	 id;
	public final Tipo 	 tipo;
	public final boolean dinamico;
	
	public int idEscopo;
	static int proximoId = 0;

	public Simbolo(String nome, Tipo tipo, boolean dinamico) {
		this.id = proximoId++;
		this.nome = nome;
		this.tipo = tipo;
		this.idEscopo = -1;
		this.dinamico = dinamico;
	}
	
	@Override
	public void print() {
		Print.abreObjeto("SimboloNome");
		Print.campoComValor("nome", nome);
		Print.campoComValor("referencia", String.format("%d", this.id));
		Print.fechaObjeto();
	}
}
