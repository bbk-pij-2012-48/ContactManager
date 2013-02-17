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

	private static final String FILENAME = "contacts.txt";
	private Set<Contact> contacts;
	private Set<FutureMeeting> futureMeetings;
	private Set<PastMeeting> pastMeetings;
	private static int nextMeetingId;
	private static int nextContactId;

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

	public static void incrementMeetingId() {
		nextMeetingId++;
	}
	
	public static void incrementContactId() {
		nextContactId++;
	}

	private boolean timeInPast(Calendar date) {
		Calendar current = Calendar.getInstance();
		if(date.before(current)) {
			return true;
		}
		
		return false;
	}

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

	@Override
	public PastMeeting getPastMeeting(int id) {
		// Check that there is no such meeting happening in the future
		if(getFutureMeetingNoException(id) != null) {
			throw new IllegalArgumentException("Error - A meeting with this ID is scheduled in the future");
		}
		return getPastMeetingNoException(id);
	}
	
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

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		// Check that there is no such meeting happening in the past
		if(getPastMeetingNoException(id) != null) {
			throw new IllegalArgumentException("Error - A meeting with this ID has happened in the past");
		}
		
		return getFutureMeetingNoException(id);
	}
	
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

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// Check contact is known
		Set<Contact> test = new TreeSet<Contact>();
		test.add(contact);
		if(unknownContact(test)) {
			throw new IllegalArgumentException("Error - Contact unknown");
		}
		
		// Add all future meetings with contact
		List<Meeting> output = new ArrayList<Meeting>();
		Iterator<FutureMeeting> itr = futureMeetings.iterator();
		while(itr.hasNext()) {
			FutureMeeting tmp = itr.next();
			if(((FutureMeetingImpl)tmp).attendedBy(contact)) {
				output.add(tmp);
			}
		}
		
		DateComparator comparator = new DateComparator();
		Collections.sort(output, comparator);
		return output;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> output = new ArrayList<Meeting>();
		Iterator<FutureMeeting> itr = futureMeetings.iterator();
		while(itr.hasNext()) {
			FutureMeeting tmp = itr.next();
			Calendar tmpDate = tmp.getDate();
			if(tmpDate.get(Calendar.DATE) == date.get(Calendar.DATE) &&
					tmpDate.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
						tmpDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
				output.add(tmp);
			}
		}
		
		DateComparator comparator = new DateComparator();
		Collections.sort(output, comparator);
		return output;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// Check contact is known
		Set<Contact> test = new TreeSet<Contact>();
		test.add(contact);
		if(unknownContact(test)) {
			throw new IllegalArgumentException("Error - Contact unknown");
		}
		
		// Add all past meetings with contact
		List<PastMeeting> output = new ArrayList<PastMeeting>();
		Iterator<PastMeeting> itr = pastMeetings.iterator();
		while(itr.hasNext()) {
			PastMeeting tmp = itr.next();
			if(((PastMeetingImpl)tmp).attendedBy(contact)) {
				output.add(tmp);
			}
		}
		
		DateComparator comparator = new DateComparator();
		Collections.sort(output, comparator);
		return output;
	}

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
			throw new IllegalStateException("Error - Meeting cannot be in the future");
		}
		((PastMeetingImpl)tmp).addNotes(text);
	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name == null || notes == null) {
			throw new NullPointerException("Must specify both name and notes");
		}
		Contact tmp = new ContactImpl(nextContactId, name);
		tmp.addNotes(notes);
		contacts.add(tmp);
	}
	
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

	@Override
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> output = new TreeSet<Contact>();
		for(int id : ids) {
			output.add(getContact(id)); // IllegalArgumentException thrown if no such contact exists
		}
		return output;
	}

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
}
