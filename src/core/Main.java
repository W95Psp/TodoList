package core;

public class Main {
	public static void main (String[] args){
		if(args.length>0 && args[0]=="cli")
			new cli.Main();
		else
			new gui.Main();
	}
}
