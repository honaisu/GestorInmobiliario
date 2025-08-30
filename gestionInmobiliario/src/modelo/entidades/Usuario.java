package modelo.entidades;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;
import servicios.roles.Rol;

/**
 * Usuario dentro de nuestra plataforma.
 * Cada usuario puede ser un COMPRADOR y/o un VENDEDOR.
 * A su vez, posee todos los atributos de una Entidad (Datos Personales y Bancarios).
 */
public class Usuario extends Entidad {
	private Rol[] roles = new Rol[Rol.values().length];
	
	public Usuario(DatosPersonales datosPersonales, CuentaBancaria cuentaBancaria) {
		super(datosPersonales, cuentaBancaria);
	}
}