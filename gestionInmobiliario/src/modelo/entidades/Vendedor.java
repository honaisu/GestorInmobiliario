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
	
	public boolean crearProyecto(String nombre) {
		ProyectoInmobiliario nuevoProyecto = new ProyectoInmobiliario(nombre, this, null);
		return (proyectosAsociados.add(nuevoProyecto)) ? true : false;
	}
	// TODO Añadir más operaciones
}
