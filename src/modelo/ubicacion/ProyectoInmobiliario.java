package modelo.ubicacion;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;

import modelo.entidades.EntidadBase;

public class ProyectoInmobiliario extends EntidadBase {
	private String nombreProyecto;
	private String vendedor;
	private LocalDate fecha;
	private LinkedList<Edificio> edificios;
	
	public ProyectoInmobiliario(long id, String nombreProyecto, String vendedor, LocalDate fecha) {
		super(id);
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = fecha;
		this.edificios = new LinkedList<>();
	}
	
	public ProyectoInmobiliario(String nombreProyecto, String vendedor, LocalDate fecha) {
		super(null);
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = fecha;
		this.edificios = new LinkedList<>();
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}
	
	public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
	
	public String getFecha() {
		return fecha.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}
	
	public String getVendedor() {
		return vendedor;
	}
	
	public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
	
	public String getFechaOferta() {
		return fecha.toString();
	}

	public void addEdificio(Edificio edificio) {
		this.edificios.add(edificio);
	}
	
	public Edificio getEdificio(long idEdificio) {
		for (Edificio e : edificios) {
			if (e.getId() == idEdificio) return e;
		}
		return null;
	}
	
	public List<Edificio> getEdificios() {
		return edificios;
	}
}
