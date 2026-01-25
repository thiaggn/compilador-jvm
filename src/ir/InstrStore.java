package ir;

public class InstrStore extends Instrucao
{
	public Slot    slot;
	public Formato formato;

	public InstrStore(Slot slot, Formato formato)
	{
		this.slot = slot;
		this.formato = formato;
	}

	@Override
	public void print() {
		Print.instrucao("store" + formato.sufixo, slot.nome);
	}
}
