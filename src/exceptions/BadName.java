package exceptions;

public class BadName extends Exception {
	private static final long serialVersionUID = 1L;
	public BadName(){
		super();
	}
	public BadName(String details){
		super(details);
	}
}
