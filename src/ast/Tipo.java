package ast;

public class Tipo
{
	public final String  nome;
	public final int 	 id;
	public final boolean ehPrimitivo;
	public final int	 bytes;
	public final int	 prioridade;

	public static int proximoId 		 =  0;
	
	public Tipo(String nome, boolean ehPrimitivo, int bytes)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome = nome;
		this.bytes = bytes;
		this.id = proximoId++;
		this.prioridade = 0;
	}
	
	private Tipo(String nome, boolean ehPrimitivo, int bytes,  int prioridade)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome = nome;
		this.bytes = bytes;
		this.id = proximoId++;
		this.prioridade = prioridade;
	}
	
	
	public static final Tipo Double		   = new Tipo("double", true, 64, 5);
	public static final Tipo Float 		   = new Tipo("float", true, 32, 4);
	public static final Tipo Long		   = new Tipo("long", true, 64, 3);
	public static final Tipo Inteiro 	   = new Tipo("int", true, 32, 2);
	public static final Tipo Short		   = new Tipo("short", true, 16, 1);
	public static final Tipo Bool 		   = new Tipo("bool", true, 1);
	public static final Tipo Char 		   = new Tipo("char", true, 1);
	public static final Tipo String		   = new Tipo("string", false, 64, 0);
	public static final Tipo Indeterminado = new Tipo("<???>", false, 0, 0);
}
