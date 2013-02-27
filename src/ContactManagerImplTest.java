import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

public class ContactManagerImplTest {
	private ContactManager demo;
	private Calendar dateA;							// general-use past date
	private Calendar dateB;							// general-use future date
	private Calendar dateC;							// future date for the same day, with time set to 2359
	private Calendar dateD;     					// alternative past date
	private Calendar dateE, dateF, dateG, dateH;    // alternative future dates
	private Calendar dateJ, dateK, dateL, dateM;	// alternative future dates, all with different times on the same day
	private Calendar dateN, dateP;					// alternative past dates
	
	@Before
	public void buildUp() {
		dateA = Calendar.getInstance();
		dateB = Calendar.getInstance();
		dateC = Calendar.getInstance();
		dateD = Calendar.getInstance();
		dateE = Calendar.getInstance();
		dateF = Calendar.getInstance();
		dateG = Calendar.getInstance();
		dateH = Calendar.getInstance();
		dateJ = Calendar.getInstance();
		dateK = Calendar.getInstance();
		dateL = Calendar.getInstance();
		dateM = Calendar.getInstance();
		dateN = Calendar.getInstance();
		dateP = Calendar.getInstance();

		dateA.set(2010,1,2);
		dateB.set(2014,2,1);
		dateC.set(2014,2,1,23,59);
		dateD.set(2009,2,1);
		dateE.set(2030,2,1);
		dateF.set(2048,2,1);
		dateG.set(2050,4,8);
		dateH.set(2060,5,1);
		dateJ.set(2020,5,3,1,55);
		dateK.set(2020,5,3,2,55);
		dateL.set(2020,5,3,3,55);
		dateM.set(2020,5,3,4,55);
		dateN.set(2000,1,5);
		dateP.set(2004,5,6);

		demo = new ContactManagerImpl();
		demo.addNewContact("Joe Bloggs", "Joe Blogg's notes");
		demo.addNewContact("John Smith", "John Smith's notes");
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));			// note that this increments the nextContactId to 4
		demo.addFutureMeeting(contacts, dateB);
		demo.addNewPastMeeting(contacts, dateA, "");
	}

	@Test
	public void testAddFutureMeeting() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		contacts.add(new ContactImpl(2, "John Smith"));
		assertEquals(3,demo.addFutureMeeting(contacts, dateE));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddFutureMeetingIllegalContact() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		contacts.add(new ContactImpl(3, "Dave Jones"));
		demo.addFutureMeeting(contacts, dateB);	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void AddFutureMeetingIllegalDate() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		demo.addFutureMeeting(contacts, dateA);
	}
	
	@Test
	public void testGetPastMeeting() {
		Meeting output = demo.getPastMeeting(2);
		
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		Meeting expected = new PastMeetingImpl(2, dateA, contacts);
		
		assertEquals((MeetingImpl)output, (MeetingImpl)expected);
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
	
	@Test
	public void testGetFutureMeeting() {
		Meeting output = demo.getFutureMeeting(1);
		
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		Meeting expected = new FutureMeetingImpl(1, dateB, contacts);
		
		assertEquals((MeetingImpl)output, (MeetingImpl)expected);
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
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		Meeting expected = new FutureMeetingImpl(1, dateB, contacts);
		Meeting output = demo.getMeeting(1);
		assertEquals((MeetingImpl)output, (MeetingImpl)expected);
		
		expected = new PastMeetingImpl(2, dateA, contacts);
		output = demo.getMeeting(2);
		assertEquals((MeetingImpl)output, (MeetingImpl)expected);
		
		output = demo.getMeeting(20);
		assertNull(output);
	}

	@Test
	public void testGetFutureMeetingListContact() {
		// test empty list
		List<Meeting> expected = new ArrayList<Meeting>();
		List<Meeting> output = demo.getFutureMeetingList(new ContactImpl(2, "John Smith"));
		assertEquals(expected, output);
		
		// test several meetings return, noting chronological order
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		expected.add(new FutureMeetingImpl(1, dateB, contacts));
		output = demo.getFutureMeetingList(new ContactImpl(1, "Joe Bloggs"));
		
		demo.addFutureMeeting(contacts, dateC);
		demo.addFutureMeeting(contacts, dateH);
		demo.addFutureMeeting(contacts, dateF);
		demo.addFutureMeeting(contacts, dateG);
		
		expected.add(new FutureMeetingImpl(4, dateC, contacts));
		expected.add(new FutureMeetingImpl(6, dateF, contacts));
		expected.add(new FutureMeetingImpl(7, dateG, contacts));
		expected.add(new FutureMeetingImpl(5, dateH, contacts));

		output = demo.getFutureMeetingList(new ContactImpl(1, "Joe Bloggs"));
		
		assertEquals(output, expected);		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeetingListIllegalContact() {
		demo.getFutureMeetingList(new ContactImpl(20, "Steve Jobs"));
	}
	
	@Test
	public void testGetFutureMeetingListCalendar() {
		// test empty list
		List<Meeting> expected = new ArrayList<Meeting>();
		List<Meeting> output = demo.getFutureMeetingList(dateE);
		assertEquals(expected, output);
		
		// test several meetings return, noting chronological order
		
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		
		demo.addFutureMeeting(contacts, dateJ);
		demo.addFutureMeeting(contacts, dateM);
		demo.addFutureMeeting(contacts, dateK);
		demo.addFutureMeeting(contacts, dateL);

		expected.add(new FutureMeetingImpl(3, dateJ, contacts));
		expected.add(new FutureMeetingImpl(5, dateK, contacts));
		expected.add(new FutureMeetingImpl(6, dateL, contacts));
		expected.add(new FutureMeetingImpl(4, dateM, contacts));
		
		output = demo.getFutureMeetingList(dateJ);
		
		assertEquals(output, expected);
	}

	@Test
	public void testGetPastMeetingList() {
		// test empty list
		List<PastMeeting> expected = new ArrayList<PastMeeting>();
		List<PastMeeting> output = demo.getPastMeetingList(new ContactImpl(2, "John Smith"));
		assertEquals(output, expected);
		
		// test several meetings return, noting chronological order
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		demo.addNewPastMeeting(contacts, dateD, "Notes");
		demo.addNewPastMeeting(contacts, dateP, "Notes");
		demo.addNewPastMeeting(contacts, dateN, "Notes");
		
		expected.add(new PastMeetingImpl(5, dateN, contacts));
		expected.add(new PastMeetingImpl(4, dateP, contacts));
		expected.add(new PastMeetingImpl(3, dateD, contacts));
		expected.add(new PastMeetingImpl(2, dateA, contacts));		
		output = demo.getPastMeetingList(new ContactImpl(1, "Joe Bloggs"));
		
		assertEquals(output, expected);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPastMeetingListIllegalContact() {
		demo.getPastMeetingList(new ContactImpl(20, "Steve Jobs"));
	}

	@Test
	public void testAddNewPastMeeting() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, dateD, notes);	
		Meeting expected = new PastMeetingImpl(3, dateD, contacts);
		Meeting output = demo.getMeeting(3);
		assertEquals((MeetingImpl)output, (MeetingImpl)expected);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNewPastMeetingNoContacts() {
		Set<Contact> contacts = new TreeSet<Contact>();
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, dateD, notes);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNewPastMeetingIllegalContact() {
		Set<Contact> contacts = new TreeSet<Contact>();
		contacts.add(new ContactImpl(1, "Joe Bloggs"));
		contacts.add(new ContactImpl(20, "Steve Jobs"));
		String notes = "Notes";
		demo.addNewPastMeeting(contacts, dateD, notes);	
	}
	
	@Test(expected = NullPointerException.class) 
	public void testAddNewPastMeetingNullContacts() {
		String notes = "Notes";
		demo.addNewPastMeeting(null, dateD, notes);
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
		demo.addNewPastMeeting(contacts, dateD, null);
	}
	
	@Test
	public void testAddMeetingNotes() {
		String expected = "New notes.";
		demo.addMeetingNotes(2, expected);
		String output = ((PastMeeting)demo.getMeeting(2)).getNotes();
		assertEquals(output, expected);		
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
		demo.addNewContact("Dave Murray", "notes");
		Set<Contact> expected = new TreeSet<Contact>();
		expected.add(new ContactImpl(4, "Dave Murray"));
		Set<Contact> output = demo.getContacts("Dave");
		assertEquals(output, expected);
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
		Set<Contact> expected = new TreeSet<Contact>();
		expected.add(new ContactImpl(1, "Joe Bloggs"));
		Set<Contact> output = demo.getContacts(1);
		assertEquals(output,expected);
		
		expected.add(new ContactImpl(2, "John Smith"));
		output = demo.getContacts(1,2);
		assertEquals(output, expected);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetContactsIntArrayUnknownContact() {
		demo.getContacts(99);
	}

	@Test
	public void testGetContactsString() {					
		Set<Contact> output = demo.getContacts("ZZZZ");
		Set<Contact> expected = new TreeSet<Contact>();
		assertEquals(output, expected);
		expected.add(new ContactImpl(2, "John Smith"));
		output = demo.getContacts("John");
		assertEquals(output, expected);
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetContactsNullString() {
		String input = null;
		demo.getContacts(input);
	}

	@Test
	public void testFlush() {
		demo.flush();
		ContactManager demo2 = new ContactManagerImpl();
		assertEquals(demo.getContacts(1,2), demo2.getContacts(1,2));
		assertEquals(demo.getFutureMeeting(1), demo2.getFutureMeeting(1));
		assertEquals(demo.getPastMeeting(2), demo2.getPastMeeting(2));
		(new File("contacts.txt")).delete();
	}

}