package main;

import gestor.DatabaseManager;
import gestor.GestorInmobiliarioService;

/*
 * Cada paquete de abajo se puede reemplazar a futuro.
 * Pues solo son utilizados para el testing (usuario, proyecto) de la app. :D
 */

import servicios.displayer.VisualDisplayer;

/**
 * @author honai
 */
public class Main {
	public static void main(String[] args) {
		DatabaseManager database = DatabaseManager.getDatabase();
		database.inicializar("data/DatabaseProyectos");
		
		GestorInmobiliarioService gestor = new GestorInmobiliarioService();
		
		new VisualDisplayer(gestor).initialize();
	}
}
