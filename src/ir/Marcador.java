package ir;

public class Marcador extends Instrucao
{
	public String id;

	public Marcador(int id)
	{
		this.id = String.format(".L%d", id);
	}

	public Marcador(String id)
	{
		this.id = id;
	}

	public void print()
	{
		Print.quebra();
		Print.marcador(this.id);
	}
}
