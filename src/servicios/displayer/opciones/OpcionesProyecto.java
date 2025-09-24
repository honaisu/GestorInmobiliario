package servicios.displayer.opciones;

public enum OpcionesProyecto {
	REGISTRAR("Registrar un Proyecto"),
	VER("Ver Datos Seleccionado"),
	BUSCAR("Buscar por Filtro"),
	SALIR("Salir del Programa");
	
	private String nombre;
	private OpcionesProyecto(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}
