package ast;

import java.util.ArrayList;
import java.util.List;

public class Bloco implements Printavel {
	public List<No> nos;

	public Bloco(List<No> nos) {
		this.nos = nos;
	}

	public Bloco(No no) {
		this.nos = new ArrayList<>();
		this.nos.add(no);
	}

	@Override
	public void print() {
		Print.abreObjeto("Bloco");
		Print.abreArray("nós");
		for (No no : this.nos) {
			Print.itemDeArray(no);
		}
		Print.fechaArray();
		Print.fechaObjeto();
	}
}
