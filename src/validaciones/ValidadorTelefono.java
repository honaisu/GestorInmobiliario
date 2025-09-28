package validaciones;

import excepciones.TelefonoInvalidoException;

/**
 * Clase utilitaria para validar números de teléfono.
 * <p>
 * Esta clase no se instancia; todos sus métodos son estáticos.
 * Permite verificar que un número de teléfono cumpla con el formato chileno estándar (9 dígitos).
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorTelefono.validarTelefono("912345678");
 * } catch (TelefonoInvalidoException e) {
 *     System.out.println("Teléfono inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class ValidadorTelefono {
	
	/**
     * Valida que un número de teléfono tenga exactamente 9 dígitos numéricos.
     * <p>
     * Se asume que el número corresponde a Chile, excluyendo el prefijo internacional (+56).
     * </p>
     *
     * @param numero número de teléfono a validar
     * @throws TelefonoInvalidoException si el número es nulo o no cumple con el formato
     */
	public static void validarTelefono(String numero) throws TelefonoInvalidoException {
        String patron = "^\\d{9}$";

        if (numero == null || !numero.matches(patron)) {
            // Ahora se lanza la nueva excepción
            throw new TelefonoInvalidoException("El número debe contener exactamente 9 dígitos numéricos.");
        }
    }

}