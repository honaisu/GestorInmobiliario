package excepciones;

public class RutIvalidoException extends Exception{

	 public RutIvalidoException(String mensaje) {
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