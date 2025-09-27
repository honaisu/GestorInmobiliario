package gestion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import modelo.entidades.Comprador;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.ProyectoInmobiliario;


/**
 * Clase encargada de poder exportar los datos de un comprador asociado (y un departamento)
 * en formato .txt; usado para poder generar recibos de compra al reservar / comprar.
 */
public class TextFileExporter {
	private TextFileExporter() { }
	
	/**
     * Exporta los datos de una compra a un archivo de texto único.
     *
     * @param comprador    El usuario que realizó la compra.
     * @param departamento El departamento que fue adquirido.
     */
    public static void exportarReciboCompra(Comprador comprador, Departamento departamento) throws IOException {
        Edificio edificio = departamento.getEdificioPadre();
        ProyectoInmobiliario proyecto = edificio.getProyectoPadre();

        // Formateador para la fecha y hora, asegurando un nombre de archivo único.
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(fileFormatter);
        String fileName = String.format("./data/recibos/recibo_%s_%s.txt", comprador.getRut(), timestamp);

        // Usamos StringBuilder para construir el contenido del recibo de forma eficiente.
        StringBuilder contenido = new StringBuilder();
        
        contenido.append("========================================\n");
        contenido.append("      RECIBO DE COMPRA INMOBILIARIA     \n");
        contenido.append("========================================\n\n");
        
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        contenido.append("Fecha de Compra: ").append(LocalDateTime.now().format(displayFormatter)).append("\n\n");

        contenido.append("--- Datos del Comprador ---\n");
        contenido.append("Nombre: ").append(comprador.getNombre()).append("\n");
        contenido.append("RUT: ").append(comprador.getRut()).append("\n");
        contenido.append("Email: ").append(comprador.getEmail()).append("\n");
        contenido.append("Teléfono: ").append(comprador.getTelefono()).append("\n\n");

        contenido.append("--- Detalles de la Propiedad ---\n");
        contenido.append("Proyecto: ").append(proyecto.getNombreProyecto()).append("\n");
        contenido.append("Edificio: ").append(edificio.getNombre()).append("\n");
        contenido.append("  - Dirección: ").append(edificio.getInformacion().getDireccion()).append("\n");
        contenido.append("  - Piscina: ").append(edificio.getInformacion().isTienePiscina() ? "Sí" : "No").append("\n");
        contenido.append("  - Estacionamiento: ").append(edificio.getInformacion().isTieneEstacionamiento() ? "Sí" : "No").append("\n\n");

        contenido.append("--- Detalles del Departamento ---\n");
        contenido.append("Código: ").append(departamento.getCodigo()).append("\n");
        contenido.append("Piso: ").append(departamento.getNumeroPiso()).append("\n");
        contenido.append("Habitaciones: ").append(departamento.getHabitaciones()).append("\n");
        contenido.append("Baños: ").append(departamento.getBanos()).append("\n\n");

        contenido.append("----------------------------------------\n");
        contenido.append(String.format("PRECIO FINAL PAGADO: $%.2f\n", departamento.getGestorPrecios().getPrecioActual()));
        contenido.append("----------------------------------------\n\n");

        contenido.append("Gracias por su compra.\n");
        contenido.append("© Los Bien Corporation\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(contenido.toString());
        } catch (IOException e) {
            throw new IOException("Error al escribir el recibo en el archivo: " + e.getMessage());
        }
    }
}
