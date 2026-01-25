package ir;

public class InstrGoto extends Instrucao
{
	Marcador marcador;

	public InstrGoto(Marcador marcador)
	{
		this.marcador = marcador;
	}

	public void print()
	{
		Print.instrucao("goto", String.format("\u001B[4m%s\u001B[0m", marcador.id));
	}
}
