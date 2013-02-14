import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;


public class MeetingImpl implements Meeting, Comparable<MeetingImpl> {
	
	private int id;
	private Calendar date;
	private Set<Contact> contacts;

	public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		this.id = id;
		this.date = date;
		this.contacts = contacts;
		ContactManagerImpl.incrementMeetingId();
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public Calendar getDate() {
		return date;
	}
	
	@Override
	public Set<Contact> getContacts() {
		return contacts;
	}
	
	public boolean attendedBy(Contact contact) {
		Iterator<Contact> itr = contacts.iterator();
		while(itr.hasNext()) {
			if(((ContactImpl)itr.next()).compareTo((ContactImpl)contact) == 0) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int compareTo(MeetingImpl meeting2) {
		return this.getDate().compareTo(meeting2.getDate());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeetingImpl other = (MeetingImpl) obj;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

}
