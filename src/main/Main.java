package main;

import gestion.GestorInmobiliarioService;
import gestion.database.DatabaseManager;
import servicios.displayer.VisualDisplayer;

/**
 * @author 🄯 Los Bien Corporation
 */
public class Main {
	public static void main(String[] args) {
		DatabaseManager database = DatabaseManager.getDatabase();
		database.inicializar("data/DatabaseProyectos");
		
		GestorInmobiliarioService gestor = new GestorInmobiliarioService();
		
		new VisualDisplayer(gestor).initialize();
	}
}
