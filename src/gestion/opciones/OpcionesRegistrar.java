package gestion.opciones;

/**
 * Enumeración que define las opciones disponibles al registrar o modificar
 * un proyecto inmobiliario en la interfaz gráfica.
 * <p>
 * Cada valor de {@code OpcionesRegistrar} tiene un nombre descriptivo
 * que puede mostrarse en botones o menús durante el registro de proyectos
 * y sus edificios o departamentos.
 * </p>
 * 
 * <ul>
 *   <li>{@link #AGREGAR_E} → Agregar un nuevo edificio al proyecto</li>
 *   <li>{@link #REMOVER_E} → Remover un edificio existente del proyecto</li>
 *   <li>{@link #AGREGAR_D} → Agregar un nuevo departamento a un edificio</li>
 *   <li>{@link #REMOVER_D} → Remover un departamento existente de un edificio</li>
 *   <li>{@link #REGISTRAR} → Registrar el proyecto completo</li>
 *   <li>{@link #SALIR} → Cancelar el registro y salir</li>
 * </ul>
 * 
 * @author 
 */
public enum OpcionesRegistrar{
	
	/** Opción para agregar un nuevo edificio. */
    AGREGAR_E("Agregar Edificio"),
    
    /** Opción para remover un edificio existente. */
    REMOVER_E("Remover Edificio"),
    
    /** Opción para agregar un nuevo departamento. */
    AGREGAR_D("Agregar Departamento"),
    
    /** Opción para remover un departamento existente. */
    REMOVER_D("Remover Departamento"),
    
    /** Opción para registrar el proyecto completo. */
    REGISTRAR("Registrar Proyecto"),
    
    /** Opción para cancelar y salir del registro. */
    SALIR("Cancelar");
	
	/** Nombre descriptivo de la opción, usado en la interfaz gráfica. */
	private String nombre;
	
	/**
     * Constructor de la enumeración.
     * 
     * @param nombre nombre descriptivo de la opción
     */
	private OpcionesRegistrar(String nombre) {
		this.nombre = nombre;
	}
	
	/**
     * Obtiene el nombre descriptivo de la opción.
     * 
     * @return nombre de la opción para mostrar en botones o menús
     */
	public String getNombre() {
		return this.nombre;
	}

}
