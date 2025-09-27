package validaciones;

import excepciones.EmailInvalidoException;

public class ValidadorEmail {
	
	 //Sirve para por ejemplo verificar que tenga el formato nombreEscrito@algo.com o similar//
	//igual preguntar si lo hacemos solo tipo correro@gmail.com//
	 public static void validarEmail(String email) throws EmailInvalidoException {
	        // Expresión regular para validar la mayoría de los emails comunes.
	        String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

	        if (email == null || !email.matches(patron)) {
	            throw new EmailInvalidoException("El formato del email no es válido.");
	        }
	    }

}
