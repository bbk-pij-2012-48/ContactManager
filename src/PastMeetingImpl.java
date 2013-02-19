import java.util.Calendar;
import java.util.Set;


@SuppressWarnings("serial")
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	
	private String notes;
	
	public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
		notes = new String();
	}

	@Override
	public String getNotes() {
		return notes;
	}
	
	public void addNotes(String newNotes) {
		if(notes.length() == 0) {
			notes += newNotes;
		} else {
			notes += "\n" + newNotes;
		}
	}

}
