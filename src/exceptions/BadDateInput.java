package exceptions;

public class BadDateInput extends Exception{
	private static final long serialVersionUID = 1L;

	public BadDateInput(){
		super();
	}
	public BadDateInput(String details){
		super(details);
	}
}