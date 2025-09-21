package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase encargada de mostrar todos los componentes visuales referentes
 * a la interfaz gráfica de nuestro programa (implementada con Java Swing).
 */
public class VisualDisplayer {
	private JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
	private JFrame visualFrame = new JFrame("Ver Proyecto");
	
	private DefaultTableModel defaultMain;
	private JTable tablaProyecto;
	
	private DefaultTableModel defaultEdi;
	private JTable tablaEdificio;
	
	private DefaultTableModel defaultDepa;
	private JTable tablaDepartamento;
	
	private JButton reservarBoton;
	private JButton verBoton;
	private JButton comprarBoton;
	private JButton eliminarBoton;
	
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
		
		JPanel headerPanel = mainHeaderPanel();
		JPanel opcionesPanel = mainOpcionesPanel();
		JPanel proyectorPanel = mainProyectorPanel();
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		
		mainFrame.add(mainPanel);
		
		//				para probar
		//-----------------------------------------//
		Object[] nuevaFila = {
                defaultMain.getRowCount() + 1, // ID autoincremental simple
                "hola",               // El nombre que ingresó el usuario
                "Gato Ingeniero",             // Vendedor de prueba 😸
                "2025-09-19"                  // Fecha de prueba
            };
		defaultMain.addRow(nuevaFila);
		//-----------------------------------------//
		
