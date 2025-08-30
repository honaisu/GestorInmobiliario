package modelo.entidades;

import modelo.datos.DatosPersonales;

/**
 * Usuario dentro de nuestra plataforma.
 * Posee atributos con datos personales.
 */
public abstract class Usuario extends Identificador {
	protected DatosPersonales datosPersonales;
	
	public Usuario(long idUnico, DatosPersonales datosPersonales) {
		super(idUnico);
		this.datosPersonales = datosPersonales;
	}
}