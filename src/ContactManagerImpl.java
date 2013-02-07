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

	public ContactManagerImpl() {
		contacts = new TreeSet<Contact>();
		futureMeetings = new TreeSet<FutureMeeting>();
		pastMeetings = new TreeSet<PastMeeting>();
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
		int meetingId;
		boolean inPast = timeInPast(date); // boolean flags for exception
		boolean contactErr = unknownContact(contacts);

		if(inPast) {
			throw new IllegalArgumentException("Error - Future Meeting cannot be in the past");
		}
		if(contactErr) {
			throw new IllegalArgumentException("Error - One or more of the specified contacts is not recognised");
		}
		
		FutureMeeting newMeeting = new FutureMeetingImpl(nextMeetingId, date, contacts);
	

		return meetingId;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		// TODO Auto-generated method stub
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
