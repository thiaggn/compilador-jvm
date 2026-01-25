package ir;

public class InstrLoad extends Instrucao
{
	Slot slot;
	Formato formato;

	public InstrLoad(Slot slot, Formato formato)
	{
		this.slot = slot;
		this.formato = formato;
	}

	public void print()
	{
		Print.instrucao("load" + formato.sufixo, slot.nome);
	}
}
