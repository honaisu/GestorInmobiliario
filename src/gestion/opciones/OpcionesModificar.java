package gestion.opciones;

public enum OpcionesModificar {
    AGREGAR_E("Agregar Edificio"),
    REMOVER_E("Remover Edificio"),
    MODIFICAR_E("Modificar Edificio"),
    AGREGAR_D("Agregar Departamento"),
    REMOVER_D("Remover Departamento"),
    MODIFICAR_D("Modificar Departamento"),
    GUARDAR_CAMBIOS("Guardar Cambios"),
    CANCELAR("Cancelar");

    private String nombre;

    OpcionesModificar(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}