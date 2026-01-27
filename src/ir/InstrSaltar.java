package ir;

public class InstrSaltar extends Instrucao
{
	Marcador marcador;

	public InstrSaltar(Marcador marcador)
	{
		this.marcador = marcador;
	}

	public void print()
	{
		Print.instrucao("jump", String.format("\u001B[4m%s\u001B[0m", marcador.id));
	}
}
