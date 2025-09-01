package modelo.ubicacion;
import java.util.LinkedList;


public class Edificio {
	private final String NOMBRE;
	private LinkedList<Departamento> departamentos;
	private Agregados informacion;
	

	public Edificio(String nombre, Agregados informacion) {
		this.NOMBRE = nombre;
		this.departamentos = new LinkedList<>();
		this.informacion = informacion;
	}
	
	public Departamento getFirstDepartamento() {
		return departamentos.getFirst();
	}

	public int getCantDepartamentos() {
		return departamentos.size();
	}

	public boolean removeDepartamento(Departamento o) {
		return departamentos.remove(o);
	}
	
	//sobrecarga (por si se quiere eliminar por primera ocurrencia, o por indice, o por codigo de departamento)
	public Departamento removeDepartamento(int index) {
		return departamentos.remove(index);
	}
	
	public boolean removeDepartamento(String codPiso) {
		for (Departamento d: this.departamentos) {
			if (d.getCodigo().equals(codPiso.toUpperCase())) {
				return removeDepartamento(d);
			}
		}
		return false;
	}

	public void clear() {
		departamentos.clear();
	}

	public Departamento getDepartamento(int index) {
		return departamentos.get(index);
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