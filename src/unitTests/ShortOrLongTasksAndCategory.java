package unitTests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import exceptions.BadDateInput;
import exceptions.BadName;
import exceptions.IncorrectDateRange;

/*
 * test tasks (long|short), and categories
 */
public class ShortOrLongTasksAndCategory {
	core.Task[] myTasks;
	
	/*
	 * Génère un nom aléatoire
	 */
	public String generate()
	{
	    String word = "";
	    String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int len = (int) Math.floor(Math.random() * 30)+1;
	    while(len-->0)
	    	word += letters.charAt((int)Math.floor(Math.random() * 62));
	    return word;
	}

	@Test
	public void test() {
		//pour toutes les tâches, on essaie des choses
		for(int i=0; i<100; i++){
			core.Task t = myTasks[i];
			assertEquals(null, t.getCategory());
			String a = generate();
			try {
				t.setCategory(new core.Category(a));
			} catch (BadName e) {
				fail();
			}
			assertEquals(a, t.getCategoryName());
			assertEquals(false, t.isDone());
		}
	}
	
	/*
	 * généère une date aléatoire
	 */
	public Date randDate() {
		return new Date(core.Task.today().getTime() + (int) (Math.random() * core.Task.today().getTime()));
	}

	@Before
	public void setUp() {
		myTasks = new core.Task[100];
		for(int i=0; i<100; i++){
			if(Math.random()<0.5){
				Date a = randDate();
				try {
					myTasks[i] = new core.ShortTask(generate(), generate(), a);
				} catch (BadDateInput | BadName e1) {
					fail();//ne devrait pas se produire
				}
			}else{
				Date a = randDate();
				Date b = randDate();
				
				try {
					myTasks[i] = new core.LongTask(generate(), generate(), a, b);
				} catch (BadDateInput | BadName | IncorrectDateRange e) {
					if(a.after(b))
						fail();//ne devrait pas se produire
					else
						i--;//c'est noraml a doit être < à b
				}
			}
		}
	}
}
