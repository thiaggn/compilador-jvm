package ir;

public class Store extends Instrucao
{
	public Slot    slot;
	public Formato formato;

	public Store(Slot slot, Formato formato)
	{
		this.slot = slot;
		this.formato = formato;
	}

	@Override
	void print() {
		
	}
}
