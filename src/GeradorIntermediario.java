

import java.util.ArrayList;
import java.util.HashMap;

public class GeradorIntermediario
{
	static ArrayList<ir.ItemIntermediario> itens;
	static HashMap<Integer, ir.Slot>       slots;

	static int contadorDeSlots;
	static int contadorDeMarcadores;
 
	static void emitir(ir.ItemIntermediario... itens)
	{
		for (ir.ItemIntermediario i : itens)
		{
			GeradorIntermediario.itens.add(i);
		}
	}

	static ir.Marcador novoMarcador()
	{
		return new ir.Marcador(contadorDeMarcadores++);
	}

	static ir.Slot novoSlot(ir.Formato formato, int ref, String nome)
	{
		int indice = contadorDeSlots;
		contadorDeSlots += formato.tamanho();

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
		contadorDeSlots = 1;
		contadorDeMarcadores = 0;
		slots = new HashMap<>();
		itens = new ArrayList<>();

		emitir(new ir.Marcador("main"));
		programa.nos.forEach(no -> traduzirNo(no));
		emitir(new ir.InstrRetorno());

		return new CodigoIntermediario(itens, contadorDeSlots);
	}

	static void traduzirNo(ast.No no) 
	{
		switch (no)
		{
			case ast.ExprAtribuicao  exprAtrib -> traduzirAtribuicao(exprAtrib);
			case ast.ExprFunc		 exprFunc  -> traduzirChamadaFunc(exprFunc, false);
			case ast.CmdDeclVariavel cmdDecl   -> traduzirDeclaracao(cmdDecl);
			case ast.CmdFor          cmdFor    -> traduzirFor(cmdFor);
			case ast.CmdWhile        cmdFor    -> traduzirWhile(cmdFor);
			case ast.CmdIf           cmdFor    -> traduzirIf(cmdFor, null, true);
			case ast.ExprUnaria      expr      -> traduzirIncremento(expr);
			case ast.CmdExibe		 cmdExibe  -> traduzirExibe(cmdExibe);
			default                            -> throw new Error("");
		};
	}

	static void traduzirChamadaFunc(ast.ExprFunc exprFunc, boolean empilharRetorno)
	{
		exprFunc.argumentos.forEach(arg -> traduzirExpr(arg));
		emitir(new ir.InstrChamadaFunc(exprFunc.identificador.nome));
	}

	static void traduzirExibe(ast.CmdExibe cmdExibe)
	{
		ir.Formato formato = mapearFormato(cmdExibe.valor.tipo);
		emitir(new ir.InstrCarregarEstatico("stdout"));
		traduzirExpr(cmdExibe.valor);
		emitir(new ir.InstrChamadaFunc("println", formato));
	}

	static void traduzirIncremento(ast.ExprUnaria exprUn)
	{
		ast.ExprId exprId = (ast.ExprId) exprUn.expr;
		ir.Formato formato = mapearFormato(exprId.tipo);
		ir.Slot    slot    = obterSlotAssociado(exprId.simbolo.ref);
		
		if (exprUn.operador == ast.Operador.IncPos) 
		{
			emitir(new ir.InstrInc(slot, 1, formato));	
		}
		else if (exprUn.operador == ast.Operador.DecPos)
		{
			emitir(new ir.InstrInc(slot, -1, formato));
		}	
	}

	static void traduzirDeclaracao(ast.CmdDeclVariavel cmd)
	{
		ir.Formato formato = mapearFormato(cmd.simbolo.tipo);
		ir.Slot    slot    = novoSlot(formato, cmd.simbolo.ref, cmd.simbolo.nome);

		traduzirExpr(cmd.exprInicial);
		emitir(new ir.InstrSalvar(slot, formato));
	}

	static void traduzirAtribuicao(ast.ExprAtribuicao atrib)
	{
		ir.Formato formato = mapearFormato(atrib.tipo);
		ir.Slot    slot    = obterSlotAssociado(atrib.simboloDestino.ref);

		traduzirExpr(atrib.exprInicial);
		emitir(new ir.InstrSalvar(slot, formato));
	}

