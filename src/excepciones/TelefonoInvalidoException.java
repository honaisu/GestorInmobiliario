package excepciones;

/**
 * Excepción personalizada que se lanza cuando un número de teléfono
 * no cumple con el formato esperado.
 * <p>
 * Normalmente utilizada en conjunto con {@link ValidadorTelefono}.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorTelefono.validarTelefono("123");
 * } catch (TelefonoInvalidoException e) {
 *     System.out.println("Teléfono inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class TelefonoInvalidoException extends Exception {
	
	/**
     * Crea una nueva excepción con un mensaje descriptivo.
     * 
     * @param mensaje mensaje que describe el error
     */
    public TelefonoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
