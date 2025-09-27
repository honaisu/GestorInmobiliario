package modelo.ubicacion;
import java.util.LinkedList;

import modelo.entidades.EntidadBase;

/**
 * Representa un edificio dentro de un proyecto inmobiliario. 
 * <p>
 * Un {@code Edificio} está compuesto por varios {@link Departamento departamentos},
 * posee información adicional representada por {@link Agregados} y puede estar asociado
 * a un {@link ProyectoInmobiliario}.
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class Edificio extends EntidadBase {
	
	/** Nombre del edificio. */
	private String nombre; 
	
	/** Lista de departamentos contenidos en el edificio. */
	private LinkedList<Departamento> departamentos;
	
	/** Información adicional de la propiedad (dirección, piscina, estacionamiento). */
	private Agregados informacion;
	
	/** Proyecto inmobiliario al cual pertenece el edificio. */
	private ProyectoInmobiliario proyectoPadre = null;
	
	
	/**
     * Crea un nuevo edificio con todos sus atributos especificados.
     *
     * @param id identificador único del edificio
     * @param nombre nombre del edificio
     * @param informacion datos adicionales asociados al edificio
     * @param proyecto proyecto inmobiliario al cual pertenece el edificio
     */
	public Edificio(long id, String nombre, Agregados informacion, ProyectoInmobiliario proyecto) {
		super(id);
		this.proyectoPadre = proyecto;
		this.nombre = nombre;
		this.departamentos = new LinkedList<>();
		this.informacion = informacion;
	}
	
	/**
     * Crea un nuevo edificio a partir de su nombre y características básicas.
     * 
     * @param nombre nombre del edificio
     * @param direccion dirección del edificio
     * @param piscina {@code true} si el edificio cuenta con piscina, {@code false} en caso contrario
     * @param estacionamiento {@code true} si el edificio cuenta con estacionamiento, {@code false} en caso contrario
     */
	public Edificio(String nombre, String direccion, boolean piscina, boolean estacionamiento) {
		super(null);
		this.nombre = nombre;
		
		this.departamentos = new LinkedList<>();
		this.informacion = new Agregados(direccion, piscina, estacionamiento);
	}

	/**
     * Crea un nuevo edificio a partir de su identificador, nombre y características básicas.
     * 
     * @param id identificador único del edificio
     * @param nombre nombre del edificio
     * @param direccion dirección del edificio
     * @param piscina {@code true} si el edificio cuenta con piscina, {@code false} en caso contrario
     * @param estacionamiento {@code true} si el edificio cuenta con estacionamiento, {@code false} en caso contrario
     */
	public Edificio(long id, String nombre, String direccion, boolean piscina, boolean estacionamiento) {
		super(id);
		this.nombre = nombre;
		
		this.departamentos = new LinkedList<>();
		this.informacion = new Agregados(direccion, piscina, estacionamiento);
	}
	
	/**
     * Agrega un nuevo departamento al edificio.
     * 
     * @param e el departamento a agregar
     * @return {@code true} si se agregó correctamente, {@code false} en caso contrario
     */
	public boolean agregarDepartamento(Departamento e) {
		return departamentos.add(e);
	}
	/**
     * Obtiene el nombre del edificio.
     * 
     * @return el nombre del edificio
     */
	public String getNombre() {
		return nombre;
	}
	
	/**
     * Establece un nuevo nombre para el edificio.
     * 
     * @param n el nuevo nombre
     */
	public void setNombre(String n) {
		this.nombre = n;
	}
	
	/**
     * Obtiene la información adicional del edificio.
     * 
     * @return la información del edificio
     */
	public Agregados getInformacion() {
		return informacion;
	}
	
	/**
     * Obtiene la lista completa de departamentos del edificio.
     * 
     * @return lista de departamentos
     */
	public LinkedList<Departamento> getDepartamentos() {
		return departamentos;
	}
	
	public Departamento getDepartamento(long idDepartamento) {
        for (Departamento d : this.departamentos) {
            if (d.getId() == idDepartamento) return d;
        }
        return null;
    }
	

	/**
     * Obtiene el proyecto inmobiliario al cual pertenece este edificio.
     * 
     * @return el proyecto padre, o {@code null} si no está asignado
     */
	public ProyectoInmobiliario getProyectoPadre() {
		return proyectoPadre;
	}
	
	/**
     * Asocia el edificio a un proyecto inmobiliario.
     * 
     * @param proyecto el proyecto a establecer como padre
     */
	public void setProyectoPadre(ProyectoInmobiliario proyecto) {
		this.proyectoPadre = proyecto;
	}
}