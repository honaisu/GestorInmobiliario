package modelo.datos;

/**
 * La clase DatosPersonales almacena todo dato PERSONAL y de CONTACTO asociado a una persona.
 * Se espera que los datos ingresados estén basados en el sistema CHILENO,
 * ya que la clase está adaptada para ello.
 */
public class DatosPersonales {
	private final String RUT;
	private final String NOMBRE; // Primer Nombre(?) o Nombre y Apellido
	private String email = "";
	private String telefono = "000000000";
	
	public DatosPersonales(String RUT, String NOMBRE, String email) {
		this.RUT = RUT;
		this.NOMBRE = NOMBRE;
		setEmail(email);
	}
	
	public DatosPersonales(String RUT, String NOMBRE, String email, String telefono) {
		this.RUT = RUT;
		this.NOMBRE = NOMBRE;
		setEmail(email);
		setTelefono(telefono);
	}
	
	public String getRUT() { return RUT; }
	public String getNOMBRE() { return NOMBRE; }
	public String getEmail() { return email; }
	public String getTelefono() { return telefono; }

	public boolean setEmail(String email) {
		if (email == null || email.isEmpty() || !email.contains("@")) return false;
		
		String dominio = email.substring(email.lastIndexOf("@"));
		if (dominio.equals("@email.com")) {
			this.email = email;
			return true;
		}
		return false;
	}

	public boolean setTelefono(String telefono) {
		if (telefono.length() > 9 || telefono.contains("+")) return false;
		this.telefono = telefono;
		return true;
	}
	
	public String getNombre() {
        return this.NOMBRE;
    }
}
