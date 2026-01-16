package ast;

public class Tipo
{
	public final String  nome;
	public final int 	 id;
	public final boolean ehPrimitivo;
	public final int	 bytes;

	
	public Tipo(String nome, boolean ehPrimitivo, int bytes)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome = nome;
		this.bytes = bytes;
		this.id = proximoId++;
	}
	
	public Tipo(String nome, boolean ehPrimitivo, int bytes, int id)
	{
		this.ehPrimitivo = ehPrimitivo;
		this.nome = nome;
		this.bytes = bytes;
		this.id = id;
	}
	
	public static final int ID_INDETERMINADO = -1;
	public static final int ID_FLOAT 		 =  1;
	public static final int ID_DOUBLE 		 =  2;
	public static final int ID_INT 			 =  3;
	public static final int ID_LONG 		 =  4;
	public static final int ID_SHORT 		 =  5;
	public static final int ID_STRING		 =  6;
	public static int 		proximoId 		 =  7;

	public static final Tipo Float 		   = new Tipo("float", true, 32, ID_FLOAT);
	public static final Tipo Double		   = new Tipo("double", true, 64, ID_DOUBLE);
	public static final Tipo Inteiro 	   = new Tipo("int", true, 32, ID_INT);
	public static final Tipo Long		   = new Tipo("long", true, 64, ID_LONG);
	public static final Tipo Short		   = new Tipo("short", true, 16, ID_SHORT);
	public static final Tipo Indeterminado = new Tipo("<???>", false, 0, ID_INDETERMINADO);
	public static final Tipo String		   = new Tipo("string", false, 64, ID_STRING);
	public static final Tipo Bool 		   = new Tipo("bool", true, ID_INT);
	public static final Tipo Char 		   = new Tipo("char", true, ID_SHORT);
}
