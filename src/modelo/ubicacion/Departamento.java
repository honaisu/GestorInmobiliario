package modelo.ubicacion;

import gestion.GestorPrecios;
import modelo.entidades.EntidadBase;

public class Departamento extends EntidadBase {
	private final String codigo;
	private final int numeroPiso;
	private double metrosCuadrados;
	private EstadoDepartamento estado;
	private int habitaciones;
	private int banos;
	private GestorPrecios precios;
	private Edificio edificioPadre = null;
	
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
			EstadoDepartamento estado, double precioBase, double precioActual) {
		super(id);
		this.codigo = codigo.toUpperCase();
		this.numeroPiso = numeroPiso;
		this.metrosCuadrados = metrosCuadrados;
		this.habitaciones = habitaciones;
		this.banos = banos;
		this.estado = estado;
		this.precios = new GestorPrecios(precioBase, precioActual);
	}
	
	public String getCodigo() {
		return codigo;
	}
	public double getMetrosCuadrados() {
		return metrosCuadrados;
	}
	
	public EstadoDepartamento getEstado() {
		return estado;
	}

	public int getNumeroPiso() {
		return numeroPiso;
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
}