	static void traduzirFor(ast.CmdFor cmdFor)
	{
		ir.Formato formato = mapearFormato(cmdFor.decl.simbolo.tipo);
		ir.Slot    slot    = novoSlot(formato, cmdFor.decl.simbolo.ref, cmdFor.decl.simbolo.nome);

		traduzirExpr(cmdFor.decl.exprInicial);
		emitir(new ir.InstrSalvar(slot, formato));

		ir.Marcador mInicio  = novoMarcador();
		ir.Marcador mSaida   = novoMarcador();
		ir.Marcador eEntrada = novoMarcador();

		// expressão de teste
		emitir(mInicio);
		traduzirExprCC(cmdFor.exprTeste, eEntrada, mSaida);

		// corpo do laço
		emitir(eEntrada);
		cmdFor.bloco.nos.forEach(no -> traduzirNo(no));

		// execução da expressão iterativa (ex.: i++)
		traduzirNo(cmdFor.exprIterativa);

		// volta para a expressão de teste
		emitir(new ir.InstrSaltar(mInicio));
		emitir(mSaida);
	}

	static void traduzirWhile(ast.CmdWhile cmdWhile)
	{
		ir.Marcador mInicio  = novoMarcador();
		ir.Marcador mEntrada = novoMarcador();
		ir.Marcador mSaida   = novoMarcador();

		// expressão de teste
		emitir(mInicio);
		traduzirExprCC(cmdWhile.exprCondicao, mEntrada, mSaida);

		// corpo do laço
		emitir(mEntrada);
		cmdWhile.bloco.nos.forEach(no -> traduzirNo(no));

		// volta para a expressão de teste
		emitir(new ir.InstrSaltar(mInicio));
		emitir(mSaida);
	}

	static void traduzirIf(ast.CmdIf cmdIf, ir.Marcador mSaida, boolean emitirSaida)
	{
		ir.Marcador mEntao = novoMarcador();
		mSaida = mSaida == null ? novoMarcador() : mSaida;

		if (cmdIf.senao == null)
		{
			traduzirExprCC(cmdIf.exprCondicao, mEntao, mSaida);
			emitir(mEntao);
			cmdIf.blocoEntao.nos.forEach(no -> traduzirNo(no));
		}
		else
		{
			ir.Marcador mSenao = novoMarcador();
			traduzirExprCC(cmdIf.exprCondicao, mEntao, mSenao);

			emitir(mEntao);
			cmdIf.blocoEntao.nos.forEach(no -> traduzirNo(no));
			emitir(new ir.InstrSaltar(mSaida));

			if (cmdIf.senao instanceof ast.Bloco blocoSenao)
			{
				emitir(mSenao);
				blocoSenao.nos.forEach(no -> traduzirNo(no));
			}
			else if (cmdIf.senao instanceof ast.CmdIf cmdElseIf)
			{
				emitir(mSenao);
				traduzirIf(cmdElseIf, mSaida, false);
			}
		}

		if (emitirSaida) emitir(mSaida);
	}

	static void traduzirExpr(ast.Expr expr)
	{
		switch(expr)	
		{
			case ast.ExprInteiro   exprInt    -> emitir(new ir.InstrEmpilhar(exprInt.valor));
			case ast.ExprFloat     exprFloat  -> emitir(new ir.InstrEmpilhar(exprFloat.valor));
			case ast.ExprBool      exprBool   -> emitir(new ir.InstrEmpilhar(exprBool.valor));
			case ast.ExprString    exprStr    -> emitir(new ir.InstrEmpilhar(exprStr.valor));
			case ast.ExprId        exprId     -> traduzirExprId(exprId);
			case ast.ExprBinaria   exprBin    -> traduzirExprBinaria(exprBin);
			case ast.ExprConversao exprConv   -> traduzirExprConversao(exprConv);

			default -> throw new Error(
				String.format("Impossivel traduzir expressão '%s'", expr.getClass().getName())
			);
		}
	}

