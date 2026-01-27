package ir;

public class InstrChamadaFunc extends Instrucao {
	public String   nome;
	public String[] sobrecarga;

	public InstrChamadaFunc(String nome)
	{
		this.nome = nome;
		this.sobrecarga = new String[0];
	}

	public InstrChamadaFunc(String nome, ir.Formato formato)
	{
		this.nome = nome;
		this.sobrecarga = new String[1];

		switch (formato)
		{
			case ir.Formato.Int         -> sobrecarga[0] = "I";
			case ir.Formato.Short       -> sobrecarga[0] = "I";
			case ir.Formato.Long        -> sobrecarga[0] = "J";
			case ir.Formato.Float       -> sobrecarga[0] = "F";
			case ir.Formato.Double      -> sobrecarga[0] = "D";
			case ir.Formato.Char        -> sobrecarga[0] = "C";
			case ir.Formato.Referencia  -> sobrecarga[0] = "Ljava/lang/Object;";
			case ir.Formato.String      -> sobrecarga[0] = "Ljava/lang/String;";
		}
	}

	@Override
	public void print()
	{
		Print.instrucao("call", String.format("%s", nome));
	}
}
