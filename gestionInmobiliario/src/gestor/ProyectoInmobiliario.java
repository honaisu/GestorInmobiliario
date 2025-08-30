package gestor;

import java.time.LocalTime;
import modelo.entidades.Usuario;
import modelo.entidades.Vendedor;
import modelo.ubicacion.Edificio;

/**
 * Clase que conforma un proyecto que será utilizado para nuestro sistema.
 * Posee un nombre descriptivo propio, un vendedor asociado, la fecha
 * a la que se realizó la subida y el edificio propio, que será el proyecto.
 */
public class ProyectoInmobiliario {
	private String nombreProyecto;
	private Vendedor vendedor;
	private LocalTime fecha;
	private Edificio edificio;
	
	public ProyectoInmobiliario(String nombreProyecto, Vendedor vendedor, Edificio edificio) {
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = LocalTime.now();
		this.edificio = edificio;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public String getFecha() {
		return fecha.toString();
	}
	
	public Edificio getEdificio() {
		return edificio;
	}
}
