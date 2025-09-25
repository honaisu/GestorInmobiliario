package servicios.displayer;

public enum OpcionesProyecto {
	REGISTRAR("Registrar un Proyecto"),
	MODIFICAR("Modificar un Proyecto"),
	VER("Ver Datos Seleccionado"),
	BUSCAR("Buscar por Filtro"),
	SALIR("Salir del Programa");
	
	String nombre;
	OpcionesProyecto(String nombre) {
		this.nombre = nombre;
	}
	
	String getNombre() {
		return this.nombre;
	}
}
