package exceptions;

public class IncorrectPercent extends Exception {
	private static final long serialVersionUID = 1L;
	public IncorrectPercent(){
		super();
	}
	public IncorrectPercent(String details){
		super(details);
	}
}
