package gestion.opciones;

/**
 * Enumeración que representa las opciones disponibles para interactuar
 * con un proyecto inmobiliario en la interfaz gráfica.
 * <p>
 * Cada valor de {@code OpcionesProyecto} tiene un nombre descriptivo
 * que puede mostrarse en botones o menús dentro de la ventana de la aplicación.
 * </p>
 * 
 * <ul>
 *   <li>{@link #REGISTRAR} → Registrar un nuevo proyecto</li>
 *   <li>{@link #MODIFICAR} → Modificar un proyecto existente</li>
 *   <li>{@link #VER} → Ver los datos del proyecto seleccionado</li>
 *   <li>{@link #BUSCAR} → Buscar un proyecto mediante filtros</li>
 *   <li>{@link #GUARDAR} → Guardar los cambios y salir</li>
 *   <li>{@link #SALIR} → Salir sin guardar cambios</li>
 * </ul>
 * 
 * @author 
 */
public enum OpcionesProyecto {
	
	/** Opción para registrar un nuevo proyecto. */
	REGISTRAR("Registrar un Proyecto"),
	
	/** Opción para modificar un proyecto existente. */
	MODIFICAR("Modificar un Proyecto"),
	
	/** Opción para ver los datos del proyecto seleccionado. */
	VER("Ver Datos Seleccionado"),
	
	/** Opción para buscar proyectos mediante filtros. */
	BUSCAR("Buscar por Filtro"),
	
	/** Opción para guardar los cambios y salir. */
	GUARDAR("Guardar y Salir"),
	
	/** Opción para salir sin guardar cambios. */
	SALIR("Salir Sin Guardar");
	
	/** Nombre descriptivo de la opción, usado en la interfaz gráfica. */
	private String nombre;
	
	/**
     * Constructor de la enumeración.
     * 
     * @param nombre nombre descriptivo de la opción
     */
	private OpcionesProyecto(String nombre) {
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
