package ir;

class Print {
	
	static void instrucao(String nome)
	{
		System.out.printf("\u001B[90m    %s\u001B[0m\n", nome);
	}
	
	static void instrucao(String nome, String operando)
	{
		System.out.printf("\u001B[90m    %s\u001B[0m\u001B[14G%s\n", nome, operando);
	}

	static void instrucao(String nome, int operando)
	{
		System.out.printf("\u001B[90m    %s\u001B[0m\u001B[14G%d\n", nome, operando);
	}

	static void instrucao(String nome, float operando)
	{
		java.math.BigDecimal bd =
			new java.math.BigDecimal(Float.toString(operando))
				.stripTrailingZeros();

		if (bd.scale() < 1) {
			bd = bd.setScale(1);
		}

		System.out.printf("\u001B[90m    %s\u001B[0m\u001B[14G%s%n", nome, bd.toPlainString());
	}

	static void instrucao(String nome, String op1, int op2)
	{
		System.out.printf("\u001B[90m    %s\u001B[0m\u001B[14G%s, %d\n", nome, op1, op2);
	}

	static void quebra()
	{
		System.out.println();
	}

	static void marcador(String nome)
	{
		System.out.printf("\u001B[93m%s:\u001B[0m\n", nome);
	}
}
