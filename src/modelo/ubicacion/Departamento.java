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
	
	/** Código único del departamento (ej: A101, B202, etc.). */
	private final String codigo;
	
	/** Número de piso en el que se encuentra el departamento. */
	private int numeroPiso; //antes tenia final
	
	/** Superficie del departamento en metros cuadrados. */
	private double metrosCuadrados;
	
	/** Estado actual del departamento (disponible, reservado, vendido, etc.). */
	private EstadoDepartamento estado;
	
	/** Número de habitaciones que posee el departamento. */
	private int habitaciones;
	
	/** Número de baños que posee el departamento. */
	private int banos;
	
	/** Gestor encargado de administrar el precio base y actual del departamento. */
	private GestorPrecios precios;
	
	/** Edificio al que pertenece este departamento. */
	private Edificio edificioPadre = null;
	
	/** Comprador asociado al departamento (en caso de reserva o compra). */
	private Comprador comprador = new Comprador();
	
	/** RUT de la persona que ha realizado una reserva, si aplica. */
	private String rutReserva = "";
	
	/**
     * Constructor usado para generar nuevos departamentos "vacíos" 
     * con estado {@link EstadoDepartamento#DISPONIBLE}.
     *
     * @param id identificador único del departamento
     * @param codigo código del departamento (ej: A101, B202)
     * @param numeroPiso número de piso en el que se encuentra
     * @param precioBase precio base inicial
     * @param edificio edificio al que pertenece el departamento
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
	
	/**
     * Constructor usado para crear un departamento nuevo 
     * sin especificar identificador único.
     *
     * @param codigo código del departamento
     * @param numeroPiso número de piso
     * @param precioBase precio base inicial
     * @param edificio edificio al que pertenece
     */
	public Departamento(String codigo, int numeroPiso, double precioBase, Edificio edificio) {
		super(null);
		this.codigo = codigo.toUpperCase();
		this.numeroPiso = numeroPiso;
		this.precios = new GestorPrecios(precioBase);
		this.estado = EstadoDepartamento.DISPONIBLE;
		this.edificioPadre = edificio;
	}
	
	/**
     * Constructor usado principalmente para reconstruir 
     * los datos del departamento desde la base de datos.
     *
     * @param id identificador único
     * @param codigo código del departamento
     * @param numeroPiso número de piso
     * @param metrosCuadrados superficie total en m²
     * @param habitaciones número de habitaciones
     * @param banos número de baños
     * @param estado estado actual del departamento
     * @param precioBase precio base del departamento
     * @param precioActual precio actual del departamento
     * @param rutReserva rut del comprador que lo ha reservado (si corresponde)
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

	/** @return código único del departamento */
	public String getCodigo() {
		return codigo;
	}
	
	/** @return superficie del departamento en m² */
	public double getMetrosCuadrados() {
		return metrosCuadrados;
	}
	
    /**
     * Define la superficie en m² del departamento.
     * @param m metros cuadrados
     */
	public void setMetrosCuadrados(double m) {
		this.metrosCuadrados = m;
	}
	
	/** @return estado actual del departamento */
	public void setEstado(EstadoDepartamento estado) {
		this.estado = estado;
	}
	
	/**
     * Establece el estado del departamento.
     * @param estado nuevo estado
     */
	public EstadoDepartamento getEstado() {
		return estado;
	}
	
	/** @return número de piso del departamento */
	public int getNumeroPiso() {
		return numeroPiso;
	}
	
	/**
     * Establece el número de piso.
     * @param num número de piso
     */
	public void setNumeroPiso(int num) {
		this.numeroPiso = num;
	}
	
	/** @return cantidad de habitaciones */
	public int getHabitaciones() {
		return habitaciones;
	}
	
	/**
     * Establece la cantidad de baños.
     * @param banos número de baños
     */
	public void setBanos(int banos) {
		this.banos = banos;
	}
	
	/** @return cantidad de baños */
	public int getBanos() {
		return banos;
	}
	
	/** @return gestor de precios del departamento */
	public GestorPrecios getGestorPrecios() {
		return precios;
	}
	
	/** @return edificio al cual pertenece el departamento */
	public Edificio getEdificioPadre() {
		return edificioPadre;
	}
	
	/**
     * Establece la cantidad de habitaciones.
     * @param banos número de baños
     */
	public void setHabitaciones(int habitaciones) {
		this.habitaciones = habitaciones;
	}

	/**
     * Define el edificio al que pertenece el departamento.
     * @param e edificio padre
     */
	public void setEdificioPadre(Edificio e) {
		this.edificioPadre = e;
	}

	/** @return rut de la persona que reservó el departamento */
	public String getRutReserva() {
		return rutReserva;
	}

	/**
     * Establece el rut de la persona que reserva el departamento.
     * @param rutReserva rut de la persona que reserva
     */
	public void setRutReserva(String rutReserva) {
		this.rutReserva = rutReserva;
	}

	/** @return comprador asociado al departamento */
	public Comprador getComprador() {
		return comprador;
	}

	/**
     * Asigna un comprador al departamento.
     * @param comprador comprador asociado
     */
	public void setComprador(Comprador comprador) {
		this.comprador = comprador;
	}
}