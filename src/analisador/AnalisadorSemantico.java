package analisador;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalisadorSemantico
{
	static HashMap<String, ast.SimboloFunc> funcoes;
	static HashMap<String, ast.Tipo> 		tipos;
	static PilhaDeEscopos 					escopos;
	static ArrayList<ErroSemantico> 		erros;

	public static Analise analisar(ast.Programa programa) 
	{
		// Popula o escopo com os tipos e funções nativas da linguagem
		tipos = new HashMap<>();
		tipos.put("float", 	ast.Tipo.Float);
		tipos.put("int",   	ast.Tipo.Inteiro);
		tipos.put("string", ast.Tipo.String);
		tipos.put("bool", 	ast.Tipo.Bool);
		tipos.put("char", 	ast.Tipo.Char);
		tipos.put("short", 	ast.Tipo.Short);
		tipos.put("long", 	ast.Tipo.Long);

		tipos.put("FLOAT",  ast.Tipo.Float);
		tipos.put("INT",    ast.Tipo.Inteiro);
		tipos.put("STRING", ast.Tipo.String);
		tipos.put("BOOL", 	ast.Tipo.Bool);
		tipos.put("CHAR", 	ast.Tipo.Char);
		tipos.put("SHORT", 	ast.Tipo.Short);
		tipos.put("LONG", 	ast.Tipo.Long);
		
		funcoes = new HashMap<>();
		funcoes.put("tam", new ast.SimboloFunc("tam", ast.Tipo.Inteiro, new ast.Tipo[] { ast.Tipo.String }));

		escopos = new PilhaDeEscopos();
		escopos.abrirEscopo();

		erros = new ArrayList<>();

		// Analisa cada nó da árvore
		//
		// O analisador pode converter nós para outro tipo. Nesse caso, analisarNo()
		// irá retornar um nó novo ou o próprio nó de volta, se nenhuma
		// modificação for feita. Por isso a gente faz programa.nos.set() de novo 
		// para substituir o nó. 
		for (int i = 0; i < programa.nos.size(); i++)
		{
			programa.nos.set(i, analisarNo(programa.nos.get(i)));
		}
		return new Analise(erros);
	}

	static ast.No analisarNo(ast.No no) 
	{
		return switch (no) 
		{
			case ast.CmdDeclVariavel decl -> analisarDeclVariavel(decl);
			case ast.CmdExibe 		 cmd  -> analisarCmdExibe(cmd);
			case ast.CmdWhile        cmd  -> analisarCmdWhile(cmd);
			case ast.CmdFor          cmd  -> analisarCmdFor(cmd);
			case ast.CmdIf 			 cmd  -> analisarCmdIf(cmd);
			case ast.ExprAtribuicao  expr -> analisarAtribuicao(expr);
			case ast.ExprUnaria      expr -> analisarIncremento(expr);
			case ast.ExprFunc 		 expr -> analisarExprFunc(expr);
			default -> no;
		};
	}

	static ast.No analisarIncremento(ast.ExprUnaria expr)
	{
		// Contexto: Analisa um incremento quando ele é usado como um comando.
		// 
		// int k := 5;
		// for (i := 10; i < 5; i++) {		<-- usado como comando
		//     k++;  <-- usado como comando
		// } 
		// 
		// Obs.: Usar o método analisarExprUnaria() se o incremento estiver sendo usado de 
		// fato como uma expressão.
		// 
		// int k := 5;
		// i := k++;	<-- usado como expressão: i é '5', k é '6'.
		// 
		if (expr.op == ast.Operador.IncPos || expr.op == ast.Operador.DecPos)
		{
			analisarExprUnaria(expr);
		}
		else {
			erro(expr, "tipo de expressão não permitida nesse contexto.");
		}
		return expr;
	}

	static ast.No analisarDeclVariavel(ast.CmdDeclVariavel decl)
	{
		boolean declaracaoEhValida = true;
		// Garante que uma variável não seja redeclarada.
		if (escopos.atual().contem(decl.identificador.nome))
		{
			var msg = String.format("redeclaração da variável '%s'.", decl.identificador.nome);
			erro(decl.identificador, msg);

			// Não retornamos de imediato porque queremos analisar mais erros a frente.
			declaracaoEhValida = false;
		}

		// Garante que o tipo da variável exista
		ast.Tipo declTipo = tipos.get(decl.tipo.nome);
		if (declTipo == null)
		{
			var msg = String.format("uso de identificador não declarado '%s'.", decl.tipo.nome);
			erro(decl.tipo, msg);

			// Não retornamos de imediato porque queremos analisar mais erros a frente.
			declaracaoEhValida = false;
		}

		// Se a declaração vem acompanhada de uma expressão inicial
		if (decl.exprInicial != null)
		{
			// Analisa expressão inicial
			decl.exprInicial = analisarExpr(decl.exprInicial);

			// O próximo passo é a conversão de tipos. Para isso, é necessário
			// que o tipo da declaração exista e a expressão inicial esteja resolvida.
			//
			// Se algum deles falhou, podemos retornar agora.
			if (declTipo == null || decl.exprInicial.tipo == ast.Tipo.Indeterminado)
			{
				return decl;
			}

			// Quando o tipo da declaração e o tipo da expressão inicial são diferentes,
			// podemos converter um no outro ou emitir um erro
			if (decl.exprInicial.tipo != declTipo)
			{
				// Se são primitvos, dá pra rebaixar ou promover a expressão.
				//
				// int a := 10.0;  <-- rebaixa 10.0 para int
				// double b := a;  <-- promove a (10) para double
				// double c := 10.0;  <-- promove 10.0 (float) para double
				// 
				if (decl.exprInicial.tipo.ehPrimitivo && declTipo.ehPrimitivo)
				{
					int pExpr = prioridade(decl.exprInicial.tipo);
					int pDecl = prioridade(declTipo);

					if (pExpr != pDecl)
					{
						decl.exprInicial = new ast.ExprConversao(decl.exprInicial, declTipo);
					}
				}
				else
				{
					var msg = String.format(
						"impossível converter '%s' para '%s'", decl.exprInicial.tipo.nome, declTipo.nome
					);
					declaracaoEhValida = false;
					erro(decl, msg);
				}
			}
		}

		if (declaracaoEhValida)
		{
			ast.Simbolo simbolo = new ast.Simbolo(decl.identificador.nome, declTipo, false);
			decl.simbolo = simbolo;
			escopos.declarar(simbolo);
		}

		return decl;
	}

	static ast.No analisarAtribuicao(ast.ExprAtribuicao atrib)
	{
		ast.Simbolo simbolo = escopos.resolver(atrib.destino.nome);

		// Caso 1) Se uma atribuição não se refere a nenhuma variável existente em qualquer escopo,
		// ela cria uma nova variável (declaração).
		// 
		// x := 10;  <-- declara x como inteiro com valor 10
		//
		if (simbolo == null)
		{
			atrib.exprInicial = analisarExpr(atrib.exprInicial);
	
			ast.Simbolo novoSimbolo = new ast.Simbolo(atrib.destino.nome, atrib.exprInicial.tipo, true);
			escopos.declarar(novoSimbolo);

			var decl = new ast.CmdDeclVariavel(
				new ast.Id(atrib.exprInicial.tipo.nome),
				atrib.destino,
				atrib.exprInicial
			);
			decl.simbolo = novoSimbolo;
			return decl;
		}
		else
		{
			atrib.simboloDestino = simbolo;
			atrib.exprInicial = analisarExpr(atrib.exprInicial);
			
			if (atrib.exprInicial.tipo == ast.Tipo.Indeterminado)
			{
				return atrib;
			}
			
			// Caso 2) Se a atribuição se refere a uma variável existente e elas têm tipos iguais 
			// ou primitivos, ela atualiza o valor dessa variável.
			//
			// int a := 10;  <-- declara a como 10
			// a := 5;   <-- atualiza a para 5
			// if (1 == 1) {
			//     a := 1;  <-- atualiza a para 1
			// }
			// exibe a;  <-- imprime 1
			//
			if (atrib.exprInicial.tipo.ehPrimitivo && simbolo.tipo.ehPrimitivo)
			{
				int pTipo = prioridade(simbolo.tipo);
				int pExpr = prioridade(atrib.exprInicial.tipo);

				if (pTipo != pExpr)
				{
					atrib.exprInicial = new ast.ExprConversao(atrib.exprInicial, simbolo.tipo);
				}
				return atrib;
			}

			if (atrib.exprInicial.tipo == simbolo.tipo)
			{
				return atrib;
			}

			// Caso 3) Se existir uma variável no mesmo escopo e o valor atribuído tiver tipo diferente,
			// a atribuição cria uma nova variável com o novo tipo (redeclaração), “sombreando” a anterior.
			//
			// x := "thiago";  <-- x é string
			// x := 10;        <-- x agora é int
			// exibe x;        <-- imprime 10
			// tam(x);         <-- erro: x não é string
			//
			if(escopos.atual().id == simbolo.idEscopo)
			{
				if (!simbolo.dinamico)
				{
					erro(atrib, String.format("'%s' não permite redeclaração com outro tipo. %s %s", simbolo.nome));
					return atrib;
				}

				ast.Simbolo novoSimbolo = new ast.Simbolo(simbolo.nome, atrib.exprInicial.tipo, true);
				escopos.redeclarar(simbolo.nome, novoSimbolo);
				
				var decl = new ast.CmdDeclVariavel(
					new ast.Id(atrib.exprInicial.tipo.nome),
					atrib.destino,
					atrib.exprInicial
				);
				decl.simbolo = novoSimbolo;
				return decl;
			}
			
			// Caso 4) Se a variável e a atribuição estiverem em escopos diferentes e os tipos forem diferentes,
			// a atribuição cria uma nova variável nesse escopo.
			//
			// y := "joão";
			// if (1 == 1) {
			//     y := 5;  <-- nova y nesse escopo
			// }
			// exibe y;  <-- imprime "joão"
			// 
			ast.Simbolo novoSimbolo = new ast.Simbolo(simbolo.nome, atrib.exprInicial.tipo, true);
			escopos.declarar(novoSimbolo);

			var decl = new ast.CmdDeclVariavel(
				new ast.Id(atrib.exprInicial.tipo.nome),
				atrib.destino,
				atrib.exprInicial
			);
			decl.simbolo = novoSimbolo;
			return decl;
		}
	}

	static ast.Expr analisarExpr(ast.Expr expr)
	{
		return switch (expr) {
			case ast.ExprBinaria    exprBin   -> analisarExprBinaria(exprBin);
			case ast.ExprUnaria     exprUn    -> analisarExprUnaria(exprUn);
			case ast.ExprTernaria   exprTern  -> analisarExprTernaria(exprTern);
			case ast.ExprAtribuicao exprAtrib -> analisarExprAtribuicao(exprAtrib);
			case ast.ExprId 		id 		  -> analisarExprId(id);
			case ast.ExprBool       literal   -> literal;
			case ast.ExprFloat   	literal   -> literal;
			case ast.ExprInteiro 	literal   -> literal;
			case ast.ExprString  	literal   -> literal;
			case ast.ExprChar 		literal   -> literal;

			default -> {
				throw new Error(String.format(
					"erro interno: expressão desconhecida '%s'.", expr.getClass().getName()
				));
			}
		};
	}

	static ast.Expr analisarExprAtribuicao(ast.ExprAtribuicao atrib)
	{
		// Contexto: atribuições, além de atualizarem o valor da variável à qual se
		// referem, também retornam o valor que elas atribuem.
		//
		// int a := 10;
		// int b := a := 5;  <-- 'a := 5' retorna 5, então 'b' recebe 5.
		// 
		// int x := 10;
		// int y := 20;
		// int z := x := y;  <-- 'y' se mantém como 20, 'x' e 'z' se tornam 20.

		ast.Simbolo simbolo = escopos.resolver(atrib.destino.nome);
		
		// Expressões de atribuição sempre devem referenciar um símbolo existente.
		//
		// int k := j := 10;  <-- uso de identificador não declarado 'j'
		//
		if (simbolo == null)
		{
			erro(atrib, String.format("uso de identificador não declarado '%s'.", atrib.destino.nome));
			return atrib;
		}
		else
		{
			atrib.simboloDestino = simbolo;
			atrib.exprInicial = analisarExpr(atrib.exprInicial);

			if (atrib.exprInicial.tipo != ast.Tipo.Indeterminado)
			{
				// Quando o símbolo referenciado tem um tipo diferente da expressão atribuìda,
				// o símbolo é redeclarado. No entanto, o símbolo deve ser dinâmico.
				if (!(atrib.exprInicial.tipo.ehPrimitivo && simbolo.tipo.ehPrimitivo) && simbolo.dinamico)
				{
					ast.Simbolo novoSimbolo = new ast.Simbolo(simbolo.nome, atrib.exprInicial.tipo, true);
					escopos.redeclarar(simbolo.nome, novoSimbolo);
					atrib.tipo = atrib.exprInicial.tipo;
					atrib.simboloDestino = novoSimbolo;
				}
				else 
				{
					erro(atrib, String.format("'%s' não permite redeclaração com outro tipo.", simbolo.nome));
				}
			}
		}
		return atrib;
	}

	static ast.Expr analisarExprBinaria(ast.ExprBinaria expr)
	{
		expr.esq = analisarExpr(expr.esq);
		expr.dir = analisarExpr(expr.dir);

		if (expr.esq.tipo == ast.Tipo.Indeterminado || expr.dir.tipo == ast.Tipo.Indeterminado)
		{
			return expr;
		}

		// impede operações ilegais entre strings
		if (expr.dir.tipo == ast.Tipo.String && expr.esq.tipo == ast.Tipo.String)
		{
			if (expr.op != ast.Operador.Mais)
			{
				var msg = String.format("operador '%s' não é permitido entre strings.", expr.op.toString());
				erro(expr, msg);
			}
			expr.tipo = ast.Tipo.String;
			return expr;
		}

		// garante que aritmética seja realizada com primitivos
		if (!(expr.esq.tipo.ehPrimitivo && expr.dir.tipo.ehPrimitivo))
		{
			var msg = String.format(
				"operação '%s' entre tipos incompatíveis: esquerda é '%s', direita é '%s'.",
				expr.op.toString(), expr.esq.tipo.nome, expr.dir.tipo.nome
			);
			erro(expr, msg);
			return expr;
		}

		// garante que operações lógicas sejam realizadas entre inteiros
		if (expr.op == ast.Operador.E || expr.op == ast.Operador.Ou)
		{
			if (expr.esq.tipo == ast.Tipo.Float || expr.esq.tipo == ast.Tipo.Double)
			{
				expr.esq = new ast.ExprConversao(expr.esq, ast.Tipo.Inteiro);
			}
			if (expr.dir.tipo == ast.Tipo.Float || expr.dir.tipo == ast.Tipo.Double)
			{
				expr.dir = new ast.ExprConversao(expr.dir, ast.Tipo.Inteiro);
			}

			expr.tipo = ast.Tipo.Inteiro;
			return expr;
		}

		int pEsq = prioridade(expr.esq.tipo);
		int pDir = prioridade(expr.dir.tipo);

		if (pEsq > pDir) 
		{
			expr.dir = new ast.ExprConversao(expr.dir, expr.esq.tipo);
		}
		else if (pDir > pEsq)
		{
			expr.esq = new ast.ExprConversao(expr.esq, expr.dir.tipo);
		}

		expr.tipo = expr.esq.tipo;
		return expr;
	}

	static int prioridade(ast.Tipo tipo)
	{
		switch (tipo.id)
		{
			case ast.Tipo.ID_SHORT:  return 1;
			case ast.Tipo.ID_INT: 	 return 2;
			case ast.Tipo.ID_LONG: 	 return 3;
			case ast.Tipo.ID_FLOAT:  return 4;
			case ast.Tipo.ID_DOUBLE: return 5;
			default: 				 return 0;
		}
	}


	static ast.Expr analisarExprUnaria(ast.ExprUnaria exprUn)
	{
		exprUn.expr = analisarExpr(exprUn.expr);
		exprUn.tipo = exprUn.expr.tipo;
		return exprUn;
	}

	static ast.Expr analisarExprTernaria(ast.ExprTernaria expr)
	{
		expr.exprCond    	 = analisarExpr(expr.exprCond);
		expr.exprEntao 	 	 = analisarExpr(expr.exprEntao);
		expr.exprSenao 	 	 = analisarExpr(expr.exprSenao);

		if (expr.exprEntao.tipo != ast.Tipo.Indeterminado && expr.exprSenao.tipo != ast.Tipo.Indeterminado)
		{
			if (expr.exprEntao.tipo != expr.exprSenao.tipo)
			{
				erro(expr, "um operador ternário deve retornar expressões do mesmo tipo.");
			}
			else
			{
				expr.tipo = expr.exprEntao.tipo;
			}
		}
		
		return expr;
	}

	static ast.Expr analisarExprFunc(ast.ExprFunc exprFunc)
	{
		// trata o uso de uma função não declarada
		ast.SimboloFunc funcao = funcoes.get(exprFunc.identificador.nome);
		if (funcao == null)
		{
			erro(exprFunc, String.format("chamada de função não declarada '%s'", exprFunc.identificador.nome));
			return exprFunc;
		}

		// trata quando argumentos estão faltando
		if (exprFunc.argumentos.size() < funcao.parametros.length)
		{
			var msg = String.format(
				"função '%s' espera %d argumentos mas apenas %d foram fornecidos.", 
				funcao.nome, funcao.parametros.length, exprFunc.argumentos.size()
			);
			erro(exprFunc, msg);
		}
		// trata quando tem argumentos em excesso
		else if (exprFunc.argumentos.size() > funcao.parametros.length)
		{
			var msg = String.format("excesso de argumentos para a função %s.", funcao.nome);
			erro(exprFunc, msg);
		}

		// verifica se o tipo de cada argumento corresponde o tipo do parâmetro que ele está
		// sendo passado.
		for (int i = 0; i < funcao.parametros.length; i++)
		{
			ast.Expr exprArgumento = analisarExpr(exprFunc.argumentos.get(i));
			exprFunc.argumentos.set(i, exprArgumento);
			ast.Tipo tipoDoParametro = funcao.parametros[i];
			
			if (tipoDoParametro != exprArgumento.tipo)
			{
				if (tipoDoParametro.ehPrimitivo && exprArgumento.tipo.ehPrimitivo)
				{
					int pArg = prioridade(exprArgumento.tipo);
					int pParam = prioridade(tipoDoParametro);

					if (pArg != pParam)
					{
						exprFunc.argumentos.set(i, new ast.ExprConversao(exprArgumento, tipoDoParametro));
					}
				}
				else
				{
					var msg = String.format(
						"tipo incompatível no argumento %d de '%s': impossível converter '%s' para '%s'.", 
						i+1, funcao.nome, exprArgumento.tipo.nome, tipoDoParametro.nome
					);
					erro(exprArgumento, msg);
				}
			}
		}

		return exprFunc;
	}

	static ast.Expr analisarExprId(ast.ExprId id)
	{
		ast.Simbolo simbolo = escopos.resolver(id.nome);
		if (simbolo != null)
		{
			id.simbolo = simbolo;
			id.tipo = simbolo.tipo;
		} 
		else 
		{
			erro(id, String.format("símbolo desconhecido '%s'.", id.nome));
		}
		return id;
	}

	static ast.CmdWhile analisarCmdWhile(ast.CmdWhile cmd)
	{
		cmd.exprCondicao = analisarExpr(cmd.exprCondicao);

		escopos.abrirEscopo(); // início do escopo do laço while
		{
			if (!cmd.exprCondicao.tipo.ehPrimitivo)
			{
				var msg = String.format(
					"o tipo da expressão de condição deve ser um primitivo; encontrou '%s'", 
					cmd.exprCondicao.tipo.nome
				);
				erro(cmd.exprCondicao, msg);
			}
			analisarBloco(cmd.bloco);
		}
		escopos.fecharEscopo(); // fim do escopo do laço while
		
		return cmd;
	}

	static ast.CmdIf analisarCmdIf(ast.CmdIf cmd)
	{
		ast.Expr exprCond = analisarExpr(cmd.exprCondicao);
		if (!exprCond.tipo.ehPrimitivo)
		{
			erro(cmd.exprCondicao, "a expressão de condição de um if deve resultar num primitivo.");
		}

		analisarBloco(cmd.blocoEntao);
		if (cmd.blocoSenao != null)
		{
			analisarBloco(cmd.blocoSenao);
		}

		return cmd;
	}

	/// A expressão de inicialização do laço for é idêntica à expressão de atribuição,
	/// mas se comporta diferente: ela não causa a reatribuição de uma variável com o
	/// mesmo nome em algum escopo externo.
	/// 
	/// i := 10;
	/// 
	/// for (i := 0; i < 5; i++) {  <-- 'i := 0' não reatribui, apenas declara
	/// 	...
	/// }
	/// 
	/// exibe i;  <-- printa 10
	/// 
	/// No entanto, o valor inicial pode referenciar variáveis com mesmo nome da
	/// variável inicial que estão num escopo externo.
	/// 
	/// i := 3;
	/// 
	/// for (i := i; i < 5; i++) {	 <-- 'i' começa como 3
	/// 	...
	/// }
	/// 
	/// exibe i; <-- printa 10
	/// 
	static ast.CmdFor analisarCmdFor(ast.CmdFor cmd)
	{
		cmd.exprInicial = analisarExpr(cmd.exprInicial);

		escopos.abrirEscopo(); // início do escopo do laço for
		{
			escopos.declarar(new ast.Simbolo(cmd.varInicial.nome, cmd.exprInicial.tipo, false));
			
			cmd.exprTeste = analisarExpr(cmd.exprTeste);
			cmd.exprIterativa = analisarExpr(cmd.exprIterativa);
	
			if (!cmd.exprTeste.tipo.ehPrimitivo)
			{
				erro(cmd.exprTeste, 
					String.format(
						"a expressão de teste de um laço for deve ser um primitivo. '%s'", 
						cmd.exprTeste.tipo.nome
				));
			}
	
			analisarBloco(cmd.bloco);
		}
		escopos.fecharEscopo(); // fim do escopo do laço for
		return cmd;
	}

	static ast.CmdExibe analisarCmdExibe(ast.CmdExibe cmd)
	{
		analisarExpr(cmd.valor);
		return cmd;
	}

	static void analisarBloco(ast.Bloco bloco)
	{
		escopos.abrirEscopo(); // início do escopo do bloco
		{
			for (int i = 0; i < bloco.nos.size(); i++)
			{
				bloco.nos.set(i, analisarNo(bloco.nos.get(i)));
			}

		}
		escopos.fecharEscopo(); // fim do escopo do bloco
	}

	static void erro(ast.No no, String msg)
	{
		erros.add(new ErroSemantico(no.linha, no.coluna, msg));
	}
}