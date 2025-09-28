package gestion;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import gestion.database.DatabaseManager;
import modelo.entidades.Comprador;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.ProyectoInmobiliario;

/**
 * Servicio que facilita la gestión de datos inmobiliarios y la interacción
 * con la base de datos a través de {@link DatabaseManager}.
 * <p>
 * Permite acceder a proyectos, edificios y compradores, así como
 * guardar información de usuarios en la base de datos.
 * </p>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class GestorInmobiliarioService {
	
	/** Instancia del gestor de la base de datos. */
    private final DatabaseManager databaseManager;
	
    /**
     * Constructor que inicializa el servicio y obtiene la instancia
     * de {@link DatabaseManager}.
     */
	public GestorInmobiliarioService() {
		this.databaseManager = DatabaseManager.getDatabase();
	}
	
	/**
     * Obtiene el gestor de la base de datos utilizado por el servicio.
     * 
     * @return la instancia de {@link DatabaseManager}
     */
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	
	public Collection<ProyectoInmobiliario> obtenerTodosLosProyectos() {
		return databaseManager.getMapProyectos().values();
    }
	
	/**
     * Obtiene todos los proyectos inmobiliarios almacenados en la base de datos.
     * 
     * @return colección de {@link ProyectoInmobiliario}
     */
	public Collection<ProyectoInmobiliario> getAllProyectos() {
		return databaseManager.getMapProyectos().values();
    }
	
	/**
     * Obtiene todos los edificios almacenados en la base de datos.
     * 
     * @return colección de {@link Edificio}
     */
	public Collection<Edificio> getAllEdificios(){
		return databaseManager.getMapEdificios().values();
	}
	
	/**
     * Obtiene el mapa completo de edificios, con sus IDs como claves.
     * 
     * @return mapa de edificios ({@code Map<Long, Edificio>})
     */
	public Map<Long, Edificio> getMapEdificios() {
		return databaseManager.getMapEdificios();
	}
	
	public int getDepartamentosTotales() {
		return databaseManager.getTotalDepartamentos();
	}
	
	public int getDepartamentosVendidosTotales() {
		return databaseManager.getTotalDepartamentosVendidos();
	}
	
	/**
     * Busca un comprador por su RUT.
     * 
     * @param rutComprador RUT del comprador a buscar
     * @return instancia de {@link Comprador} si existe, {@code null} en caso contrario
     */
	public Comprador getCompradorPorRut(String rutComprador) {
		Comprador usuario = (Comprador) databaseManager.buscarUsuarioPorRut(rutComprador);
		return usuario;
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
	
	/**
     * Guarda un comprador en la base de datos.
     * 
     * @param comprador comprador a guardar
     * @throws SQLException si ocurre un error durante la operación de guardado
     */
	public void guardarUsuarioEnDatabase(Comprador comprador) throws SQLException {
		databaseManager.guardarUsuario(comprador);
	}
}
