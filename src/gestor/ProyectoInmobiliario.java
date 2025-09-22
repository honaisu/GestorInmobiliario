package gestor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;

import modelo.ubicacion.Edificio;

public class ProyectoInmobiliario {
	private long id;
	private String nombreProyecto;
	private String vendedor;
	private LocalDate fecha;
	private LinkedList<Edificio> edificio;
	
	public ProyectoInmobiliario(long id, String nombreProyecto, String vendedor, LocalDate fecha) {
		this.id = id;
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = fecha;
		this.edificio = new LinkedList<>();
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public String getFecha() {
		return fecha.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}
	
	public long getId() {
		return id;
	}
	
	public String getVendedor() {
		return vendedor;
	}
	
	public String getFechaOferta() {
		return fecha.toString();
	}

	public void addEdificio(Edificio edificio) {
		this.edificio.add(edificio);
	}
}
