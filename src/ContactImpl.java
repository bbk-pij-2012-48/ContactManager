
public class ContactImpl implements Contact {
	
	private int id;
	private String name;
	private String notes;
	
	public ContactImpl(int id, String name) {
		this.id = id;
		this.name = new String(name);
		this.notes = "";		
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getNotes() {
		return notes;
	}

	@Override
	public void addNotes(String note) {
		if(notes.length() == 0){
			notes += note;
		} else {
			notes += "\n" + note;
		}

	}

}
