package modelo.entidades;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;

/**
 * Usuario dentro de nuestra plataforma.
 * Cada usuario puede ser un COMPRADOR y/o un VENDEDOR.
 * A su vez, posee todos los atributos de una Entidad (Datos Personales y Bancarios).
 */
public abstract class Usuario extends Identificador {
	protected DatosPersonales datosPersonales;
	protected CuentaBancaria cuentaBancaria;
	
	public Usuario(long idUnico, DatosPersonales datosPersonales, CuentaBancaria cuentaBancaria) {
		super(idUnico);
		this.datosPersonales = datosPersonales;
		this.cuentaBancaria = cuentaBancaria;
	}
	
}