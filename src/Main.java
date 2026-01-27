import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import parser.Parser;

public class Main 
{
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			File entrada = new File(args[0]);

			var parser = new Parser(new FileInputStream(entrada));
			var programa = parser.gerarAST();

			Analise analise = AnalisadorSemantico.analisar(programa);
			if (!analise.ok()) {
				analise.exibirErros();
				return;
			}
			CodigoIntermediario codigo = GeradorIntermediario.traduzir(programa);

			Writer saida = criarArquivo("./saida/Programa.j");
			GeradorDestino.gerar(saida, codigo);
			saida.close();

			if (Arrays.asList(args).contains("--arvore"))
			{
				programa.print();
				System.out.println();
			}
			if (Arrays.asList(args).contains("--ir"))
			{
				 codigo.print();
				 System.out.println();
			}
			if (Arrays.asList(args).contains("--j"))
			{
				BufferedReader reader = new BufferedReader(new FileReader("./saida/Programa.j"));
            	String linha;
				while ((linha = reader.readLine()) != null) {
					System.out.println(linha);
				}
				reader.close();
			}
		} 
		catch (Throwable err) 
		{
			throw err;
		}
	}

	static Writer criarArquivo(String caminho) throws Exception
	{
		Path arquivo = Path.of(caminho);
		Path diretorio = arquivo.getParent();

		if (diretorio != null) {
			Files.createDirectories(diretorio);
		}

		return new BufferedWriter(new FileWriter(arquivo.toFile()));
	}
}