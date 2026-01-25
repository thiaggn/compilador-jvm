package intermediario;

import java.util.ArrayList;
import java.util.HashMap;

public class GeradorIR
{
	static ArrayList<ir.Instrucao>   instrucoes;
	static HashMap<Integer, ir.Slot> slots;

	static int proximoIndice;
	static int proximoMarcador;
 
	static void emitir(ir.Instrucao instrucao)
	{
		instrucoes.add(instrucao);
	}

	static ir.Marcador novoMarcador()
	{
		return new ir.Marcador(proximoMarcador++);
	}

	static ir.Slot criarSlot(int bytes, int ref, String nome)
	{
		int indice = proximoIndice;
		proximoIndice += bytes;

		ir.Slot slot = new ir.Slot(indice, nome);
		slots.put(ref, slot);

		return slot;
	}

	static ir.Slot obterSlotAssociado(int ref)
	{
		return slots.get(ref);
	}

	public static CodigoIntermediario traduzir(ast.Programa programa)
	{
		proximoIndice = 1;
		proximoMarcador = 0;
		slots = new HashMap<>();
		instrucoes = new ArrayList<>();

		emitir(new ir.Marcador("main"));
		programa.nos.forEach(no -> traduzirNo(no));
		emitir(new ir.InstrRetorno());

		return new CodigoIntermediario(instrucoes);
	}

	static void traduzirNo(ast.No no) 
	{
		switch (no)
		{
			case ast.ExprAtribuicao  exprAtrib -> traduzirAtribuicao(exprAtrib);
			case ast.CmdDeclVariavel cmdDecl   -> traduzirDeclaracao(cmdDecl);
			default                            -> throw new Error("");
		};
	}

	static void traduzirExpr(ast.Expr expr)
	{
		switch(expr)	
		{
			case ast.ExprInteiro   exprInt   -> emitir(new ir.InstrPush(exprInt.valor));
			case ast.ExprFloat     exprFloat -> emitir(new ir.InstrPush(exprFloat.valor));
			case ast.ExprBool      exprBool  -> emitir(new ir.InstrPush(exprBool.valor));
			case ast.ExprId        exprId    -> traduzirExprId(exprId);
			case ast.ExprBinaria   exprBin   -> traduzirExprBinaria(exprBin);
			default                          -> throw new Error();
		}
	}

	static void traduzirDeclaracao(ast.CmdDeclVariavel cmd)
	{
		ir.Formato formato = mapearFormato(cmd.simbolo.tipo);
		ir.Slot    slot    = criarSlot(formato.bytes, cmd.simbolo.ref, cmd.simbolo.nome);

		traduzirExpr(cmd.exprInicial);
		emitir(new ir.InstrStore(slot,formato));
	}

	static void traduzirAtribuicao(ast.ExprAtribuicao atrib)
	{
		ir.Formato formato = mapearFormato(atrib.tipo);
		ir.Slot    slot    = obterSlotAssociado(atrib.simboloDestino.ref);

		traduzirExpr(atrib.exprInicial);
		emitir(new ir.InstrStore(slot, formato));
	}

	static void traduzirExprId(ast.ExprId expr)
	{
		ir.Slot    slot    = obterSlotAssociado(expr.simbolo.ref);
		ir.Formato formato = mapearFormato(expr.tipo);

		emitir(new ir.InstrLoad(slot, formato));
	}

	static void traduzirExprBinaria(ast.ExprBinaria expr)
	{
		if (expr.operador == ast.Operador.OuOu)
		{
			ir.Marcador mTrue  = novoMarcador();
			ir.Marcador mFalse = novoMarcador();
			ir.Marcador mSaida = novoMarcador();

			traduzirExprCC(expr.esq, false, false, mTrue, mFalse);
			
			// Se a expressão da direita também for um OuOu, então ela não é a expressão mais à
			// direita da cadeia. Nessas condições, não é necessário negá-la. Além disso, 
			// sinalizamos para a função de tradução que essa expressão não é a expressão raiz.
			if (expr.dir instanceof ast.ExprBinaria bin && bin.operador == ast.Operador.OuOu)
			{
				traduzirExprCC(expr.dir, false, false, mTrue, null);
			}
			else if (expr.dir instanceof ast.ExprBinaria bin && bin.operador == ast.Operador.EE)
			{
				traduzirExprCC(expr.dir, false, false, null, mFalse);
			}
			else
			{
				traduzirExprCC(expr.dir, true, false, mFalse, null);
			}

			emitir(mTrue);
			emitir(new ir.InstrPush(1));
			emitir(new ir.InstrGoto(mSaida));

			emitir(mFalse);
			emitir(new ir.InstrPush(0));
			emitir(mSaida);
		}
		else if (expr.operador == ast.Operador.EE)
		{
			
		}
		else if (expr.operador.ehRelacional())
		{
			ir.Marcador mTrue = novoMarcador();
			ir.Marcador mSaida = novoMarcador();

			traduzirExprCC(expr, false, false, mTrue, null);

			emitir(new ir.InstrPush(0));
			emitir(mTrue);
			emitir(new ir.InstrPush(1));
			emitir(new ir.InstrGoto(mSaida));
			emitir(mSaida);
		}
	}

