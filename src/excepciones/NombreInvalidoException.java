package excepciones;

/**
 * Excepción personalizada que se lanza cuando un nombre no cumple
 * con las reglas de formato o longitud permitidas.
 * <p>
 * Normalmente utilizada en conjunto con {@link ValidadorNombre}.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorNombre.validarNombre("Juan123");
 * } catch (NombreInvalidoException e) {
 *     System.out.println("Nombre inválido: " + e.getMessage());
 * }
 * </pre>
 */
public class NombreInvalidoException extends Exception{
	
	/**
     * Crea una nueva excepción con un mensaje descriptivo.
     * 
     * @param mensaje mensaje que describe el error
     */
	public NombreInvalidoException(String mensaje) {
        super(mensaje);
    }
}
