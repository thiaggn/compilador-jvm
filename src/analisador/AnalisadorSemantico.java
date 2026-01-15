package analisador;

import java.util.HashMap;

import ast.No;
import ast.Tipo;
import ast.simbolo.*;

public class AnalisadorSemantico
{
	static HashMap<String, SimboloFunc> funcoes;
	static HashMap<String, ast.Tipo> 		tipos;
	static PilhaDeEscopos 				escopos;
	static Analise 						analise;

	public static Analise analisar(ast.Programa programa) 
	{
		// antes, precisamos popular o escopo com os tipos e funções nativas da linguagem.
		tipos = new HashMap<>();
		tipos.put("float", 	ast.Tipo.Float);
		tipos.put("int",   	ast.Tipo.Inteiro);
		tipos.put("string", ast.Tipo.String);
		tipos.put("FLOAT",  ast.Tipo.Float);
		tipos.put("INT",    ast.Tipo.Inteiro);
		tipos.put("STRING", ast.Tipo.String);
		
		funcoes = new HashMap<>();
		funcoes.put("tam", new SimboloFunc("tam", ast.Tipo.Inteiro, new ast.Tipo[] { ast.Tipo.Inteiro }));

		escopos = new PilhaDeEscopos();
		escopos.abrirEscopo();
		analise = new Analise();

		// faz a análise de cada nó da árvore, produzindo erros caso existam.
		for (ast.No no : programa.nos)
		{
			analisarNo(no);
		}
		return analise;
	}

	static void analisarNo(ast.No no) 
	{
		switch (no) 
		{
			case ast.CmdDeclVariavel   decl -> analisarDeclVariavel(decl);
			case ast.ExprAtribuicao expr -> analisarExprAtribuicao(expr);
			case ast.CmdWhile       cmd  -> analisarCmdWhile(cmd);
			case ast.CmdFor         cmd  -> analisarCmdFor(cmd);
			case ast.CmdExibe 		cmd  -> analisarCmdExibe(cmd);
			case ast.CmdIf 			cmd  -> analisarCmdIf(cmd);
			case ast.ExprFunc 	expr -> analisarExprChamadaFunc(expr);
			case ast.ExprUnaria     expr -> {
				if (expr.op == ast.Operador.IncPos || expr.op == ast.Operador.DecPos) {
					analisarExprUnaria(expr);
				}
				else {
					addErro(expr.linha, expr.coluna, "tipo de expressão não permitida nesse contexto.");
				}
			}
			default -> {
				var msg = String.format(
					"esperava-se uma declaração ou comando, obteve %s.", no.getClass().getName()
				);
				addErro(0, 0, msg);
			}
		}
	}

	static void analisarDeclVariavel(ast.CmdDeclVariavel decl)
	{
		boolean contemErros = false;
		if (escopos.atual().contem(decl.identificador.nome))
		{
			addErro(decl.identificador, "redeclação de variável.");
			contemErros = true;
		}

		ast.Tipo tipo = tipos.get(decl.tipo.nome);
		if (tipo == null)
		{
			addErro(decl.tipo, "declaração de variável com tipo desconhecido.");
			contemErros = true;
		}

		if (decl.exprInicial != null)
		{
			ast.Tipo tipoExpr = analisarExpr(decl.exprInicial);
			if (tipoExpr == ast.Tipo.Indeterminado) return;
			else if (!compativeis(tipo, tipoExpr))
			{
				addErro(decl.exprInicial, "inicialização de variável com tipo incompatível.");
			}
		}

		if (contemErros == false)
		{
			SimboloNome simbolo = new SimboloNome(decl.identificador.nome, tipo);
			decl.simbolo = simbolo;
			escopos.declarar(simbolo);
		}
	}

	static ast.Tipo analisarExpr(ast.Expr expr)
	{
		return switch (expr) {
			case ast.ExprBinaria    exprBin   -> analisarExprBinaria(exprBin);
			case ast.ExprUnaria     exprUn    -> analisarExprUnaria(exprUn);
			case ast.ExprTernaria   exprTern  -> analisarExprTernaria(exprTern);
			case ast.ExprAtribuicao exprAtrib -> analisarExprAtribuicao(exprAtrib);
			case ast.ExprId 		id 		  -> analisarExprId(id);
			case ast.ExprBool       lit 	  -> lit.tipo;
			case ast.ExprFloat   	lit 	  -> lit.tipo;
			case ast.ExprInteiro 	lit 	  -> lit.tipo;
			case ast.ExprString  	lit 	  -> lit.tipo;

			default -> {
				throw new Error(String.format(
					"erro interno: expressão desconhecida '%s'.", expr.getClass().getName()
				));
			}
		};
	}

