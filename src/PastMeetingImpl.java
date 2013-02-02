import java.util.Calendar;
import java.util.Set;


public class PastMeetingImpl implements PastMeeting {
	
	private int id;
	private Calendar date;
	private Set<Contact> contacts;
	private String notes;

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

	@Override
	public String getNotes() {
		return notes;
	}

}
