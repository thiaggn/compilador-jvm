package ir;

public class InstrSaltarSe extends Instrucao {
	public Condicao condicao;
	public Marcador marcador;

	public InstrSaltarSe(Condicao condicao, Marcador marcador)
	{
		this.condicao = condicao;
		this.marcador = marcador;
	} 

	@Override
	void print() {
		
	}
	
}
