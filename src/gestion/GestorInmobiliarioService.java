package gestion;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gestion.database.DatabaseManager;
import modelo.entidades.Comprador;
import modelo.entidades.Usuario;
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
	
	public Comprador getCompradorPorRut(String rutComprador) {
		Comprador usuario = (Comprador) databaseManager.buscarUsuarioPorRut(rutComprador);
		return usuario;
	}
	
	public void guardarUsuarioEnDatabase(Usuario usuario) throws SQLException {
		try {	
			databaseManager.guardarUsuario(usuario);
		} catch (SQLException e) {
			throw e;
		}
	}
}
