import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import analisador.AnalisadorSemantico;
import parser.Parser;
import analisador.Analise;
import intermediario.CodigoIntermediario;
import intermediario.GeradorIR;

public class Main 
{
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			File arquivo = new File(args[0]);
			InputStream stream = new FileInputStream(arquivo);

			var parser = new Parser(stream);
			var programa = parser.gerarAST();
			
			// faz a análise semântica
			Analise analise = AnalisadorSemantico.analisar(programa);
			if (!analise.ok())
			{
				analise.exibirErros();
				return;
			}

			if (args.length > 1 && args[1].equals("--arvore"))
			{
				programa.print();
			}

			CodigoIntermediario codigo = GeradorIR.traduzir(programa);
			codigo.exibir();
		} 
		catch (Throwable err) 
		{
			throw err;
		}
	}

	static Writer criarArquivo(String nome) throws Exception
	{
		Path dir = Path.of("saida");
		Files.createDirectories(Path.of("saida"));
		var arquivo = new FileWriter(dir.resolve(nome).toFile());
		var buffer = new BufferedWriter(arquivo);
		return buffer;
	}
}