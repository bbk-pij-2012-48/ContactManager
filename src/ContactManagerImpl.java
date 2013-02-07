import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
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
	
	public static void incrementId() {
		nextMeetingId++;
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
		if(getFutureMeeting(id) != null) {
			throw new IllegalArgumentException("Error - A meeting with this ID is scheduled in the future");
		}
		
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
		if(getPastMeeting(id) != null) {
			throw new IllegalArgumentException("Error - A meeting with this ID has happened in the past");
		}
		
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
		PastMeeting tmp = getPastMeeting(id);
		if(tmp != null) {
			return tmp;
		}
		
		// Search future meetings
		FutureMeeting tmp2 = getFutureMeeting(id);
		if(tmp2 != null) {
			return tmp2;
		}
		
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMeetingNotes(int id, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNewContact(String name, String notes) {
		// TODO Auto-generated method stub

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
