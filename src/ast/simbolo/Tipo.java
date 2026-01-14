package ast.simbolo;

public class Tipo {
	public final String  nome;
	public final boolean ehPrimitivo;
	public final int 	 id;

	public static final Tipo INT 	  = new Tipo("int", true);
	public static final Tipo FLOAT 	  = new Tipo("float", true);
	public static final Tipo bool     = new Tipo("bool", true);
	public static final Tipo STR 	  = new Tipo("string", false);
	public static final Tipo invalido = new Tipo("erro", false, -1);

	static int proximoId = 0;

	public Tipo(String nome, boolean ehPrimitivo) {
		this.nome = nome;
		this.ehPrimitivo = ehPrimitivo;
		this.id = proximoId++;
	}

	public Tipo(String nome, boolean ehPrimitivo, int id) {
		this.nome = nome;
		this.ehPrimitivo = ehPrimitivo;
		this.id = -1;
	}

	@Override
	public String toString() {
		return nome;
	}

	public boolean ehInvalido() {
		return this.id == -1;
	}

	public boolean ehString() {
		return this.id == Tipo.STR.id;
	}
}
