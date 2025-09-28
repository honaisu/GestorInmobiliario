package excepciones;

/**
 * Excepción personalizada que se lanza cuando un correo electrónico
 * no cumple con el formato esperado.
 * <p>
 * Normalmente utilizada en conjunto con {@link ValidadorEmail}.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorEmail.validarEmail("usuario@dominio");
 * } catch (EmailInvalidoException e) {
 *     System.out.println("Email inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class EmailInvalidoException extends Exception {
	
	/**
     * Crea una nueva excepción con un mensaje descriptivo.
     * 
     * @param mensaje mensaje que describe el error
     */
    public EmailInvalidoException(String mensaje) {
        super(mensaje);
    }
}