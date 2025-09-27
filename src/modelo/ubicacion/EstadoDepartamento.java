package modelo.ubicacion;

/**
 * Representa los posibles estados en que puede encontrarse un {@link Departamento}.
 * <p>
 * Los estados permiten manejar el ciclo de vida del departamento en un proyecto
 * inmobiliario, desde que está disponible hasta que se vende.
 * </p>
 * 
 * <ul>
 *   <li>{@link #DISPONIBLE} → El departamento está libre y puede ser adquirido.</li>
 *   <li>{@link #RESERVADO} → El departamento ha sido apartado temporalmente por un comprador.</li>
 *   <li>{@link #VENDIDO} → El departamento ya fue vendido y no está disponible.</li>
 * </ul>
 * 
 * @author 🄯 Los Bien Corporation
 */
public enum EstadoDepartamento {
	/** El departamento está libre y disponible para su venta. */
	DISPONIBLE,
	
	/** El departamento está reservado por un comprador, pendiente de compra definitiva. */
	RESERVADO,
	
	/** El departamento ya fue vendido y no puede ser adquirido. */
	VENDIDO;
}
