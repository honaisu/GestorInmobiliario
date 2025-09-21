package gestor;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import modelo.entidades.Vendedor;
import modelo.ubicacion.Edificio;

public class GestorInmobiliarioService {
	private static HashMap<Long, ProyectoInmobiliario> proyectos = new HashMap<>();
	private static HashMap<Long, Reserva> reservas = new HashMap<>();
	
	// No se inicializa
	private GestorInmobiliarioService() {}
	
	public static boolean crearNuevoProyecto(long id, String nombre, Vendedor vendedor, Edificio edi) {
		if (nombre.length() == 0 ||
			proyectos.containsKey(id) ||
			vendedor == null) return false;
		
		ProyectoInmobiliario proyecto = new ProyectoInmobiliario(id, nombre, vendedor, edi);
		agregarProyecto(proyecto);
		return true;
	}
	
	
	private static void mostrarDatosEdificio(Edificio e) {
		System.out.println("Edificio asociado: " + e.getNOMBRE());
		System.out.println("Dirección: " + e.getInformacion().getDireccion());
		System.out.println("Departamentos DISPONIBLES: " + e.getCantDepartamentos());			
		if (e.getInformacion().isTienePiscina()) {
			System.out.println("El edificio posee piscina.");
		}
		if (e.getInformacion().isTieneEstacionamineto()) {
			System.out.println("El edificio cuenta con estacionamiento.");
		}
	}
	
	public static void mostrarProyectos() {
		for (ProyectoInmobiliario p : proyectos.values()) {
			System.out.println("== Proyecto #" + p.getIdUnico() + ": " + p.getNombreProyecto() + " ==");
			System.out.println("Fecha de Ingreso: " + p.getFecha());
			if (p.getVendedor() != null) System.out.println("Vendedor asociado: " + p.getVendedor().getNombre());
			mostrarDatosEdificio(p.getEdificio());
			System.out.println();
		}
	}
	
	//Metodos Proyecto
	public static void agregarProyecto(ProyectoInmobiliario proyecto) {
		proyectos.put(proyecto.getIdUnico(), proyecto);
	}
	
	public ProyectoInmobiliario getProyecto(Long key) {
		return proyectos.get(key);
	}
	
	public boolean containsKeyProyecto(Long key) {
		return proyectos.containsKey(key);
	}
	
	public ProyectoInmobiliario removeProyecto(Long key) {
		return proyectos.remove(key);
	}
	
	public Set<Entry<Long, ProyectoInmobiliario>> entrySetProyecto() {
		return proyectos.entrySet();
	}

	
	//metodos Reserva
	public void agregarReserva(Long key, Reserva reserva) {
		reservas.put(key, reserva);
	}

	public Reserva getReserva(Long key) {
		return reservas.get(key);
	}

	public boolean containsReserva(Long key) {
		return reservas.containsKey(key);
	}

	public Reserva removeReserva(Long key) {
		return reservas.remove(key);
	}

	public Set<Entry<Long, Reserva>> entrySetReserva() {
		return reservas.entrySet();
	}
	
}
