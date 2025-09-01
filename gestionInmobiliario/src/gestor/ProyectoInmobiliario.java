package gestor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import modelo.entidades.Identificador;
import modelo.entidades.Vendedor;
import modelo.ubicacion.Edificio;

public class ProyectoInmobiliario extends Identificador {
	private String nombreProyecto;
	private Vendedor vendedor;
	private LocalDateTime fecha;
	private Edificio edificio;
	
	
	public ProyectoInmobiliario(long idUnico, String nombreProyecto, Vendedor vendedor, Edificio edificio) {
		super(idUnico);
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = LocalDateTime.now();
		this.edificio = edificio;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}
	
	public Edificio getEdificio() {
		return edificio;
	}
	
	public long getIdentificador() {
		return idUnico;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public String getFecha() {
		return fecha.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}
}