	static ast.Tipo analisarExprAtribuicao(ast.ExprAtribuicao atrib)
	{
		SimboloNome simbolo = escopos.resolver(atrib.destino.nome);
		atrib.simboloDestino  = simbolo;

		// se for encontrado um símbolo com o mesmo nome, mas com tipo diferente
		// do valor que está sendo atribuído, é criado um novo símbolo com o mesmo
		// identificador e o novo tipo, substituindo o símbolo existente em seu escopo.
		//
		// esse novo símbolo tem um identificador novo. a partir daqui todas as 
		// referências à essa variável passam a apontar para o símbolo substituído.
		if (simbolo != null)
		{
			ast.Tipo tipoExprInicial = analisarExpr(atrib.exprInicial);
			if (!compativeis(simbolo.tipo, tipoExprInicial))
			{
				SimboloNome novoSimbolo = new SimboloNome(simbolo.nome, tipoExprInicial);
				escopos.substituir(simbolo.nome, novoSimbolo);
				atrib.ehDeclaracao = true;
				atrib.simboloDestino = novoSimbolo;
			}
			return tipoExprInicial;
		}
		// se o símbolo não existe em nenhum escopo, então essa atribuição se comporta 
		// como uma declaração com valor inicial.
		else
		{
			ast.Tipo tipo = analisarExpr(atrib.exprInicial);

			SimboloNome novoSimbolo = new SimboloNome(atrib.destino.nome, tipo);
			escopos.declarar(novoSimbolo);

			atrib.simboloDestino = novoSimbolo;
			atrib.ehDeclaracao = true;

			return tipo;
		}
	}

	static ast.Tipo analisarExprBinaria(ast.ExprBinaria expr)
	{
		ast.Tipo tipoEsq = analisarExpr(expr.esq);
		ast.Tipo tipoDir = analisarExpr(expr.dir);

		// se esq e dir forem primitivos, são compatíveis (ex.: float e int)
		// se esq e dir forem o mesmo tipo, são compatíveis (ex.: int e int, string e string)
		if (!compativeis(tipoEsq, tipoDir))
		{
			var msg = String.format(
				"operação entre tipos incompatíveis: esquerda é '%s', direita é '%s'.",
				tipoEsq.nome, tipoDir.nome
			);
			addErro(expr, msg);
			return ast.Tipo.Indeterminado;
		}
		// importante: strings são compatíveis, mas apenas com o operador '+' (concatenação)
		else if (tipoDir == ast.Tipo.String && tipoEsq == ast.Tipo.String)
		{
			if (expr.op != ast.Operador.Mais)
			{
				var msg = String.format(
					"operador '%s' não é permitido entre strings; apenas + é suportado.", 
					expr.op.toString()
				);
				addErro(expr, msg);
			}
		}

		return tipoEsq;
	}

	static ast.Tipo analisarExprUnaria(ast.ExprUnaria exprUn)
	{
		return analisarExpr(exprUn.expr);
	}

	static ast.Tipo analisarExprTernaria(ast.ExprTernaria expr)
	{
		boolean contemErros = false;
		ast.Tipo tipoCond 		= analisarExpr(expr.exprCondicao);
		ast.Tipo tipoEntao 		= analisarExpr(expr.exprEntao);
		ast.Tipo tipoSenao 		= analisarExpr(expr.exprSenao);

		if (!tipoCond.ehPrimitivo)
		{
			addErro(expr.exprCondicao, "a condição de um operador ternário deve ser um primitivo.");
		}

		if (!compativeis(tipoEntao, tipoSenao))
		{
			addErro(expr, "um operador ternário deve retornar expressões do mesmo tipo.");
			contemErros = true;
		}

		if (!contemErros) {
			return tipoEntao;
		}

		return ast.Tipo.Indeterminado;
	}

