package validaciones;

import excepciones.NombreInvalidoException;

/**
 * Clase utilitaria para validar nombres de personas.
 * <p>
 * Esta clase no se instancia; todos sus métodos son estáticos.
 * Permite verificar que un nombre cumpla con restricciones de longitud y caracteres válidos.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorNombre.validarNombre("Juan Pérez");
 * } catch (NombreInvalidoException e) {
 *     System.out.println("Nombre inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class ValidadorNombre {
	
	/**
     * Valida que un nombre cumpla con las reglas de formato y longitud.
     * <p>
     * Reglas:
     * <ul>
     *   <li>Máximo 50 caracteres.</li>
     *   <li>Sólo letras y espacios (incluye acentos y la letra ñ).</li>
     * </ul>
     * </p>
     *
     * @param nombre nombre a validar
     * @throws NombreInvalidoException si el nombre excede la longitud máxima
     *                                  o contiene caracteres no permitidos
     */
	public static void validarNombre(String nombre) throws NombreInvalidoException {

        // Limitar la longitud máxima (ej: 50 caracteres).
        if (nombre.length() > 50) {
            throw new NombreInvalidoException("El nombre es demasiado largo (máximo 50 caracteres).");
        }
        
        // El nombre debe contener solo letras y espacios. //enfocada en nombre de persona
        // La expresión regular permite letras (incluyendo acentos y ñ) y espacios.
        String patron = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";
        if (!nombre.matches(patron)) {
            throw new NombreInvalidoException("El nombre solo puede contener letras y espacios.");
        }
    }

}
