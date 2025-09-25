package servicios.displayer;

public enum OpcionesProyecto {
	REGISTRAR("Registrar un Proyecto"),
	VER("Ver Datos Seleccionado"),
	BUSCAR("Buscar por Filtro"),
	GUARDAR("Guardar y Salir"),
	SALIR("Salir Sin Guardar");
	
	String nombre;
	OpcionesProyecto(String nombre) {
		this.nombre = nombre;
	}
	
	String getNombre() {
		return this.nombre;
	}
}
