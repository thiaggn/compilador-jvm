package ast;

public class Simbolo implements Debug {
	public final String  nome;
	public final int 	 ref;
	public final Tipo 	 tipo;
	public final boolean dinamico;
	
	public int idEscopo;
	static int proximoId = 0;

	public Simbolo(String nome, Tipo tipo, boolean dinamico) {
		this.ref = proximoId++;
		this.nome = nome;
		this.tipo = tipo;
		this.idEscopo = -1;
		this.dinamico = dinamico;
	}
	
	@Override
	public void print() {
		Print.abreObjeto("SimboloNome");
		Print.campoComValor("nome", nome);
		Print.campoComValor("referencia", String.format("%d", this.ref));
		Print.fechaObjeto();
	}
}
