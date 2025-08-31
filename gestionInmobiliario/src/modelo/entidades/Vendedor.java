package modelo.entidades;

import java.util.LinkedList;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;
import modelo.ubicacion.Departamento;

/**
 * Clase que definimos como un "vendedor" dentro del sistema.
 * Es el rol que se dedica a la venta exclusiva de departamentos.
 * Posee atributos propios como una lista interna de departamentos en venta.
 */
public class Vendedor extends Usuario {
	private LinkedList<Departamento> departamentosEnVenta;
	
	public Vendedor(long idUnico, DatosPersonales datosPersonales, CuentaBancaria cuentaBancaria) {
		super(idUnico, datosPersonales, cuentaBancaria);
	}
	// TODO Añadir más variables y operaciones
	
	
	
	
}
