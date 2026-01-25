package intermediario;

import java.util.ArrayList;

import ir.Instrucao;

public class CodigoIntermediario
{
	public ArrayList<Instrucao> instrucoes;

	public CodigoIntermediario(ArrayList<Instrucao> instrucoes)
	{
		this.instrucoes = instrucoes;
	}

	public void exibir()
	{
		System.out.printf("\n[ok] Gerou %d instruções\n", this.instrucoes.size());

		for (Instrucao instr : this.instrucoes)
		{
			instr.print();
		}
	}
}
