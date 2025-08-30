package modelo.ubicacion;

/**
 * Clase que define un departamento.
 * Cada departamento va a contener un código de identificación, un número
 * de piso; y metros cuadrados, habitaciones y baños que posee de base.
 */
public class Departamento {
	private final String CODIGO;
	private final int NUMERO_PISO;
	private int metrosCuadrados;
	private int habitaciones;
	private int banos;
	private EstadoDepartamento estado;
	
	public Departamento(String codigo, int numeroPiso, int metrosCuadrados, int habitaciones, int banos) {
		this.CODIGO = codigo;
		this.NUMERO_PISO = numeroPiso;
		this.metrosCuadrados = metrosCuadrados;
		this.habitaciones = habitaciones;
		this.banos = banos;
		this.estado = EstadoDepartamento.DISPONIBLE;
	}
	
	public String getCodigo() { return CODIGO; }
	public int getNUMERO_PISO() { return NUMERO_PISO; }
	public int getHabitaciones() { return habitaciones; }
	public int getMetrosCuadrados() { return metrosCuadrados; }
	public int getBanos() { return banos; }
	
	public String getEstado(){
		return estado.toString();
	}
	
	public void setMetrosCudrados(int metros) {
		this.metrosCuadrados = metros;
	}
	
	public void setHabitaciones(int habitaciones) {
		this.habitaciones = habitaciones;
	}

	public void setBanos(int banos) {
		this.banos = banos;
	}

	private void setEstado(EstadoDepartamento e) {
		this.estado = e;
	}
	
	/* TESTING ONLY.
	 * Si fuera más interactivo el programa, podríamos pedirle al usuario que ingrese
	 * si no sólo es por hacer nomás.
	public boolean setEstado(String str) {
		try {
			this.estado = EstadoDepartamento.valueOf(str.toUpperCase());
			return true;
		} catch(IllegalArgumentException e) {
			return false;
		}
	}*/
	
	public void setHasReservado() {
		setEstado(EstadoDepartamento.RESERVADO);
	}
	
	public void setHasDisponible() {
		setEstado(EstadoDepartamento.DISPONIBLE);
	}
	
	public void setHasVendido() {
		setEstado(EstadoDepartamento.VENDIDO);
	}
}