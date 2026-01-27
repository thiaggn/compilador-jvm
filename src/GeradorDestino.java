
import java.io.Writer;
import java.util.HashMap;

public class GeradorDestino
{
	static Writer                  arquivo;
	static CodigoIntermediario     codigo;
	static HashMap<String, String> estaticos;
	static HashMap<String, String> funcoes;

	static {
		estaticos = new HashMap<>();
		estaticos.put("stdout", "java/lang/System/out Ljava/io/PrintStream;");

		funcoes = new HashMap<>();
		funcoes.put("println",                     "java/io/PrintStream/println()V");
		funcoes.put("println(I)",                  "java/io/PrintStream/println(I)V");
		funcoes.put("println(Ljava/lang/String;)", "java/io/PrintStream/println(Ljava/lang/String;)V");
		funcoes.put("concat", 					   "java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
	}

	static void w(String f, Object... args) throws Exception
	{
		arquivo.write(String.format(f, args));
		arquivo.write("\n");
	}
	
	static void t(String f, Object... args) throws Exception
	{
		arquivo.write("\t");
		arquivo.write(String.format(f, args));
		arquivo.write("\n");
	}

	public static void gerar(Writer arq, CodigoIntermediario codigo) throws Exception
	{
		GeradorDestino.arquivo = arq;
		GeradorDestino.codigo = codigo;

		escreverCabecalho();
		for (ir.ItemIntermediario item : codigo.itens) 
		{
			converterItem(item);
		}
		t("return");
		w(".end method");
	}
	static void escreverCabecalho() throws Exception
	{
		w(".source Programa.java");	
		w(".class public Programa");
		w(".super java/lang/Object");
		w("\n.method public <init>()V");
		w(".limit stack 1");
		w(".limit locals 1");
		t("aload_0");
		t("invokespecial java/lang/Object/<init>()V");
		t("return");
		w(".end method");
		w("\n.method public static main([Ljava/lang/String;)V");
		w(".limit stack 400");  
		w(".limit locals %d", codigo.quantidadeDeSlots); 
	}

	static void converterItem(ir.ItemIntermediario item) throws Exception
	{
		switch (item)
		{
			case ir.InstrEmpilhar         push -> converterPush(push);
			case ir.InstrSalvar           save -> converterSave(save);
			case ir.InstrCarregar         load -> converterLoad(load);
			case ir.InstrCarregarEstatico get  -> converterGet(get);    
			case ir.InstrChamadaFunc      call -> converterCall(call); 
			default -> {}
		}
	}

	static void converterPush(ir.InstrEmpilhar push) throws Exception
	{
		if (push.formato == ir.Formato.Referencia)
		{
			t("ldc \"%s\"", push.valorStr);
		}
	}

	static void converterSave(ir.InstrSalvar save) throws Exception
	{
		if (save.formato == ir.Formato.String || save.formato == ir.Formato.Referencia) {
			switch (save.slot.indice)
			{
				case 0:
				case 1:
				case 2:
				case 3:
					t("astore_%d", save.slot.indice);
					break;

				default:
					t("astore %d", save.slot.indice);
					break;
			}
		}
	}

	static void converterLoad(ir.InstrCarregar load) throws Exception
	{
		if (load.formato == ir.Formato.Referencia || load.formato == ir.Formato.String) {
			switch (load.slot.indice)
			{
				case 0:
				case 1:
				case 2:
				case 3:
					t("aload_%d", load.slot.indice);
					break;
				
				default:
					t("aload %d", load.slot.indice);
					break;
			}
		}
	}

	static void converterGet(ir.InstrCarregarEstatico get) throws Exception
	{
		t("getstatic %s", estaticos.get(get.nome));
	}

	static void converterCall(ir.InstrChamadaFunc call) throws Exception
	{
		String nomeFuncao = call.nome;

		if (call.sobrecarga.length == 1)
		{
			nomeFuncao = String.format("%s(%s)", call.nome, call.sobrecarga[0]);
		}

		t("invokevirtual %s", funcoes.get(nomeFuncao));
	}
}