	static ast.Tipo analisarExprChamadaFunc(ast.ExprFunc exprFunc)
	{
		// trata o uso de uma função não declarada
		SimboloFunc funcao = funcoes.get(exprFunc.identificador.nome);
		if (funcao == null)
		{
			return ast.Tipo.Indeterminado;
		}

		// trata quando argumentos estão faltando
		if (exprFunc.argumentos.size() < funcao.parametros.length)
		{
			var msg = String.format(
				"função '%s' espera %d argumentos mas apenas %d foram fornecidos.", 
				funcao.nome, funcao.parametros.length, exprFunc.argumentos.size()
			);
			addErro(exprFunc, msg);
		}
		// trata quando tem argumentos em excesso
		else if (exprFunc.argumentos.size() > funcao.parametros.length)
		{
			var msg = String.format("excesso de argumentos para a função %s.", funcao.nome);
			addErro(exprFunc, msg);
		}

		// verifica se o tipo de cada argumento corresponde o tipo do parâmetro que ele está
		// sendo passado.
		for (int i = 0; i < funcao.parametros.length; i++)
		{
			ast.Expr exprArg 	   = exprFunc.argumentos.get(i);
			ast.Tipo tipoArgumento = analisarExpr(exprArg);
			ast.Tipo tipoParametro = funcao.parametros[i];
			
			if (!compativeis(tipoArgumento, tipoParametro))
			{
				var msg = String.format(
					"tipo incompatível no argumento %d de '%s': esperado '%s' mas encontrou '%s'.", 
					i+1, funcao.nome, tipoParametro.nome, tipoArgumento.nome
				);
				addErro(exprArg, msg);
			}
		}
		return funcao.tipoRetorno;
	}

	static ast.Tipo analisarExprId(ast.ExprId id)
	{
		SimboloNome simbolo = escopos.resolver(id.nome);
		if (simbolo != null)
		{
			id.simbolo = simbolo;
			return simbolo.tipo;
		} 
		else 
		{
			addErro(id, String.format("símbolo desconhecido '%s'.", id.nome));
			return ast.Tipo.Indeterminado;
		}
	}

	static void analisarCmdWhile(ast.CmdWhile cmd)
	{
		escopos.abrirEscopo();
		ast.Tipo tipoCond = analisarExpr(cmd.exprCondicao);
		if (!tipoCond.ehPrimitivo)
		{
			addErro(cmd.exprCondicao, "a condição do while deve ser uma expressão booleana.");
		}
		analisarBloco(cmd.bloco);
		escopos.fecharEscopo();
	}

	static void analisarCmdIf(ast.CmdIf cmd)
	{
		ast.Tipo tipoCond = analisarExpr(cmd.exprCondicao);
		if (!tipoCond.ehPrimitivo)
		{
			addErro(cmd.exprCondicao, "a expressão de condição de um if deve resultar num primitivo.");
		}

		analisarBloco(cmd.blocoEntao);
		if (cmd.blocoSenao != null)
		{
			analisarBloco(cmd.blocoSenao);
		}
	}

	static void analisarCmdFor(ast.CmdFor cmd)
	{
		escopos.abrirEscopo();

		if (cmd.exprInicial instanceof ast.CmdDeclVariavel decl)
		{
			analisarDeclVariavel(decl);
		} 
		else if (cmd.exprInicial instanceof ast.Expr expr)
		{
			ast.Tipo tipoInicial = analisarExpr(expr);
			if (!tipoInicial.ehPrimitivo)
			{
				addErro(cmd.exprInicial,  "o tipo da expressão inicial do laço loop deve ser um primitivo.");
			}
		}

		ast.Tipo tipoCond = analisarExpr(cmd.exprCond);
		if (!tipoCond.ehPrimitivo)
		{
			addErro(cmd.exprCond, "a expressão de condiçao de um laço for deve ser um primitivo.");
		}

		ast.Tipo tipoUpdate = analisarExpr(cmd.exprUpdate);
		if (!tipoUpdate.ehPrimitivo)
		{
			addErro(cmd.exprUpdate, "a expressão de condiçao de um laço for deve ser um primitivo.");
		}

		analisarBloco(cmd.bloco);
		escopos.fecharEscopo();
	}

	static void analisarCmdExibe(ast.CmdExibe cmd)
	{
		analisarExpr(cmd.valor);
	}

	static void analisarBloco(ast.Bloco bloco)
	{
		escopos.abrirEscopo();
		for (ast.No no : bloco.nos)
		{
			analisarNo(no);
		}
		escopos.fecharEscopo();
	}

	static boolean compativeis(ast.Tipo a, ast.Tipo b)
	{
		if (a == Tipo.Indeterminado|| b == Tipo.Indeterminado) return false;
		if (a.ehPrimitivo && b.ehPrimitivo) return true;
		else return a.id == b.id;
	}
	
	static void addErro(No no, String msg)
	{
		addErro(no.linha, no.coluna, msg);
	}

	static void addErro(int linha, int coluna, String msg)
	{
		addErro(linha, coluna, msg);
	}
}