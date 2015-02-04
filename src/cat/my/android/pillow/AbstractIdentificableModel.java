package cat.my.android.pillow;

/**
 * Base implementation of IdentificableModel
 * It has the Id definition and Equals and hashcode methods using Id.
 * There is no need to use this class, it's just a helper.
 */
public abstract class AbstractIdentificableModel implements IdentificableModel{
	protected String id;
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AbstractIdentificableModel other = (AbstractIdentificableModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
