package validaciones;

import excepciones.TelefonoInvalidoException;

public class ValidadorTelefono {
	
	//Tomando en cuenta lo de usurario que tiene 9 datos, osea se "asume" que es del +56 en adelante
	public static void validarTelefono(String numero) throws TelefonoInvalidoException {
        String patron = "^\\d{9}$";

        if (numero == null || !numero.matches(patron)) {
            // Ahora se lanza la nueva excepción
            throw new TelefonoInvalidoException("El número debe contener exactamente 9 dígitos numéricos.");
        }
    }

}

/*
//Forma de usarlo igual
	try {
        // Llamamos al nuevo validador
        ValidadorTelefono.validarFormato(numero);
        System.out.println("✅ El teléfono '" + numero + "' es VÁLIDO.");
    } catch (TelefonoInvalidoException e) { // Capturamos la nueva excepción
        System.out.println("❌ Error en '" + numero + "': " + e.getMessage());
    }
 */