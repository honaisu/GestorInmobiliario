package gestor;

import modelo.entidades.Usuario;
import modelo.ubicacion.Departamento;

/**
 * Clase que permite poder reservar un departamento asociado
 * con un dueño. Sirve como el intermediario entre el usuario
 * y el departamento que se quiere conseguir.
 * Sólo es una reserva.
 */
public class Reserva {
	
	private Usuario comprador;
	private Departamento departamento;
	
	public Reserva(Usuario comprador, Departamento departamento) {
		this.comprador = comprador;
		this.departamento = departamento;
		this.departamento.setHasReservado();
	}

	public Usuario getComprador() {
		return comprador;
	}

	public Departamento getDepartamento() {
		return departamento;
	}
}
