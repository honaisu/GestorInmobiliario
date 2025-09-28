package main;

import gestion.GestorInmobiliarioService;
import gestion.database.DatabaseManager;
import servicios.displayer.ControladorPrincipal;
import servicios.displayer.VisualDisplayerV2;

/**
 * @author 🄯 Los Bien Corporation
 */
public class MainConV2 {
	public static ControladorPrincipal inicializarDatos() {
		DatabaseManager database = DatabaseManager.getDatabase();
		database.inicializar("data/database/DatabaseProyectos");
		
		GestorInmobiliarioService gestor = new GestorInmobiliarioService();
		VisualDisplayerV2 ventanaPrograma = new VisualDisplayerV2();
		ControladorPrincipal controlador = new ControladorPrincipal(gestor, ventanaPrograma);
		
		ventanaPrograma.setControlador(controlador);
		return controlador;
	}
	
	public static void main(String[] args) {
		ControladorPrincipal controladorPrincipal = inicializarDatos();
		controladorPrincipal.iniciarPrograma();
	}
}
