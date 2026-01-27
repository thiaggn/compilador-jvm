package ast;

public class SimboloTipo
{
	public final Primitivo primitivo;
	public final String    nome;
	public final int       id;
	public final int       prioridade;

	public static int proximoId  =  0;

	public boolean ehPrimitivo()
	{
		return this.primitivo != Primitivo.Outro;
	}
	
	public SimboloTipo(String nome, Primitivo primitivo)
	{
		this.nome       = nome;
		this.id         = proximoId++;
		this.prioridade = 0;
		this.primitivo  = primitivo;
	}
	
	private SimboloTipo(String nome, int prioridade, Primitivo primitivo)
	{
		this.nome       = nome;
		this.id         = proximoId++;
		this.prioridade = prioridade;
		this.primitivo  = primitivo;
	}

	public boolean ehValido()
	{
		return this != SimboloTipo.Indeterminado;
	}
	
	public static final SimboloTipo Double        = new SimboloTipo("double", 7, Primitivo.Double);
	public static final SimboloTipo Float         = new SimboloTipo("float",  6, Primitivo.Float);
	public static final SimboloTipo Long          = new SimboloTipo("long",   5, Primitivo.Long);
	public static final SimboloTipo Inteiro       = new SimboloTipo("int",    4, Primitivo.Int);
	public static final SimboloTipo Short         = new SimboloTipo("short",  3, Primitivo.Short);
	public static final SimboloTipo Char          = new SimboloTipo("char",   2, Primitivo.Short);
	public static final SimboloTipo Bool          = new SimboloTipo("bool",   1, Primitivo.Int);
	public static final SimboloTipo String        = new SimboloTipo("string", 0, Primitivo.Ponteiro);
	public static final SimboloTipo Indeterminado = new SimboloTipo("<???>",  0, Primitivo.Outro);
}
