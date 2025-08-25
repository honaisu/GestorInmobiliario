package modelo.entidades;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;

/** 
 * La Clase Entidad se encarga de almacenar todo dato necesario para
 * poder definir a una persona como "válida".
 * 
 * Cada Entidad tiene sus propios datos de usuario y cuenta bancaria.
 */
public abstract class Entidad {
	protected DatosPersonales datosPersonales;
	protected CuentaBancaria cuentaBancaria;
	
	public Entidad(DatosPersonales datosPersonales, CuentaBancaria cuentaBancaria) {
		this.datosPersonales = datosPersonales;
		this.cuentaBancaria = cuentaBancaria;
	}
}
