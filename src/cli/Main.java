package cli;
import core.*;

public class Main {
	public static void main (){
		System.out.println("Non fonctionnel - donne le nombre de t�ches");
		Manager global = Manager.createFromFile("data");
		System.out.print(global.tasks.size());
	}
}