		// Size automático con pack, y visibilidad a verdadero. :)
		mainFrame.pack();
		// Para colocar la ventana en "medio"
		mainFrame.setLocationRelativeTo(null);
		// Visibilidad
		mainFrame.setVisible(true);
	}
	
	private JPanel mainHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 100));
		
		JLabel titulo = new JLabel("GESTOR INMOBILIARIO", JLabel.CENTER);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");

		panel.add(titulo, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel mainOpcionesPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 20));
		panel.setPreferredSize(new Dimension(200, 250));
		
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		for (OpcionesProyecto o : OpcionesProyecto.values()) {
			JButton opcionBoton = new JButton();
			opcionBoton.setText(o.getNombre());
			
			if (OpcionesProyecto.VER.equals(o)) {
				this.verBoton = opcionBoton;
				this.verBoton.setEnabled(false);
			}
			
			opcionBoton.addActionListener(lambda -> {
				accionOpcionesProyecto(o);
			});
			
			panel.add(opcionBoton);
		}
		
		return panel;
	}
	
	private JPanel mainProyectorPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(500, 200));
		
		// Tabla con datos.
		String[] columnas = {"ID", "Nombre Proyecto", "Vendedor", "Fecha Ingreso"};
		this.defaultMain = new DefaultTableModel(columnas, 0);
		this.tablaProyecto = new JTable(defaultMain);
		
		// Para añadir funcionalidad al elegir una fila
		tablaProyecto.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
	            boolean filaSeleccionada = false;
				if (tablaProyecto.getSelectedRow() != -1) {
					filaSeleccionada = true;
				}
				
	            verBoton.setEnabled(filaSeleccionada);
			}
		});
		
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollPane = new JScrollPane(
				this.tablaProyecto,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollPane);
		return panel;
	}
	
	private void accionOpcionesProyecto(OpcionesProyecto opcion) {
		switch (opcion) {
		case VER: {
			int filaSeleccionada = tablaProyecto.getSelectedRow();
			
			if (filaSeleccionada >= 0) {
				String id = defaultMain.getValueAt(filaSeleccionada, 0).toString();
	            String nombre = defaultMain.getValueAt(filaSeleccionada, 1).toString();
	            String vendedor = defaultMain.getValueAt(filaSeleccionada, 2).toString();
	            String fecha = defaultMain.getValueAt(filaSeleccionada, 3).toString();
	            
	            String mensaje = "Detalles del Proyecto:\n\n" +
                        "ID: " + id + "\n" +
                        "Nombre: " + nombre + "\n" +
                        "Vendedor: " + vendedor + "\n" +
                        "Fecha de Ingreso: " + fecha;
	            
	            verProyectoPanel(filaSeleccionada);
	            //JOptionPane.showMessageDialog(mainFrame, mensaje, "Detalles Proyecto", JOptionPane.PLAIN_MESSAGE);
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
		                defaultMain.getRowCount() + 1, // ID autoincremental simple
		                nombreProyecto,               // El nombre que ingresó el usuario
		                "Gato Ingeniero",             // Vendedor de prueba 😸
		                "2025-09-19"                  // Fecha de prueba
		            };
				
				defaultMain.addRow(nuevaFila);
			}
			break;
		}
		/*
		case ELIMINAR: {
			int filaSeleccionada = tabla.getSelectedRow();
			
			if (filaSeleccionada >= 0) {				
				defaultTable.removeRow(filaSeleccionada);
			}
			break;
		}
		*/
		case BUSCAR: {
			JOptionPane.showMessageDialog(mainFrame.getRootPane(), "Algún día va a buscar...");
			break;
		}
		case SALIR:
			System.exit(0); 
			break;
		}
	}
	
	
	//Frame Ver Proyecto
	
	private void verProyectoPanel(int filaSeleccionada) {
		//visualFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		visualFrame.setResizable(false);
		//visualFrame.setPreferredSize(new Dimension(400, 300));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		JPanel headerPanel = verHeaderPanel(filaSeleccionada);
		JPanel opcionesPanel = verOpcionesPanel();
		JPanel proyectorPanel = verProyectorPanel();
		
		
		mainPanel.add(proyectorPanel, BorderLayout.WEST);
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		
		visualFrame.add(mainPanel);
		visualFrame.pack();
		visualFrame.setLocationRelativeTo(null);
		visualFrame.setVisible(true);
		
	}
	
	private JPanel verHeaderPanel(int filaSel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 50));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		String tit = tablaProyecto.getValueAt(filaSel, 1).toString();
		
		JLabel titulo = new JLabel("Proyecto: " + tit, JLabel.LEFT);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");

		panel.add(titulo, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel; 
	}
	
	private JPanel verOpcionesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.setPreferredSize(new Dimension(200, 100));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		for (OpcionesVer o : OpcionesVer.values()) {
			JButton opcionBoton = new JButton();
			opcionBoton.setText(o.getNombre());
			
			
			if (OpcionesVer.COMPRAR.equals(o)) {
				this.comprarBoton = opcionBoton;
				this.comprarBoton.setEnabled(false);
			}
			if (OpcionesVer.RESERVAR.equals(o)) {
				this.reservarBoton = opcionBoton;
				this.reservarBoton.setEnabled(false);
			}
			
			opcionBoton.addActionListener(lambda -> {
				accionOpcionesVer(o);
			});
			panel.add(Box.createVerticalStrut(10));
			panel.add(opcionBoton);
		}
		
		return panel;
	}
	
	private void accionOpcionesVer(OpcionesVer opcion) {
		switch (opcion) {
		case COMPRAR:{
			break;
		}
		case RESERVAR:{
			break;
		}
		case SALIR:{
			visualFrame.dispose();
			break;
		}
		
		}
		
	}
	
	private JPanel verProyectorPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		//panel.setPreferredSize(new Dimension(500, 200));
		
		// Tabla Edificio.
		String[] ediCols = {"ID", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
		this.defaultEdi = new DefaultTableModel(ediCols, 0);
		this.tablaEdificio = new JTable(defaultEdi);
		
		// Para añadir funcionalidad al elegir una fila
		tablaEdificio.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
	            boolean filaSeleccionada = false;
				if (tablaEdificio.getSelectedRow() != -1) {
					filaSeleccionada = true;
				}
				//TODO LOGICA VER LISTA DE DEPARTAMENTOS
			}
		});
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollEdi = new JScrollPane(
				this.tablaEdificio,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//Tabla Departamento
		String[] DepaCols = {"Código", "Piso", "metros^2", "Estado", "Precio"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0);
		this.tablaDepartamento = new JTable(defaultDepa);
		
		// Para añadir funcionalidad al elegir una fila
		tablaDepartamento.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
	            boolean filaSeleccionada = false;
				if (tablaDepartamento.getSelectedRow() != -1) {
					filaSeleccionada = true;
				}
				
	            comprarBoton.setEnabled(filaSeleccionada);
	            reservarBoton.setEnabled(filaSeleccionada);
			}
		});
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollDepa = new JScrollPane(
				this.tablaDepartamento,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		panel.add(scrollEdi);
		panel.add(scrollDepa);
		return panel;
	}
}
