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
	
	//Direccion del Edificio
	private String direccion;
	
	//Indica si el Edificio tiene piscina
	private boolean tienePiscina;
	
	//Indica si el Edificio tiene estacionamiento
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
	
	public Agregados(String direccion) {
		this.direccion = direccion;
	}


	public boolean isTienePiscina() {
		return tienePiscina;
	}


	public void setTienePiscina(boolean tienePiscina) {
		this.tienePiscina = tienePiscina;
	}


	public boolean isTieneEstacionamiento() {
		return tieneEstacionamiento;
	}


	public void setTieneEstacionamiento(boolean tieneEstacionamineto) {
		this.tieneEstacionamiento = tieneEstacionamineto;
	}


	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String d) {
		this.direccion = d;
	}

}
