package modelo.ubicacion;

/**
 * Agregados es una clase que se conecta mucho con la clase de Edificio.
 * Se encarga de poder guardar parámetros que no son "esenciales"
 * para poder conformar un edificio.
 * Puede servir como sistema de Tags.
 */
public class Agregados {
	private final String direccion;
	private boolean tienePiscina;
	private boolean tieneEstacionamineto;
	
	
	public Agregados(String direccion, boolean tienePiscina, boolean tieneEstacionamineto) {
		this.direccion = direccion;
		this.tienePiscina = tienePiscina;
		this.tieneEstacionamineto = tieneEstacionamineto;
	}


	public boolean isTienePiscina() {
		return tienePiscina;
	}


	public void setTienePiscina(boolean tienePiscina) {
		this.tienePiscina = tienePiscina;
	}


	public boolean isTieneEstacionamineto() {
		return tieneEstacionamineto;
	}


	public void setTieneEstacionamineto(boolean tieneEstacionamineto) {
		this.tieneEstacionamineto = tieneEstacionamineto;
	}


	public String getDireccion() {
		return direccion;
	}
}
