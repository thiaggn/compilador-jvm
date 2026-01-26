package ir;

public class InstrJump extends Instrucao
{
	Label marcador;

	public InstrJump(Label marcador)
	{
		this.marcador = marcador;
	}

	public void print()
	{
		Print.instrucao("jump", String.format("\u001B[4m%s\u001B[0m", marcador.id));
	}
}
