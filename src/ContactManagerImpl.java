import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class ContactManagerImpl implements ContactManager {

	private Set<Contact> contacts;
	private Set<FutureMeeting> futureMeetings;
	private Set<PastMeeting> pastMeetings;
	private static int nextMeetingId;
	private static int nextContactId;

	public ContactManagerImpl() {
		contacts = new TreeSet<Contact>();
		futureMeetings = new TreeSet<FutureMeeting>();
		pastMeetings = new TreeSet<PastMeeting>();
		nextMeetingId = 1;
		nextContactId = 1;
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
				if(itr2.next().equals(tmp)) {
					break;
				}else {
					if(!itr2.hasNext()) {
						return true;
					}
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

	@Override
	public Set<Contact> getContacts(int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

}
