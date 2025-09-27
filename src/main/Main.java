package main;

import gestion.GestorInmobiliarioService;
import gestion.database.DatabaseManager;
import servicios.displayer.VisualDisplayer;
import servicios.displayer.VisualDisplayerV2;

/*
 * Cada paquete de abajo se puede reemplazar a futuro.
 * Pues solo son utilizados para el testing (usuario, proyecto) de la app. :D
 */

/**
 * @author honai
 */
public class Main {
	public static void main(String[] args) {
		DatabaseManager database = DatabaseManager.getDatabase();
		database.inicializar("data/DatabaseProyectos");
		
		GestorInmobiliarioService gestor = new GestorInmobiliarioService();
		
		
		//new VisualDisplayerV2(gestor).initialize();
		new VisualDisplayer(gestor).initialize();
		//database.probar();
		//database.actualizarDatosDatabase();
	}
}
