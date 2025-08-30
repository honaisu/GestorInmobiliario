package modelo.entidades;

/**
 * Una clase abstracta que posee un parámetro IDENTIFICADOR.
 * Este servirá para poder asignarle a cada sub-clase un
 * identificador propio.
 * Útil para el mapa de nuestro gestor principal.
 */
public abstract class Identificador {
	protected long idUnico;
	
	public Identificador(long idUnico) {
		this.idUnico = idUnico;
	}

	public long getIdUnico() {
		return idUnico;
	}
}
