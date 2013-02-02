import static org.junit.Assert.*;

import org.junit.*;


public class ContactImplTest {
	
	@Before
	public void buildUp() {
		ContactImpl demo = new ContactImpl(123456789,"Joseph Bloggs");
		demo.addNotes("Meeting 1");
		demo.addNotes("Meeting 2");
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNotes() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNotes() {
		fail("Not yet implemented");
	}

}
