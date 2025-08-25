package modelo.datos;

/**
 * La clase DatosPersonales almacena todo dato PERSONAL y de CONTACTO asociado a una persona.
 * Se espera que los datos ingresados estén basados en el sistema CHILENO,
 * ya que la clase está adaptada para ello.
 */
public class DatosPersonales {
	private final String RUT;
	private final String NOMBRE; // Primer Nombre(?) o Nombre y Apellido
	private String email;
	private int telefono;
	
	public DatosPersonales(String RUT, String NOMBRE, String email, int telefono) {
		this.RUT = RUT;
		this.NOMBRE = NOMBRE;
		this.email = email;
		this.telefono = telefono;
	}
	
	public String getRUT() { return RUT; }
	public String getNOMBRE() { return NOMBRE; }
	public String getEmail() { return email;}
	public String getTelefono() { return "+" + telefono; }

	public boolean setEmail(String email) {
		if (email.contentEquals("@email.com")) { // Puede haber más proveedores
			this.email = email;
			return true;
		}
		return false;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
}
