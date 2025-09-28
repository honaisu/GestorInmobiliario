package validaciones;

import excepciones.RutInvalidoException;

/**
 * Clase utilitaria para validar RUTs chilenos.
 * <p>
 * Esta clase no se instancia; todos sus métodos son estáticos.
 * Permite verificar que un RUT tenga el formato correcto y que el dígito
 * verificador sea válido según el algoritmo del módulo 11.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * try {
 *     ValidadorRut.validarRut("12345678-5");
 * } catch (RutInvalidoException e) {
 *     System.out.println("RUT inválido: " + e.getMessage());
 * }
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class ValidadorRut {
	
	/**
     * Valida que un RUT tenga un formato correcto y un dígito verificador válido.
     * <p>
     * El formato esperado es {@code 12345678-9} o {@code 12345678-K}.
     * </p>
     *
     * @param rut RUT a validar
     * @throws RutInvalidoException si el RUT es nulo, no cumple con el formato,
     *                               contiene caracteres inválidos o tiene un dígito verificador incorrecto
     */
	public static void validarRut(String rut) throws RutInvalidoException {
        if (rut == null || !rut.matches("^\\d{7,8}-[kK\\d]$")) {
            throw new RutInvalidoException("El formato del RUT no es válido. Debe ser 12345678-9.");
        }

        try {
            String[] rutParts = rut.split("-");
            String numeroStr = rutParts[0];
            char dv = rutParts[1].toLowerCase().charAt(0);

            int rutNumerico = Integer.parseInt(numeroStr);
            char dvCalculado = calcularDigitoVerificador(rutNumerico);

            if (dv != dvCalculado) {
                throw new RutInvalidoException("El dígito verificador es incorrecto.");
            }
        } catch (NumberFormatException e) {
            // Esto no debería ocurrir si la expresión regular es correcta, pero por si acaso.
            throw new RutInvalidoException("El número del RUT contiene caracteres no válidos.");
        }
    }
	
	/**
     * Calcula el dígito verificador de un RUT usando el algoritmo del módulo 11.
     *
     * @param rutNumerico número del RUT (sin dígito verificador)
     * @return dígito verificador correspondiente ('0'-'9' o 'k')
     */
	private static char calcularDigitoVerificador(int rutNumerico) {
        int m = 0, s = 1;
        for (; rutNumerico != 0; rutNumerico /= 10) {
            s = (s + rutNumerico % 10 * (9 - m++ % 6)) % 11;
        }
        return (char) (s != 0 ? s + 47 : 'k');
    }
}
