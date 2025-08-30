package modelo.ubicacion;
import java.util.LinkedList;

/**
 * Clase que contiene una lista de Departamentos, conformando un Edificio.
 * Posee un nombre propio, la lista de departamentos que posee, y
 * "agregados" que es posible agregarle al edificio.
 */
public class Edificio {
	private final String NOMBRE;
	private LinkedList<Departamento> departamentos;
	private Agregados informacion;

	public Edificio(String nombre, Agregados informacion) {
		this.NOMBRE = nombre;
		this.departamentos = new LinkedList<>();
		this.informacion = informacion;
	}
	
	public Departamento getFirst() {
		return departamentos.getFirst();
	}

	public int size() {
		return departamentos.size();
	}

	public boolean remove(Departamento o) {
		return departamentos.remove(o);
	}
	
	//sobrecarga (por si se quiere eliminar por primera ocurrencia, o por indice)
	public Departamento remove(int index) {
		return departamentos.remove(index);
	}

	public void clear() {
		departamentos.clear();
	}

	public Departamento get(int index) {
		return departamentos.get(index);
	}

	public Object[] toArray() {
		return departamentos.toArray();
	}

	public boolean agregarDepartamento(Departamento e) {
		return departamentos.add(e);
	}

	public String getNOMBRE() {
		return NOMBRE;
	}
	
	public Agregados getInformacion() {
		return informacion;
	}
	
}