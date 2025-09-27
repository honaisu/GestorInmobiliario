package validaciones;

import excepciones.EmailInvalidoException;

/**
 * Clase utilitaria para validar correos electrónicos.
 * <p>
 * Esta clase no se instancia; todos sus métodos son estáticos.
 * Permite verificar que un email cumpla con un formato estándar.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorEmail.validarEmail("usuario@dominio.com");
 * } catch (EmailInvalidoException e) {
 *     System.out.println("Email inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class ValidadorEmail {
	
	/**
	 * Clase utilitaria para validar correos electrónicos.
	 * <p>
	 * Esta clase no se instancia; todos sus métodos son estáticos.
	 * Permite verificar que un email cumpla con un formato estándar.
	 * </p>
	 * 
	 * <p>Ejemplo de uso:</p>
	 * <pre>
	 * try {
	 *     ValidadorEmail.validarEmail("usuario@dominio.com");
	 * } catch (EmailInvalidoException e) {
	 *     System.out.println("Email inválido: " + e.getMessage());
	 * }
	 * </pre>
	 */
	 public static void validarEmail(String email) throws EmailInvalidoException {
	        // Expresión regular para validar la mayoría de los emails comunes.
	        String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	        
	        if (email == null || !email.matches(patron)) {
	            throw new EmailInvalidoException("El formato del email no es válido.");
	        }
	    }

}
