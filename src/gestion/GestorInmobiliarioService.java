package gestion;

import java.util.Collection;
import java.util.Map;

import gestion.database.DatabaseManager;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.ProyectoInmobiliario;

public class GestorInmobiliarioService {
    private final DatabaseManager databaseManager;
	
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
}
