package gestion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 * Clase encargada de poder exportar una conexión SQL a un formato específico (en este caso, .csv)
 */
public class SQLExporter {
	// No se inicializa, sólo se encarga de exportar :)
	private SQLExporter() { }
	
    /**
     * Exporta los datos de una tabla específica a un archivo de texto (EJ: ".csv").
     *
     * @param connection La conexión a la base de datos.
     * @param tableName  El nombre de la tabla que se desea exportar.
     * @param filePath   La ruta del archivo ("exportacion.txt").
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