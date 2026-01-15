package ast;

import java.util.ArrayList;
import java.util.List;

public class Bloco extends No {
	public List<No> nos;

	public Bloco(List<No> nos) {
		super(0, 0);
		this.nos = nos;
	}

	public Bloco(No no) {
		super(0, 0);
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
