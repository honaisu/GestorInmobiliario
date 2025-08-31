package modelo.datos;

/**
 * --- STAND BY ---
 * Clase que nos permite crear una cuenta Bancaria simple.
 * Posee un identificador conocido como Número de Cuenta y
 * está al pendiente del saldo que sea ingresado.
 * Dependiendo de las implementaciones, se podrá o no conseguir
 * más o menos dinero para la cuenta.
 */
public class CuentaBancaria {
	private final String NUMEROCUENTA; // Puede ser algo como "BE-123", "ST-456"
	private double saldo;
	
	public CuentaBancaria(String NUMEROCUENTA) {
		this.NUMEROCUENTA = NUMEROCUENTA;
		this.saldo = 0.0;
	}

	public String getNumeroCuenta() {
		return NUMEROCUENTA;
	}

	public double getSaldo() {
		return saldo;
	}

	public boolean aumentarSaldo(double monto) {
		if (monto <= 0.0) {
			return false;
		}
		saldo += monto;
		return true;
	}
	
	public boolean disminuirSaldo(double monto) {
		if (monto <= 0.0) {
			return false;
		}
		saldo -= monto;
		return true;
	}
}