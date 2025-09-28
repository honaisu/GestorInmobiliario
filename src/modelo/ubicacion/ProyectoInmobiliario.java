package modelo.ubicacion;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;

import modelo.entidades.EntidadBase;

/**
 * Representa un proyecto inmobiliario que agrupa a uno o varios {@link Edificio edificios}.
 * <p>
 * Un {@code ProyectoInmobiliario} incluye información como su nombre,
 * el vendedor responsable, la fecha de inicio/oferta y los edificios asociados.
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class ProyectoInmobiliario extends EntidadBase {
	
	/** Nombre del proyecto inmobiliario. */
	private String nombreProyecto;
	
	/** Nombre del vendedor o empresa a cargo del proyecto. */
	private String vendedor;
	
	/** Fecha de inicio u oferta del proyecto. */
	private LocalDate fecha;
	
	/** Lista de edificios que componen el proyecto. */
	private LinkedList<Edificio> edificios;
	
	/**
     * Crea un nuevo proyecto inmobiliario con todos sus atributos especificados.
     * 
     * @param id identificador único del proyecto
     * @param nombreProyecto nombre del proyecto
     * @param vendedor nombre del vendedor o empresa responsable
     * @param fecha fecha de inicio u oferta del proyecto
     */
	public ProyectoInmobiliario(long id, String nombreProyecto, String vendedor, LocalDate fecha) {
		super(id);
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = fecha;
		this.edificios = new LinkedList<>();
	}
	
	/**
     * Crea un nuevo proyecto inmobiliario sin identificador único explícito.
     * 
     * @param nombreProyecto nombre del proyecto
     * @param vendedor nombre del vendedor o empresa responsable
     * @param fecha fecha de inicio u oferta del proyecto
     */
	public ProyectoInmobiliario(String nombreProyecto, String vendedor, LocalDate fecha) {
		super(null);
		this.nombreProyecto = nombreProyecto;
		this.vendedor = vendedor;
		this.fecha = fecha;
		this.edificios = new LinkedList<>();
	}

	/**
     * Obtiene el nombre del proyecto inmobiliario.
     * 
     * @return nombre del proyecto
     */
	public String getNombreProyecto() {
		return nombreProyecto;
	}
	
	/**
     * Obtiene la fecha del proyecto en un formato legible para el usuario.
     * 
     * @return fecha en formato localizado (ej: "27-sep-2025 13:45:12")
     */
	public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
	
	/**
     * Obtiene la fecha del proyecto en un formato legible para el usuario.
     * 
     * @return fecha en formato localizado (ej: "27-sep-2025 13:45:12")
     */
	public String getFecha() {
		return fecha.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}
	
	/**
     * Obtiene el nombre del vendedor o empresa responsable.
     * 
     * @return nombre del vendedor
     */
	public String getVendedor() {
		return vendedor;
	}
	
	/**
     * Obtiene el nombre del vendedor o empresa responsable.
     * 
     * @return nombre del vendedor
     */
	public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
	
	/**
     * Obtiene la fecha del proyecto en formato ISO (ej: "2025-09-27").
     * 
     * @return fecha del proyecto en formato estándar
     */
	public String getFechaOferta() {
		return fecha.toString();
	}

	/**
     * Agrega un nuevo edificio al proyecto inmobiliario.
     * 
     * @param edificio el edificio a agregar
     */
	public void addEdificio(Edificio edificio) {
		this.edificios.add(edificio);
	}
	
	/**
     * Busca un edificio dentro del proyecto por su identificador.
     * 
     * @param idEdificio identificador del edificio
     * @return el edificio encontrado, o {@code null} si no existe
     */
	public Edificio getEdificio(long idEdificio) {
		for (Edificio e : edificios) {
			if (e.getId() == idEdificio) return e;
		}
		return null;
	}
	
	/**
     * Obtiene la lista completa de edificios que conforman el proyecto.
     * 
     * @return lista de edificios
     */
	public List<Edificio> getEdificios() {
		return edificios;
	}
}
