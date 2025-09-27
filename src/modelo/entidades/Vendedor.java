package modelo.entidades;

import java.util.LinkedList;

import modelo.ubicacion.ProyectoInmobiliario;

/**
 * Clase que definimos como un "vendedor" dentro del sistema.
 * Es el rol que se dedica a la venta exclusiva de departamentos.
 * Posee atributos propios como una lista interna de departamentos en venta.
 */
public class Vendedor extends Usuario {
	private LinkedList<ProyectoInmobiliario> proyectosAsociados;
	
	public Vendedor(String RUT, String NOMBRE, String email) {
		super(RUT, NOMBRE, email);
		// TODO Auto-generated constructor stub
	}
	
	// Método de prueba para crear un nuevo proyecto
	// Debería tal vez ser gestionado por el gestor :)
	// TODO: sistema de verificación del ID (saber si existe en el gestor o no)
	public boolean ingresarProyecto(ProyectoInmobiliario proyecto) {
		if (proyecto == null) return false;
		proyectosAsociados.add(proyecto);
		return true;
	}
}
