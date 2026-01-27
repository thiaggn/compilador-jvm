package ir;

public class InstrChamadaFunc extends Instrucao {
	public String nome;

	public InstrChamadaFunc(String nome)
	{
		this.nome = nome;
	}

	@Override
	public void print() {
		Print.instrucao("call", String.format("%s", nome));
	}
	
}
