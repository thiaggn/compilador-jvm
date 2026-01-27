package ir;

public class Slot 
{
	public String nome;
	public int    indice;

	public Slot(int indice, String nome)
	{
		this.indice = indice;
		this.nome = String.format("%%%s", nome);
	}
}