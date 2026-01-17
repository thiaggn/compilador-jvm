package ast;

public class SimboloFunc
{
	public final String nome;
	public final int    id;
	public final SimboloTipo   tipoRetorno;
	public final SimboloTipo[] parametros;

	static int proximoId = 0;
	public SimboloFunc(String nome, SimboloTipo tipoRetorno, SimboloTipo[] tipoParametros)
	{
		this.id          = proximoId++;
		this.nome        = nome;
		this.tipoRetorno = tipoRetorno;
		this.parametros  = tipoParametros;
	}
}
