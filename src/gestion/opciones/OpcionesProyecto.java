package gestion.opciones;

public enum OpcionesProyecto {
	REGISTRAR("Registrar un Proyecto"),
	VER("Ver Datos Seleccionado"),
	BUSCAR("Buscar por Filtro"),
	GUARDAR("Guardar y Salir"),
	SALIR("Salir Sin Guardar");
	
	private String nombre;
	private OpcionesProyecto(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}
