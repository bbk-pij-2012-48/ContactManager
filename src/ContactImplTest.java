import static org.junit.Assert.*;

import org.junit.*;


public class ContactImplTest {
	public ContactImpl demo;
	
	@Before
	public void buildUp() {
		demo = new ContactImpl(123456789,"Joseph Bloggs");
		demo.addNotes("Meeting 1");
		demo.addNotes("Meeting 2");
	}
	
	@Test
	public void testGetId() {
		int output = demo.getId();
		int expected = 123456789;
		assertEquals(output, expected);
	}

	@Test
	public void testGetName() {
		String output = demo.getName();
		String expected = "Joseph Bloggs";
		assertEquals(output, expected);
	}

	@Test
	public void testNotes() {
		String output = demo.getNotes();
		String expected = "Meeting 1\nMeeting 2";
		assertEquals(output,expected);
	}

}
