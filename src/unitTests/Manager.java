package unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/*
 * On essaie la classe Manager, du moins autant que l'on peut..
 */
public class Manager {
	core.Manager global;
	@Test
	public void test() {
		assertNotNull(global);
		assertNotNull(global.categories);
		assertNotNull(global.tasks);
	}
	@Before
	public void setUp() {
		global = core.Manager.createFromFile("data");
	}
}
