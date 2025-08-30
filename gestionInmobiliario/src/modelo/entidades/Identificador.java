package modelo.entidades;

public abstract class Identificador {
	
	protected long idUnico;
	
	public Identificador(long idUnico) {
		this.idUnico = idUnico;
	}

	public long getIdUnico() {
		return idUnico;
	}
	
	
}
