package modelo.ubicacion;

public class Departamento {
	private final String CODIGO;
	private final int NUMERO_PISO;
	private int metrosCuadrados;
	private int habitaciones;
	private int banos;
	private boolean disponible;
	private double precio;
	
	public Departamento(String codigo, int numeroPiso, int metrosCuadrados, int habitaciones, int banos) {
		this.CODIGO = codigo.toUpperCase();
		this.NUMERO_PISO = numeroPiso;
		this.metrosCuadrados = metrosCuadrados;
		this.habitaciones = habitaciones;
		this.banos = banos;
		this.disponible = true;
	}
	
	public String getCodigo() {
		return CODIGO;
	}

	public int getNUMERO_PISO() {
		return NUMERO_PISO;
	}

	public int getHabitaciones() {
		return habitaciones;
	}

	public int getMetrosCuadrados() {
		return metrosCuadrados;
	}
	
	public int getBanos() {
		return banos;
	}
	
	//Maneja precios dinámicamente
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public void setPrecio(double precio, int metros) {
		this.precio = precio * (1.2 * metros + 10000);
	}
	
	public void setPrecio(double precio, int metros, int banos) {
		this.precio = precio *(1.4 *metros + 10000) + banos * 100000;
	}
	
	public void setPrecio(double precio, int metros, int banos, int numPiso){
		if (numPiso > 0) {
			this.precio = (precio *(1.4 *metros + 10000) + banos * 100000)*(numPiso);
		}else {
			this.precio = (precio *(1.4 *metros + 10000) + banos * 100000)*(numPiso + 1);
		}
	}
	
	public void setHabitaciones(int habitaciones) {
		this.habitaciones = habitaciones;
	}

	public void setBanos(int banos) {
		this.banos = banos;
	}

	public boolean isDisponible(){
		return disponible;
	}
}