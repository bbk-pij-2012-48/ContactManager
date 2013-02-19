import java.util.Calendar;
import java.util.Set;


@SuppressWarnings("serial")
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
	}

}
