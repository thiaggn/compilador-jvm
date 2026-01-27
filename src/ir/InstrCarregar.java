package ir;

public class InstrCarregar extends Instrucao
{
	public Slot    slot;
	public Formato formato;

	public InstrCarregar(Slot slot, Formato formato)
	{
		this.slot = slot;
		this.formato = formato;
	}

	public void print()
	{
		Print.instrucao("load", slot.nome);
	}
}
