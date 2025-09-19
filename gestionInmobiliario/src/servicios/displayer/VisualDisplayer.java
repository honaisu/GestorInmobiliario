package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class VisualDisplayer extends JFrame {
	DefaultTableModel defaultTable;
	
	private JTable tabla;
	private JButton verBoton;
	private JButton comprarBoton;
	private JButton eliminarBoton;
	
	public void initialize() {
		JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		
		JPanel headerPanel = crearHeaderPanel();
		JPanel opcionesPanel = crearOpcionesPanel();
		JPanel proyectorPanel = crearProyectorPanel();
		
		mainFrame.add(headerPanel, BorderLayout.NORTH);
		mainFrame.add(opcionesPanel, BorderLayout.EAST);
		mainFrame.add(proyectorPanel, BorderLayout.CENTER);
		
		// Size automático con pack, y visibilidad a verdadero. :)
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	private JPanel crearHeaderPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 100));
		
		JLabel titulo = new JLabel("Bienvenido al Gestor Inmobiliario.");
		panel.add(titulo);
		
		return panel;
	}
	
	private JPanel crearOpcionesPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 20));
		panel.setPreferredSize(new Dimension(200, 250));
		
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
		panel.setPreferredSize(new Dimension(700, 200));
		
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
			JOptionPane.showMessageDialog(this, "Función \"Comprar\" activada para la fila: " + (tabla.getSelectedRow() + 1));
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
	            
	            JOptionPane.showMessageDialog(this, mensaje, "Detalles Proyecto", JOptionPane.PLAIN_MESSAGE);
			}
			break;
		}
		case REGISTRAR: {
			String nombreProyecto = JOptionPane.showInputDialog(
	                this, 
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
			JOptionPane.showMessageDialog(rootPane, "Algún día va a buscar...");
			break;
		}
		case SALIR:
			dispose();
			System.exit(0); 
			break;
		}
	}
}
