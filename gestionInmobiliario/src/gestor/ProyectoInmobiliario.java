package gestor;

import java.time.LocalTime;
import java.util.LinkedList;

import modelo.entidades.Usuario;
import modelo.ubicacion.Edificio;

public class ProyectoInmobiliario {
	
	private String nombreProyecto;
	private Usuario vendedor;
	private LocalTime fecha;
	private LinkedList<Edificio> listaEdificios;
	
	
	public ProyectoInmobiliario(String nombreProyecto, Usuario vendedor, LocalTime fecha) {
		super();
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = fecha;
		this.listaEdificios = new LinkedList<Edificio>();
	}
	
	public void agregarEdificio(Edificio edificio) {
		listaEdificios.add(edificio);
		
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}


	public Usuario getVendedor() {
		return vendedor;
	}

	public LocalTime getFecha() {
		return fecha;
	}

	public LinkedList<Edificio> getListaEdificios() {
		return listaEdificios;
	}
	
}
