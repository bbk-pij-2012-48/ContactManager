import java.util.Calendar;
import java.util.Set;


public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	
	public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
	}

	private String notes;

	@Override
	public String getNotes() {
		return notes;
	}

}
