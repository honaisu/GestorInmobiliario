package gestion;

/**
 * Clase encargada de gestionar todo lo referente a lo "financiero".
 * <p>
 * Esto involucra los precios base y actuales de las entidades que
 * posean esta clase.
 */
public class GestorPrecios {
	private final double precioBase;
	private double precioActual;
	
	public GestorPrecios(double precioBase) {
		this.precioBase = precioBase;
		this.precioActual = precioBase;
	}
	
	public GestorPrecios(double precioBase, double precioActual) {
		this.precioBase = precioBase;
		this.precioActual = precioActual;
	}
	
	public double getPrecioBase() {
		return precioBase;
	}
	
	public double getPrecioActual() {
		return precioActual;
	}
	
	//Maneja precios dinámicamente
	public void setPrecio(double precio) {
		this.precioActual = precio;
	}
	
	public void setPrecio(double precio, int metros) {
		this.precioActual = precio * (1.2 * metros + 10000);
	}
	
	public void setPrecio(double precio, int metros, int banos) {
		this.precioActual = precio *(1.4 *metros + 10000) + banos * 100000;
	}
	
	public void setPrecio(double precio, int metros, int banos, int numPiso){
		if (numPiso > 0) {
			this.precioActual = (precio *(1.4 *metros + 10000) + banos * 100000)*(numPiso);
		}else {
			this.precioActual = (precio *(1.4 *metros + 10000) + banos * 100000)*(numPiso + 1);
		}
	}
}
