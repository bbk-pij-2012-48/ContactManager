import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
		Meeting expected = new PastMeetingImpl(2, testDate, contacts);
		
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
		Meeting output = demo.getFutureMeeting(1);
		
		Calendar testDate = Calendar.getInstance();
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		testDate.set(1,2,2014);
		Meeting expected = new FutureMeetingImpl(1, testDate, contacts);
		
		assertEquals(output, expected);
	}
	
	@Test
	public void testGetFutureMeetingNull() {
		Meeting output = demo.getFutureMeeting(20);
		
		assertNull(output);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetFutureMeetingPast() {
		demo.getFutureMeeting(2);		
	}

	@Test
	public void testGetMeeting() {
		Calendar testDate = Calendar.getInstance();
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		testDate.set(1,2,2014);
		Meeting expected = new FutureMeetingImpl(1, testDate, contacts);
		Meeting output = demo.getMeeting(1);
		assertEquals(expected, output);
		
		testDate.set(1,2,2010);
		expected = new PastMeetingImpl(2, testDate, contacts);
		output = demo.getMeeting(2);
		assertEquals(expected, output);
		
		output = demo.getMeeting(20);
		assertNull(output);
	}

	@Test
	public void testGetFutureMeetingListContact() {
		List<Meeting> expected = new ArrayList<Meeting>();
		List<Meeting> output = demo.getFutureMeetingList(new ContactImpl(2, "John Smith"));
		assertTrue(expected.equals(output));
		
		Calendar testDate = Calendar.getInstance();
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		testDate.set(1,2,2014);
		expected.add(new FutureMeetingImpl(1, testDate, contacts));
		output = demo.getFutureMeetingList(new ContactImpl(1, "Joe Bloggs"));
		assertTrue(expected.equals(output));
	}// Need to test chronological sorting.**********************************************************************************

	@Test(expected = IllegalArgumentException.class)
	public void testGetFutureMeetingListIllegalContact() {
		demo.getFutureMeetingList(new ContactImpl(20, "Steve Jobs"));
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
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		Calendar testDate = Calendar.getInstance();
		testDate.set(1, 2, 2012);
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, testDate, notes);	
		Meeting expected = new PastMeetingImpl(3, testDate, contacts);
		Meeting output = demo.getMeeting(3);
		assertEquals(expected, output);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNewPastMeetingNoContacts() {
		Set<Contact> contacts = new TreeSet<Contact>();
		Calendar testDate = Calendar.getInstance();
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, testDate, notes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNewPastMeetingIllegalContact() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		contacts.add(new ContactImpl(20, "Steve Jobs"));
		Calendar testDate = Calendar.getInstance();
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, testDate, notes);	
	}
	
	@Test(expected = NullPointerException.class) 
	public void testAddNewPastMeetingNullContacts() {
		Calendar testDate = Calendar.getInstance();
		String notes = "Notes";
		demo.addNewPastMeeting(null, testDate, notes);
	}
	
	@Test(expected = NullPointerException.class) 
	public void testAddNewPastMeetingNullCalendar() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, null, notes);
	}
	
	@Test(expected = NullPointerException.class) 
	public void testAddNewPastMeetingNullNotes() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		Calendar testDate = Calendar.getInstance();
		demo.addNewPastMeeting(contacts, testDate, null);
	}
	
	@Test
	public void testAddMeetingNotes() {
		String expected = "New notes.";
		demo.addMeetingNotes(2, expected);
		String output = ((PastMeeting)demo.getMeeting(2)).getNotes();
		assertTrue(output.equals(expected));		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddMeetingNotesIllegalId() {
		demo.addMeetingNotes(20, "notes");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testAddMeetingNotesFutureMeeting() {
		demo.addMeetingNotes(1, "notes");
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddMeetingNotesNull() {
		demo.addMeetingNotes(2, null);
	}

	@Test
	public void testAddNewContact() {
		demo.addNewContact("Dave Murray", "");
		Set<Contact> expected = new TreeSet<Contact>();
		expected.add(new ContactImpl(3, "Dave Murray"));
		Set<Contact> output = demo.getContacts("Dave Murray");
		assertTrue(output.equals(expected));
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNewContactNullName() {
		demo.addNewContact(null, "notes");
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNewContactNullNotes() {
		demo.addNewContact("Dave Murray", null);
	}

	@Test
	public void testGetContactsIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContactsString() {
		Set<Contact> output = demo.getContacts("ZZZZ");
		Set<Contact> expected = new TreeSet<Contact>();
		assertTrue(output.equals(expected));
		
		expected.add(new ContactImpl(2, "John Smith"));
		output = demo.getContacts("John");
		assertTrue(output.equals(expected));
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetContactsNullString() {
		String input = null;
		demo.getContacts(input);
	}

	@Test
	public void testFlush() {
		fail("Not yet implemented");
	}

}
