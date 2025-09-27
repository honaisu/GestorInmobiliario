package modelo.ubicacion;
/**
 * Representa los agregados o características adicionales asociadas a un Edificio,
 * como la existencia de piscina o estacionamiento, junto con su dirección.
 * <p>
 * Esta clase puede ser utilizada para complementar la información de una vivienda
 * o inmueble dentro de un sistema inmobiliario.
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class Agregados {
	
	/** Dirección del Edificio asociada a los agregados. */
	private String direccion;
	
	/** Indica si el Edificio cuenta con piscina. */
	private boolean tienePiscina;
	
	/** Indica si el Edificio cuenta con estacionamiento. */
	private boolean tieneEstacionamiento;
	
	/**
	 * Crea un objeto {@code Agregados} con todos sus atributos especificados.
	 * 
	 * @param direccion
	 * @param tienePiscina
	 * @param tieneEstacionamiento
	 */
	public Agregados(String direccion, boolean tienePiscina, boolean tieneEstacionamiento) {
		this.direccion = direccion;
		this.tienePiscina = tienePiscina;
		this.tieneEstacionamiento = tieneEstacionamiento;
	}
	
	/**
	 * Crea un objeto {@code Agregados} únicamente con la direccion del Edificio.
	 * 
	 * @param direccion
	 */
	public Agregados(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Verifica si el Edificio tiene Piscina
	 * 
	 * @return  {@code true} si el Edificio cuenta con piscina, {@code false} en caso contrario
	 */
	public boolean isTienePiscina() {
		return tienePiscina;
	}

	/**
	 * Establece si el Edificio cuenta con piscina. 
	 * 
	 * @param tienePiscina {@code true} si el Edificio cuenta con piscina, {@code false} en caso contrario
	 */
	public void setTienePiscina(boolean tienePiscina) {
		this.tienePiscina = tienePiscina;
	}

	
	/**
	 * Verifica si el Edificio tiene Estacionamiento.
	 * 
	 * @return  {@code true} si el Edificio cuenta con estacionamiento, {@code false} en caso contrario
	 */
	public boolean isTieneEstacionamiento() {
		return tieneEstacionamiento;
	}

	/**
	 * Establece si el Edificio cuenta con piscina. 
	 * 
	 * @param tieneEstacionamineto {@code true} si el Edificio cuenta con estacionamiento, {@code false} en caso contrario
	 */
	public void setTieneEstacionamiento(boolean tieneEstacionamineto) {
		this.tieneEstacionamiento = tieneEstacionamineto;
	}

	
	/**
     * Obtiene la dirección del Edificio.
     * 
     * @return la dirección del Edificio
     */
	public String getDireccion() {
		return direccion;
	}
	
	/**
     * Establece la dirección del Edificio.
     * 
     * @param d la nueva dirección del Edificio
     */
	public void setDireccion(String d) {
		this.direccion = d;
	}

}
