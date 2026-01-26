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

	// ---------------------------- Expressões como valor ---------------------------
	static void traduzirExpr(ast.Expr expr)
	{
		switch(expr)	
		{
			case ast.ExprInteiro   exprInt   -> emitir(new ir.InstrPush(exprInt.valor));
			case ast.ExprFloat     exprFloat -> emitir(new ir.InstrPush(exprFloat.valor));
			case ast.ExprBool      exprBool  -> emitir(new ir.InstrPush(exprBool.valor));
			case ast.ExprId        exprId    -> traduzirExprId(exprId);
			case ast.ExprBinaria   exprBin   -> traduzirExprBinaria(exprBin);
			case ast.ExprConversao exprConv  -> traduzirExprConversao(exprConv);

			default   -> throw new Error(
				String.format("Impossivel traduzir expressão '%s'", expr.getClass().getName())
			);
		}
	}

	static void traduzirExprConversao(ast.ExprConversao conv)
	{
		if (conv.alvo.tipo.primitivo == conv.tipo.primitivo)
		{
			traduzirExpr(conv.alvo);
		}
	}

	static void traduzirExprBinaria(ast.ExprBinaria expr)
	{
		switch (expr.operador)
		{
			case ast.Operador.Mais:
			case ast.Operador.Menos:
			case ast.Operador.Mul:
			case ast.Operador.Div:
				traduzirExprAritmetica(expr);
				break;

			case ast.Operador.MenorIg:
			case ast.Operador.Menor:
			case ast.Operador.MaiorIg:
			case ast.Operador.Maior:
			case ast.Operador.Dif:
			case ast.Operador.Igual:
				traduzirExprRelacional(expr);
				break;

			case ast.Operador.OuOu:
			case ast.Operador.EE:
				traduzirExprLogica(expr);
				break;

			default:
				throw new Error("Impossível traduzir expressão binária");
		}
	}

	static void traduzirExprLogica(ast.ExprBinaria expr)
	{
		if (expr.operador == ast.Operador.OuOu)
		{
			ir.Marcador mS = novoMarcador();
			ir.Marcador mF = novoMarcador();
			ir.Marcador mV = novoMarcador();

			// expressão da esquerda
			traduzirExprCC(expr.esq, false, mV, mF);
			traduzirExprCC(expr.dir, true,  mV, mF);

			// instruções
			emitir(mV);
			emitir(new ir.InstrPush(1));
			emitir(new ir.InstrGoto(mS));

			emitir(mF);
			emitir(new ir.InstrPush(0));
			
			emitir(mS);
		}
		else if (expr.operador == ast.Operador.EE)
		{

		}
	}

	static void traduzirExprRelacional(ast.ExprBinaria expr)
	{
		ir.Marcador mV = novoMarcador();
		ir.Marcador mS = novoMarcador();

		traduzirExprRelacionalCC(expr, false, mV);
		emitir(new ir.InstrPush(0));
		emitir(new ir.InstrGoto(mS));
		emitir(mV);
		emitir(new ir.InstrPush(1));
		emitir(new ir.InstrGoto(mS));
		emitir(mS);
	}

	static void traduzirExprAritmetica(ast.ExprBinaria expr)
	{
		ir.Operacao operacao = mapearOperacao(expr.operador);
		ir.Formato  formato  = mapearFormato(expr.tipo);

		traduzirExpr(expr.esq);
		traduzirExpr(expr.dir);
		emitir(new ir.InstrOpBinaria(operacao, formato));
	}

	// --------------------------- Expressões como curto circuito --------------------------

	static void traduzirExprCC(ast.Expr expr, boolean negar, ir.Marcador mV, ir.Marcador mF)
	{
		if (expr instanceof ast.ExprBinaria exprBin)
		{
			switch (exprBin.operador)
			{
				case ast.Operador.MenorIg:
				case ast.Operador.Menor:
				case ast.Operador.MaiorIg:
				case ast.Operador.Maior:
				case ast.Operador.Dif:
				case ast.Operador.Igual:
					traduzirExprRelacionalCC(exprBin, negar, mV);
					break;
				
				case ast.Operador.OuOu: 
				case ast.Operador.EE:
					traduzirExprLogicaCC(exprBin, negar, mV, mF);
					break;

				default:
					 throw new Error("impossível de converter expessão");
			}
		}
		else if (expr instanceof ast.ExprConversao exprConv)
		{
			traduzirExprCC(exprConv.alvo, negar, mV, mF);
		}
		else if (expr instanceof ast.ExprInteiro exprInt)
		{
			ir.Condicao condicao = negar ? ir.Condicao.Igual : ir.Condicao.Diferente;
			emitir(new ir.InstrPush(exprInt.valor));
			emitir(new ir.InstrZeroSaltarSe(condicao, mV, ir.Formato.Int));
		}
		else if (expr instanceof ast.ExprId exprId)
		{
			ir.Condicao condicao = negar ? ir.Condicao.Igual : ir.Condicao.Diferente;
			ir.Formato  formato  = mapearFormato(exprId.tipo);
			ir.Slot     slot     = obterSlotAssociado(exprId.simbolo.ref);

			emitir(new ir.InstrLoad(slot, formato));
			emitir(new ir.InstrZeroSaltarSe(condicao, mV, formato));
		}
		else
		{
			throw new Error(String.format("não suportado: '%s'", expr.getClass().getName()));
		}
	}

	static void traduzirExprLogicaCC(ast.ExprBinaria expr, boolean negar, ir.Marcador mV, ir.Marcador mF)
	{
		if (expr.operador == ast.Operador.OuOu)
		{
			traduzirExprCC(expr.esq, false, mV, mF);
			traduzirExprCC(expr.dir, negar, mF, null);
		}
		else if (expr.operador == ast.Operador.EE)
		{
			traduzirExprCC(expr.esq, false, mF, null);
			traduzirExprCC(expr.dir, false, mF, null);
		}
	}

	static void traduzirExprRelacionalCC(ast.ExprBinaria expr, boolean negar, ir.Marcador mV)
	{
		ir.Formato  formato  = mapearFormato(expr.tipo);
		ir.Condicao condicao = mapearCondicao(expr.operador);

		traduzirExpr(expr.esq);
		traduzirExpr(expr.dir);
		emitir(new ir.InstrSaltarSe(negar ? condicao.inverso() : condicao, mV, formato));
	}

	// --------------------------------------------------------------------------------------

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

	static ir.Formato mapearFormato(ast.SimboloTipo tipo)
	{
		return switch(tipo.primitivo)
		{
			case ast.Primitivo.Int   -> ir.Formato.Int;
			case ast.Primitivo.Float -> ir.Formato.Float;
			default                  -> throw new Error("Impossível mapear formato");
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
