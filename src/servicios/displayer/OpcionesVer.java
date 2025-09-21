package servicios.displayer;

public enum OpcionesVer {
	COMPRAR("Comprar Seleccionado"),
	RESERVAR("Reservar Seleccionado"),
	SALIR("Cancelar");
	
	String nombre;
	OpcionesVer(String nombre) {
		this.nombre = nombre;
	}
	
	String getNombre() {
		return this.nombre;
	}

}
