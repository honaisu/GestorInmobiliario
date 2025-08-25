package modelo.entidades;
import modelo.ubicacion.Departamento;

/**
 * Interfaz exclusiva para la compra de departamento.
 */
public interface Comprador {
	public boolean comprar(Departamento departamento);
}
