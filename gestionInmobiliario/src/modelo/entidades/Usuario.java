package modelo.entidades;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;
import servicios.roles.RolComprador;
import servicios.roles.RolVendedor;

/**
 * Usuario dentro de nuestra plataforma.
 * Cada usuario puede ser un COMPRADOR y/o un VENDEDOR.
 * A su vez, posee todos los atributos de una Entidad (Datos Personales y Bancarios).
 */
public class Usuario extends Entidad {
	private RolComprador comprador = null;
	private RolVendedor vendedor = null;
	
	public Usuario(DatosPersonales datosPersonales, CuentaBancaria cuentaBancaria) {
		super(datosPersonales, cuentaBancaria);
	}
	
	public void setVendedorStatus() {
		if (this.vendedor == null) {
			this.vendedor = new RolVendedor();
		}
	}
	
	public void setCompradorStatus() {
		if (this.comprador == null) {
			this.comprador = new RolComprador();
		}
	}

}