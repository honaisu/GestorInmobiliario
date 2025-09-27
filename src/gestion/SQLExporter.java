package gestion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase utilitaria para exportar los datos de tablas de una base de datos
 * a archivos de texto (por ejemplo, CSV).
 * <p>
 * Esta clase no se instancia; todos sus métodos son estáticos.
 * </p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * Connection conn = DriverManager.getConnection(url, user, pass);
 * SQLExporter.exportarComoCsv(conn, "Departamentos", "departamentos.csv");
 * </pre>
 * 
 * @author 🄯 Los Bien Corporation
 */
public class SQLExporter {
	/** Constructor privado para evitar instanciación. */
	private SQLExporter() { 
		
	}
	
	/**
     * Exporta los datos de una tabla específica a un archivo CSV.
     * <p>
     * El archivo generado tendrá la primera fila con los nombres de las columnas,
     * y las filas siguientes con los valores de cada registro. Los valores nulos
     * se representan como "NULL".
     * </p>
     * 
     * @param connection La conexión activa a la base de datos.
     * @param tableName  Nombre de la tabla que se desea exportar.
     * @param filePath   Ruta completa del archivo de salida (por ejemplo, "exportacion.csv").
     * @throws SQLException si ocurre un error al consultar la base de datos.
     * @throws IOException  si ocurre un error al escribir el archivo de salida.
     */
    public static void exportarComoCsv(Connection connection, String tableName, String filePath) throws SQLException, IOException {
        // Usamos una consulta simple para obtener todos los datos de la tabla. :)
        String query = "SELECT * FROM " + tableName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
        	
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                writer.write(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                	String value = "NULL";
                	if (resultSet.getString(i) != null) value = resultSet.getString(i);
                    
                	writer.write(value);
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine(); 
            }
        }
    }
}