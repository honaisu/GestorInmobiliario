package validaciones;

import excepciones.NombreInvalidoException;

public class ValidadorNombre {
	
	//Enfocado en validar el nombre del vendedor
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
