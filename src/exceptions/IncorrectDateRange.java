package exceptions;

public class IncorrectDateRange extends Exception {
	private static final long serialVersionUID = 1L;
	public IncorrectDateRange(){
		super();
	}
	public IncorrectDateRange(String details){
		super(details);
	}
}
