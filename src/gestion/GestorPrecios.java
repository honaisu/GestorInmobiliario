package gestion;

/**
 * Clase encargada de gestionar todo lo referente a lo "financiero".
 * <p>
 * Esto involucra los precios base y actuales de las entidades que
 * posean esta clase.
 */
public class GestorPrecios {
	private double precioBase;
	private double precioActual;
	
	/**
	 * Constructor usado para construir los NUEVOS precios
	 * @param precioBase
	 */
	public GestorPrecios(double precioBase) {
		this.precioBase = precioBase;
		this.precioActual = precioBase;
	}
	
	/**
	 * Constructor para poder usarlo en la DB, con un precioActual definido (y guardado en la base de datos).
	 * @param precioBase
	 * @param precioActual
	 */
	public GestorPrecios(double precioBase, double precioActual) {
		this.precioBase = precioBase;
		this.precioActual = precioBase;
	}
	
	public double getPrecioBase() {
		return precioBase;
	}
	
	public double getPrecioActual() {
		return precioActual;
	}
	
	/**
     * Actualiza el precio base y resetea el precio actual a este nuevo valor.
     * @param nuevoPrecioBase El nuevo precio base.
     */
    public void setPrecio(double nuevoPrecioBase) {
        this.precioBase = nuevoPrecioBase;
        this.precioActual = nuevoPrecioBase;
    }
    
    /**
     * Lógica de precios dinámica.
     * Aumenta el precio actual basado en la ocupación del edificio.
     * @param totalDepartamentos 	El número total de departamentos en el edificio.
     * @param departamentosVendidos El número de departamentos ya vendidos.
     */
    public void actualizarPrecioPorDemanda(int totalDepartamentos, int departamentosVendidos) {
        if (totalDepartamentos == 0) return;

        double ocupacion = (double) departamentosVendidos / totalDepartamentos;

        // Creamos un factor de aumento: a mayor ocupación, mayor precio.
        // De ejemplo :3, si el 50% está vendido, el precio aumenta un 10%.
        double factorAumento = 1.0 + (ocupacion * 0.20);
        
        this.precioActual = this.precioBase * factorAumento;
    }
}
