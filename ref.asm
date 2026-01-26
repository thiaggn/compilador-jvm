
class Main {
	public static void main(String[] args) {
		int a = 5;
		int b = 6;
		float c = 7.0f;

		if (a != 0 && b != 0 && c != 0) {
			a = a * 2;
		} 
		else if (a == 0 || b == 0 || c == 0)
		{
			b = b * 4;
		}
		else 
		{
			c = c * 8.0f;
		}

		a = 9;
	}
}

{
	"java.project.sourcePaths": [
		"src"
	]
}