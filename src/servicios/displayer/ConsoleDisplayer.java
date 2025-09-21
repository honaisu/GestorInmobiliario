package servicios.displayer;

import modelo.entidades.Vendedor;

public final class ConsoleDisplayer {
	public static void limpiarTerminal() {
		// LIMPIA LA PANTALLA (no funciona bien con la terminal de Eclipse)
		System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
	}
	
	/**
	 *  TODO Implementar correctamente los menús :)
	 *  TODO Sistema de usuarios y proyectos asociados.
	 *  TODO Vista de proyectos sin necesidad de cuenta :D
	 */
	public static void menuPrincipal() {
		System.out.println("== GESTOR INMOBILIARIO (Los Bien Corp.) ==");
		System.out.println("(1.) Ingresar Usuario");
		System.out.println("(2.) Crear Usuario");
		System.out.println("(0.) Salir del Programa");
		System.out.println("==========================================");
		System.out.println("🄯 Los Bien Corporation. All lefts reserved");
	}
	
	// Puede ser para el menú de vendedores (manejar sus proyectos)
	public static void menuVendedorProyectos(Vendedor vendedor) {
		System.out.println("== SISTEMA DE PROYECTOS (Preliminar) ==");
		if (vendedor != null) System.out.println("Usuario Actual: " + vendedor.getNombre());
		System.out.println("(1.) Crear nuevo proyecto");
		System.out.println("(2.) Mostrar proyecto(s)");
		System.out.println("(0.) Salir del Menú");
		System.out.println("==========================================");
		System.out.println("🄯 Los Bien Corporation. All lefts reserved");
		System.out.print("Ingrese su opción: ");
	}
}
