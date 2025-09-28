package gestion.opciones;

/**
 * Enumeración que define las opciones disponibles al visualizar un proyecto
 * o departamento en la interfaz gráfica.
 * <p>
 * Cada valor de {@code OpcionesVer} tiene un nombre descriptivo
 * que puede mostrarse en botones o menús durante la visualización de datos.
 * </p>
 * 
 * <ul>
 *   <li>{@link #COMPRAR} → Comprar el proyecto o departamento seleccionado</li>
 *   <li>{@link #RESERVAR} → Reservar el proyecto o departamento seleccionado</li>
 *   <li>{@link #SALIR} → Cancelar y cerrar la ventana de visualización</li>
 * </ul>
 * 
 */
public enum OpcionesVer {
	
	/** Opción para comprar el elemento seleccionado. */
    COMPRAR("Comprar Seleccionado"),
    
    /** Opción para reservar el elemento seleccionado. */
    RESERVAR("Reservar Seleccionado"),
    
    /** Opción para cancelar la acción y salir de la ventana. */
    SALIR("Cancelar");
    
    /** Nombre descriptivo de la opción, usado en la interfaz gráfica. */
    private String nombre;
    
    /**
     * Constructor de la enumeración.
     * 
     * @param nombre nombre descriptivo de la opción
     */
    private OpcionesVer(String nombre) {
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
