import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TreeSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

public class ContactManagerImplTest {
	private ContactManager demo;
	
	@Before
	public void buildUp() {
		demo = new ContactManagerImpl();
		demo.addNewContact("Joe Bloggs", "Joe Blogg's notes");
		demo.addNewContact("John Smith", "John Smith's notes");
		Calendar testDate = Calendar.getInstance();
		testDate.set(1,2,2014);
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		demo.addFutureMeeting(contacts, testDate);
		testDate.set(1,2,2010);
		demo.addNewPastMeeting(contacts, testDate, "");
	}

	@Test
	public void testAddFutureMeeting() {
		Calendar testDate = Calendar.getInstance();
		testDate.set(1,3,2014);
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		contacts.add(new ContactImpl(2, "John Smith"));
		assertEquals(3,demo.addFutureMeeting(contacts, testDate));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void AddFutureMeetingIllegalContact() {
		Calendar testDate = Calendar.getInstance();
		testDate.set(1,2,2014);
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		contacts.add(new ContactImpl(2, "Dave Jones"));
		demo.addFutureMeeting(contacts, testDate);	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void AddFutureMeetingIllegalDate() {
		Calendar testDate = Calendar.getInstance();
		testDate.set(1,2,2012);
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		demo.addFutureMeeting(contacts, testDate);
	}
	
	@Test
	public void testGetPastMeeting() {
		Meeting output = demo.getPastMeeting(2);
		
		Calendar testDate = Calendar.getInstance();
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		testDate.set(1,2,2010);
		Meeting expected = new MeetingImpl(2, testDate, contacts);
		
		assertEquals(output, expected);
	}

	@Test
	public void testGetPastMeetingNull() {
		Meeting output = demo.getPastMeeting(9);
		assertNull(output);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPastMeetingFuture() {
		demo.getPastMeeting(1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void GetPastMeetingIllegalMeeting() {
		
		demo.getMeeting(2);
	}

	@Test
	public void testGetFutureMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeetingListContact() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeetingListCalendar() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPastMeetingList() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewPastMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeetingNotes() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewContact() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContactsIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContactsString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlush() {
		fail("Not yet implemented");
	}

}
