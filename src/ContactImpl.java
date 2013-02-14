
public class ContactImpl implements Contact, Comparable<ContactImpl>{
	
	private int id;
	private String name;
	private String notes;
	
	public ContactImpl(int id, String name) {
		this.id = id;
		this.name = new String(name);
		this.notes = "";
		ContactManagerImpl.incrementContactId();
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

	@Override
	public int compareTo(ContactImpl contact2) {
		if(this.getId() == contact2.getId()) {
			return 0;  								
		}											
		if(this.getId() < contact2.getId()) {
			return -1;
		}
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ContactImpl other = (ContactImpl) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}

