package validaciones;

import excepciones.RutIvalidoException;

public class ValidadorRut {
	
	
	/*
	 * Tomando en cuenta que es RUT Chileno eso si
	 * con formato tipo -> 12345678-9 o -k (por eso es kK\\d)
	 * */ //no se si sirva o es muy básico
	public static void validarRut(String rut) throws RutIvalidoException {
        if (rut == null || !rut.matches("^\\d{7,8}-[kK\\d]$")) {
            throw new RutIvalidoException("El formato del RUT no es válido. Debe ser 12345678-9.");
        }

        try {
            String[] rutParts = rut.split("-");
            String numeroStr = rutParts[0];
            char dv = rutParts[1].toLowerCase().charAt(0);

            int rutNumerico = Integer.parseInt(numeroStr);
            char dvCalculado = calcularDigitoVerificador(rutNumerico);

            if (dv != dvCalculado) {
                throw new RutIvalidoException("El dígito verificador es incorrecto.");
            }
        } catch (NumberFormatException e) {
            // Esto no debería ocurrir si la expresión regular es correcta, pero por si acaso.
            throw new RutIvalidoException("El número del RUT contiene caracteres no válidos.");
        }
    }
	
	//Algoritmo del modulo 11 (literal es por %11 xd)
	private static char calcularDigitoVerificador(int rutNumerico) {
        int m = 0, s = 1;
        for (; rutNumerico != 0; rutNumerico /= 10) {
            s = (s + rutNumerico % 10 * (9 - m++ % 6)) % 11;
        }
        return (char) (s != 0 ? s + 47 : 'k');
    }
}
