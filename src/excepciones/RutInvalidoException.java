package excepciones;

/**
 * Excepción personalizada que se lanza cuando un RUT chileno
 * no cumple con el formato o tiene un dígito verificador incorrecto.
 * <p>
 * Normalmente utilizada en conjunto con {@link ValidadorRut}.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorRut.validarRut("12345678-9");
 * } catch (RutInvalidoException e) {
 *     System.out.println("RUT inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class RutInvalidoException extends Exception{
	
	/**
	 * Crea una nueva excepción con un mensaje descriptivo.
	 * 
	 * @param mensaje mensaje que describe el error
	 */
	public RutInvalidoException(String mensaje) {
		super(mensaje); // Llama al constructor de la clase padre (Exception)
	}
}

//Como debería usarse
/*
	try {
        ValidadorRut.validarRut(rutValido);
        System.out.println("El RUT '" + rutValido + "' es válido.");
    } catch (RutIvalidoException e) {
        System.out.println("Error al validar '" + rutValido + "': " + e.getMessage());
    }
 */