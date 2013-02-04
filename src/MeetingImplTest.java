import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;


public class MeetingImplTest {

	private MeetingImpl demo;
	
	@Before
	public void buildUp() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 2, 3);
		Set<Contact> contacts = new TreeSet<Contact>();
		Contact contact1 = new ContactImpl(1,"Dave");
		Contact contact2 = new ContactImpl(2,"Murray");
		contacts.add(contact1);
		contacts.add(contact2);
		demo = new MeetingImpl(123456789, cal, contacts);
	}
	
	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContacts() {
		fail("Not yet implemented");
	}

}
