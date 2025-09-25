package modelo.ubicacion;
import java.util.LinkedList;

import modelo.entidades.EntidadBase;


public class Edificio extends EntidadBase {
	private final String NOMBRE;
	private LinkedList<Departamento> departamentos;
	private Agregados informacion;
	private ProyectoInmobiliario proyectoPadre = null;
	
	public Edificio(long id, String nombre, Agregados informacion, ProyectoInmobiliario proyecto) {
		super(id);
		this.proyectoPadre = proyecto;
		this.NOMBRE = nombre;
		this.departamentos = new LinkedList<>();
		this.informacion = informacion;
	}
	
	public Edificio(String nombre, String direccion, boolean piscina, boolean estacionamiento) {
		super(null);
		this.NOMBRE = nombre;
		
		this.departamentos = new LinkedList<>();
		this.informacion = new Agregados(direccion, piscina, estacionamiento);
	}


	public Edificio(long id, String nombre, String direccion, boolean piscina, boolean estacionamiento) {
		super(id);
		this.NOMBRE = nombre;
		
		this.departamentos = new LinkedList<>();
		this.informacion = new Agregados(direccion, piscina, estacionamiento);
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
	
	//sobrecarga (por si se quiere eliminar por primera o currencia, o por indice, o por codigo de departamento)
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
	
	public Departamento getDepartamento(long idDepartamento) {
		for (Departamento d : this.departamentos) {
			if (d.getId() == idDepartamento) return d;
		}
		return null;
	}

	public boolean agregarDepartamento(Departamento e) {
		return departamentos.add(e);
	}

	public String getNombre() {
		return NOMBRE;
	}
	
	public Agregados getInformacion() {
		return informacion;
	}
	
	public String getNOMBRE() {
		return NOMBRE;
	}

	public LinkedList<Departamento> getDepartamentos() {
		return departamentos;
	}

	public ProyectoInmobiliario getProyectoPadre() {
		return proyectoPadre;
	}
	public void setProyectoPadre(ProyectoInmobiliario proyecto) {
		this.proyectoPadre = proyecto;
	}
}