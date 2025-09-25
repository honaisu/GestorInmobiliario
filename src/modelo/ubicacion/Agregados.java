package modelo.ubicacion;

public class Agregados {
	private String direccion; //antes final
	private boolean tienePiscina;
	private boolean tieneEstacionamiento;
	
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
