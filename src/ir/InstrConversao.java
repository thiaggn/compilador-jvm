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
	public void print()
	{
		Print.instrucao(
			String.format("%c2%c",
			resolverNome(origem),
			 resolverNome(destino)
		));
	}

	char resolverNome(Formato f) {
		if (f == Formato.Float)  return 'f';
		if (f == Formato.Int)    return 'i';
		if (f == Formato.Short)  return 's';
		if (f == Formato.Double) return 'd';
		if (f == Formato.Long)   return 'l';
		throw new Error();
	}
}
