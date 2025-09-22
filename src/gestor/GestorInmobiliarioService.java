package gestor;


import java.util.Collection;

public class GestorInmobiliarioService {
    private final DatabaseManager databaseManager;
	
	public GestorInmobiliarioService() {
		this.databaseManager = DatabaseManager.getDatabase();
	}

	public Collection<ProyectoInmobiliario> getAllProyectos() {
        return databaseManager.getMapProyectos().values();
    }
}
