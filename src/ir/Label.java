package ir;

public class Label extends Instrucao
{
	public String id;

	public Label(int id)
	{
		this.id = String.format(".L%d", id);
	}

	public Label(String id)
	{
		this.id = id;
	}

	public void print()
	{
		Print.quebra();
		Print.marcador(this.id);
	}
}
