package modelo.entidades;

import java.util.LinkedList;

import modelo.datos.CuentaBancaria;
import modelo.datos.DatosPersonales;
import modelo.ubicacion.Departamento;

/**
 * Clase que nos permite poder definir un "comprador" dentro del sistema.
 * Definimos un Comprador como una clase que puede ser capaz de comprar propiedades
 * y de ver su propia lista de compra.
 */
public class Comprador extends Usuario {
	private LinkedList<Departamento> departamentosComprados;
	
	public Comprador(long idUnico, DatosPersonales datosPersonales, CuentaBancaria cuentaBancaria) {
		super(idUnico, datosPersonales, cuentaBancaria);
	}
	
	public void agregarDepartamento(Departamento nuevoDepartamento) {
        this.departamentosComprados.add(nuevoDepartamento); 
	}
	
	public int getCantDepartamentosComprados( ) {
		return departamentosComprados.size();
	}
}
