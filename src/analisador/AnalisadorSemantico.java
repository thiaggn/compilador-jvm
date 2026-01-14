package analisador;

import java.util.HashMap;

import ast.simbolo.*;

public class AnalisadorSemantico
{
	static HashMap<String, SimboloFunc> funcoes;
	static HashMap<String, Tipo> 		tipos;
	static PilhaDeEscopos 				escopos;
	static Analise 						erros;

	public static Analise analisar(ast.Programa programa) 
	{
		// antes, precisamos popular o escopo com os tipos e funções nativas da linguagem.
		tipos = new HashMap<>();
		tipos.put("float", 	Tipo.FLOAT);
		tipos.put("int",   	Tipo.INT);
		tipos.put("string", Tipo.STR);
		tipos.put("FLOAT",  Tipo.FLOAT);
		tipos.put("INT",    Tipo.INT);
		tipos.put("STRING", Tipo.STR);
		
		funcoes = new HashMap<>();
		funcoes.put("tam", new SimboloFunc("tam", Tipo.INT, new Tipo[] { Tipo.STR }));

		escopos = new PilhaDeEscopos();
		escopos.abrirEscopo();
		erros = new Analise();

		// faz a análise de cada nó da árvore, produzindo erros caso existam.
		for (ast.No no : programa.nos)
		{
			analisarNo(no);
		}
		return erros;
	}

