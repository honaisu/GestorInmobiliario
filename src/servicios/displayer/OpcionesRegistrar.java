package servicios.displayer;

public enum OpcionesRegistrar{
	AGREGAR_E("Agregar Edificio"),
	REMOVER_E("Remover Edificio"),
	AGREGAR_D("Agregar Departamento"),
	REMOVER_D("Remover Departamento"),
	REGISTRAR("Registrar Proyecto"),
	SALIR("Cancelar");
	
	String nombre;
	OpcionesRegistrar(String nombre) {
		this.nombre = nombre;
	}
	
	String getNombre() {
		return this.nombre;
	}

}
