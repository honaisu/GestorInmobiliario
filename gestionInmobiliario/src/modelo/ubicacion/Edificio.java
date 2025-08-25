package modelo.ubicacion;
import java.util.LinkedList;
import modelo.datos.Direccion;

public class Edificio {
	private final String NOMBRE;
	private final Direccion DIRECCION;
	private LinkedList<Departamento> departamentos;
	
	public Edificio(String nombre, Direccion direccion) {
		NOMBRE = nombre;
		DIRECCION = direccion;
		departamentos = new LinkedList<>();
	}

	public String getNOMBRE() {
		return NOMBRE;
	}
}