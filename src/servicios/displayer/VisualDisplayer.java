package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gestor.GestorInmobiliarioService;
import gestor.ProyectoInmobiliario;

/**
 * Clase encargada de mostrar todos los componentes visuales referentes
 * a la interfaz gráfica de nuestro programa (implementada con Java Swing).
 */
public class VisualDisplayer {
	private static JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
	
	private DefaultTableModel defaultTable;
	private JTable tabla;
	
	private JButton verBoton;
	private JButton comprarBoton;
	private JButton eliminarBoton;
	
    private final GestorInmobiliarioService gestorService; 
    
    public VisualDisplayer(GestorInmobiliarioService service) {
		this.gestorService = service;
	}
	
	/**
	 * Método que hace que una instancia de la clase inicialicé la interfaz gráfica.
	 * <p>
	 * Creará una instancia de <b>JFrame</b> propia con todas las componentes clave
	 * que posee el gestor.
	 */
	public void initialize() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel headerPanel = crearHeaderPanel();
		JPanel opcionesPanel = crearOpcionesPanel();
		JPanel proyectorPanel = crearProyectorPanel();
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		
		mainFrame.add(mainPanel);
		
		cargarProyectosEnTabla();
		
		// Size automático con pack, y visibilidad a verdadero. :)
		mainFrame.pack();
		// Para colocar la ventana en "medio"
		mainFrame.setLocationRelativeTo(null);
		// Visibilidad
		mainFrame.setVisible(true);
	}
	
	public void cargarProyectosEnTabla() {
        // Limpiamos la tabla por si tiene datos viejos
        defaultTable.setRowCount(0);

        // Pedimos los proyectos al controlador (no sabemos de dónde los saca, y no nos importa)
        Collection<ProyectoInmobiliario> proyectos = gestorService.getAllProyectos();

        // Iteramos y añadimos cada proyecto a la tabla
        for (ProyectoInmobiliario proyecto : proyectos) {
            Object[] fila = {
                proyecto.getId(),
                proyecto.getNombreProyecto(),
                proyecto.getVendedor(),
                proyecto.getFechaOferta()
            };
            defaultTable.addRow(fila);
        }
    }
	
	private JPanel crearHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 100));
		
		JLabel titulo = new JLabel("GESTOR INMOBILIARIO", JLabel.CENTER);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");

		panel.add(titulo, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel crearOpcionesPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 20));
		panel.setPreferredSize(new Dimension(200, 250));
		
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		for (OpcionesProyecto o : OpcionesProyecto.values()) {
			JButton opcionBoton = new JButton();
			opcionBoton.setText(o.getNombre());
			
			if (OpcionesProyecto.COMPRAR.equals(o)) {
				this.comprarBoton = opcionBoton;
				this.comprarBoton.setEnabled(false);
			}
			if (OpcionesProyecto.VER.equals(o)) {
				this.verBoton = opcionBoton;
				this.verBoton.setEnabled(false);
			}
			if (OpcionesProyecto.ELIMINAR.equals(o)) {
				this.eliminarBoton = opcionBoton;
				this.eliminarBoton.setEnabled(false);
			}
			
			opcionBoton.addActionListener(lambda -> {
				accionOpcionesProyecto(o);
			});
			
			panel.add(opcionBoton);
		}
		
		return panel;
	}
	
	private JPanel crearProyectorPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(500, 200));
		
		// Tabla con datos.
		String[] columnas = {"ID", "Nombre Proyecto", "Vendedor", "Fecha Ingreso"};
		this.defaultTable = new DefaultTableModel(columnas, 0);
		this.tabla = new JTable(defaultTable);
		
		// Para añadir funcionalidad al elegir una fila
		tabla.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
	            boolean filaSeleccionada = false;
				if (tabla.getSelectedRow() != -1) {
					filaSeleccionada = true;
				}
				
	            verBoton.setEnabled(filaSeleccionada);
	            comprarBoton.setEnabled(filaSeleccionada);
	            eliminarBoton.setEnabled(filaSeleccionada);
			}
		});
		
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollPane = new JScrollPane(
				this.tabla,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollPane);
		return panel;
	}
	
	private void accionOpcionesProyecto(OpcionesProyecto opcion) {
		switch (opcion) {
		case COMPRAR: {
			JOptionPane.showMessageDialog(mainFrame, "Función \"Comprar\" activada para la fila: " + (tabla.getSelectedRow() + 1));
			break;
		}
		case VER: {
			int filaSeleccionada = tabla.getSelectedRow();
			
			if (filaSeleccionada >= 0) {
				String id = defaultTable.getValueAt(filaSeleccionada, 0).toString();
	            String nombre = defaultTable.getValueAt(filaSeleccionada, 1).toString();
	            String vendedor = defaultTable.getValueAt(filaSeleccionada, 2).toString();
	            String fecha = defaultTable.getValueAt(filaSeleccionada, 3).toString();
	            
	            String mensaje = "Detalles del Proyecto:\n\n" +
                        "ID: " + id + "\n" +
                        "Nombre: " + nombre + "\n" +
                        "Vendedor: " + vendedor + "\n" +
                        "Fecha de Ingreso: " + fecha;
	            
	            JOptionPane.showMessageDialog(mainFrame, mensaje, "Detalles Proyecto", JOptionPane.PLAIN_MESSAGE);
			}
			break;
		}
		case REGISTRAR: {
			String nombreProyecto = JOptionPane.showInputDialog(
	                mainFrame, 
	                "Ingrese el nombre del proyecto:", 
	                "Registrar Proyecto", 
	                JOptionPane.QUESTION_MESSAGE);
			
			if (nombreProyecto != null) {
				Object[] nuevaFila = {
		                defaultTable.getRowCount() + 1, // ID autoincremental simple
		                nombreProyecto,               // El nombre que ingresó el usuario
		                "Gato Ingeniero",             // Vendedor de prueba 😸
		                "2025-09-19"                  // Fecha de prueba
		            };
				
				defaultTable.addRow(nuevaFila);
			}
			break;
		}
		case ELIMINAR: {
			int filaSeleccionada = tabla.getSelectedRow();
			
			if (filaSeleccionada >= 0) {				
				defaultTable.removeRow(filaSeleccionada);
			}
			break;
		}
		case BUSCAR: {
			JOptionPane.showMessageDialog(mainFrame.getRootPane(), "Algún día va a buscar...");
			break;
		}
		case SALIR:
			System.exit(0); 
			break;
		}
	}
	
	public static JFrame getFrame() {
		return mainFrame;
	}
}
