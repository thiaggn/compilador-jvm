package ir;

public class InstrSalvar extends Instrucao
{
	public Slot    slot;
	public Formato formato;

	public InstrSalvar(Slot slot, Formato formato)
	{
		this.slot = slot;
		this.formato = formato;
	}

	@Override
	public void print() {
		Print.instrucao("save", slot.nome);
	}
}
