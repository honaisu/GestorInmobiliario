package modelo.entidades;

import modelo.ubicacion.Departamento;

/**
 * Interfaz exclusiva para la venta de un departamento.
 */
public interface Vendedor {
	public boolean vender(Departamento departamento);
}
