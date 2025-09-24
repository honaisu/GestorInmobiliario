package servicios.displayer.opciones;

public enum OpcionesVer {
	COMPRAR("Comprar Seleccionado"),
	RESERVAR("Reservar Seleccionado"),
	SALIR("Cancelar");
	
	private String nombre;
	private OpcionesVer(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}

}
