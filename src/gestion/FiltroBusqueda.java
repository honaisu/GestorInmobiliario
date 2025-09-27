package gestion;


import modelo.ubicacion.EstadoDepartamento;

/**
 * Clase que representa los criterios de búsqueda para filtrar departamentos
 * o proyectos inmobiliarios.
 * <p>
 * Permite establecer rangos de precios, cantidad mínima de habitaciones y baños,
 * estado del departamento, y características adicionales como piscina, 
 * estacionamiento o dirección.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * FiltroBusqueda filtro = new FiltroBusqueda();
 * filtro.setPrecioMin(50000.0);
 * filtro.setHabitacionesMin(2);
 * filtro.setConPiscina(true);
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class FiltroBusqueda {
	/** Precio mínimo del departamento. */
    private Double precioMin;
    
    /** Precio máximo del departamento. */
    private Double precioMax;
    
    /** Cantidad mínima de habitaciones. */
    private Integer habitacionesMin;
    
    /** Cantidad mínima de baños. */
    private Integer banosMin;
    
    /** Estado del departamento (DISPONIBLE, RESERVADO, VENDIDO). */
    private EstadoDepartamento estado;
    
    /** Indica si se requiere que el departamento tenga piscina. */
    private boolean conPiscina;
    
    /** Indica si se requiere que el departamento tenga estacionamiento. */
    private boolean conEstacionamiento;
    
    /** Dirección o ubicación deseada del departamento. */
    private String direccion;
    
    /** Constructor vacío que crea un filtro sin restricciones. */
	public FiltroBusqueda() {
		
	}
	/**
     * Constructor completo que permite inicializar todos los criterios de búsqueda.
     * 
     * @param precioMin precio mínimo deseado
     * @param precioMax precio máximo deseado
     * @param habitacionesMin cantidad mínima de habitaciones
     * @param banosMin cantidad mínima de baños
     * @param estado estado del departamento
     * @param conPiscina indica si se requiere piscina
     * @param conEstacionamiento indica si se requiere estacionamiento
     * @param direccion dirección deseada
     */
	public FiltroBusqueda(Double precioMin, Double precioMax, Integer habitacionesMin, Integer banosMin,
			EstadoDepartamento estado, boolean conPiscina, boolean conEstacionamiento, String direccion) {
		this.precioMin = precioMin;
		this.precioMax = precioMax;
		this.habitacionesMin = habitacionesMin;
		this.banosMin = banosMin;
		this.estado = estado;
		this.conPiscina = conPiscina;
		this.conEstacionamiento = conEstacionamiento;
		this.direccion = direccion;
	}
	
	/** @return el precio mínimo */
	public Double getPrecioMin() {
		return precioMin;
	}
	
	/** @return el precio máximo */
	public Double getPrecioMax() {
		return precioMax;
	}
	
	/** @return la cantidad mínima de habitaciones */
	public Integer getHabitacionesMin() {
		return habitacionesMin;
	}
	
	/** @return la cantidad mínima de baños */
	public Integer getBanosMin() {
		return banosMin;
	}
	
	/** @return el estado del departamento */
	public EstadoDepartamento getEstado() {
		return estado;
	}
	
	/** @return true si se requiere piscina */
	public boolean getConPiscina() {
		return conPiscina;
	}
	
	/** @return true si se requiere estacionamiento */
	public boolean getConEstacionamiento() {
		return conEstacionamiento;
	}
	
	/** @return la dirección deseada */
	public String getDireccion() {
		return direccion;
	}
	
	/** @param precioMin establece el precio mínimo */
	public void setPrecioMin(Double precioMin) {
		this.precioMin = precioMin;
	}
	
	/** @param precioMax establece el precio máximo */
	public void setPrecioMax(Double precioMax) {
		this.precioMax = precioMax;
	}
	
	/** @param habitacionesMin establece la cantidad mínima de habitaciones */
	public void setHabitacionesMin(Integer habitacionesMin) {
		this.habitacionesMin = habitacionesMin;
	}
	
	/** @param banosMin establece la cantidad mínima de baños */
	public void setBanosMin(Integer banosMin) {
		this.banosMin = banosMin;
	}
	
	/** @param estado establece el estado del departamento */
	public void setEstado(EstadoDepartamento estado) {
		this.estado = estado;
	}
	
	/** @param conPiscina establece si se requiere piscina */
	public void setConPiscina(Boolean conPiscina) {
		this.conPiscina = conPiscina;
	}
	
	/** @param conEstacionamiento establece si se requiere estacionamiento */
	public void setConEstacionamiento(Boolean conEstacionamiento) {
		this.conEstacionamiento = conEstacionamiento;
	}
	
}