	static void traduzirExprId(ast.ExprId expr)
	{
		ir.Slot    slot    = obterSlotAssociado(expr.simbolo.ref);
		ir.Formato formato = mapearFormato(expr.tipo);

		emitir(new ir.InstrCarregar(slot, formato));
	}

	static void traduzirExprConversao(ast.ExprConversao conv)
	{
		if (conv.alvo.tipo.primitivo == conv.tipo.primitivo)
		{
			traduzirExpr(conv.alvo);
		}
		else
		{
			ir.Formato formatoOrigem  = mapearFormato(conv.alvo.tipo);
			ir.Formato formatoDestino = mapearFormato(conv.tipo);
			emitir(new ir.InstrConversao(formatoOrigem, formatoDestino));
		}
	}

	static void traduzirConcatencao(ast.ExprBinaria expr) 
	{
		if (expr.esq instanceof ast.ExprString exprStr)
		{
			emitir(new ir.InstrEmpilhar(exprStr.valor));
		}
		else if (expr.esq instanceof ast.ExprId exprId)
		{
			ir.Formato formato = mapearFormato(exprId.tipo);
			ir.Slot    slot    = obterSlotAssociado(exprId.simbolo.ref);
			emitir(new ir.InstrCarregar(slot, formato));
		}
		if (expr.dir instanceof ast.ExprString exprStr)
		{
			emitir(new ir.InstrEmpilhar(exprStr.valor));
		}
		else if (expr.dir instanceof ast.ExprId exprId)
		{
			ir.Formato formato = mapearFormato(exprId.tipo);
			ir.Slot    slot    = obterSlotAssociado(exprId.simbolo.ref);
			emitir(new ir.InstrCarregar(slot, formato));
		}

		emitir(new ir.InstrChamadaFunc("concat"));
	}

