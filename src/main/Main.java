package main;

import gestion.GestorInmobiliarioService;
import gestion.database.DatabaseManager;
import servicios.displayer.ControladorPrincipal;
import servicios.displayer.VisualDisplayer;

/**
 * @author 🄯 Los Bien Corporation
 */
public class Main {
	public static ControladorPrincipal inicializarDatos() {
		DatabaseManager database = DatabaseManager.getDatabase();
		database.inicializar("data/database/DatabaseProyectos");
		
		GestorInmobiliarioService gestor = new GestorInmobiliarioService();
		VisualDisplayer ventanaPrograma = new VisualDisplayer();
		ControladorPrincipal controlador = new ControladorPrincipal(gestor, ventanaPrograma);
		
		ventanaPrograma.setControlador(controlador);
		return controlador;
	}
	
	public static void main(String[] args) {
		ControladorPrincipal controladorPrincipal = inicializarDatos();
		controladorPrincipal.iniciarPrograma();
	}
}
