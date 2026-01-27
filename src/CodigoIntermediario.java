

import java.util.ArrayList;

import ir.ItemIntermediario;

public class CodigoIntermediario
{
	public ArrayList<ItemIntermediario> itens;
	public int quantidadeDeSlots;

	public CodigoIntermediario(ArrayList<ItemIntermediario> instrucoes, int slots)
	{
		this.itens = instrucoes;
		this.quantidadeDeSlots = slots;
	}

	public void exibir()
	{
		System.out.printf("\n[ok] Gerou %d instruções\n", this.itens.size());

		for (ItemIntermediario instr : this.itens)
		{
			instr.print();
		}
	}
}
