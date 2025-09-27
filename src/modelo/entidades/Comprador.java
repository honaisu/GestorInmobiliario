package modelo.entidades;

import java.util.LinkedList;
import modelo.ubicacion.Departamento;

/**
 * Clase que nos permite poder definir un "comprador" dentro del sistema.
 * Definimos un Comprador como una clase que puede ser capaz de comprar propiedades
 * y de ver su propia lista de compra.
 */
public class Comprador extends Usuario {
	public Comprador() {
	}
	
	public Comprador(String RUT, String NOMBRE, String email, String numero) {
		super(RUT, NOMBRE, email, numero);
		// TODO Auto-generated constructor stub
	}
	
}
