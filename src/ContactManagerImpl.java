import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ContactManagerImpl implements ContactManager, Serializable {

	private static final String FILENAME = "contacts.txt";		// Default path for storing data
	private Set<Contact> contacts;
	private Set<FutureMeeting> futureMeetings;
	private Set<PastMeeting> pastMeetings;
	private static int nextMeetingId;
	private static int nextContactId;

	/**
	 * Constructor.
	 * If no contacts.txt file from a prior session exists, then the attributes are initialised. 
	 * If a contacts.txt file does exist, then the attributes are imported from there.
	 */
	@SuppressWarnings("unchecked")
	public ContactManagerImpl() {
		nextMeetingId = 1;
		nextContactId = 1;
		File tmp = new File(FILENAME);
		
		if(!tmp.exists()) {
			// Create new data structures
			contacts = new TreeSet<Contact>();
			futureMeetings = new TreeSet<FutureMeeting>();
			pastMeetings = new TreeSet<PastMeeting>();
		} else {
			// Load previous data
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream(FILENAME)));
				contacts = (Set<Contact>) in.readObject();
				futureMeetings = (Set<FutureMeeting>) in.readObject();
				pastMeetings = (Set<PastMeeting>) in.readObject();
			} catch (IOException ex) {
				System.err.println("Error: Read error " + ex);
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				System.err.println("Error: Read error " + ex);
				ex.printStackTrace();
			} finally {
				try {
					in.close();
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Increments the nextMeetingId. 
	 * Used when creating a new meeting.
	 */
	public static void incrementMeetingId() {
		nextMeetingId++;
	}
	
	/**
	 * Increments the nextContactId.
	 * Used when creating a new contact.
	 */
	public static void incrementContactId() {
		nextContactId++;
	}

	/**
	 * Tests whether a given date is in the past.
	 * 
	 * @param date the date for testing
	 * @return true if the date is in the past, false otherwise.
	 */
	private boolean timeInPast(Calendar date) {
		Calendar current = Calendar.getInstance();
		if(date.before(current)) {
			return true;
		}
		
		return false;
	}

	/**
	 * Tests whether any contact in a given set is unknown,
	 * i.e. not stored in contacts.
	 * 
	 * @param testContacts the set of contacts to test
	 * @return true if one or more of the contacts is unknown. false otherwise
	 */
	private boolean unknownContact(Set<Contact> testContacts) {
		Iterator<Contact> itr = testContacts.iterator();
		while(itr.hasNext()) {
			Contact tmp = itr.next();
			Iterator<Contact> itr2 = contacts.iterator();
			while(itr2.hasNext()) {
				Contact tmp2 = itr2.next();
				if(((ContactImpl)(tmp2)).compareTo((ContactImpl)tmp) == 0) {
					break;
				}
				if(!itr2.hasNext()) {
					return true;

				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		int meetingId = nextMeetingId; // not using nextMeetingId directly as this will increment during the method
		
		// Test for any illegal arguments
		if(timeInPast(date)) {
			throw new IllegalArgumentException("Error - Future Meeting cannot be in the past");
		}
		if(unknownContact(contacts)) {
			throw new IllegalArgumentException("Error - One or more of the specified contacts is not recognised");
		}
		
		FutureMeeting newMeeting = new FutureMeetingImpl(meetingId, date, contacts);
		futureMeetings.add(newMeeting);
		
		return meetingId;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public PastMeeting getPastMeeting(int id) {
		// Check that there is no such meeting happening in the future
		if(getFutureMeetingNoException(id) != null) {
			throw new IllegalArgumentException("Error - A meeting with this ID is scheduled in the future");
		}
		return getPastMeetingNoException(id);
	}
	
	/**
	 * Has the same function as getPastMeeting(int), but does not check if a future meeting
	 * with the same id exists, and so does not throw an IllegalArgumentException.
	 * Used to avoid circularity in calls between getPastMeeting(int) and getFutureMeeting(int).
	 * 
	 * @param id the meeting id
	 * @return the PastMeeting with the given id, or null if no such PastMeeting exists
	 */
	private PastMeeting getPastMeetingNoException(int id) {
		Iterator<PastMeeting> itr = pastMeetings.iterator();
		while(itr.hasNext()) {
			PastMeeting tmp = itr.next();
			if(tmp.getId() == id) {
				return tmp;
			}
		}
		
		return null;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public FutureMeeting getFutureMeeting(int id) {
		// Check that there is no such meeting happening in the past
		if(getPastMeetingNoException(id) != null) {
			throw new IllegalArgumentException("Error - A meeting with this ID has happened in the past");
		}
		
		return getFutureMeetingNoException(id);
	}
	
	/**
	 * Has the same function as getFutureMeeting(int), but does not check if a past meeting
	 * with the same id exists, and so does not throw an IllegalArgumentException.
	 * Used to avoid circularity in calls between getPastMeeting(int) and getFutureMeeting(int).
	 * 
	 * @param id the meeting id
	 * @return the FutureMeeting with the given id, or null if no such FutureMeeting exists
	 */
	private FutureMeeting getFutureMeetingNoException(int id) {
		Iterator<FutureMeeting> itr = futureMeetings.iterator();
		while(itr.hasNext()) {
			FutureMeeting tmp = itr.next();
			if(tmp.getId() == id) {
				return tmp;
			}
		}
		
		return null;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Meeting getMeeting(int id) {
		// Search past meetings
		PastMeeting tmp = getPastMeetingNoException(id);
		if(tmp != null) {
			return tmp;
		}
		
		// Search future meetings
		FutureMeeting tmp2 = getFutureMeetingNoException(id);
		if(tmp2 != null) {
			return tmp2;
		}
		
		return null;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// Check contact is known
		Set<Contact> test = new TreeSet<Contact>();
		test.add(contact);
		if(unknownContact(test)) {
			throw new IllegalArgumentException("Error - Contact unknown");
		}
		
		// Add all future meetings with contact
		List<MeetingImpl> output = new ArrayList<MeetingImpl>();
		Iterator<FutureMeeting> itr = futureMeetings.iterator();
		while(itr.hasNext()) {
			FutureMeeting tmp = itr.next();
			if(((FutureMeetingImpl)tmp).attendedBy(contact)) {
				output.add((MeetingImpl)tmp);
			}
		}
		
		Collections.sort(output);
		
		List<?> castOutput1 = (List<?>)output;
		@SuppressWarnings("unchecked")
		List<Meeting> castOutput2 = (List<Meeting>)castOutput1;
		
		return castOutput2;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<MeetingImpl> output = new ArrayList<MeetingImpl>();
		Iterator<FutureMeeting> itr = futureMeetings.iterator();
		
		while(itr.hasNext()) {
			FutureMeeting tmp = itr.next();
			Calendar tmpDate = tmp.getDate();
			if(tmpDate.get(Calendar.DATE) == date.get(Calendar.DATE) &&
					tmpDate.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
						tmpDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
				output.add((MeetingImpl)tmp);
			}
		}
		
		Collections.sort(output);
		
		List<?> castOutput1 = (List<?>)output;
		@SuppressWarnings("unchecked")
		List<Meeting> castOutput2 = (List<Meeting>)castOutput1;
		
		return castOutput2;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// Check contact is known
		Set<Contact> test = new TreeSet<Contact>();
		test.add(contact);
		if(unknownContact(test)) {
			throw new IllegalArgumentException("Error - Contact unknown");
		}
		
		// Add all past meetings with contact
		List<PastMeetingImpl> output = new ArrayList<PastMeetingImpl>();
		Iterator<PastMeeting> itr = pastMeetings.iterator();
		while(itr.hasNext()) {
			PastMeeting tmp = itr.next();
			if(((PastMeetingImpl)tmp).attendedBy(contact)) {
				output.add((PastMeetingImpl)tmp);
			}
		}
		
		Collections.sort(output);
		
		List<?> castOutput1 = (List<?>)output;
		@SuppressWarnings("unchecked")
		List<PastMeeting> castOutput2 = (List<PastMeeting>)castOutput1;
		return castOutput2;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		int meetingId = nextMeetingId; // not using nextMeetingId directly as this will increment during the method
		
		// Test for any illegal arguments
		if(contacts == null || date == null || text == null) {
			throw new NullPointerException("Error - Must specify contacts, date and notes for meeting");
		}
		if(unknownContact(contacts)) {
			throw new IllegalArgumentException("Error - One or more of the specified contacts is not recognised");
		}
		if(contacts.isEmpty()) {
			throw new IllegalArgumentException("Error - Must specify at least one contact");
		}
		
		PastMeetingImpl tmp = new PastMeetingImpl(meetingId, date, contacts);
		tmp.addNotes(text);
		pastMeetings.add(tmp);
		
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void addMeetingNotes(int id, String text) {
		// Test for any illegal arguments
		if(text == null) {
			throw new NullPointerException("Error - Must add notes");
		}
		Meeting tmp = getMeeting(id);
		if(tmp == null) {
			throw new IllegalArgumentException("Error - Meeting does not exist");
		}

		if(tmp instanceof FutureMeetingImpl) {
			if(!timeInPast(tmp.getDate())) {
				throw new IllegalStateException("Error - Meeting cannot be in the future");
			} 
		}
		
		((PastMeetingImpl)tmp).addNotes(text);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void addNewContact(String name, String notes) {
		if(name == null || notes == null) {
			throw new NullPointerException("Must specify both name and notes");
		}
		Contact tmp = new ContactImpl(nextContactId, name);
		tmp.addNotes(notes);
		contacts.add(tmp);
	}
	
	/**
	 * Returns the single contact with a given id.
	 * 
	 * @param id the contact id to search for
	 * @return	Contact the contact with the given id
	 * @throws IllegalArgumentException if no such contact exists
	 */
	private Contact getContact(int id) {
		Iterator<Contact> itr = contacts.iterator();
		while(itr.hasNext()) {
			Contact tmp = itr.next();
			if(tmp.getId() == id) {
				return tmp;
			}
		}
		throw new IllegalArgumentException("Error - Contact is unknown");
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> output = new TreeSet<Contact>();
		for(int id : ids) {
			output.add(getContact(id)); // IllegalArgumentException thrown if no such contact exists
		}
		return output;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Set<Contact> getContacts(String name) {
		if(name == null) {
			throw new NullPointerException("Error - Must enter a name");
		}
		Set<Contact> output = new TreeSet<Contact>();
		Iterator<Contact> itr = contacts.iterator();
		while(itr.hasNext()) {
			Contact tmp = itr.next();
			if(tmp.getName().contains(name)) {
			output.add(tmp);
			}
		}
		return output;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void flush() {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(FILENAME)));
			out.writeObject(contacts);
			out.writeObject(futureMeetings);
			out.writeObject(pastMeetings);
		} catch (IOException ex) {
			System.err.println("Error: Write error " + ex);
			ex.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ContactManagerImpl launcher = new ContactManagerImpl();
		launcher.launch();
	}
	
	public void launch() {
		
	}
	
}