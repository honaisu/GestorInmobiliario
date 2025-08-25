package servicios.roles;

import java.util.LinkedList;
import modelo.ubicacion.Departamento;

/**
 * Clase que nos permite poder definir un "comprador" dentro del sistema.
 * Definimos un Comprador como una clase que puede ser capaz de comprar propiedades
 * y de ver su propia lista de compra.
 */
public class RolComprador {
	private LinkedList<Departamento> departamentosComprados;
	
	public RolComprador() {
		this.departamentosComprados = new LinkedList<>();
	}
	
	public void comprarDepartamento(Departamento nuevoDepartamento) {
        this.departamentosComprados.add(nuevoDepartamento); 
	}
	
	public int getCantDepartamentosComprados( ) {
		return departamentosComprados.size();
	}
}
