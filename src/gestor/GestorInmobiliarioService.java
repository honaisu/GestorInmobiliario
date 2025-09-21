package gestor;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import modelo.entidades.Vendedor;
import modelo.ubicacion.Edificio;

public class GestorInmobiliarioService {
    private final DatabaseManager databaseManager;
	
	public GestorInmobiliarioService() {
		this.databaseManager = DatabaseManager.getDatabase();
	}

	public Collection<ProyectoInmobiliario> getAllProyectos() {
        return databaseManager.getMapProyectos().values();
    }
}