	/// O parâmetro 'negar' indica se a condição deve ser invertida ao gerar a instrução de salto. Por
	/// exemplo, se 'negar == true', o operador '<' vai virar um '>=' em vez de '<'. É usada só no último
	/// teste de uma expressão lógica 'OU'.
	/// 
	/// O parâmetro 'raiz' indica se a expressão passada é a primeira expressão lógica numa cadeia de 
	/// expressões lógicas.
	static void traduzirExprCC(ast.Expr expr, boolean negar, boolean raiz, ir.Marcador mT, ir.Marcador mF)
	{
		if (expr instanceof ast.ExprBinaria bin)
		{
			 traduzirExprBinariaCC(bin, negar, raiz, mT, mF);
		}
		else if (expr instanceof ast.ExprConversao conv)
		{
			traduzirExprCC(conv.alvo, negar, raiz, mT, mF);
		}
		else if (expr instanceof ast.ExprInteiro exprInt)
		{
			ir.Condicao condicao = negar ? ir.Condicao.Igual : ir.Condicao.Diferente;
			emitir(new ir.InstrPush(exprInt.valor));
			emitir(new ir.InstrZeroSaltarSe(condicao, mT, ir.Formato.Int));
		}
		else if (expr instanceof ast.ExprFloat exprFloat)
		{
			ir.Condicao condicao = negar ? ir.Condicao.Igual : ir.Condicao.Diferente;
			emitir(new ir.InstrPush(exprFloat.valor));
			emitir(new ir.InstrZeroSaltarSe(condicao, mT, ir.Formato.Float));
		}
		else if (expr instanceof ast.ExprId exprId)
		{
			ir.Condicao condicao = negar ? ir.Condicao.Igual : ir.Condicao.Diferente;
			ir.Formato  formato  = mapearFormato(exprId.tipo);
			ir.Slot     slot     = obterSlotAssociado(exprId.simbolo.ref);

			emitir(new ir.InstrLoad(slot, formato));
			emitir(new ir.InstrZeroSaltarSe(condicao, mT, formato));
		}
		else
		{
			throw new Error(String.format("não suportado: '%s'", expr.getClass().getName()));
		}
	}

	static void traduzirExprBinariaCC(ast.ExprBinaria expr, boolean negar, boolean raiz, ir.Marcador mT, ir.Marcador mF)
	{
		if (expr.operador == ast.Operador.EE) 
		{
			traduzirExprCC(expr.esq, false, false, mF, null);
			traduzirExprCC(expr.dir, false, false, mF, null);
		}
		else if (expr.operador == ast.Operador.OuOu)
		{
			traduzirExprCC(expr.esq, false, raiz, mT, mF);

			if (raiz == false) traduzirExprCC(expr.dir, false, false, mF, null);
			else               traduzirExprCC(expr.dir, true,  false, mT, mF);
		}
		else if (expr.operador.ehRelacional())
		{
			ir.Formato  fmt  = mapearFormato(expr.tipo);
			ir.Condicao cond = mapearCondicao(expr.operador);

			traduzirExpr(expr.esq);
			traduzirExpr(expr.dir);
			emitir(new ir.InstrSaltarSe(negar ? cond.negado() : cond, mT, fmt));
		}
	}

	static ir.Formato mapearFormato(ast.SimboloTipo tipo)
	{
		return switch(tipo.primitivo)
		{
			case ast.Primitivo.Int   -> ir.Formato.Int;
			case ast.Primitivo.Float -> ir.Formato.Float;
			default                  -> throw new Error();
		};
	}

	static ir.Operacao mapearOperacao(ast.Operador op)
	{
		return switch (op)
		{
			case ast.Operador.Mais  -> ir.Operacao.Adicao;
			case ast.Operador.Menos -> ir.Operacao.Subtracao;
			case ast.Operador.Mul   -> ir.Operacao.Multiplicacao;
			case ast.Operador.Div   -> ir.Operacao.Divisao;
			case ast.Operador.Resto -> ir.Operacao.Resto;
			default                 -> throw new Error("Operador deve ser aritmetico");
		};
	}

	static ir.Condicao mapearCondicao(ast.Operador op)
	{
		return switch (op)
		{
			case ast.Operador.Menor   -> ir.Condicao.Menor;
			case ast.Operador.MenorIg -> ir.Condicao.MenorIg;
			case ast.Operador.Maior   -> ir.Condicao.Maior;
			case ast.Operador.MaiorIg -> ir.Condicao.MaiorIg;
			case ast.Operador.Igual   -> ir.Condicao.Igual;
			case ast.Operador.Dif     -> ir.Condicao.Diferente;
			default                   -> throw new Error("Operador deve ser relacional");
		};
	}
}
