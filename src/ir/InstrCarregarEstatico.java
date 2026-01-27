package ir;

public class InstrCarregarEstatico extends Instrucao {

	public String nome;

	public InstrCarregarEstatico(String nome)
	{
		this.nome = nome;
	}

	@Override
	public void print()
	{
		Print.instrucao("get", String.format("%%%s", this.nome));	
	}
}
