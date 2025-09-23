package modelo.datos;

public abstract class EntidadBase {
	protected Long id = null;
	
	public EntidadBase(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
}
