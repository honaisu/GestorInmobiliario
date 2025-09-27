package gestion.opciones;

public enum OpcionesRegistrar{
	AGREGAR_E("Agregar Edificio"),
	REMOVER_E("Remover Edificio"),
	AGREGAR_D("Agregar Departamento"),
	REMOVER_D("Remover Departamento"),
	REGISTRAR("Registrar Proyecto"),
	SALIR("Cancelar");
	
	private String nombre;
	private OpcionesRegistrar(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}

}
