package gestion;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import gestion.database.DatabaseManager;
import modelo.entidades.Comprador;
import modelo.entidades.Usuario;
import modelo.ubicacion.Departamento;
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

	public Collection<ProyectoInmobiliario> obtenerTodosLosProyectos() {
		return databaseManager.getMapProyectos().values();
    }

	public Collection<Edificio> getAllEdificios(){
		return databaseManager.getMapEdificios().values();
	}
	
	public Map<Long, Edificio> getMapEdificios() {
		return databaseManager.getMapEdificios();
	}
	
	public int getDepartamentosTotales() {
		return databaseManager.getTotalDepartamentos();
	}
	
	public int getDepartamentosVendidosTotales() {
		return databaseManager.getTotalDepartamentosVendidos();
	}
	
	public Comprador getCompradorPorRut(String rutComprador) {
		Comprador usuario = (Comprador) databaseManager.buscarUsuarioPorRut(rutComprador);
		return usuario;
	}
	
	public void guardarUsuarioEnDatabase(Usuario usuario) throws SQLException {
		databaseManager.guardarUsuario(usuario);
	}
	
	public void guardarCambiosDelPrograma() throws SQLException {
		databaseManager.actualizarDatosDatabase();
	}
	
	public void marcarProyectoParaModificar(Long idProyecto) {
		databaseManager.marcarProyectoParaModificar(idProyecto);
	}
	
	public void aumentarContadorVendidos() {
		databaseManager.aumentarContadorVendidos();
	}

	public void marcarEdificioParaEliminar(Long edificioId) {
		// TODO Auto-generated method stub
		databaseManager.marcarEdificioParaEliminar(edificioId);
	}

	public void marcarDepartamentoParaEliminar(Long departamentoId) {
		// TODO Auto-generated method stub
		databaseManager.marcarDepartamentoParaEliminar(departamentoId);
	}

	public void agregarProyectoADatabase(ProyectoInmobiliario nuevoProyecto) {
		databaseManager.agregarNuevoProyecto(nuevoProyecto);
	}

	public Edificio getEdificioPorId(long idEdificio) {
		return databaseManager.getMapEdificios().get(idEdificio);
	}

	public ProyectoInmobiliario getProyectoPorId(long idProyecto) {
		return databaseManager.getMapProyectos().get(idProyecto);
	}

	public List<Departamento> buscarDepartamentosPorFiltro(FiltroBusqueda filtro) {
		return databaseManager.getDepartamentosPorFiltro(filtro);
	}
}
