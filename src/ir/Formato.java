package ir;

public class Formato 
{
	public final int bytes;
	public final char sufixo;

	public Formato(int bytes, char sufixo)
	{
		this.bytes = bytes;
		this.sufixo = sufixo;
	}

	public static final ir.Formato Short  = new Formato(2, 's');
	public static final ir.Formato Float  = new Formato(4, 'f');
	public static final ir.Formato Int    = new Formato(4, 'i');
	public static final ir.Formato Double = new Formato(8, 'd');
	public static final ir.Formato Long   = new Formato(8, 'l');
}