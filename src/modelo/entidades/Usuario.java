package modelo.entidades;

/**
 * Usuario dentro de nuestra plataforma.
 * Cada usuario puede ser un COMPRADOR y/o un VENDEDOR.
 * A su vez, posee todos los atributos de una Entidad (Datos Personales y Bancarios).
 */
public abstract class Usuario {
	private String RUT;
	private String NOMBRE; // Primer Nombre(?) o Nombre y Apellido
	private String email = "";
	private String telefono = "000000000";
	
	public Usuario() {
	}
	
	public Usuario(String RUT, String NOMBRE, String email) {
		this.RUT = RUT;
		this.NOMBRE = NOMBRE;
		setEmail(email);
	}
	
	public Usuario(String RUT, String NOMBRE, String email, String telefono) {
		this.RUT = RUT;
		this.NOMBRE = NOMBRE;
		setEmail(email);
		setTelefono(telefono);
	}
	
	public String getRut() { return RUT; }
	public String getNombre() { return NOMBRE; }
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
}