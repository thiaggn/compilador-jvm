package ast;

public class SimboloTipo
{
	public final String  nome;
	public final int     id;
	public final boolean ehPrimitivo;
	public final int     bytes;
	public final int     prioridade;

	public static int proximoId  =  0;
	
	public SimboloTipo(String nome, boolean ehPrimitivo, int bytes)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome        = nome;
		this.bytes       = bytes;
		this.id          = proximoId++;
		this.prioridade  = 0;
	}
	
	private SimboloTipo(String nome, boolean ehPrimitivo, int bytes, int prioridade)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome        = nome;
		this.bytes       = bytes;
		this.id          = proximoId++;
		this.prioridade  = prioridade;
	}
	
	public static final SimboloTipo Double        = new SimboloTipo("double", true, 64, 5);
	public static final SimboloTipo Float         = new SimboloTipo("float", true, 32, 4);
	public static final SimboloTipo Long          = new SimboloTipo("long", true, 64, 3);
	public static final SimboloTipo Inteiro       = new SimboloTipo("int", true, 32, 2);
	public static final SimboloTipo Short         = new SimboloTipo("short", true, 16, 1);
	public static final SimboloTipo Bool          = new SimboloTipo("bool", true, 1);
	public static final SimboloTipo Char          = new SimboloTipo("char", true, 1);
	public static final SimboloTipo String        = new SimboloTipo("string", false, 64, 0);
	public static final SimboloTipo Indeterminado = new SimboloTipo("<???>", false, 0, 0);
}
