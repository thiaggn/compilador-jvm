package ast;

public class Simbolo implements Debug
{
	public String  nome;
	public int     ref;
	public Tipo    tipo;
	public boolean ehRedeclaravel;
	public int     idEscopo;
	
	static int proximoId = 0;

	public Simbolo(String nome, Tipo tipo, boolean ehRedeclaravel)
	{
		this.ref            = proximoId++;
		this.nome           = nome;
		this.tipo           = tipo;
		this.idEscopo       = -1;
		this.ehRedeclaravel = ehRedeclaravel;
	}
	
	@Override
	public void print()
	{
		Print.abreObjeto("SimboloNome");
		Print.campoComValor("nome", nome);
		Print.campoComValor("referencia", String.format("%d", this.ref));
		Print.fechaObjeto();
	}
}
