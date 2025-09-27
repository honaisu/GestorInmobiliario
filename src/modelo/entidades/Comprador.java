package modelo.entidades;

/**
 * Clase abstracta que representa a un usuario genérico dentro del sistema.
 * <p>
 * Un {@code Usuario} contiene información de identificación y contacto
 * como RUT, nombre, correo electrónico y teléfono. 
 * Esta clase está diseñada para ser extendida por tipos de usuarios
 * específicos (por ejemplo, {@code Comprador}, {@code Vendedor}, etc.).
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class Comprador {
	
	/** Rol Único Tributario (RUT) del comprador. */
	private String RUT;
	
	/** Nombre completo del comprador. */
	private String nombre;
	
	/** Correo electrónico del comprador. */
	private String email = "";
	
	/** Teléfono de contacto del comprador (por defecto "000000000"). */
	private String telefono = "000000000";
	
	
	/**
     * Constructor vacío. 
     * Útil cuando se necesita crear el objeto sin inicializar atributos
     * para luego asignarlos con setters.
     */
	public Comprador() {
	}
	
	/**
     * Constructor básico de {@code Comprador}.
     * 
     * @param RUT RUT del comprador
     * @param nombre nombre completo del comprador
     * @param email correo electrónico del comprador
     */
	public Comprador(String RUT, String nombre, String email) {
		this.RUT = RUT;
		this.nombre = nombre;
		setEmail(email);
	}
	
	/**
     * Constructor completo de {@code Usuario}.
     * 
     * @param RUT RUT del comprador
     * @param nombre nombre completo del comprador
     * @param email correo electrónico del comprador
     * @param telefono número de teléfono del comprador
     */
	public Comprador(String RUT, String nombre, String email, String telefono) {
		this.RUT = RUT;
		this.nombre = nombre;
		setEmail(email);
		setTelefono(telefono);
	}
	
	/** @return el RUT del comprador */
	public String getRut() {
		return RUT; 
	}
	
	/** @return el nombre del comprador */
	public String getNombre() {
		return nombre; 
	}
	
	/** @return el correo electrónico del comprador */
	public String getEmail() {
		return email;
	}
	
	/** @return el número de teléfono del comprador */
	public String getTelefono() {
		return telefono; 
	}

	/**
     * Establece el correo electrónico del comprador.
     * 
     * @param email nuevo correo electrónico
     */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
     * Establece el número de teléfono del comprador.
     * 
     * @param telefono nuevo número de teléfono (máximo 9 dígitos, sin prefijo +)
     * @return {@code true} si el número es válido y se asignó, 
     *         {@code false} en caso contrario
     */
	public boolean setTelefono(String telefono) {
		if (telefono.length() > 9 || telefono.contains("+")) return false;
		this.telefono = telefono;
		return true;
	}
}