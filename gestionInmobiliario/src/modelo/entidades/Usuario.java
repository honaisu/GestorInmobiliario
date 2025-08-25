package modelo.entidades;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;
import modelo.ubicacion.Departamento;
import servicios.roles.RolComprador;
import servicios.roles.RolVendedor;

/**
 * Usuario dentro de nuestra plataforma.
 * Cada usuario puede ser un COMPRADOR y/o un VENDEDOR.
 * A su vez, posee todos los atributos de una Entidad (Datos Personales y Bancarios).
 */
public class Usuario extends Entidad implements Comprador, Vendedor {
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

	@Override
	public boolean vender(Departamento departamento) {
		if (vendedor == null) return false;
		// TODO Sistema de venta!
		return true;
	}

	@Override
	public boolean comprar(Departamento departamento) {
		if (comprador == null) return false;
		// TODO Sistema de compra!
		return true;
	}
}