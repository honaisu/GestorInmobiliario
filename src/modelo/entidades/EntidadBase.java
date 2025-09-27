package modelo.entidades;

/**
 * Clase base abstracta que proporciona un identificador único común
 * para todas las entidades del sistema.
 * <p>
 * Todas las clases que heredan de {@code EntidadBase} cuentan con un
 * atributo {@link #id} que representa su identificador dentro del sistema
 * o base de datos.
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public abstract class EntidadBase {
	
	/** Identificador único de la entidad. */
	protected Long id = null;
	
	/**
     * Constructor de la entidad base.
     * 
     * @param id identificador único de la entidad, o {@code null} si no se asigna inicialmente
     */
	public EntidadBase(Long id) {
		this.id = id;
	}
	
	/**
     * Obtiene el identificador único de la entidad.
     * 
     * @return el identificador de la entidad, o {@code null} si no está definido
     */
	public Long getId() {
		return id;
	}
	
	/**
     * Establece un nuevo identificador para la entidad.
     * 
     * @param id nuevo identificador
     */
	public void setId(long id) {
		this.id = id;
	}
}
