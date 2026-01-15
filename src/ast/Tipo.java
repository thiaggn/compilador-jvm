package ast;

public class Tipo
{
	public final String nome;
	public final int id;
	public final boolean ehPrimitivo;

	public static int proximoId = 1;

	public Tipo(String nome, boolean ehPrimitivo)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome = nome;
		this.id = proximoId++;
	}

	public static Tipo Indeterminado = new Tipo("", false);
	public static Tipo Float 		 = new Tipo("float", true);
	public static Tipo Inteiro 	     = new Tipo("int", true);
	public static Tipo String		 = new Tipo("string", true);
	public static Tipo Bool			 = new Tipo("bool", true);
}
