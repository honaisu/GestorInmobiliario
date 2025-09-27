package modelo.ubicacion;



import modelo.entidades.Comprador;
import gestion.GestorPrecios;
import modelo.entidades.EntidadBase;

/**
 * Representa un departamento dentro de un edificio.
 * <p>
 * Un {@code Departamento} contiene información sobre su ubicación, 
 * dimensiones, cantidad de habitaciones y baños, estado actual, 
 * precios asociados y datos de su comprador (en caso de estar reservado o vendido).
 * </p>
 * 
 * Está asociado a un {@link Edificio} y utiliza un {@link GestorPrecios} 
 * para manejar el precio base y el precio actual del inmueble.
 * 
 * @author 🄯 Los Bien Corporation
 */
public class Departamento extends EntidadBase {
	private final String codigo;
	private int numeroPiso; //antes tenia final
	private double metrosCuadrados;
	private EstadoDepartamento estado;
	private int habitaciones;
	private int banos;
	private GestorPrecios precios;
	private Edificio edificioPadre = null;
	
	private Comprador comprador = new Comprador();
	private String rutReserva = "";
	
	/**
	 * Constructor usado para generar nuevos departamentos "vacíos".
	 */
	public Departamento(long id, String codigo, int numeroPiso, 
			double precioBase, Edificio edificio) {
		super(id);
		this.codigo = codigo.toUpperCase();
		this.numeroPiso = numeroPiso;
		this.precios = new GestorPrecios(precioBase);
		this.estado = EstadoDepartamento.DISPONIBLE;
		this.edificioPadre = edificio;
	}
	
	public Departamento(String codigo, int numeroPiso, double precioBase, Edificio edificio) {
		super(null);
		this.codigo = codigo.toUpperCase();
		this.numeroPiso = numeroPiso;
		this.precios = new GestorPrecios(precioBase);
		this.estado = EstadoDepartamento.DISPONIBLE;
		this.edificioPadre = edificio;
	}
	
	/**
	 * Constructor usado para poder crear los datos del departamento
	 * dentro de la DB.
	 */
	public Departamento(long id, String codigo, int numeroPiso, 
			double metrosCuadrados, int habitaciones, int banos, 
			EstadoDepartamento estado, double precioBase, double precioActual, String rutReserva) {
		super(id);
		this.codigo = codigo.toUpperCase();
		this.numeroPiso = numeroPiso;
		this.metrosCuadrados = metrosCuadrados;
		this.habitaciones = habitaciones;
		this.banos = banos;
		this.estado = estado;
		this.precios = new GestorPrecios(precioBase, precioActual);
		this.rutReserva = rutReserva;
	}

	
	public String getCodigo() {
		return codigo;
	}
	public double getMetrosCuadrados() {
		return metrosCuadrados;
	}
	
	public void setMetrosCuadrados(double m) {
		this.metrosCuadrados = m;
	}
	
	public void setEstado(EstadoDepartamento estado) {
		this.estado = estado;
	}

	public EstadoDepartamento getEstado() {
		return estado;
	}

	public int getNumeroPiso() {
		return numeroPiso;
	}
	
	public void setNumeroPiso(int num) {
		this.numeroPiso = num;
	}

	public int getHabitaciones() {
		return habitaciones;
	}
	public int getBanos() {
		return banos;
	}
	
	public GestorPrecios getGestorPrecios() {
		return precios;
	}
	
	public Edificio getEdificioPadre() {
		return edificioPadre;
	}
	
	public void setHabitaciones(int habitaciones) {
		this.habitaciones = habitaciones;
	}

	public void setBanos(int banos) {
		this.banos = banos;
	}
	
	public void setEdificioPadre(Edificio e) {
		this.edificioPadre = e;
	}

	public String getRutReserva() {
		return rutReserva;
	}

	public void setRutReserva(String rutReserva) {
		this.rutReserva = rutReserva;
	}

	public Comprador getComprador() {
		return comprador;
	}

	public void setComprador(Comprador comprador) {
		this.comprador = comprador;
	}
}