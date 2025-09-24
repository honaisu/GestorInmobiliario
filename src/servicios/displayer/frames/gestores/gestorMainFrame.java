package servicios.displayer.frames.gestores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gestor.ProyectoInmobiliario;
import servicios.displayer.opciones.OpcionesProyecto;

public class gestorMainFrame {
	private static JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
	private JButton verBoton;
	
	private DefaultTableModel defaultMain;
	private JTable tablaProyecto;
	
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
		
		cargarProyectosEnTabla();
		
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
		this.defaultMain = new DefaultTableModel(columnas, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
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
	
	public void cargarProyectosEnTabla() {
        // Limpiamos la tabla por si tiene datos viejos
        defaultMain.setRowCount(0);

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
            defaultMain.addRow(fila);
        }
    }
}
