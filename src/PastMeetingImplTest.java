import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.Before;


public class PastMeetingImplTest {
	
	private PastMeetingImpl demo;

	@Before
	public void buildUp(){
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 2, 3);
		Set<Contact> contacts = new TreeSet<Contact>();		
		Contact contact1 = new ContactImpl(1,"Dave");
		Contact contact2 = new ContactImpl(2,"Murray");
		contacts.add(contact1);
		contacts.add(contact2);
		demo = new PastMeetingImpl(123456789, cal, contacts);
	}
	
	@Test
	public void testGetId() {
		int output = demo.getId();
		int expected = 123456789;
		assertEquals(output, expected);
	}

	@Test
	public void testGetDate() {
		Calendar output = demo.getDate();
		Calendar expected = Calendar.getInstance();
		expected.set(2013, 2, 3);
		assertEquals(output.compareTo(expected),0); 
	}

	@Test
	public void testGetContacts() {
		Set<Contact> output = demo.getContacts();
		Set<Contact> expected = new TreeSet<Contact>();
		Contact contact1 = new ContactImpl(1,"Dave");
		Contact contact2 = new ContactImpl(2,"Murray");
		expected.add(contact1);
		expected.add(contact2);	
		assertEquals(output, expected);
	}

	@Test
	public void testGetNotes() {
		demo.addNotes("Note 1");
		demo.addNotes("Note 2");
		String output = demo.getNotes();
		String expected = "Note 1\nNote2";
		assertEquals(output, expected);
	}

}
