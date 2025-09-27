package servicios.displayer.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gestion.ControladorGeneral;
import gestion.GestorInmobiliarioService;
import gestion.opciones.OpcionesProyecto;
import modelo.ubicacion.ProyectoInmobiliario;

/**
 * Frame principal utilizado para nuestro programa. Se encarga de todo el orden
 * que posee la interfaz gráfica principal presentada.
 * 
 * 
 */
public class MainFrame extends JFrame {
    private GestorInmobiliarioService gestorService;
    
    private DefaultTableModel defaultTableModel;
    private JTable tablaProyectos;
    
    private List<Long> idProyectos = new ArrayList<>();
    private JButton verBoton;
    private JButton modificarBoton;
	
    public MainFrame(GestorInmobiliarioService service) {
        super("Gestor de Inmobiliaria");
        this.gestorService = service;

        inicializarComponentes();
        cargarProyectosEnTabla();
    }
    
    public void inicializarComponentes() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel headerPanel = crearHeaderPanel();
		JPanel opcionesPanel = crearOpcionesPanel();
		JPanel proyectorPanel = crearProyectorPanel();
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		
		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
    }
    
    public void cargarProyectosEnTabla() {
        // Limpiamos la tabla por si tiene datos viejos
        defaultTableModel.setRowCount(0);
        
        Collection<ProyectoInmobiliario> proyectos = gestorService.getAllProyectos();
        
        int numeroFila = 1;
        // Iteramos y añadimos cada proyecto a la tabla
        for (ProyectoInmobiliario proyecto : proyectos) {
        	// Usado para almacenar los IDs originales
        	idProyectos.add(proyecto.getId());
        	
            Object[] fila = {
            	numeroFila,
                proyecto.getNombreProyecto(),
                proyecto.getVendedor(),
                proyecto.getFechaOferta()
            };
            defaultTableModel.addRow(fila);
            numeroFila++;
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
			
			if (OpcionesProyecto.VER.equals(o)) {
				this.verBoton = opcionBoton;
			}
			
			opcionBoton.addActionListener(e -> accionOpcionesProyecto(o));
			panel.add(opcionBoton);
		}
		
		this.verBoton.setEnabled(false);
		return panel;
	}
	
	private JPanel crearProyectorPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(500, 200));
		
		// Tabla con datos.
		String[] columnas = {"N°", "Nombre Proyecto", "Vendedor", "Fecha Ingreso"};
		this.defaultTableModel = new DefaultTableModel(columnas, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
			};
		};
		
		this.tablaProyectos = new JTable(defaultTableModel);
		
		// Para añadir funcionalidad al elegir una fila
		tablaProyectos.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
	            boolean filaSeleccionada = true;
				
	            verBoton.setEnabled(filaSeleccionada);
			}
		});
		
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollPane = new JScrollPane(
				this.tablaProyectos,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollPane);
		return panel;
	}
	
	private void accionOpcionesProyecto(OpcionesProyecto opcion) {
		if (opcion == OpcionesProyecto.GUARDAR) {
	        gestorService.getDatabaseManager().actualizarDatosDatabase();
	        System.exit(0);
	        return;
	    } else if (opcion == OpcionesProyecto.SALIR) {
	        System.exit(0);
	        return;
	    }
		
        JFrame frameSecundario = null;
        int fila = tablaProyectos.getSelectedRow();

        switch (opcion) {
            case VER: {
                if (fila != -1) {
                    ProyectoInmobiliario p = gestorService.getProyectoPorId(idProyectos.get(fila));
                    if (p != null) frameSecundario = new VerProyectoFrame(gestorService, p);
                }
                break;
            }
            case REGISTRAR:
                frameSecundario = new RegistrarProyectoFrame(gestorService);
                break;
            case MODIFICAR: {
                if (fila != -1) {
                    ProyectoInmobiliario p = gestorService.getProyectoPorId(idProyectos.get(fila));
                    if (p != null) frameSecundario = new ModificarProyectoFrame(gestorService, p);
                }
                break;
            }
            case BUSCAR:
                frameSecundario = new BuscarDepartamentosFrame(gestorService);
                break;
			default:
				System.out.println("No se debería acceder normalmente a este default... :)");
				break;
        }

        // Si se creó una ventana secundaria, la mostramos y esperamos a que se cierre
        if (frameSecundario != null) {
            this.setVisible(false); // Ocultamos la ventana principal
            frameSecundario.setVisible(true);
            frameSecundario.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    cargarProyectosEnTabla();
                    MainFrame.this.setVisible(true); // La volvemos a mostrar al cerrar la otra
                }
            });
        }
    }
}
