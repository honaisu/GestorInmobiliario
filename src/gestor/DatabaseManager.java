package gestor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import excepciones.FiltroNoValidoException;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.EstadoDepartamento;

/**
 * Clase encargada del manejo de todo lo referente a la database implementada con SQLite.
 * Usa toda la lógica referente a SQL para poder manejar los datos que posee la database.
 * <p>
 * Requiere de la librería externa SQLite-JDBC (agregada en el proyecto).
 * 
 * @param database			Instancia de la database que almacena toda la información.
 * @param cacheProyectos	Mapa Hash encargado de almacenar los proyectos.
 * @param cacheEdificios	Mapa Hash encargado de almacenar los edificios.
 * @param connection		Instancia de la clase <b>Connection</b> encargada de la DB.
 */
public class DatabaseManager {
    private static final DatabaseManager database = new DatabaseManager();
    
    private final HashMap<Long, ProyectoInmobiliario> cacheProyectos = new HashMap<>();
    private final HashMap<Long, Edificio> cacheEdificios = new HashMap<>();
    
    private Connection connection;

    /**
     * Constructor vacío que verifica si el driver de SQLite está o no.
     */
	private DatabaseManager() {
		try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de SQLite: " + e.getMessage());
        }
	}
	
	/**
	 * Método encargado de devolver una instancia de DatabaseManager (esta clase).
	 * 
	 * @return Una instancia de <b>DatabaseManager</b>.
	 */
	public static DatabaseManager getDatabase() {
		return database;
	}
	
	/**
	 * Método encargado de devolver el mapa de proyectos almacenados en caché.
	 * 
	 * @return Una instancia <b>Map</b> con valores de la clase <b>ProyectoInmobiliario</b>.
	 */
	public Map<Long, ProyectoInmobiliario> getMapProyectos() {
		return cacheProyectos;
	}
	
	/**
	 * Método encargado de inicializar la database (crear una conexión segura),
	 * cargando los datos necesarios para el programa.
	 * 
	 * @param databasePath	La ruta donde se ubica la DB.
	 */
	public void inicializar(String databasePath) {
        try {
        	// Establece una conexión con la DB (verificando que Connection exista o no esté cerrada).
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:sqlite:" + databasePath;
                this.connection = DriverManager.getConnection(url);
                cargarDatos();
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
	
	/**
	 * Método que carga los datos del proyecto.
	 * <p>
	 * Esto incluye a:
	 * <ul> 
	 * 	<li> Datos Proyecto.
	 * 	<li> Datos Edificios.
	 * 	<li> Datos Departamentos.
	 * </ul>
	 * 
	 * @throws SQLException
	 */
	public void cargarDatos() throws SQLException {
		cacheProyectos.clear();
		
		rellenarDatosProyecto();
		rellenarDatosEdificios();
		rellenarDatosDepartamentos();
	}
	
	/**
	 * Método encargado de rellenar la variable <b>cacheProyectos</b>.
	 * <p>
	 * Guarda los datos ubicados dentro de la DB y los almacena en el mapa.
	 * 
	 * @throws SQLException
	 */
	public void rellenarDatosProyecto() throws SQLException {
		String query = "SELECT * FROM Proyectos";

		// Colocamos las nuevas variables para capturarlas en caso de error
		// (Ya que tiran SQLException)
		try (Statement statement = connection.createStatement();
			ResultSet resultados = statement.executeQuery(query);) {
			
			while (resultados.next()) {
				long id = resultados.getLong("id");
				String nombre = resultados.getString("nombre_proyecto");
				String vendedor = resultados.getString("vendedor_asociado");
				LocalDate fechaOferta = LocalDate.parse(resultados.getString("fecha_oferta"));
				
				ProyectoInmobiliario nuevoProyecto = new ProyectoInmobiliario(id, nombre, vendedor, fechaOferta);
				cacheProyectos.put(id, nuevoProyecto);
			}
		}
	}
	
	/**
	 * Método encargado de rellenar los datos de los edificios,
	 * almacenándolos en <b>cacheEdificios</b>.
	 * 
	 * @throws SQLException
	 */
	public void rellenarDatosEdificios() throws SQLException {
		String query = "SELECT * FROM Edificios";
		
		// Mismo caso para las dos variables, ya que también tiran SQLException.
		try (Statement statement = connection.createStatement();
			ResultSet resultados = statement.executeQuery(query);) {
			
			while (resultados.next()) {
				long id = resultados.getLong("id");
				String nombre = resultados.getString("nombre_asociado");
				String direccion = resultados.getString("direccion");
				boolean tienePiscina = resultados.getBoolean("tiene_piscina");
				boolean tieneEstacionamiento = resultados.getBoolean("tiene_estacionamiento");
				long proyectoId = resultados.getLong("proyecto_id");
				
				Edificio edificio = new Edificio(id, nombre, direccion, tienePiscina, tieneEstacionamiento);
				
				ProyectoInmobiliario proyectoAsociado = cacheProyectos.get(proyectoId);
				if (proyectoAsociado != null) {
					edificio.setProyectoPadre(proyectoAsociado);
					proyectoAsociado.addEdificio(edificio);
					cacheEdificios.put(id, edificio);
				}
			}
		}
	}
	
	/**
	 * Método encargado de rellenar todos los datos de los departamentos.
	 * 
	 * @throws SQLException
	 */
	public void rellenarDatosDepartamentos() throws SQLException {
		String query = "SELECT * FROM Departamentos";
		
		try (Statement statement = connection.createStatement();
			ResultSet resultados = statement.executeQuery(query);) {
			
			while (resultados.next()) {
				long id = resultados.getLong("id");
				String codigo = resultados.getString("codigo");
				int numeroPiso = resultados.getInt("numero_piso");
				double metrosCuadrados = resultados.getDouble("metros_cuadrados");
				int habitaciones = resultados.getInt("habitaciones");
				int banos = resultados.getInt("banos");
				EstadoDepartamento estado = EstadoDepartamento.valueOf(resultados.getString("estado"));
				double precioBase = resultados.getDouble("precio_base");
				double precioActual = resultados.getDouble("precio_actual");
				// para su papá
				long edificioId = resultados.getLong("edificio_id");
				
				// Lo siento pero tiene muchos datos :c
				Departamento departamento = new Departamento(
						id, 
						codigo, 
						numeroPiso, 
						metrosCuadrados, 
						habitaciones, 
						banos, 
						estado, 
						precioBase,
						precioActual);
				
				Edificio edificioAsociado = cacheEdificios.get(edificioId);
				if (edificioAsociado != null) {
					departamento.setEdificioPadre(edificioAsociado);
					edificioAsociado.agregarDepartamento(departamento);
				}
			}
		}
	}
	
	/**
	 * Método encargado de poder buscar datos de manera dinámica en la DB.
	 * <p>
	 * Utiliza un <b>StringBuilder</b> para construir dinámicamente las <i>queries</i>
	 * que se quieran utilizar (los filtros que se quiere). :)
	 * 
	 * @param filtro 	Instancia de Filtros de Búsqueda (clase).
	 * @return Una lista de Departamentos con los filtros escogidos.
	 */
	public List<Departamento> getDepartamentosPorFiltro(FiltroBusqueda filtro) {
		ArrayList<Departamento> departamentos = new ArrayList<>();
		// El StringBuilder está especializado en appends.
		StringBuilder constructerQuery = new StringBuilder();
		constructerQuery.append("SELECT d.*, e.id AS edificio_id FROM Departamentos d ");
		constructerQuery.append("JOIN Edificios e ON d.edificio_id = e.id ");
		constructerQuery.append("WHERE 1=1 ");
		
		ArrayList<Object> parametros = new ArrayList<>();
		
				
		// Agrega los filtros de la clase de búsqueda, con un comando SQL propio para cada parámetro
		agregarFiltroValido(filtro.getPrecioMin(),
							"AND d.precio_actual >= ?", 
							constructerQuery, parametros);
		
		agregarFiltroValido(filtro.getPrecioMax(),
							"AND d.precio_actual <= ?", 
							constructerQuery, parametros);
		
		agregarFiltroValido(filtro.getHabitacionesMin(),
							"AND d.habitaciones >= ?",
							constructerQuery, parametros);
		
		agregarFiltroValido(filtro.getBanosMin(), 
							"AND d.banos >= ?",
							constructerQuery, parametros);
		
		agregarFiltroValido(filtro.getConEstacionamiento(), 
							"AND e.tiene_piscina = 1", 
							constructerQuery, parametros);
		
		agregarFiltroValido(filtro.getConPiscina(), 
							"AND e.tiene_estacionamiento = 1", 
							constructerQuery, parametros);
		
		if (filtro.getEstado() != null) {		
			agregarFiltroValido(filtro.getEstado().name(),	
								"AND d.estado = ?", 
								constructerQuery, parametros);
		}
		if (filtro.getDireccion() != null) {			
			agregarFiltroValido("%" + filtro.getDireccion() + "%", 	
								"AND e.direccion LIKE ?", 
								constructerQuery, parametros);
		}
		
		try (PreparedStatement statement = connection.prepareStatement(constructerQuery.toString())) {
			/*
			 *  A diferencia de los Statement "normales", el Prepared necesita ser rellenado
			 *  en cada valor '?' que encuentre, reemplazando con sea parámetro que sea.
			 *  En este caso, se rellena con los valores que encuentre de los filtros (que no sean nulo). :D
			 */
			for (int i = 0; i < parametros.size(); i++) {
	            statement.setObject(i + 1, parametros.get(i));
	        }
			
			ResultSet resultados = statement.executeQuery();
			
			while (resultados.next()) {
				long departamentoId = resultados.getLong("id");
				long edificioId = resultados.getLong("edificio_id");
				Edificio edificioObtenido = cacheEdificios.get(edificioId);
				if (edificioObtenido == null) continue;
				
				Departamento departamentoEncontrado = edificioObtenido.getDepartamento(departamentoId);
				
				departamentos.add(departamentoEncontrado);
			}
		} catch (SQLException e) {
			System.out.println("Error al crear la conexión: " + e.getMessage());
		}
		
		return departamentos;
	}
	
	/**
	 * Método que verifica la validez de los filtros SQL a agregar.
	 * 
	 * @param valor			
	 * @param lineaSQL		
	 * @param query			
	 * @param parametros	
	 * 
	 * @throws FiltroNoValidoException 
	 */
	public void agregarFiltroValido(Object valor, String lineaSQL, 
			StringBuilder query, List<Object> parametros) {
		if (valor == null || //Verifica null
			(valor instanceof String && ((String) valor).isEmpty()) || // Verifica si es String y está vacío
			(valor instanceof Boolean && !((Boolean) valor).booleanValue()) // Verifica si es Boolean y es falso
			) return;
		
		query.append(" " + lineaSQL);
		parametros.add(valor);
	}
	
	public void probar(FiltroBusqueda bq) {
		//FiltroBusqueda bq = new FiltroBusqueda();
		//bq.setEstado(EstadoDepartamento.DISPONIBLE);
		List<Departamento> deps = getDepartamentosPorFiltro(bq);
		for (Departamento d : deps) {
			System.out.println("ID: " + d.getId());
			System.out.println("CODIGO: " + d.getCodigo());
			System.out.println("PRECIO: $" + d.getGestorPrecios().getPrecioActual());
			System.out.println("ESTADO: " + d.getEstado().name());
			System.out.println("DIRECCION: " + d.getEdificioPadre().getInformacion().getDireccion());
		}
	}
	
	public HashMap<Long, Edificio> getMapEdificios(){
		return cacheEdificios;
	}
}
