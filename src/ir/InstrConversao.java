package ir;

public class InstrConversao extends Instrucao
{
	public Formato origem;
	public Formato destino;

	public InstrConversao(Formato origem, Formato destino)
	{
		this.origem = origem;
		this.destino = destino;
	}

	@Override
	void print()
	{

	}
}
