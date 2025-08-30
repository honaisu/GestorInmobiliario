package gestor;

import modelo.entidades.Usuario;
import modelo.ubicacion.Departamento;


public class Reserva {
	
	private Usuario comprador;
	private Departamento departamento;
	
	public Reserva(Usuario comprador, Departamento departamento) {
		super();
		this.comprador = comprador;
		this.departamento = departamento;
	}

	public Usuario getComprador() {
		return comprador;
	}

	public Departamento getDepartamento() {
		return departamento;
	}
	
	
	
	
	
}
