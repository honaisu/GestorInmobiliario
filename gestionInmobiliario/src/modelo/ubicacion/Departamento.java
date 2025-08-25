package modelo.ubicacion;

public class Departamento {
	private final String CODIGO;
	private final int NUMERO_PISO;
	private int metrosCuadrados;
	private int habitaciones;
	private int banos;
	private boolean disponible;
	
	public Departamento(String codigo, int numeroPiso, int metrosCuadrados, int habitaciones, int banos) {
		this.CODIGO = codigo;
		this.NUMERO_PISO = numeroPiso;
		this.metrosCuadrados = metrosCuadrados;
		this.habitaciones = habitaciones;
		this.banos = banos;
		this.disponible = true;
	}
	
	public String getCodigo() {
		return CODIGO;
	}

	public int getNUMERO_PISO() {
		return NUMERO_PISO;
	}

	public int getHabitaciones() {
		return habitaciones;
	}

	public int getMetrosCuadrados() {
		return metrosCuadrados;
	}
	
	public int getBanos() {
		return banos;
	}
	
	public void setHabitaciones(int habitaciones) {
		this.habitaciones = habitaciones;
	}

	public void setBanos(int banos) {
		this.banos = banos;
	}

	public boolean isDisponible(){
		return disponible;
	}
}