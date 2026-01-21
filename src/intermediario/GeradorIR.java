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

	static ir.Marcador criarMarcador()
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

	public static ArrayList<ir.Instrucao> traduzir(ast.Programa programa)
	{
		proximoIndice = 1;
		proximoMarcador = 0;
		slots = new HashMap<>();
		instrucoes = new ArrayList<>();

		for (ast.No no : programa.nos) 
		{
			traduzirNo(no);
		}

		return instrucoes;
	}

	static void traduzirNo(ast.No no) 
	{
		switch (no)
		{
			case ast.ExprAtribuicao  atrib -> traduzirAtribuicao(atrib);
			case ast.CmdDeclVariavel cmd   -> traduzirDeclaracao(cmd);
			case ast.CmdWhile        cmd   -> traduzirWhile(cmd);
			case ast.CmdFor          cmd   -> traduzirFor(cmd);
			case ast.CmdExibe        cmd   -> traduzirExibe(cmd);
			case ast.CmdIf           cmd   -> traduzirIf(cmd);

			default -> throw new Error("");
		};
	}

	static void traduzirAtribuicao(ast.ExprAtribuicao atrib)
	{

	}

	static void traduzirDeclaracao(ast.CmdDeclVariavel cmd)
	{
		ir.Formato formato = resolverFormato(cmd.simbolo.tipo);
		ir.Slot    slot    = criarSlot(formato.bytes, cmd.simbolo.ref, cmd.simbolo.nome);

		traduzirExpr(cmd.exprInicial);
		emitir(new ir.Store(slot,formato));
	}

	static void traduzirFor(ast.CmdFor cmd)
	{

	}

	static void traduzirWhile(ast.CmdWhile cmd)
	{

	}

	static void traduzirExibe(ast.CmdExibe cmd)
	{

	}

	static void traduzirIf(ast.CmdIf cmd)
	{

	}

	static void traduzirExpr(ast.Expr expr)
	{
		switch(expr)	
		{
			case ast.ExprBinaria   exprBin  -> traduzirExprBinaria(exprBin);
			case ast.ExprConversao exprConv -> traduzirExprConversao(exprConv);
			case ast.ExprId        id       -> traduzirExprId(id);
			case ast.ExprInteiro   literal  -> emitir(new ir.InstrPush(literal.valor));
			case ast.ExprFloat     literal  -> emitir(new ir.InstrPush(literal.valor));
			case ast.ExprBool      literal  -> emitir(new ir.InstrPush(literal.valor));
			default -> {}
		}
	}

	static void traduzirExprId(ast.ExprId expr)
	{
		ir.Slot slot = obterSlotAssociado(expr.simbolo.ref);
		emitir(new ir.InstrLoad(slot));
	}

	static void traduzirExprUnaria(ast.ExprBinaria expr)
	{

	}

	static void traduzirExprBinaria(ast.ExprBinaria expr)
	{
		traduzirExpr(expr.esq);
		traduzirExpr(expr.dir);

		ir.Formato formato = resolverFormato(expr.tipo);

		switch (expr.op)
		{
			case ast.Operador.Mais:
				emitir(new ir.InstrOpBinaria(ir.Operacao.Adicao, formato));
				break;
			case ast.Operador.Menos:
				emitir(new ir.InstrOpBinaria(ir.Operacao.Subtracao, formato));
				break;
			case ast.Operador.Mul:
				emitir(new ir.InstrOpBinaria(ir.Operacao.Multiplicacao, formato));
				break;
			case ast.Operador.Div:
				emitir(new ir.InstrOpBinaria(ir.Operacao.Divisao, formato));
				break;
			case ast.Operador.Resto:
				emitir(new ir.InstrOpBinaria(ir.Operacao.Resto, formato));
				break;

			case ast.Operador.Menor:
	
				break;

			case ast.Operador.MenorIg:
				break;

			case ast.Operador.Maior:
				break;

			case ast.Operador.MaiorIg:
				break;

			case ast.Operador.Igual:
				break;

			case ast.Operador.Dif:
				break;

			default:
				throw new Error();
		}
	}

	static void traduzirExprConversao(ast.ExprConversao expr)
	{
		ir.Formato origem = resolverFormato(expr.alvo.tipo);
		ir.Formato destino = resolverFormato(expr.tipo);
		emitir(new ir.InstrConversao(origem, destino));
	}
	
	static ir.Formato resolverFormato(ast.SimboloTipo tipo)
	{
		if (tipo == ast.SimboloTipo.Float)   return ir.Formato.Float;
		if (tipo == ast.SimboloTipo.Inteiro) return ir.Formato.Int;
		throw new Error();
	}

}
