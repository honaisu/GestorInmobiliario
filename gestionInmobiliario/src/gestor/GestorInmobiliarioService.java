package gestor;

import java.util.HashMap;

import modelo.entidades.Identificador;

public class GestorInmobiliarioService {
	
	private HashMap<Identificador, ProyectoInmobiliario> proyectos;
	private HashMap<Identificador, Reserva> reservas;
	
	public GestorInmobiliarioService() {
		this.proyectos = new HashMap<Identificador, ProyectoInmobiliario>();
		this.reservas = new HashMap<Identificador, Reserva>();
	}
	
	public void agregarProyecto(Identificador codigo, ProyectoInmobiliario proyecto) {
		proyectos.put(codigo, proyecto);
	}
}
