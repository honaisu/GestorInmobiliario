package modelo.entidades;

import java.util.LinkedList;

import gestor.ProyectoInmobiliario;
import modelo.datos.DatosPersonales;

/**
 * Clase que definimos como un "vendedor" dentro del sistema.
 * Es el rol que se dedica a la venta exclusiva de departamentos.
 * Posee atributos propios como una lista interna de departamentos en venta.
 */
public class Vendedor extends Usuario {
	private LinkedList<ProyectoInmobiliario> proyectosAsociados;
	
	public Vendedor(long idUnico, DatosPersonales datosPersonales) {
		super(idUnico, datosPersonales);
	}
	// TODO Añadir más variables y operaciones
	
	public String getNombre() {
		return datosPersonales.getNombre();
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
