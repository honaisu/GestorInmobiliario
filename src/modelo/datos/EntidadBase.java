package modelo.datos;

public abstract class EntidadBase {
	protected long id;
	
	public EntidadBase(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
}