	static void traduzirExprBinaria(ast.ExprBinaria expr)
	{
		if (expr.tipo == ast.SimboloTipo.String)
		{
			traduzirConcatencao(expr);
			return;
		}

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
			case ast.Operador.OuOu:
			case ast.Operador.EE:
				traduzirExprBooleana(expr);
				break;

			default:
				throw new Error("Impossível traduzir expressão binária");
		}
	}

	static void traduzirExprAritmetica(ast.ExprBinaria expr)
	{
		ir.Operacao operacao = mapearOperacao(expr.operador);
		ir.Formato  formato  = mapearFormato(expr.tipo);

		traduzirExpr(expr.esq);
		traduzirExpr(expr.dir);
		emitir(new ir.InstrOpBinaria(operacao, formato));
	}

	static void traduzirExprBooleana(ast.ExprBinaria expr)
	{
		ir.Marcador mSaida   = novoMarcador();
		ir.Marcador mFalso   = novoMarcador();
		ir.Marcador mVerdade = novoMarcador();

		traduzirExprCC(expr, mVerdade, mFalso);

		emitir(
			mVerdade,
			new ir.InstrEmpilhar(1),
			new ir.InstrSaltar(mSaida),

			mFalso,
			new ir.InstrEmpilhar(0),
			new ir.InstrSaltar(mSaida),

			mSaida
		);
	}

	static void traduzirExprCC(ast.Expr expr, ir.Marcador mV, ir.Marcador mF)
	{
		if (expr instanceof ast.ExprBinaria exprBin)
		{
			traduzirExprBinariaCC(exprBin, mV, mF);
		}
		else if (expr instanceof ast.ExprConversao exprConv)
		{
			traduzirExprCC(exprConv.alvo, mV, mF);
		}
		else if (expr instanceof ast.ExprInteiro exprInt)
		{
			emitir(
				new ir.InstrEmpilhar(exprInt.valor),
				new ir.InstrSaltarSe(ir.Condicao.DiferenteZero, mV, ir.Formato.Int),
				new ir.InstrSaltar(mF)
			);
		}
		else if (expr instanceof ast.ExprFloat exprFloat)
		{
			emitir(
				new ir.InstrEmpilhar(exprFloat.valor),
				new ir.InstrSaltarSe(ir.Condicao.DiferenteZero, mV, ir.Formato.Float),
				new ir.InstrSaltar(mF)
			);
		}
		else if (expr instanceof ast.ExprId exprId)
		{
			ir.Formato formato = mapearFormato(exprId.tipo);
			ir.Slot    slot    = obterSlotAssociado(exprId.simbolo.ref);

			emitir(
				new ir.InstrCarregar(slot, formato),
				new ir.InstrSaltarSe(ir.Condicao.DiferenteZero, mV, formato),
				new ir.InstrSaltar(mF)
			);
		}
		else
		{
			throw new Error();
		}
	}

	static void traduzirExprBinariaCC(ast.ExprBinaria expr, ir.Marcador mV, ir.Marcador mF)
	{
		switch (expr.operador)
		{
			case ast.Operador.Mais:
			case ast.Operador.Menos:
			case ast.Operador.Mul:
			case ast.Operador.Div:
			case ast.Operador.Resto:
				traduzirExprAritmeticaCC(expr, mV, mF);
				break;

			case ast.Operador.MenorIg:
			case ast.Operador.Menor:
			case ast.Operador.MaiorIg:
			case ast.Operador.Maior:
			case ast.Operador.Dif:
			case ast.Operador.Igual:
				traduzirExprRelacionalCC(expr, mV, mF);
				break;

			case ast.Operador.OuOu:
			case ast.Operador.EE:
				traduzirExprLogicaCC(expr, mV, mF);
				break;

			default:
				throw new Error(String.format("Operador não suportado '%s'", expr.operador.toString()));
		}
	}

	static void traduzirExprAritmeticaCC(ast.ExprBinaria expr, ir.Marcador mV, ir.Marcador mF)
	{
		ir.Formato fmt = mapearFormato(expr.tipo);
		traduzirExpr(expr);
		emitir(
			new ir.InstrSaltarSe(ir.Condicao.DiferenteZero, mV, fmt),
			new ir.InstrSaltar(mF)
		);
	}

	static void traduzirExprRelacionalCC(ast.ExprBinaria expr, ir.Marcador mV, ir.Marcador mF)
	{
		ir.Condicao cond = mapearCondicao(expr.operador);
		ir.Formato  fmt  = mapearFormato(expr.tipo);

		traduzirExpr(expr.esq);
		traduzirExpr(expr.dir);
		emitir(
			new ir.InstrSaltarSe(cond, mV, fmt),
			new ir.InstrSaltar(mF)
		);
	}

	static void traduzirExprLogicaCC(ast.ExprBinaria expr, ir.Marcador mV, ir.Marcador mF)
	{
		if (expr.operador == ast.Operador.EE)
		{
			ir.Marcador mDireita = novoMarcador();
			traduzirExprCC(expr.esq, mDireita, mF);
			emitir(mDireita);
			traduzirExprCC(expr.dir, mV, mF);
		}
		else if (expr.operador == ast.Operador.OuOu)
		{
			ir.Marcador mDireita = novoMarcador();
			traduzirExprCC(expr.esq, mV, mDireita);
			emitir(mDireita);
			traduzirExprCC(expr.dir, mV, mF);
		}
	}

	static ir.Formato mapearFormato(ast.SimboloTipo tipo)
	{
		return switch(tipo.primitivo)
		{
			case ast.Primitivo.Int        -> ir.Formato.Int;
			case ast.Primitivo.Float      -> ir.Formato.Float;
			case ast.Primitivo.String     -> ir.Formato.String;
			case ast.Primitivo.Referencia -> ir.Formato.Referencia;
			default                       -> throw new Error("Impossível mapear formato");
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
