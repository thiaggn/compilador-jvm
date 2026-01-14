import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import analisador.AnalisadorSemantico;
import parser.Parser;
import analisador.Analise;
import intermediario.GeradorIR;

public class Main 
{
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			// faz a análise léxica e sintática
			var parser = new Parser(System.in);
			var programa = parser.gerarAST();
			
			// faz a análise semântica
			Analise analise = AnalisadorSemantico.analisar(programa);
			if (!analise.ok())
			{
				analise.exibirErros();
				return;
			}

			programa.print();

			// gera o código intermediário
			GeradorIR.gerarIntermediario(programa);
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