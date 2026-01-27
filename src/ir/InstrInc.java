package ir;

public class InstrInc extends Instrucao {
	public Slot    slot;
	public int     value;
	public Formato formato;

	public InstrInc(Slot slot, int value, Formato formato)
	{
		this.slot = slot;
		this.value = value;
	}

	@Override
	public void print()
	{
		Print.instrucao("inc", this.slot.nome, this.value);
	}
	
}
