package gestor;


import java.util.Collection;
import java.util.Map;

import modelo.ubicacion.Edificio;

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
	
	/*TODO terminar esta implementación
	public Edificio getEdificioPorId(long idEdificio) {
		Map<Long, Edificio> mapEdificios = databaseManager.getMapEdificios();
	}*/
}
