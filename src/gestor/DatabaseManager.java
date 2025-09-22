package gestor;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

import modelo.entidades.Vendedor;
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
	 * Método encargado de inicializar la database (crear una conexión segura),
	 * cargando los datos necesarios para el programa.
	 * 
	 * @param databasePath	La ruta donde se ubica la DB.
	 */
	public void inicializar(String databasePath) {
        try {
        	// Establece una conección con la DB (verificando que connection no exista).
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
	
	public void rellenarDatosEdificios() throws SQLException {
		String query = "SELECT * FROM Edificios";
		// Colocamos las nuevas variables para capturarlas en caso de error
		// (Ya que tiran SQLException)
		try (Statement statement = connection.createStatement();
			ResultSet resultados = statement.executeQuery(query);) {
			
			while (resultados.next()) {
				long id = resultados.getLong("id");
				String nombre = resultados.getString("nombre_asociado");
				String direccion = resultados.getString("direccion");
				boolean tienePiscina = resultados.getBoolean("tiene_piscina");
				boolean tieneEstacionamiento = resultados.getBoolean("tiene_estacionamiento");
				long proyectoId = resultados.getLong("proyecto_id");
				
				Edificio edificio = new Edificio(id, proyectoId, nombre, direccion, tienePiscina, tieneEstacionamiento);
				
				ProyectoInmobiliario proyectoAsociado = cacheProyectos.get(proyectoId);
				if (proyectoAsociado != null) {
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
						precioBase);
				
				Edificio edificioAsociado = cacheEdificios.get(edificioId);
				if (edificioAsociado != null) {
					edificioAsociado.agregarDepartamento(departamento);
				}
			}
		}
	}
	
	/**
	 * Método encargado de devolver el mapa de proyectos.
	 * 
	 * @return Una instancia <b>HashMap</b>, con valores de la clase <b>ProyectoInmobiliario</b>.
	 */
	public HashMap<Long, ProyectoInmobiliario> getMapProyectos() {
		return cacheProyectos;
	}
	
	public HashMap<Long, Edificio> getMapEdificios(){
		return cacheEdificios;
	}
}
