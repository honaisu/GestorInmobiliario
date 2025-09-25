package gestion;


import modelo.ubicacion.EstadoDepartamento;

/**
 * Clase encargada de almacenar todos los filtros utilizados para
 * la búsqueda de datos en nuestra aplicación.
 * <p>
 * Cada atributo de esta clase utiliza las clases de los primitivos,
 * hecho para poder ocupar el valor <b>null</b> al querer buscar por filtro.
 */
public class FiltroBusqueda {
	private Double precioMin;
	private Double precioMax;
	private Integer habitacionesMin;
	private Integer banosMin;
	private EstadoDepartamento estado;
	private boolean conPiscina;
	private boolean conEstacionamiento;
	private String direccion;
	
	public FiltroBusqueda() {
		
	}
	
	public FiltroBusqueda(Double precioMin, Double precioMax, Integer habitacionesMin, Integer banosMin,
			EstadoDepartamento estado, boolean conPiscina, boolean conEstacionamiento, String direccion) {
		this.precioMin = precioMin;
		this.precioMax = precioMax;
		this.habitacionesMin = habitacionesMin;
		this.banosMin = banosMin;
		this.estado = estado;
		this.conPiscina = conPiscina;
		this.conEstacionamiento = conEstacionamiento;
		this.direccion = direccion;
	}
	public Double getPrecioMin() {
		return precioMin;
	}
	public Double getPrecioMax() {
		return precioMax;
	}
	public Integer getHabitacionesMin() {
		return habitacionesMin;
	}
	public Integer getBanosMin() {
		return banosMin;
	}
	
	public EstadoDepartamento getEstado() {
		return estado;
	}
	public boolean getConPiscina() {
		return conPiscina;
	}
	public boolean getConEstacionamiento() {
		return conEstacionamiento;
	}
	public String getDireccion() {
		return direccion;
	}
	
	public void setPrecioMin(Double precioMin) {
		this.precioMin = precioMin;
	}
	public void setPrecioMax(Double precioMax) {
		this.precioMax = precioMax;
	}
	public void setHabitacionesMin(Integer habitacionesMin) {
		this.habitacionesMin = habitacionesMin;
	}
	public void setBanosMin(Integer banosMin) {
		this.banosMin = banosMin;
	}
	
	public void setEstado(EstadoDepartamento estado) {
		this.estado = estado;
	}
	public void setConPiscina(Boolean conPiscina) {
		this.conPiscina = conPiscina;
	}
	public void setConEstacionamiento(Boolean conEstacionamiento) {
		this.conEstacionamiento = conEstacionamiento;
	}
	
}
