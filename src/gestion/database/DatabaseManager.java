package gestion.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import gestion.FiltroBusqueda;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.EstadoDepartamento;
import modelo.ubicacion.ProyectoInmobiliario;

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
    
    private final Map<Long, ProyectoInmobiliario> cacheProyectos = new HashMap<>();
    private final Map<Long, Edificio> cacheEdificios = new HashMap<>();
    private final List<Long> proyectosAEliminar = new ArrayList<>();
    private final List<Long> edificiosAEliminar = new ArrayList<>();
    private final List<Long> departamentosAEliminar = new ArrayList<>();
    private final List<Long> proyectosAModificar = new ArrayList<>();
    private final Set<String> rutsExistentes = new HashSet<>();
    
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
	
	public void rellenarRuts() throws SQLException {
		
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
				
				String rut = resultados.getString("rut_usuario_asociado");
				if (rut != null) rutsExistentes.add(rut);
				
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
		
		if (filtro.getConEstacionamiento()) {
			constructerQuery.append("AND e.tiene_piscina = 1 ");
		}
		
		if (filtro.getConPiscina()) {
			constructerQuery.append("AND e.tiene_estacionamiento = 1 ");
		}
		
		
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
		if (valor == null) return;
		
		query.append(" " + lineaSQL);
		parametros.add(valor);
	}
	
	public Map<Long, Edificio> getMapEdificios(){
		return cacheEdificios;
	}
	
	/**
	 * Agregar proyecto
	 */
	public void agregarNuevoProyecto(ProyectoInmobiliario proyecto) {
		if (proyecto.getId() != null) return;
		
		// Elegimos un ID único para cada nuevo proyecto
		// Negativo para evitar combinar con datos REALES.
		long idTemporal = -System.currentTimeMillis();
		proyecto.setId(idTemporal);
		
		cacheProyectos.put(proyecto.getId(), proyecto);
	}
	
	public void agregarNuevoEdificio(Edificio edificio) {
		if (edificio.getId() != null) return;
		
		long idTemporal = -System.currentTimeMillis();
		edificio.setId(idTemporal);
		cacheEdificios.put(edificio.getId(), edificio);
	}
	
	/**
	 * Marca un proyecto existente para que sea actualizado en la base de datos.
	 * @param idProyecto El ID del proyecto que fue modificado.
	 */
	public void marcarProyectoParaModificar(Long idProyecto) {
	    if (idProyecto > 0 && !proyectosAModificar.contains(idProyecto)) {
	        proyectosAModificar.add(idProyecto);
	    }
	}
	
	
	public void eliminarProyecto(Long idProyecto) {
	    if (idProyecto == null || idProyecto <= 0) return;

	    ProyectoInmobiliario proyecto = cacheProyectos.get(idProyecto);
	    if (proyecto != null) {
	        for (Edificio edificio : proyecto.getEdificios()) {
	            if (edificio.getId() != null) {
	                cacheEdificios.remove(edificio.getId());
	            }
	        }
	    }
	    
	    cacheProyectos.remove(idProyecto);
	    
	    if (!proyectosAEliminar.contains(idProyecto)) {
	        proyectosAEliminar.add(idProyecto);
	    }
	}
	
	private void procesarEliminaciones() throws SQLException {
	    if (!departamentosAEliminar.isEmpty()) {
	        String query = "DELETE FROM Departamentos WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            for (Long id : departamentosAEliminar) {
	                stmt.setLong(1, id);
	                stmt.addBatch();
	            }
	            stmt.executeBatch();
	            departamentosAEliminar.clear();
	        }
	    }

	    if (!edificiosAEliminar.isEmpty()) {
	        String query = "DELETE FROM Edificios WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            for (Long id : edificiosAEliminar) {
	                stmt.setLong(1, id);
	                stmt.addBatch();
	            }
	            stmt.executeBatch();
	            edificiosAEliminar.clear();
	        }
	    }

	    if (!proyectosAEliminar.isEmpty()) {
	        String query = "DELETE FROM Proyectos WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            for (Long id : proyectosAEliminar) {
	                stmt.setLong(1, id);
	                stmt.addBatch();
	            }
	            stmt.executeBatch();
	            proyectosAEliminar.clear();
	        }
	    }
	}
	
	public void modificarProyecto(Long idProyecto, ProyectoInmobiliario proyectoModificado) {
		cacheProyectos.replace(idProyecto, proyectoModificado);
	}
	
	public void modificarEdificio(Long idEdificio, Edificio edificioModificado) {
		cacheEdificios.replace(idEdificio, edificioModificado);
	}

	
	/**
	 * Actualiza la base de datos con los nuevos proyectos, edificios y departamentos
	 * que se han agregado hasta el momento :)
	 */
	public void actualizarDatosDatabase() {
		String proyectosQuery = "INSERT INTO Proyectos(nombre_proyecto, vendedor_asociado, fecha_oferta) VALUES(?, ?, ?)";
		
		try (PreparedStatement statement = connection.prepareStatement(proyectosQuery, Statement.RETURN_GENERATED_KEYS)) {
			connection.setAutoCommit(false);
			
			procesarEliminaciones();
			
			procesarModificaciones();
			
			List<Long> idsTemporales = new ArrayList<>();
			for (Long id : cacheProyectos.keySet()) {
			    if (id < 0) idsTemporales.add(id);
			}
			
			// Si la lista está vacía, significa que no hay datos nuevos que agregar :).
			if (idsTemporales.isEmpty()) return;
			
			for (Long idTemporal : idsTemporales) {
				
				ProyectoInmobiliario proyectoNuevo = cacheProyectos.get(idTemporal);
				
				statement.setString(1, proyectoNuevo.getNombreProyecto());
                statement.setString(2, proyectoNuevo.getVendedor());
                statement.setString(3, proyectoNuevo.getFechaOferta());
                
                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("La inserción falló, no se afectaron filas.");
                }
                
                
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long nuevoIdProyecto = generatedKeys.getLong(1);
                        
                        // Guardamos los edificios asociados a este nuevo proyecto
                        insertarEdificios(proyectoNuevo, nuevoIdProyecto);
                        
                        // Actualizamos la caché con el ID correcto
                        cacheProyectos.remove(idTemporal);
                        proyectoNuevo.setId(nuevoIdProyecto);
                        cacheProyectos.put(nuevoIdProyecto, proyectoNuevo);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de un proyecto agregado.");
                    }
                }
                
				System.out.println("probar : " + proyectoNuevo.getNombreProyecto());
			}
			connection.commit();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} finally {
			try {				
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void procesarModificaciones() throws SQLException {
	    if (proyectosAModificar.isEmpty()) return;

	    for (Long idProyecto : proyectosAModificar) {
	        ProyectoInmobiliario proyecto = cacheProyectos.get(idProyecto);
	        if (proyecto == null) continue;

	        actualizarProyecto(proyecto);

	        for (Edificio edificio : proyecto.getEdificios()) {
	            if (edificio.getId() < 0) {
	                // Es un edificio nuevo, hay que insertarlo junto con sus departamentos
	                insertarEdificioYDepartamentos(edificio, proyecto.getId());
	            } else {
	                // Es un edificio existente, actualizarlo
	                actualizarEdificio(edificio);
	                
	                // Sincronizar sus departamentos
	                for (Departamento depto : edificio.getDepartamentos()) {
	                    if (depto.getId() < 0) {
	                        // Departamento nuevo
	                        insertarDepartamento(depto, edificio.getId());
	                    } else {
	                        // Departamento existente
	                        actualizarDepartamento(depto);
	                    }
	                }
	            }
	        }
	    }
	    // Limpiar la lista una vez procesada
	    proyectosAModificar.clear();
	}

	/**
	 * Inserta los edificios de un proyecto en la base de datos.
	 * 
	 * @param proyecto 		El proyecto padre.
	 * @param idProyecto 	El ID del proyecto ya insertado en la DB.
	 * @throws SQLException
	 */
	private void insertarEdificios(ProyectoInmobiliario proyecto, long idProyecto) throws SQLException {
	    String edificioQuery = "INSERT INTO Edificios(nombre_asociado, direccion, tiene_piscina, tiene_estacionamiento, proyecto_id) VALUES(?, ?, ?, ?, ?)";
	    
	    for (Edificio edificio : proyecto.getEdificios()) {
	        // Asignamos el proyecto padre antes de guardarlo
	        edificio.setProyectoPadre(proyecto);
	        
	        try (PreparedStatement statement = connection.prepareStatement(edificioQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, edificio.getNombre());
	            statement.setString(2, edificio.getInformacion().getDireccion());
	            statement.setBoolean(3, edificio.getInformacion().isTienePiscina());
	            statement.setBoolean(4, edificio.getInformacion().isTieneEstacionamiento());
	            statement.setLong(5, idProyecto);

	            statement.executeUpdate();

	            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    long nuevoIdEdificio = generatedKeys.getLong(1);
	                    
	                    // Guardamos los departamentos de este edificio
	                    insertarDepartamentos(edificio, nuevoIdEdificio);
	                    
	                    // Actualizamos la caché de edificios
	                    edificio.setId(nuevoIdEdificio);
	                    cacheEdificios.put(nuevoIdEdificio, edificio);
	                } else {
	                    throw new SQLException("No se pudo obtener el ID para el edificio: " + edificio.getNombre());
	                }
	            }
	        }
	    }
	}
	
	private void insertarEdificioYDepartamentos(Edificio edificio, long proyectoId) throws SQLException {
	    String edificioQuery = "INSERT INTO Edificios(nombre_asociado, direccion, tiene_piscina, tiene_estacionamiento, proyecto_id) VALUES(?, ?, ?, ?, ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(edificioQuery, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, edificio.getNombre());
	        stmt.setString(2, edificio.getInformacion().getDireccion());
	        stmt.setBoolean(3, edificio.getInformacion().isTienePiscina());
	        stmt.setBoolean(4, edificio.getInformacion().isTieneEstacionamiento());
	        stmt.setLong(5, proyectoId);
	        stmt.executeUpdate();

	        try (ResultSet keys = stmt.getGeneratedKeys()) {
	            if (keys.next()) {
	                long nuevoIdEdificio = keys.getLong(1);
	                // Actualizamos el ID temporal por el real
	                cacheEdificios.remove(edificio.getId());
	                edificio.setId(nuevoIdEdificio);
	                cacheEdificios.put(nuevoIdEdificio, edificio);

	                // Insertar los departamentos asociados
	                for (Departamento depto : edificio.getDepartamentos()) {
	                    insertarDepartamento(depto, nuevoIdEdificio);
	                }
	            }
	        }
	    }
	}

	/**
	 * Inserta los departamentos de un edificio en la base de datos.
	 * 
	 * @param edificio 		El edificio padre.
	 * @param idEdificio 	El ID del edificio ya insertado en la DB.
	 * @throws SQLException
	 */
	private void insertarDepartamentos(Edificio edificio, long idEdificio) throws SQLException {
	    String departamentoQuery = "INSERT INTO Departamentos(codigo, numero_piso, metros_cuadrados, habitaciones, banos, estado, precio_base, precio_actual, edificio_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    for (Departamento depto : edificio.getDepartamentos()) {
	        // Asignamos el edificio padre
	        depto.setEdificioPadre(edificio);

	        try (PreparedStatement statement = connection.prepareStatement(departamentoQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, depto.getCodigo());
	            statement.setInt(2, depto.getNumeroPiso());
	            statement.setDouble(3, depto.getMetrosCuadrados());
	            statement.setInt(4, depto.getHabitaciones());
	            statement.setInt(5, depto.getBanos());
	            statement.setString(6, depto.getEstado().name());
	            statement.setDouble(7, depto.getGestorPrecios().getPrecioBase());
	            statement.setDouble(8, depto.getGestorPrecios().getPrecioActual());
	            statement.setLong(9, idEdificio);

	            statement.executeUpdate();
	            
	            // Opcional: Si también necesitas actualizar el ID del departamento en memoria
	            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
	                if(generatedKeys.next()){
	                    depto.setId(generatedKeys.getLong(1));
	                }
	            }
	        }
	    }
	}
	
	
	
	//Ni idea si funcione pero mucho ojo con esto, #miedo
	public void actualizarProyecto(ProyectoInmobiliario proyecto) throws SQLException {
	    String query = "UPDATE Proyectos SET nombre_proyecto = ?, vendedor_asociado = ?, fecha_oferta = ? WHERE id = ?";
	    PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, proyecto.getNombreProyecto());
        // Fecha: convertir a String (funciona tanto si es String como LocalDate)
        stmt.setString(2, proyecto.getVendedor());
        stmt.setString(3, proyecto.getFechaOferta());
        stmt.setLong(4, proyecto.getId());
        stmt.executeUpdate();
	}
	
	public void actualizarEdificio(Edificio edificio) throws SQLException {
	    String query = "UPDATE Edificios SET nombre_asociado = ?, direccion = ?, tiene_piscina = ?, tiene_estacionamiento = ? WHERE id = ?";
	    PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, edificio.getNombre());
        stmt.setString(2, edificio.getInformacion().getDireccion());
        stmt.setBoolean(3, edificio.getInformacion().isTienePiscina());
        stmt.setBoolean(4, edificio.getInformacion().isTieneEstacionamiento());
        stmt.setLong(5, edificio.getId());
        stmt.executeUpdate();

        cacheEdificios.put(edificio.getId(), edificio);
	}
	
	public void actualizarDepartamento(Departamento depto) throws SQLException {
	    String query = "UPDATE Departamentos SET codigo = ?, numero_piso = ?, metros_cuadrados = ?, habitaciones = ?, banos = ?, estado = ?, precio_base = ?, precio_actual = ? WHERE id = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, depto.getCodigo());
	        stmt.setInt(2, depto.getNumeroPiso());
	        stmt.setDouble(3, depto.getMetrosCuadrados());
	        stmt.setInt(4, depto.getHabitaciones());
	        stmt.setInt(5, depto.getBanos());
	        stmt.setString(6, depto.getEstado().name());
	        stmt.setDouble(7, depto.getGestorPrecios().getPrecioBase());
	        stmt.setDouble(8, depto.getGestorPrecios().getPrecioActual());
	        stmt.setLong(9, depto.getId());
	        stmt.executeUpdate();
	    }
	}
	
	
	public void insertarDepartamento(Departamento depto, long idEdificio) throws SQLException {
	    String departamentoQuery = "INSERT INTO Departamentos(codigo, numero_piso, metros_cuadrados, habitaciones, banos, estado, precio_base, precio_actual, edificio_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(departamentoQuery, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString	(1, depto.getCodigo());
	        stmt.setInt		(2, depto.getNumeroPiso());
	        stmt.setDouble	(3, depto.getMetrosCuadrados());
	        stmt.setInt		(4, depto.getHabitaciones());
	        stmt.setInt		(5, depto.getBanos());
	        stmt.setString	(6, depto.getEstado().name());
	        stmt.setDouble	(7, depto.getGestorPrecios().getPrecioBase());
	        stmt.setDouble	(8, depto.getGestorPrecios().getPrecioActual());
	        stmt.setLong	(9, idEdificio);
	        
	        stmt.executeUpdate();
	        
	        try (ResultSet keys = stmt.getGeneratedKeys()) {
	            if (keys.next()) depto.setId(keys.getLong(1));
	        }
	    }
	}

	/**
     * Devuelve la instancia de la conexión actual a la base de datos.
     * @return El objeto Connection.
     */
    public Connection getConnection() {
        return this.connection;
    }
    
    /**
     * Marca un edificio para ser eliminado de la base de datos al guardar.
     * @param edificioId El ID del edificio a eliminar.
     */
    public void marcarEdificioParaEliminar(Long edificioId) {
        if (edificioId > 0 && !edificiosAEliminar.contains(edificioId)) {
            edificiosAEliminar.add(edificioId);
        }
    }

    /**
     * Marca un departamento para ser eliminado de la base de datos al guardar.
     * @param departamentoId El ID del departamento a eliminar.
     */
    public void marcarDepartamentoParaEliminar(Long departamentoId) {
        if (departamentoId > 0 && !departamentosAEliminar.contains(departamentoId)) {
            departamentosAEliminar.add(departamentoId);
        }
    }

	public boolean verificarRut(String rut) {
		if (rutsExistentes.contains(rut)) return true;
		return false;
	}
}

