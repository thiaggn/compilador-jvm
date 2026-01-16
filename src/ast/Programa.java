package ast;

import java.util.ArrayList;


public class Programa implements Debug {
	public ArrayList<No> nos;

	public Programa(ArrayList<No> nos) {
		this.nos = nos;
	}

	@Override
	public void print() {
		Print.abreObjeto("Programa");
		Print.abreArray("nos");
		for (No no : this.nos) {
			Print.itemDeArray(no);
		}
		Print.fechaArray();
		Print.fechaObjeto();
		System.out.println();
	}
}
