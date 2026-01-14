package ast;

public class Print {
	static int nivel = 0;

	public static void abreArray(String nome) {
		Print.abreCampo(nome);
		Out.format("[\n");
		Print.nivel += 1;
	}

	public static void fechaArray() {
		Print.nivel -= 1;
		Print.tab();
		Out.format("]");
		Print.fechaCampo();
	}

	public static void itemDeArray(Printavel no) {
		Print.tab();
		no.print();
		Out.format(",\n");
	}

	public static void campoComObjeto(String nome, Printavel no) {
		Print.abreCampo(nome);
		no.print();
		Print.fechaCampo();
	}


	public static void campoComValor(String nome, String valor) {
		Print.abreCampo(nome);
		Print.valorDeCampo(valor);
		Print.fechaCampo();
	}

	private static void tab() {
		for (int i = 0; i < Print.nivel; i++) {
			Out.format("   ");
		}
	}

	public static void abreObjeto(String tipo) {
		Out.format("\u001B[93m%s\u001B[0m {\n", tipo);
		Print.nivel += 1;
	}

	public static void fechaObjeto() {
		Print.nivel -= 1;
		Print.tab();
		Out.format("}");
	}

	public static void valorDeCampo(String nome) {
		Out.format("%s", nome);
	}

	public static void valorDeCampoObj(Printavel no) {
		no.print();
	}

	private static void abreCampo(String nome) {
		Print.tab();
		Out.format("\u001B[90m%s\u001B[0m: ", nome);
	}

	private static void fechaCampo() {
		Out.format("\n");
	}
}

class Out {
	static void linha(Object obj) {
		System.out.println(obj);
	}

	static void linha() {
		System.out.println();
	}

	static void format(String formato, Object... args) {
		System.out.printf(formato, args);
	}
}