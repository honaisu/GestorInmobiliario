package gestion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gestion.database.DatabaseManager;
import modelo.entidades.Comprador;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.ProyectoInmobiliario;

public class GestorInmobiliarioService {
    private final DatabaseManager databaseManager;
    private final Map<String, Comprador> compradores = new HashMap<>();
	
	public GestorInmobiliarioService() {
		this.databaseManager = DatabaseManager.getDatabase();
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public Collection<ProyectoInmobiliario> getAllProyectos() {
        return databaseManager.getMapProyectos().values();
    }
	
	public Collection<Edificio> getAllEdificios(){
		return databaseManager.getMapEdificios().values();
	}
	
	public Map<Long, Edificio> getMapEdificios() {
		return databaseManager.getMapEdificios();
	}
	
	public Comprador getCompradorPorRut(String rutComprador) {
		if (databaseManager.verificarRut(rutComprador)) return compradores.get(rutComprador);
		return null;
	}
	
	public void insertComprador(Comprador nuevoComprador) {
		compradores.put(nuevoComprador.getRut(), nuevoComprador);
	}
}
