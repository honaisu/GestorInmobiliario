package gestion;

import modelo.ubicacion.Edificio;

/**
 * Clase que gestiona el precio base y el precio actual de un departamento
 * dentro de un proyecto inmobiliario.
 * <p>
 * El {@code GestorPrecios} permite inicializar un precio base, 
 * actualizar el precio actual y recalcularlo dinámicamente en función 
 * de la demanda (relación entre departamentos vendidos y totales).
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class GestorPrecios {
	
	/** Precio inicial asignado al departamento. */
	private double precioBase;
	
	/** Precio actual del departamento, que puede variar según la demanda. */
	private double precioActual;
	
	/**
     * Constructor que asigna un precio base y lo establece 
     * también como precio actual.
     * 
     * @param precioBase precio inicial del departamento
     */
	public GestorPrecios(double precioBase) {
		this.precioBase = precioBase;
		this.precioActual = precioBase;
	}
	
	/**
     * Constructor que permite asignar un precio base y un precio actual inicial.
     * 
     * @param precioBase precio base del departamento
     * @param precioActual precio actual del departamento
     */
	public GestorPrecios(double precioBase, double precioActual) {
		this.precioBase = precioBase;
		this.precioActual = precioBase;
	}
	
	/** @return el precio base del departamento */
	public double getPrecioBase() {
		return precioBase;
	}
	
	/** @return el precio actual del departamento */
	public double getPrecioActual() {
		return precioActual;
	}
	
	/**
     * Establece un nuevo precio base y lo asigna también 
     * como precio actual.
     * 
     * @param nuevoPrecioBase nuevo valor para el precio base
     */
    public void setPrecio(double nuevoPrecioBase) {
        this.precioBase = nuevoPrecioBase;
        this.precioActual = nuevoPrecioBase;
    }
    
    /**
     * Actualiza el precio actual del departamento en función de la demanda.
     * <p>
     * El cálculo se hace según la ocupación:  
     * {@code ocupacion = departamentosVendidos / totalDepartamentos}.
     * </p>
     * <p>
     * Mientras mayor sea la ocupación, mayor será el aumento porcentual
     * respecto al precio base. Ejemplo: si el 50% de los departamentos 
     * están vendidos, el precio aumenta un 10%.
     * </p>
     * 
     * @param totalDepartamentos número total de departamentos del edificio/proyecto
     * @param departamentosVendidos número de departamentos ya vendidos
     */
    public void actualizarPrecioPorDemanda(int totalDepartamentos, int departamentosVendidos) {
        if (totalDepartamentos == 0) return;

        double ocupacion = (double) departamentosVendidos / totalDepartamentos;
        
        // Creamos un factor de aumento: a mayor ocupación, mayor precio.
        // De ejemplo :3, si el 50% está vendido, el precio aumenta un 10%.
        double factorAumento = 1.0 + (ocupacion * 0.20);
        
        this.precioActual = Math.round(this.precioBase * factorAumento * 100.0) / 100.0;
    }
}