	static void analisarNo(ast.No no) 
	{
		switch (no) 
		{
			case ast.DeclVariavel   decl -> analisarDeclVariavel(decl);
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
					erros.add(expr, "tipo de expressão não permitida nesse contexto.");
				}
			}
			default -> {
				var msg = String.format(
					"esperava-se uma declaração ou comando, obteve %s.", no.getClass().getName()
				);
				erros.add(no, msg);
			}
		}
	}

	static void analisarDeclVariavel(ast.DeclVariavel decl)
	{
		boolean contemErros = false;
		if (escopos.atual().contem(decl.identificador.nome))
		{
			erros.add(decl.identificador, "redeclação de variável.");
			contemErros = true;
		}

		Tipo tipo = tipos.get(decl.tipo.nome);
		if (tipo == null)
		{
			erros.add(decl.tipo, "declaração de variável com tipo desconhecido.");
			contemErros = true;
		}

		if (decl.exprInicial != null)
		{
			Tipo tipoExpr = analisarExpr(decl.exprInicial);
			if (tipoExpr.ehInvalido()) return;
			else if (!compativeis(tipo, tipoExpr))
			{
				erros.add(decl.exprInicial, "inicialização de variável com tipo incompatível.");
			}
		}

		if (contemErros == false)
		{
			SimboloNome simbolo = new SimboloNome(decl.identificador.nome, tipo);
			decl.simbolo = simbolo;
			escopos.declarar(simbolo);
		}
	}

	static Tipo analisarExpr(ast.Expr expr)
	{
		return switch (expr) {
			case ast.ExprBinaria    exprBin   -> analisarExprBinaria(exprBin);
			case ast.ExprUnaria     exprUn    -> analisarExprUnaria(exprUn);
			case ast.ExprTernaria   exprTern  -> analisarExprTernaria(exprTern);
			case ast.ExprAtribuicao exprAtrib -> analisarExprAtribuicao(exprAtrib);
			case ast.ExprId 		id 		  -> analisarExprId(id);
			case ast.ExprBool    _ 		  	  -> Tipo.bool;
			case ast.ExprFloat   _ 		  	  -> Tipo.FLOAT;
			case ast.ExprInteiro _ 	  	  -> Tipo.INT;
			case ast.ExprString  _ 	 	  	  -> Tipo.STR;

			default -> {
				throw new Error(String.format(
					"erro interno: expressão desconhecida '%s'.", expr.getClass().getName()
				));
			}
		};
	}

	static Tipo analisarExprAtribuicao(ast.ExprAtribuicao atrib)
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
			Tipo tipoExprInicial = analisarExpr(atrib.exprInicial);
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
			Tipo tipo = analisarExpr(atrib.exprInicial);

			SimboloNome novoSimbolo = new SimboloNome(atrib.destino.nome, tipo);
			escopos.declarar(novoSimbolo);

			atrib.simboloDestino = novoSimbolo;
			atrib.ehDeclaracao = true;

			return tipo;
		}
	}

	static Tipo analisarExprBinaria(ast.ExprBinaria expr)
	{
		Tipo tipoEsq = analisarExpr(expr.esq);
		Tipo tipoDir = analisarExpr(expr.dir);

		// se esq e dir forem primitivos, são compatíveis (ex.: float e int)
		// se esq e dir forem o mesmo tipo, são compatíveis (ex.: int e int, string e string)
		if (!compativeis(tipoEsq, tipoDir))
		{
			var msg = String.format(
				"operação entre tipos incompatíveis: esquerda é '%s', direita é '%s'.",
				tipoEsq.nome, tipoDir.nome
			);
			erros.add(expr, msg);
			return Tipo.invalido;
		}
		// importante: strings são compatíveis, mas apenas com o operador '+' (concatenação)
		else if (tipoDir.ehString() && tipoEsq.ehString())
		{
			if (expr.op != ast.Operador.Mais)
			{
				var msg = String.format(
					"operador '%s' não é permitido entre strings; apenas + é suportado.", 
					expr.op.toString()
				);
				erros.add(expr, msg);
			}
		}

		return tipoEsq;
	}

	static Tipo analisarExprUnaria(ast.ExprUnaria exprUn)
	{
		return analisarExpr(exprUn.expr);
	}

	static Tipo analisarExprTernaria(ast.ExprTernaria expr)
	{
		boolean contemErros = false;
		Tipo tipoCond 		= analisarExpr(expr.exprCondicao);
		Tipo tipoEntao 		= analisarExpr(expr.exprEntao);
		Tipo tipoSenao 		= analisarExpr(expr.exprSenao);

		if (!tipoCond.ehPrimitivo)
		{
			erros.add(expr.exprCondicao, "a condição de um operador ternário deve ser um primitivo.");
		}

		if (!compativeis(tipoEntao, tipoSenao))
		{
			erros.add(expr, "um operador ternário deve retornar expressões do mesmo tipo.");
			contemErros = true;
		}

		if (!contemErros) {
			return tipoEntao;
		}

		return Tipo.invalido;
	}

	static Tipo analisarExprChamadaFunc(ast.ExprFunc exprFunc)
	{
		// trata o uso de uma função não declarada
		SimboloFunc funcao = funcoes.get(exprFunc.identificador.nome);
		if (funcao == null)
		{
			return Tipo.invalido;
		}

		// trata quando argumentos estão faltando
		if (exprFunc.argumentos.size() < funcao.parametros.length)
		{
			var msg = String.format(
				"função '%s' espera %d argumentos mas apenas %d foram fornecidos.", 
				funcao.nome, funcao.parametros.length, exprFunc.argumentos.size()
			);
			erros.add(exprFunc, msg);
		}
		// trata quando tem argumentos em excesso
		else if (exprFunc.argumentos.size() > funcao.parametros.length)
		{
			var msg = String.format("excesso de argumentos para a função %s.", funcao.nome);
			erros.add(exprFunc, msg);
		}

		// verifica se o tipo de cada argumento corresponde o tipo do parâmetro que ele está
		// sendo passado.
		for (int i = 0; i < funcao.parametros.length; i++)
		{
			ast.Expr exprArg 	   = exprFunc.argumentos.get(i);
			Tipo tipoArgumento = analisarExpr(exprArg);
			Tipo tipoParametro = funcao.parametros[i];
			
			if (!compativeis(tipoArgumento, tipoParametro))
			{
				var msg = String.format(
					"tipo incompatível no argumento %d de '%s': esperado '%s' mas encontrou '%s'.", 
					i+1, funcao.nome, tipoParametro.nome, tipoArgumento.nome
				);
				erros.add(exprArg, msg);
			}
		}
		return funcao.tipoRetorno;
	}

	static Tipo analisarExprId(ast.ExprId id)
	{
		SimboloNome simbolo = escopos.resolver(id.nome);
		if (simbolo != null)
		{
			id.simbolo = simbolo;
			return simbolo.tipo;
		} 
		else 
		{
			erros.add(id, String.format("símbolo desconhecido '%s'.", id.nome));
			return Tipo.invalido;
		}
	}

	static void analisarCmdWhile(ast.CmdWhile cmd)
	{
		escopos.abrirEscopo();
		Tipo tipoCond = analisarExpr(cmd.exprCondicao);
		if (!tipoCond.ehPrimitivo)
		{
			erros.add(cmd.exprCondicao, "a condição do while deve ser uma expressão booleana.");
		}
		analisarBloco(cmd.bloco);
		escopos.fecharEscopo();
	}

	static void analisarCmdIf(ast.CmdIf cmd)
	{
		Tipo tipoCond = analisarExpr(cmd.exprCondicao);
		if (!tipoCond.ehPrimitivo)
		{
			erros.add(cmd.exprCondicao, "a expressão de condição de um if deve resultar num primitivo.");
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

		if (cmd.exprInicial instanceof ast.DeclVariavel decl)
		{
			analisarDeclVariavel(decl);
		} 
		else 
		{
			Tipo tipoInicial = analisarExpr(cmd.exprInicial);
			if (!tipoInicial.ehPrimitivo)
			{
				erros.add(cmd.exprInicial,  "o tipo da expressão inicial do laço loop deve ser um primitivo.");
			}
		}

		Tipo tipoCond = analisarExpr(cmd.exprCond);
		if (!tipoCond.ehPrimitivo)
		{
			erros.add(cmd.exprCond, "a expressão de condiçao de um laço for deve ser um primitivo.");
		}

		Tipo tipoUpdate = analisarExpr(cmd.exprUpdate);
		if (!tipoUpdate.ehPrimitivo)
		{
			erros.add(cmd.exprUpdate, "a expressão de condiçao de um laço for deve ser um primitivo.");
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

	static boolean compativeis(Tipo a, Tipo b)
	{
		if (a.ehInvalido() || b.ehInvalido()) return false;
		if (a.ehPrimitivo && b.ehPrimitivo) return true;
		else return a.id == b.id;
	}
}