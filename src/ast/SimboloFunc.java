package ast;

public class SimboloFunc {
	public final String nome;
	public final int id;
	public final Tipo tipoRetorno;
	public final Tipo[] parametros;

	static int proximoId = 0;
	public SimboloFunc(String nome, Tipo tipoRetorno, Tipo[] tipoParametros) {
		this.id = proximoId++;
		this.nome = nome;
		this.tipoRetorno = tipoRetorno;
		this.parametros = tipoParametros;
	}
}
