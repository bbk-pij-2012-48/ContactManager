import java.util.Comparator;


public class DateComparator implements Comparator<Meeting> {

	@Override
	public int compare(Meeting meeting1, Meeting meeting2) {
		return (meeting1.getDate()).compareTo(meeting2.getDate());
	}

}
