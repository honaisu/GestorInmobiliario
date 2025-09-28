package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.FlowLayout; //pa botones?
import java.util.List;
import java.util.Collection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton; //boton de puntito
import javax.swing.JScrollPane;
import javax.swing.JSpinner;//para cosa de <cosa>
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;


import excepciones.EmailInvalidoException;
import excepciones.NombreInvalidoException;
import excepciones.RutInvalidoException;
import excepciones.TelefonoInvalidoException;
import validaciones.ValidadorEmail;
import validaciones.ValidadorNombre;
import validaciones.ValidadorRut;
import validaciones.ValidadorTelefono;
import modelo.entidades.Comprador;

import gestion.opciones.*;
import gestion.FiltroBusqueda;
import modelo.ubicacion.ProyectoInmobiliario;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.EstadoDepartamento;

/**
 * Clase encargada de mostrar todos los componentes visuales referentes
 * a la interfaz gráfica de nuestro programa (implementada con Java Swing).
 */
public class VisualDisplayer {
	private ImageIcon icono = new ImageIcon("data/images/icon.png");
	private ImageIcon like = new ImageIcon("data/images/like.png");
	private ImageIcon nonoRed = new ImageIcon("data/images/nono.gif");
	private ImageIcon sad = new ImageIcon("data/images/sad.png");
	private ImageIcon vanishRed = new ImageIcon("data/images/vanish.gif");
	private ImageIcon question = new ImageIcon("data/images/question.png");
	
	private Image likeEsc = like.getImage().getScaledInstance(
            64, 64, Image.SCALE_SMOOTH
    );
	private Image SadEsc = sad.getImage().getScaledInstance(
            64, 64, Image.SCALE_SMOOTH
    );
	private Image quesEsc = question.getImage().getScaledInstance(
            64, 64, Image.SCALE_SMOOTH
    );
	
    private ImageIcon likeRed = new ImageIcon(likeEsc);
    private ImageIcon sadRed = new ImageIcon(SadEsc);
    private ImageIcon quesRed = new ImageIcon(quesEsc);
	
	private static JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
	private JFrame registrarFrame;
	private JFrame buscarFrame = new JFrame("Filtrar Edificios");
	private JFrame visualFrame;
	private JFrame modificarFrame;
	
	private JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");
	
	private DefaultTableModel defaultMain;
	private JTable tablaProyecto;
	
	private DefaultTableModel defaultEdi;
	private JTable tablaEdificio;
	
	private DefaultTableModel defaultDepa;
	private JTable tablaDepartamento;
	private JTable tablaDepartamentosFiltrados;
	
	private JButton reservarBoton;
	private JButton modificarBoton;
	private JButton verBoton;
	private JButton comprarBoton;
	
	//Para el registrarProyecto
	private JButton botonRemoverE;
	private JButton botonRemoverD;
	private JButton botonAgregarE;
	private JButton botonAgregarD;
	private JTextField txtNombreProyecto;
	private JTextField txtVendedorProyecto;
	
	private DefaultTableModel defaultEdiModificar;
	private DefaultTableModel defaultDepaModificar;
	
    private JTextField txtPrecioMinBusqueda;
    private JTextField txtPrecioMaxBusqueda;
    private JSpinner spinnerHabBusqueda;
    private JSpinner spinnerBaniosBusqueda;
    private JComboBox<String> comboEstadoBusqueda;
    private JTextField txtDireccionBusqueda;
    private JCheckBox chkPiscinaBusqueda;
    private JCheckBox chkEstacionamientoBusqueda;
	
	private ControladorPrincipal controlador;
	
	//Sirve para establecer "limites" par los spiners <>
	private int clamp(int value, int min, int max) {
	    return Math.max(min, Math.min(max, value));
	}

	private double clamp(double value, double min, double max) {
	    return Math.max(min, Math.min(max, value));
	}
	
	//Modificar 
	private JButton botonModificarE;
	private JButton botonModificarD;
	
	private DefaultTableModel defaultDepaBusqueda; 
    
    public VisualDisplayer() {
		mainFrame.setIconImage(icono.getImage());
		buscarFrame.setIconImage(icono.getImage());
	}
    
    public void setControlador(ControladorPrincipal controlador) {
        this.controlador = controlador;
    }
	
	/**
	 * Método que hace que una instancia de la clase inicialicé la interfaz gráfica.
	 * <p>
	 * Creará una instancia de <b>JFrame</b> propia con todas las componentes clave
	 * que posee el gestor.
	 */
	public void iniciarVentanaPrincipal() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel headerPanel = crearPanelCabeceraPrincipal();
		JPanel opcionesPanel = crearPanelOpcionesPrincipal();
		JPanel proyectorPanel = crearPanelTablasProyectoPrincipal();
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		
		mainFrame.add(mainPanel);
		
		cargarProyectosEnModeloTabla();
		
		// Size automático con pack, y visibilidad a verdadero. :)
		mainFrame.pack();
		// Para colocar la ventana en "medio"
		mainFrame.setLocationRelativeTo(null);
		// Visibilidad
		mainFrame.setVisible(true);
	}
	
	public void cargarProyectosEnModeloTabla() {
        // Limpiamos la tabla por si tiene datos viejos
        defaultMain.setRowCount(0);
        
        // Pedimos los proyectos al controlador (no sabemos de dónde los saca, y no nos importa)
        Collection<ProyectoInmobiliario> proyectos = controlador.obtenerTodosLosProyectos();

        // Iteramos y añadimos cada proyecto a la tabla
        int idFalso = 1;
        for (ProyectoInmobiliario proyecto : proyectos) {
            Object[] fila = {
                proyecto.getId(),
                idFalso,
                proyecto.getNombreProyecto(),
                proyecto.getVendedor(),
                proyecto.getFechaOferta()
            };
            defaultMain.addRow(fila);
            idFalso++;
        }
    }
	
	private JPanel crearPanelCabeceraPrincipal() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 100));
		JLabel titulo = new JLabel("GESTOR INMOBILIARIO", JLabel.CENTER);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		panel.add(titulo, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel crearPanelOpcionesPrincipal() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 20));
		panel.setPreferredSize(new Dimension(300, 250));
		
		JPanel panelBajo = new JPanel(new GridLayout(0, 2, 10, 20));
		
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		panelBajo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		for (OpcionesProyecto o : OpcionesProyecto.values()) {
			JButton opcionBoton = new JButton();
			opcionBoton.setText(o.getNombre());
			
			if (OpcionesProyecto.VER.equals(o)) {
				this.verBoton = opcionBoton;
				this.verBoton.setEnabled(false);
			}

			if (OpcionesProyecto.MODIFICAR.equals(o)) {
				this.modificarBoton = opcionBoton;
				this.modificarBoton.setEnabled(false);
			}

			if (OpcionesProyecto.SALIR.equals(o) || OpcionesProyecto.GUARDAR.equals(o)) {
				panelBajo.add(opcionBoton);
			}
			else {
				panel.add(opcionBoton);
			}
			
			opcionBoton.addActionListener(lambda -> { controlador.manejarOpcionesProyecto(o); });
		}
		
		panel.add(panelBajo, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel crearPanelTablasProyectoPrincipal() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(700, 200));
		
		// Tabla con datos.
		String[] columnas = {"ID Real", "N°", "Nombre Proyecto", "Vendedor", "Fecha Ingreso"};
		this.defaultMain = new DefaultTableModel(columnas, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
			};
		};
		this.tablaProyecto = new JTable(defaultMain);
		
		tablaProyecto.getColumnModel().getColumn(0).setMinWidth(0);
		tablaProyecto.getColumnModel().getColumn(0).setMaxWidth(0);
		tablaProyecto.getColumnModel().getColumn(0).setWidth(0);
		formatearTablaProyectoPrincipal();
		
		// Para añadir funcionalidad al elegir una fila
		tablaProyecto.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
	            boolean filaSeleccionada = false;
				if (tablaProyecto.getSelectedRow() != -1) {
					filaSeleccionada = true;
				}
				
	            verBoton.setEnabled(filaSeleccionada);
	            modificarBoton.setEnabled(filaSeleccionada);
			}
		});
		
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollPane = new JScrollPane(
				tablaProyecto,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollPane);
		return panel;
	}
	
	private JPanel crearPanelCabeceraVerDatos(String titulo) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 50));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		JLabel tituloLabel = new JLabel("Proyecto: " + titulo, JLabel.LEFT);
		tituloLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		panel.add(tituloLabel, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		return panel; 
	}
	
	private JPanel crearPanelOpcionesVerDatos() {
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
			
			opcionBoton.addActionListener(lambda -> { controlador.manejarOpcionesVerDatos(o); });
			panel.add(Box.createVerticalStrut(10));
			panel.add(opcionBoton);
		}
		
		return panel;
	}
	
	public long getSelectedEdificioId() {
	    int filaSel = tablaEdificio.getSelectedRow();
	    if (filaSel == -1) return -1;
	    
	    return (long) defaultEdi.getValueAt(filaSel, 0);
	}
	
	public String getSelectedDepartamentoCodigo() {
	    int filaSel = tablaDepartamento.getSelectedRow();
	    if (filaSel == -1) return null;
	    
	    return defaultDepa.getValueAt(filaSel, 0).toString();
	}

	public void cargarEdificiosEnTabla(List<Edificio> edificios) {
		defaultEdi.setRowCount(0);
		
		int idFalso = 1;
		for (Edificio e : edificios) {
			Object[] fila = {
	            e.getId(),
	            idFalso,
	            e.getNombre(),
	            e.getInformacion().getDireccion(),
	            e.getInformacion().isTienePiscina() ? "Sí" : "No",
	            e.getInformacion().isTieneEstacionamiento() ? "Sí" : "No"
	        };
	        defaultEdi.addRow(fila);
		}
	}
	
	public void cargarDepartamentosEnTabla(List<Departamento> departamentos) {
	    // Limpiamos la tabla antes
	    defaultDepa.setRowCount(0);
	    if (departamentos == null) return;

	    for (Departamento d : departamentos) {
	        Object[] fila = {
	            d.getCodigo(),
	            d.getNumeroPiso(),
	            d.getMetrosCuadrados(),
	            d.getHabitaciones(),
	            d.getBanos(),
	            d.getEstado().toString(),
	            d.getGestorPrecios().getPrecioActual()
	        };
	        defaultDepa.addRow(fila);
	    }
	    
	    tablaDepartamento.clearSelection();
	    
	}
	
	public void actualizarTablaDepartamentosRegistrar(List<Departamento> departamentos) {
	    // Limpiar la tabla primero
	    defaultDepa.setRowCount(0);

	    // Recorrer la lista y añadir filas
	    for (Departamento d : departamentos) {
	        Object[] fila = {
        		d.getCodigo(),
	            d.getNumeroPiso(),
	            d.getMetrosCuadrados(),
	            d.getHabitaciones(),
	            d.getBanos(),
	            d.getEstado().toString(),
	            d.getGestorPrecios().getPrecioActual(),
	        };
	        
	        defaultDepa.addRow(fila);
	    }
	}
	
	public void actualizarTablaDepartamentosBusqueda(List<Departamento> departamentos) {
	    // Limpiar la tabla primero
	    defaultDepaBusqueda.setRowCount(0);

	    // Recorrer la lista y añadir filas
	    for (Departamento d : departamentos) {
	        Object[] fila = {
	        	d.getEdificioPadre().getProyectoPadre().getNombreProyecto(),
        		d.getCodigo(),
	            d.getNumeroPiso(),
	            d.getMetrosCuadrados(),
	            d.getHabitaciones(),
	            d.getBanos(),
	            d.getEstado().toString(),
	            d.getGestorPrecios().getPrecioActual(),
	            d.getEdificioPadre().getInformacion().getDireccion(),
	            d.getEdificioPadre().getInformacion().isTieneEstacionamiento() ? "SI" : "NO",
	            d.getEdificioPadre().getInformacion().isTienePiscina() ? "SI" : "NO"
	        };
	        
	        defaultDepaBusqueda.addRow(fila);
	    }
	}

	private JPanel crearPanelTablasVerDatos(List<Edificio> edificios) {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		//panel.setPreferredSize(new Dimension(500, 200));
		
		// Tabla Edificio.
		String[] ediCols = {"ID Real", "N°", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
		this.defaultEdi = new DefaultTableModel(ediCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		this.tablaEdificio = new JTable(defaultEdi);
		
		//Formatear Tabla
		formatearTablaEdificios();
		
		this.cargarEdificiosEnTabla(edificios);
		comprarBoton.setEnabled(false);
        reservarBoton.setEnabled(false);
		
		// Para añadir funcionalidad al elegir una fila
		tablaEdificio.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			//tablaDepartamento.clearSelection();
			if (!lambda.getValueIsAdjusting()) {
				if (tablaEdificio.getSelectedRow() == -1) {
		            defaultDepa.setRowCount(0);
		        }
				
		        controlador.edificioSeleccionadoEnVer(getSelectedEdificioId());
			}
		});
		
		//Tabla Departamento
		String[] DepaCols = {"Código", "Piso", "Metros²", "Habitaciones", "Baños", "Estado", "Precio"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		this.tablaDepartamento = new JTable(defaultDepa);
		
		formatearTablaDepartamentos();
		
		// Para añadir funcionalidad al elegir una fila
		tablaDepartamento.getSelectionModel().addListSelectionListener(lambda -> {
			//tablaDepartamento.clearSelection();
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			if (!lambda.getValueIsAdjusting()) {
				int filaSelDepa = tablaDepartamento.getSelectedRow();
				if (filaSelDepa != -1) {
					String estado = defaultDepa.getValueAt(filaSelDepa, 5).toString();

		            boolean disponible = estado.toString().equals("DISPONIBLE");
		            boolean reservado = estado.toString().equals("RESERVADO");
		            if(disponible) {
		            	comprarBoton.setEnabled(disponible);
			            reservarBoton.setEnabled(disponible);
		            }else if(reservado) {
		            	comprarBoton.setEnabled(reservado);
		            	reservarBoton.setEnabled(false);
		            }
		            else {
		            	comprarBoton.setEnabled(false);
		            	reservarBoton.setEnabled(false);
		            }
		        } else {
		            // Nada seleccionado → deshabilitar botones
		            comprarBoton.setEnabled(false);
		            reservarBoton.setEnabled(false);
		        
				}
			}
		});
		
		// Encargado de mostrar la barrita vertical.
		
		JScrollPane scrollEdi = new JScrollPane(
				this.tablaEdificio,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollPane scrollDepa = new JScrollPane(
				this.tablaDepartamento,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		panel.add(scrollEdi);
		panel.add(scrollDepa);
		return panel;
	}
	
	//----------------------------
	//	Frame Registrar Proyecto
	//----------------------------
	
	/// Parte de registrar ///
	public void iniciarVentanaRegistrar() {
		registrarFrame = new JFrame("Registrar Proyecto");
		registrarFrame.setIconImage(icono.getImage());
		registrarFrame.setResizable(false);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel headerPanel = crearPanelCabeceraRegistrar();//arriba
		JPanel opcionesPanel = crearPanelOpcionesRegistrar();//abajo
		JPanel proyectorPanel = crearPanelTablasRegistrar(); //parte "central"
		
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		mainPanel.add(opcionesPanel, BorderLayout.SOUTH);
		
		registrarFrame.add(mainPanel);
		
		registrarFrame.pack();
		registrarFrame.setLocationRelativeTo(null);
		registrarFrame.setVisible(true);
		
		registrarFrame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	mainFrame.setVisible(true);
		        
		    }
		});
		
	}
	
	private JPanel crearPanelCabeceraRegistrar() {
		JPanel panel = new JPanel(new BorderLayout()); 
		panel.setPreferredSize(new Dimension(600, 150));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel titulo = new JLabel("Registrar", JLabel.CENTER);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		panel.add(titulo, BorderLayout.NORTH);
		
		///Parte central
		JPanel panelCentral = new JPanel(new BorderLayout(5,5));
		
		// Panel de inputs en el centro
	    JPanel datosPanel = new JPanel(new GridLayout(2, 2, 10, 10));
	    
	    //Nombre del proyecto 
	    datosPanel.add(new JLabel("Nombre: "));
	    txtNombreProyecto = new JTextField(25);
	    datosPanel.add(txtNombreProyecto);
	    
	    //Nombre del vendedor
	    datosPanel.add(new JLabel("Vendedor: "));
	    txtVendedorProyecto = new JTextField(25);
	    datosPanel.add(txtVendedorProyecto);

	    panelCentral.add(datosPanel, BorderLayout.WEST);
	    
	    ///Parte derecha con la fecha (no se si implementemos hora)
	    JLabel panelHora = new JLabel("Fecha Ingreso: ");
	    panelHora.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
	    panelHora.setHorizontalAlignment(JLabel.RIGHT);
	    panelHora.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	    panelCentral.add(panelHora, BorderLayout.EAST);

	    Timer timer = new Timer(1000, e -> {
	        LocalDateTime now = LocalDateTime.now();
	        panelHora.setText("Fecha Ingreso: " + now.format(
	            DateTimeFormatter.ofPattern("dd-MM-yy")
	        ));
	    });
	    timer.start();

	    panel.add(panelCentral, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel; 
	}
	
	private JPanel crearPanelOpcionesRegistrar() {
	    JPanel panel = new JPanel(new BorderLayout(10, 10));

	    // Panel central con dos bloques de Edificio y Departamento
	    JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));

	    // Bloque botones Edificio
	    JPanel panelEdificio = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	    botonAgregarE = new JButton(OpcionesRegistrar.AGREGAR_E.getNombre());
	    botonRemoverE = new JButton(OpcionesRegistrar.REMOVER_E.getNombre());
	    botonRemoverE.setEnabled(false); //parte "desabilitado" ya que todavia no seleccionamos una fila
	    
	    
	    panelEdificio.add(botonAgregarE);
	    panelEdificio.add(botonRemoverE);

	    // Bloque botones Departamento
	    JPanel panelDepartamento = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	    botonAgregarD = new JButton(OpcionesRegistrar.AGREGAR_D.getNombre());
	    botonRemoverD = new JButton(OpcionesRegistrar.REMOVER_D.getNombre());
	    botonAgregarD.setEnabled(false);
	    botonRemoverD.setEnabled(false); //parte "desabilitado" ya que todavia no seleccionamos una fila
	    
	    panelDepartamento.add(botonAgregarD);
	    panelDepartamento.add(botonRemoverD);

	    panelCentral.add(panelEdificio);
	    panelCentral.add(panelDepartamento);

	    // Panel inferior para botones Registrar y Salir
	    JPanel 	panelInferior 	= new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JButton botonRegistrar 	= new JButton(OpcionesRegistrar.REGISTRAR.getNombre());
	    JButton botonSalir 		= new JButton(OpcionesRegistrar.SALIR.getNombre());
	    panelInferior.add(botonRegistrar);
	    panelInferior.add(botonSalir);

	    // Armar todo
	    panel.add(panelCentral, BorderLayout.CENTER);
	    panel.add(panelInferior, BorderLayout.SOUTH);

	    // --- Listeners --- //esto todavia ni lo entiendo, pero creo que es como el for de benjoid
	    botonAgregarE.addActionListener	(e -> controlador.manejarOpcionesRegistrar(OpcionesRegistrar.AGREGAR_E));
	    botonRemoverE.addActionListener	(e -> controlador.manejarOpcionesRegistrar(OpcionesRegistrar.REMOVER_E));
	    botonAgregarD.addActionListener	(e -> controlador.manejarOpcionesRegistrar(OpcionesRegistrar.AGREGAR_D));
	    botonRemoverD.addActionListener	(e -> controlador.manejarOpcionesRegistrar(OpcionesRegistrar.REMOVER_D));
	    botonSalir.addActionListener	(e -> controlador.manejarOpcionesRegistrar(OpcionesRegistrar.SALIR));
	    botonRegistrar.addActionListener(e -> controlador.manejarOpcionesRegistrar(OpcionesRegistrar.REGISTRAR));
	    
	    return panel;
	}
	
	public String getNombreProyectoRegistrar() {
	    // Devuelve el texto del campo de nombre del proyecto
	    return txtNombreProyecto.getText().trim();
	}

	public String getVendedorRegistrar() {
	    // Devuelve el texto del campo del vendedor
	    return txtVendedorProyecto.getText().trim();
	}
	
	private JPanel crearPanelTablasRegistrar() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(900, 350));
		
		// Tabla Edificio.
		String[] columnasEdificio = {"N°", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
		this.defaultEdi = new DefaultTableModel(columnasEdificio, 0);
		this.tablaEdificio = new JTable(defaultEdi);
		
		formatearTablaEdificios();
		
		// Para añadir funcionalidad al elegir una fila
		tablaEdificio.getSelectionModel().addListSelectionListener(lambda -> {
		    if (!lambda.getValueIsAdjusting()) {
		        int filaSel = tablaEdificio.getSelectedRow();
		        boolean filaSeleccionada = filaSel != -1;

		        botonRemoverE.setEnabled(filaSeleccionada);
		        botonAgregarD.setEnabled(filaSeleccionada);

		        if (!filaSeleccionada) {
		        	defaultDepa.setRowCount(0);
		        }

		        controlador.edificioSeleccionadoEnRegistrar(getSelectedEdificioId());
		    }
		});
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollEdi = new JScrollPane(
				this.tablaEdificio,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		/// Tabla Departamento /// //el id es momentaneo, e spara ver si el depa se agregaba a su respectivo edificio
		String[] DepaCols = {"Código", "Piso", "metros²","Habitacion", "Baños", "Estado", "Precio"};

		this.defaultDepa = new DefaultTableModel(DepaCols, 0);
		this.tablaDepartamento = new JTable(defaultDepa);
		
		
		formatearTablaDepartamentos();
		
		// Para añadir funcionalidad al elegir una fila
		tablaDepartamento.getSelectionModel().addListSelectionListener(lambda -> {
		    if (!lambda.getValueIsAdjusting()) {
		        boolean filaSeleccionada = tablaDepartamento.getSelectedRow() != -1;
		        botonRemoverD.setEnabled(filaSeleccionada);
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
	
	//-----------------------------
	//	Frame Busqueda por Filtro
	//-----------------------------
	
	private void iniciarVentanaBuscar() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel headerPanel 		= crearPanelCabeceraBuscar();
		JPanel opcionesPanel 	= crearPanelOpcionesBuscar();
		JPanel proyectorPanel 	= crearPanelTablasBuscar();
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		
		buscarFrame.add(mainPanel);
		buscarFrame.pack();
		buscarFrame.setLocationRelativeTo(null);
		buscarFrame.setResizable(false);
		buscarFrame.setVisible(true);
		
		buscarFrame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	mainFrame.setVisible(true);
		        
		    }
		});
	}
	
	private JPanel crearPanelCabeceraBuscar() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 50));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		JLabel titulo = new JLabel("Filtrar Edificios", JLabel.LEFT);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		panel.add(titulo, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel crearPanelTablasBuscar() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(900, 300));
		
		//Tabla Departamento
		String[] DepaCols = {"Nombre Proyecto", "Código", "Piso", "Metros²", "Habitaciones", "Baños",
				"Estado", "Precio", "Dirección", "Estacionamiento", "Piscina"};
		this.defaultDepaBusqueda = new DefaultTableModel(DepaCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		
		this.tablaDepartamentosFiltrados = new JTable(defaultDepaBusqueda);
	
		//Formatear Columnas
		formatearTablaDepBus();
		
		JScrollPane scrollDepa = new JScrollPane(
				this.tablaDepartamentosFiltrados,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollDepa);
		return panel;
	}
	
	private JPanel crearPanelOpcionesBuscar() {
		JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setPreferredSize(new Dimension(250, 100));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    // --- Precio ---
	    JPanel precioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    // Usamos las variables de instancia (this. ... )
	    this.txtPrecioMinBusqueda = new JTextField(6);
	    this.txtPrecioMaxBusqueda = new JTextField(6);
	    precioPanel.add(new JLabel("Precio:"));
	    precioPanel.add(txtPrecioMinBusqueda);
	    precioPanel.add(new JLabel("-"));
	    precioPanel.add(txtPrecioMaxBusqueda);

	    // --- Habitaciones ---
	    JPanel habPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    this.spinnerHabBusqueda = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
	    habPanel.add(new JLabel("Habitaciones:"));
	    habPanel.add(spinnerHabBusqueda);

	    // --- Baños ---
	    JPanel banioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    this.spinnerBaniosBusqueda = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
	    banioPanel.add(new JLabel("Baños:"));
	    banioPanel.add(spinnerBaniosBusqueda);

	    // --- Estado ---
	    JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    this.comboEstadoBusqueda = new JComboBox<>(new String[]{"", "DISPONIBLE", "RESERVADO", "VENDIDO"});
	    estadoPanel.add(new JLabel("Estado:"));
	    estadoPanel.add(comboEstadoBusqueda);

	    // --- Dirección ---
	    JPanel dirPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    this.txtDireccionBusqueda = new JTextField(10);
	    dirPanel.add(new JLabel("Dirección:"));
	    dirPanel.add(txtDireccionBusqueda);

	    // --- Extras ---
	    JPanel extrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    this.chkPiscinaBusqueda = new JCheckBox("Piscina");
	    this.chkEstacionamientoBusqueda = new JCheckBox("Estacionamiento");
	    extrasPanel.add(chkPiscinaBusqueda);
	    extrasPanel.add(chkEstacionamientoBusqueda);

	    // --- Botón de búsqueda ---
	    JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JButton btnBuscar = new JButton("Buscar");
	    JButton btnCancelar = new JButton("Cancelar");
	    botonPanel.add(btnBuscar);
	    botonPanel.add(btnCancelar);
	    
	    btnCancelar.addActionListener(e -> {
	        buscarFrame.dispose();
	        mainFrame.setVisible(true);
	    });
	    
	    
	    //Obtener Valores
	    btnBuscar.addActionListener(e -> { controlador.realizarBusquedaDepartamentos(); } );

	    // Añadir todo
	    panel.add(precioPanel);
	    panel.add(habPanel);
	    panel.add(banioPanel);
	    panel.add(estadoPanel);
	    panel.add(dirPanel);
	    panel.add(extrasPanel);
	    panel.add(Box.createVerticalStrut(20)); // espacio
	    panel.add(botonPanel);

	    return panel;
	}
	
	/**
	 * Formatea las tablas del proyecto para que queden sin poder reordenarse.
	 */
	private void formatearTablaProyectoPrincipal() {
		for (int i = 0; i < tablaProyecto.getColumnCount(); i++) {
			tablaProyecto.getColumnModel().getColumn(i).setResizable(false);
			
		}
		
		tablaProyecto.getColumnModel().getColumn(0).setCellRenderer(rendererColumna);
		
		tablaProyecto.getTableHeader().setReorderingAllowed(false);
		tablaProyecto.getColumnModel().getColumn(0).setPreferredWidth(1);
	}
	
	private void formatearTablaDepBus() {
		for (int i = 0; i < tablaDepartamentosFiltrados.getColumnCount(); i++) {
			tablaDepartamentosFiltrados.getColumnModel().getColumn(i).setResizable(false);
			
		}
		
		tablaDepartamentosFiltrados.getColumnModel().getColumn(0).setCellRenderer(rendererColumna);
		
		tablaDepartamentosFiltrados.getTableHeader().setReorderingAllowed(false);
		tablaDepartamentosFiltrados.getColumnModel().getColumn(0).setPreferredWidth(120);  	// NOMBRE PROYECTO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(1).setPreferredWidth(50);  	// CODIGO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(2).setPreferredWidth(30);  	// PISO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(3).setPreferredWidth(50); 	// METROS²
		tablaDepartamentosFiltrados.getColumnModel().getColumn(4).setPreferredWidth(60);  	// HABITACIONES
		tablaDepartamentosFiltrados.getColumnModel().getColumn(5).setPreferredWidth(30);  	// BAÑOS
		tablaDepartamentosFiltrados.getColumnModel().getColumn(6).setPreferredWidth(70); 	// ESTADO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(7).setPreferredWidth(60); 	// PRECIO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(8).setPreferredWidth(120); 	// DIRECCION
		tablaDepartamentosFiltrados.getColumnModel().getColumn(9).setPreferredWidth(100); 	// ESTACIONAMIENTO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(10).setPreferredWidth(50); 	// PISCINA
	}
	
	private void formatearTablaEdificios() {
		for (int i = 0; i < tablaEdificio.getColumnCount(); i++) {
			tablaEdificio.getColumnModel().getColumn(i).setResizable(false);	
		}
		
		tablaEdificio.getColumnModel().getColumn(1).setCellRenderer(rendererColumna);
		
		tablaEdificio.getTableHeader().setReorderingAllowed(false);
		tablaEdificio.getColumnModel().getColumn(0).setPreferredWidth(0);
		
		
		tablaEdificio.getColumnModel().getColumn(1).setPreferredWidth(5); 	// ID
		tablaEdificio.getColumnModel().getColumn(2).setPreferredWidth(100); // EDIFICIO
		tablaEdificio.getColumnModel().getColumn(3).setPreferredWidth(120); // DIRECCION
		tablaEdificio.getColumnModel().getColumn(4).setPreferredWidth(30); 	// PISCINA
		tablaEdificio.getColumnModel().getColumn(5).setPreferredWidth(80); 	// ESTACIONAMIENTO
		
	}
	
	private void formatearTablaDepartamentos() {
		for (int i = 0; i < tablaDepartamento.getColumnCount(); i++) {
			tablaDepartamento.getColumnModel().getColumn(i).setResizable(false);
			
		}
		
		tablaDepartamento.getColumnModel().getColumn(0).setCellRenderer(rendererColumna);
		
		tablaDepartamento.getTableHeader().setReorderingAllowed(false);
		tablaDepartamento.getColumnModel().getColumn(0).setPreferredWidth(50); //CODIGO
		tablaDepartamento.getColumnModel().getColumn(1).setPreferredWidth(30); //PISO
		tablaDepartamento.getColumnModel().getColumn(2).setPreferredWidth(50); //METROS²
		tablaDepartamento.getColumnModel().getColumn(3).setPreferredWidth(70); //HABITACIONES
		tablaDepartamento.getColumnModel().getColumn(4).setPreferredWidth(30); //BAÑOS
		tablaDepartamento.getColumnModel().getColumn(5).setPreferredWidth(70); //ESTADO
		tablaDepartamento.getColumnModel().getColumn(6).setPreferredWidth(60); //PRECIO
	}
	
	//Color gris en columna en tabla
	DefaultTableCellRenderer rendererColumna = new DefaultTableCellRenderer() {
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {

	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	        // Si está seleccionada, mantenemos el color de selección
	        if (!isSelected) {
	            c.setBackground(new Color(230, 230, 230)); // gris claro
	        }

	        return c;
	    }
	};
	
	//----------------------------
	//	Frame Modificar Proyecto
	//----------------------------
	public void iniciarVentanaModificar(ProyectoInmobiliario proyecto) {
	    modificarFrame = new JFrame("Modificar Proyecto");
	    modificarFrame.setIconImage(icono.getImage());
	    modificarFrame.setResizable(false);
	    
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // subpaneles en el mismo estilo que "registrar"
	    JPanel headerPanel = crearPanelCabeceraModificar(proyecto);
	    JPanel opcionesPanel = crearPanelOpcionesModificar(proyecto);
	    JPanel proyectorPanel = crearPanelTablasModificar(proyecto);

	    mainPanel.add(headerPanel, BorderLayout.NORTH);
	    mainPanel.add(opcionesPanel, BorderLayout.SOUTH);
	    mainPanel.add(proyectorPanel, BorderLayout.CENTER);

	    modificarFrame.add(mainPanel);
	    modificarFrame.pack();
	    modificarFrame.setLocationRelativeTo(null);
	    modificarFrame.setVisible(true);

	    modificarFrame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            mainFrame.setVisible(true);
	        }
	    });
		
	}
	
	private JPanel crearPanelCabeceraModificar(ProyectoInmobiliario  proyectoSel) {
		JPanel panel = new JPanel(new BorderLayout());
	    panel.setPreferredSize(new Dimension(600, 150));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // El título usa el nombre del proyecto
	    JLabel titulo = new JLabel("Modificar Proyecto: " + proyectoSel.getNombreProyecto(), JLabel.CENTER);
	    titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
	    panel.add(titulo, BorderLayout.NORTH);

	    JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
	    JPanel datosPanel = new JPanel(new GridLayout(2, 2, 10, 10));

	    // Nombre del proyecto
	    datosPanel.add(new JLabel("Nombre: "));
	    // 2. Se inicializa el JTextField con el nombre actual del proyecto
	    txtNombreProyecto = new JTextField(proyectoSel.getNombreProyecto(), 25);
	    datosPanel.add(txtNombreProyecto);

	    // Nombre del vendedor
	    datosPanel.add(new JLabel("Vendedor: "));
	    // 2. Se inicializa el JTextField con el vendedor actual
	    txtVendedorProyecto = new JTextField(proyectoSel.getVendedor(), 25);
	    datosPanel.add(txtVendedorProyecto);

	    panelCentral.add(datosPanel, BorderLayout.WEST);

	    // 3. Mostramos la fecha original del proyecto, sin Timer
	    //    Asumimos que proyecto.getFechaOferta() devuelve un String o un objeto formateable.
	    JLabel panelFecha = new JLabel("Fecha Ingreso: " + proyectoSel.getFechaOferta());
	    panelFecha.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
	    panelFecha.setHorizontalAlignment(JLabel.RIGHT);
	    panelFecha.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	    panelCentral.add(panelFecha, BorderLayout.EAST);

	    panel.add(panelCentral, BorderLayout.CENTER);
	    panel.add(marcaAgua, BorderLayout.SOUTH);

	    return panel;

	}
	
	private JPanel crearPanelOpcionesModificar(ProyectoInmobiliario proyectoSel) {
		JPanel panel = new JPanel(new BorderLayout(10, 10));

	    // --- Panel Central para Agregar/Remover ---
	    JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));

	    // --- Bloque Edificio ---
	    JPanel panelEdificio = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    botonAgregarE = new JButton(OpcionesModificar.AGREGAR_E.getNombre());
	    botonRemoverE = new JButton(OpcionesModificar.REMOVER_E.getNombre());
	    botonModificarE = new JButton(OpcionesModificar.MODIFICAR_E.getNombre());
	    botonRemoverE.setEnabled(false);
	    botonModificarE.setEnabled(false);
	    panelEdificio.add(botonAgregarE);
	    panelEdificio.add(botonRemoverE);
	    panelEdificio.add(botonModificarE);

	    // --- Bloque Departamento ---
	    JPanel panelDepartamento = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    botonAgregarD = new JButton(OpcionesModificar.AGREGAR_D.getNombre());
	    botonRemoverD = new JButton(OpcionesModificar.REMOVER_D.getNombre());
	    botonModificarD = new JButton(OpcionesModificar.MODIFICAR_D.getNombre());
	    botonAgregarD.setEnabled(false);
	    botonRemoverD.setEnabled(false);
	    botonModificarD.setEnabled(false);
	    panelDepartamento.add(botonAgregarD);
	    panelDepartamento.add(botonRemoverD);
	    panelDepartamento.add(botonModificarD);

	    panelCentral.add(panelEdificio);
	    panelCentral.add(panelDepartamento);

	    // --- Panel Inferior Izquierda (Guardar/Cancelar) ---
	    JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JButton botonGuardar = new JButton(OpcionesModificar.GUARDAR_CAMBIOS.getNombre());
	    JButton botonCancelar = new JButton(OpcionesModificar.CANCELAR.getNombre());
	    panelInferior.add(botonGuardar);
	    panelInferior.add(botonCancelar);

	    // --- Panel Inferior Derecha (Eliminar) ---
	    JPanel panelEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton botonEliminar = new JButton("Eliminar Proyecto");
	    
	    botonEliminar.setBackground(new Color(227, 89, 89));
	    botonEliminar.setForeground(Color.WHITE);
	    panelEliminar.add(botonEliminar);

	    // --- Contenedor para Sur ---
	    JPanel panelSur = new JPanel(new BorderLayout());
	    panelSur.add(panelInferior, BorderLayout.CENTER);
	    panelSur.add(panelEliminar, BorderLayout.EAST);

	    panel.add(panelCentral, BorderLayout.CENTER);
	    panel.add(panelSur, BorderLayout.SOUTH);

	    // --- Asignación de Listeners ---
	    botonAgregarE.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.AGREGAR_E, proyectoSel));
	    botonRemoverE.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.REMOVER_E, proyectoSel));
	    botonModificarE.addActionListener	(e -> controlador.manejarOpcionesModificar(OpcionesModificar.MODIFICAR_E, proyectoSel)); 
	    botonAgregarD.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.AGREGAR_D, proyectoSel));
	    botonRemoverD.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.REMOVER_D, proyectoSel));
	    botonModificarD.addActionListener	(e -> controlador.manejarOpcionesModificar(OpcionesModificar.MODIFICAR_D, proyectoSel)); 
	    botonGuardar.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.GUARDAR_CAMBIOS, proyectoSel));
	    botonCancelar.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.CANCELAR, proyectoSel));
	    botonEliminar.addActionListener		(e -> controlador.manejarOpcionesModificar(OpcionesModificar.ELIMINAR_PROYECTO, proyectoSel));
	    
	    return panel;
	}
	
	public String getSelectedDepartamentoCodigoFromTablaModificar() {
	    int filaSel = tablaDepartamento.getSelectedRow();
	    if (filaSel == -1) return null;
	    
	    // Lee del modelo de tabla correcto: defaultDepaModificar
	    return defaultDepaModificar.getValueAt(filaSel, 0).toString();
	}
	
	private JPanel crearPanelTablasModificar(ProyectoInmobiliario proyectoSel) {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
	    panel.setPreferredSize(new Dimension(900, 350));
	    
	    // --- Tabla Edificio ---
	    String[] ediCols = {"ID", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
	    this.defaultEdiModificar = new DefaultTableModel(ediCols, 0);
	    this.tablaEdificio = new JTable(defaultEdiModificar);

	    // --- Tabla Departamento ---
	    String[] DepaCols = {"Código", "Piso", "m²", "Hab.", "Baños", "Estado", "Precio"};
	    this.defaultDepaModificar = new DefaultTableModel(DepaCols, 0);
	    this.tablaDepartamento = new JTable(defaultDepaModificar);
	    
	    actualizarTablaEdificiosModificar(controlador.obtenerEdificiosTemporales());
	    
	    tablaEdificio.getSelectionModel().addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) {
	            int filaSel = tablaEdificio.getSelectedRow();
	            boolean seleccionado = filaSel != -1;

	            botonRemoverE.setEnabled(seleccionado);
	            botonModificarE.setEnabled(seleccionado);
	            botonAgregarD.setEnabled(seleccionado);

	            // Limpia la tabla de deptos si no hay nada seleccionado
	            if (!seleccionado) {
	                defaultDepaModificar.setRowCount(0);
	            }

	            controlador.edificioSeleccionadoEnModificar(getEdificioIdFromTablaModificar());
	        }
	    });
	    
	    panel.add(new JScrollPane(tablaEdificio));
	    panel.add(new JScrollPane(tablaDepartamento));
	    return panel;
	}
	
	public long getEdificioIdFromTablaModificar() {
	    int filaSel = tablaEdificio.getSelectedRow();
	    if (filaSel == -1) return -1;
	    return (long) defaultEdiModificar.getValueAt(filaSel, 0);
	}

	public void actualizarTablaEdificiosModificar(List<Edificio> edificios) {
		defaultEdiModificar.setRowCount(0);
		if (edificios == null) return;
		
		for (Edificio e : edificios) {
	        Object[] fila = {
	            e.getId(), 
	            e.getNombre(), 
	            e.getInformacion().getDireccion(),
	            e.getInformacion().isTienePiscina() ? "Sí" : "No",
	            e.getInformacion().isTieneEstacionamiento() ? "Sí" : "No"
	        };
	        defaultEdiModificar.addRow(fila);
	    }
	}

	public void mostrarVentanaPrincipal() {
		mainFrame.setVisible(true);
	}
	
	public void ocultarVentanaPrincipal() {
		mainFrame.setVisible(false);
	}


	public void mostrarVentanaVerDatos(String tituloProyecto, List<Edificio> edificios) {
	    visualFrame = new JFrame("Ver Proyecto");
	    visualFrame.setIconImage(icono.getImage());
	    visualFrame.setResizable(false);
	
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
	    // Usa los datos que recibe como parámetros
	    JPanel headerPanel = crearPanelCabeceraVerDatos(tituloProyecto);
	    JPanel opcionesPanel = crearPanelOpcionesVerDatos();
	    JPanel proyectorPanel = crearPanelTablasVerDatos(edificios);
	
	    mainPanel.add(headerPanel, BorderLayout.NORTH);
	    mainPanel.add(opcionesPanel, BorderLayout.EAST);
	    mainPanel.add(proyectorPanel, BorderLayout.WEST);
	
	    visualFrame.add(mainPanel);
	    visualFrame.pack();
	    visualFrame.setLocationRelativeTo(null);
	    visualFrame.setVisible(true);
	
	    visualFrame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            mainFrame.setVisible(true);
	        }
	    });
	}
	
	public void mostrarVentanaBuscar() {
		iniciarVentanaBuscar();
	}

	public void mostrarMensajeDeError(Exception e) {
		JOptionPane.showMessageDialog(null, "Error al guardar en DB: " + e, "Error con el programa", JOptionPane.PLAIN_MESSAGE, likeRed);
	}

	public void cerrarVentanaVerDatos() {
		visualFrame.dispose();
	}

	public void solicitarRegistroUsuario(Edificio edificioSel, Departamento deptoSel, boolean b) {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		JTextField txtNombre = new JTextField(20);
	    JTextField txtRut = new JTextField(20);
	    JTextField txtEmail = new JTextField(20);
	    JTextField txtNumero = new JTextField(20);
	    
	    panel.add(new JLabel("Ingrese su nombre:"));
	    panel.add(txtNombre);
	    panel.add(new JLabel("Ingrese su rut"));
	    panel.add(txtRut);
	    panel.add(new JLabel("Ingrese su Email"));
	    panel.add(txtEmail);
	    panel.add(new JLabel("ingrese su Teléfono"));
	    panel.add(txtNumero);
	    
	    int result = JOptionPane.showConfirmDialog(
	            visualFrame,   // ventana padre que vamos a usar
	            panel, 
	            "Registrar Usuario", 
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE
	    );
	    
	    if (result == JOptionPane.CANCEL_OPTION) return;
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String email = txtEmail.getText().trim();
        String numero = txtNumero.getText().trim();

        if (nombre.isEmpty() || rut.isEmpty() || email.isEmpty() || numero.isEmpty()) {
        	JOptionPane.showMessageDialog(
                    registrarFrame, 
                    "Debe ingresar todos los campos", 
                    "Error", 
                    JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
                );
        	return;
        }
    	
    	//EXCEPCIONES
    	try {
            ValidadorRut.validarRut(rut);
            ValidadorNombre.validarNombre(nombre);
            ValidadorEmail.validarEmail(email);
            ValidadorTelefono.validarTelefono(numero);
        } catch (RutInvalidoException ex) {
        	JOptionPane.showMessageDialog(
	                registrarFrame, 
	                ex.getMessage(), 
	                "Error", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
            return;
        } catch (NombreInvalidoException ex) {
        	JOptionPane.showMessageDialog(
	                registrarFrame, 
	                ex.getMessage(), 
	                "Error", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
            return;
        } catch (EmailInvalidoException ex) {
        	JOptionPane.showMessageDialog(
	                registrarFrame, 
	                ex.getMessage(), 
	                "Error", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
            return;
        } catch (TelefonoInvalidoException ex) {
        	JOptionPane.showMessageDialog(
	                registrarFrame, 
	                ex.getMessage(), 
	                "Error", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
            return;
        }
    	
    	Comprador nuevoUsuario = new Comprador(rut, nombre, email, numero);
    	try {
			controlador.generarNuevoRegistroUsuario(nuevoUsuario, edificioSel, deptoSel, b);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(
			        registrarFrame,
			        "Error con la DB: " + e,
			        "Error al Registrar",
			        JOptionPane.PLAIN_MESSAGE,
			        likeRed
			);
		}
        
        JOptionPane.showMessageDialog(
		        registrarFrame,
		        "Datos ingresados correctamente!",
		        "RUT Correcto",
		        JOptionPane.PLAIN_MESSAGE,
		        likeRed
		);
        
        cargarDepartamentosEnTabla(edificioSel);
	}

	private void cargarDepartamentosEnTabla(Edificio edificioSel) {
		// Limpiamos la tabla antes
	    defaultDepa.setRowCount(0);
	    if (edificioSel.getDepartamentos() == null) return;

	    for (Departamento d : edificioSel.getDepartamentos()) {
	        Object[] fila = {
	            d.getCodigo(),
	            d.getNumeroPiso(),
	            d.getMetrosCuadrados(),
	            d.getHabitaciones(),
	            d.getBanos(),
	            d.getEstado().toString(),
	            d.getGestorPrecios().getPrecioActual()
	        };
	        defaultDepa.addRow(fila);
	    }
	    
	    tablaDepartamento.clearSelection();
	}

	public void solicitarVerificacionUsuario(Edificio edificioSel, Departamento deptoSel) {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		
		JTextField txtRut = new JTextField(20);
		panel.add(new JLabel("Ingrese su RUT"));
	    panel.add(txtRut);
	    
	    int result = JOptionPane.showConfirmDialog(
	            visualFrame,   // ventana padre que vamos a usar
	            panel, 
	            "Verificar Usuario", 
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE
	    );
	    if (result == JOptionPane.CANCEL_OPTION) return;
	    
        String rut = txtRut.getText().trim();
        
        //EXCEPCIÓN 
        try {
            ValidadorRut.validarRut(rut);
        } catch (RutInvalidoException ex) {
        	JOptionPane.showMessageDialog(
	                registrarFrame, 
	                ex.getMessage(),
	                "Error con el RUT", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
        	return;
        }
    	
    	controlador.confirmarVerificacionUsuario(rut, deptoSel, edificioSel);	
		cargarDepartamentosEnTabla(edificioSel);
	}

	public boolean mostrarDialogoRecibo() {
		int result = JOptionPane.showConfirmDialog(
	            visualFrame,
	            "¿Desea recibo?", 
	            "Compra Realizada",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.YES_OPTION) {
			return true;
		} else return false;
	}

	public void mostrarMensajeRutInvalido() {
		JOptionPane.showMessageDialog(
                registrarFrame, 
                "El RUT ingresado es incorrecto.", 
                "Error", 
                JOptionPane.PLAIN_MESSAGE,
		        sadRed
            );
	}

	public void mostrarExitoRutCorrecto() {
		JOptionPane.showMessageDialog(
		        registrarFrame,
		        "Exito",
		        "RUT Correcto",
		        JOptionPane.PLAIN_MESSAGE,
		        likeRed
		);
	}

	public void cerrarVentanaRegistrar() {
		registrarFrame.dispose();
	}

	public Edificio solicitarDatosNuevoEdificio() {
		JTextField txtNombre = new JTextField(20);
	    JTextField txtDireccion = new JTextField(20);
	    
	    //esto e brijido
	    // Radio buttons para Piscina// los puntitos :D
	    JRadioButton puntoPiscinaSi = new JRadioButton("Sí");
	    JRadioButton puntoPiscinaNo = new JRadioButton("No"); 
	    ButtonGroup grupoPiscina = new ButtonGroup();
	    grupoPiscina.add(puntoPiscinaSi);
	    grupoPiscina.add(puntoPiscinaNo);
	    
	    JPanel panelPiscina = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    panelPiscina.add(new JLabel("Piscina:"));
	    panelPiscina.add(puntoPiscinaSi);
	    panelPiscina.add(puntoPiscinaNo);
	    
	    
	    //Lo mismo pero para el estacionamiento
	    JRadioButton puntoEstacionamientoSi = new JRadioButton("Sí");
	    JRadioButton puntoEstacionamientoNo = new JRadioButton("No"); 
	    ButtonGroup grupoEstacionamiento = new ButtonGroup();
	    grupoEstacionamiento.add(puntoEstacionamientoSi);
	    grupoEstacionamiento.add(puntoEstacionamientoNo);
	    
	    JPanel panelEstacionamiento = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    panelEstacionamiento.add(new JLabel("Estacionamiento:"));
	    panelEstacionamiento.add(puntoEstacionamientoSi);
	    panelEstacionamiento.add(puntoEstacionamientoNo);
	    
	    
	    //Creamos el panel
	    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
	    panel.add(new JLabel("Nombre del Edificio:"));
	    panel.add(txtNombre);
	    panel.add(new JLabel("Dirección:"));
	    panel.add(txtDireccion);
	    panel.add(panelPiscina);
	    panel.add(panelEstacionamiento);
	    
	    // Mostrar ventana con los inputs
	    int result = JOptionPane.showConfirmDialog(
	            registrarFrame,   // ventana padre que vamos a usar
	            panel, 
	            "Registrar Edificio", 
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE
	    );
	    if (result == JOptionPane.CANCEL_OPTION) return null;
	    
	    String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        boolean tienePiscina = puntoPiscinaSi.isSelected();
        boolean tieneEstacionamiento = puntoEstacionamientoSi.isSelected();
        
        if (!nombre.isEmpty() && !direccion.isEmpty()) {
            long idTemporal = -System.currentTimeMillis();
            Edificio nuevoEdificio = new Edificio(idTemporal, nombre, direccion, tienePiscina, tieneEstacionamiento);
            return nuevoEdificio; 
        } else {
        	JOptionPane.showMessageDialog(
                    registrarFrame, 
                    "Debe ingresar todos los campos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
        }
		return null;
	}
	
	public Departamento solicitarDatosNuevoDepartamento() {
		JTextField txtCodigo = new JTextField(20);
        JTextField txtEstado = new JTextField("DISPONIBLE");//inicializamos el depa en disponbible

        // spinner funciona así
        // SpinnerNumberModel(valorInicial, minimo, maximo, paso);

        // Piso con JSpinner (ej: de 1 a 50)
        SpinnerNumberModel pisoModel = new SpinnerNumberModel(1, 1, 65, 1);
        JSpinner spinnerPiso = new JSpinner(pisoModel);

        // Metros cuadrados con JSpinner 
        SpinnerNumberModel metrosModel = new SpinnerNumberModel(10, 10, 140, 2);
        JSpinner spinnerMetros = new JSpinner(metrosModel);

        // Habitacion con JSpinner 
        SpinnerNumberModel habitacionesModel = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner spinnerHabitaciones = new JSpinner(habitacionesModel);

        // Baños con JSpinner 
        SpinnerNumberModel banosModel = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner spinnerBanos = new JSpinner(banosModel);

        // Precio con JSpinner 
        SpinnerNumberModel precioModel = new SpinnerNumberModel(1000, 1000, 100000, 50);
        JSpinner spinnerPrecio = new JSpinner(precioModel);

        // Creamos un panel
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Código:"));
        panel.add(txtCodigo);
        panel.add(new JLabel("Piso:"));
        panel.add(spinnerPiso);
        panel.add(new JLabel("Metros²:"));
        panel.add(spinnerMetros);
        panel.add(new JLabel("Habitaciones:"));
        panel.add(spinnerHabitaciones);
        panel.add(new JLabel("Baños:"));
        panel.add(spinnerBanos);
        panel.add(new JLabel("Estado:"));
        panel.add(txtEstado); 
        panel.add(new JLabel("Precio:"));
        panel.add(spinnerPrecio);

        int result = JOptionPane.showConfirmDialog(
            registrarFrame,
            panel,
            "Registrar Departamento",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        if (result == JOptionPane.CANCEL_OPTION) return null;
        
        String codigo 	= txtCodigo.getText().trim();
        int piso 		= ((Number) spinnerPiso.getValue()).intValue();
        int metros 		= ((Number) spinnerMetros.getValue()).intValue();
        int habitaciones = ((Number) spinnerHabitaciones.getValue()).intValue();
        int banos 		= ((Number) spinnerBanos.getValue()).intValue();
        EstadoDepartamento estado 	= EstadoDepartamento.valueOf(txtEstado.getText().trim());
        int precio 		= ((Number) spinnerPrecio.getValue()).intValue();

        if (codigo.isEmpty()) {
        	JOptionPane.showMessageDialog(
                    registrarFrame,
                    "Debe ingresar todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
        	return null;
        }
        
        // creamos el objeto departamento (todavía no en DB)
        Departamento nuevoDepartemento = new Departamento(
        	-System.currentTimeMillis(), 
            codigo, 
            piso, 
            metros, 
            habitaciones, 
            banos, 
            estado, 
            precio, 
            precio, 
            null);
        
        return nuevoDepartemento;
	}

	public void actualizarTablaEdificiosRegistrar(List<Edificio> edificios) {
	    defaultEdi.setRowCount(0);
	    
	    for (Edificio e : edificios) {
	    	Object[] fila = {
	                e.getId(),
	                e.getNombre(),
	                e.getInformacion().getDireccion(),
	                e.getInformacion().isTienePiscina() ? "Sí" : "No",
	                e.getInformacion().isTieneEstacionamiento() ? "Sí" : "No"
	    		};
	    	defaultEdi.addRow(fila);
	    }
	}

	public void mostrarMensajeExito(String mensaje) {
		JOptionPane.showMessageDialog(
		        registrarFrame, // O el frame que corresponda
		        mensaje,
		        "Éxito",
		        JOptionPane.INFORMATION_MESSAGE,
		        likeRed // Usando tus íconos personalizados
		    );
	}

	public long getSelectedProyectoId() {
		int filaSeleccionada = tablaProyecto.getSelectedRow();
	    if (filaSeleccionada == -1) return -1;
	    
	    return (long) defaultMain.getValueAt(filaSeleccionada, 0);
	}

	public void cerrarVentanaModificar() {
	    modificarFrame.dispose();
	}

	public void actualizarTablaDepartamentosModificar(List<Departamento> departamentos) {
	    defaultDepaModificar.setRowCount(0); // Usa el modelo de modificar
	    for (Departamento d : departamentos) {
	    	Object[] fila = {
		            d.getCodigo(),
		            d.getNumeroPiso(),
		            d.getMetrosCuadrados(),
		            d.getHabitaciones(),
		            d.getBanos(),
		            d.getEstado().toString(),
		            d.getGestorPrecios().getPrecioActual()
		        };
	    	
	    	defaultDepaModificar.addRow(fila);
	    }
	}

	public Edificio solicitarDatosModificarEdificio(Edificio edificioAModificar) {
		JPanel panelModificar = new JPanel(new GridLayout(0, 2, 10, 10));
        
        JTextField txtNombre = new JTextField(edificioAModificar.getNombre(), 20);
        JTextField txtDireccion = new JTextField(edificioAModificar.getInformacion().getDireccion(), 20);

        JRadioButton rbPiscinaSi = new JRadioButton("Sí", edificioAModificar.getInformacion().isTienePiscina());
        JRadioButton rbPiscinaNo = new JRadioButton("No", !edificioAModificar.getInformacion().isTienePiscina());
        ButtonGroup grupoPiscina = new ButtonGroup();
        grupoPiscina.add(rbPiscinaSi);
        grupoPiscina.add(rbPiscinaNo);
        JPanel panelPiscina = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPiscina.add(new JLabel("Piscina:"));
        panelPiscina.add(rbPiscinaSi);
        panelPiscina.add(rbPiscinaNo);

        JRadioButton rbEstacionamientoSi = new JRadioButton("Sí", edificioAModificar.getInformacion().isTieneEstacionamiento());
        JRadioButton rbEstacionamientoNo = new JRadioButton("No", !edificioAModificar.getInformacion().isTieneEstacionamiento());
        ButtonGroup grupoEstacionamiento = new ButtonGroup();
        grupoEstacionamiento.add(rbEstacionamientoSi);
        grupoEstacionamiento.add(rbEstacionamientoNo);
        JPanel panelEstacionamiento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstacionamiento.add(new JLabel("Estacionamiento:"));
        panelEstacionamiento.add(rbEstacionamientoSi);
        panelEstacionamiento.add(rbEstacionamientoNo);

        panelModificar.add(new JLabel("Nombre del Edificio:"));
        panelModificar.add(txtNombre);
        panelModificar.add(new JLabel("Dirección:"));
        panelModificar.add(txtDireccion);
        panelModificar.add(panelPiscina);
        panelModificar.add(panelEstacionamiento);

        int result = JOptionPane.showConfirmDialog(modificarFrame, panelModificar, "Modificar Edificio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // 3. Obtener los nuevos valores y actualizar el objeto
            String nuevoNombre = txtNombre.getText().trim();
            String nuevaDireccion = txtDireccion.getText().trim();
            boolean nuevaPiscina = rbPiscinaSi.isSelected();
            boolean nuevoEstacionamiento = rbEstacionamientoSi.isSelected();
            
            edificioAModificar.setNombre(nuevoNombre);
            edificioAModificar.getInformacion().setDireccion(nuevaDireccion);
            edificioAModificar.getInformacion().setTienePiscina(nuevaPiscina);
            edificioAModificar.getInformacion().setTieneEstacionamiento(nuevoEstacionamiento);
            return edificioAModificar;
        }
        
        return null;
	}

	public Departamento solicitarDatosModificarDepartamento(Departamento deptoAModificar) {
		JTextField txtCodigo = new JTextField(deptoAModificar.getCodigo(), 20);
        txtCodigo.setEditable(false);
        
     // --- SPINNERS CON RANGOS UNIFICADOS ---
        int piso = clamp(deptoAModificar.getNumeroPiso(), 1, 65);
        SpinnerNumberModel pisoModel = new SpinnerNumberModel(piso, 1, 65, 1);
        JSpinner spinnerPiso = new JSpinner(pisoModel);

        double metros = clamp(deptoAModificar.getMetrosCuadrados(), 10, 140);
        SpinnerNumberModel metrosModel = new SpinnerNumberModel(metros, 10, 140, 2);
        JSpinner spinnerMetros = new JSpinner(metrosModel);

        int habitaciones = clamp(deptoAModificar.getHabitaciones(), 1, 5);
        SpinnerNumberModel habitacionesModel = new SpinnerNumberModel(habitaciones, 1, 5, 1);
        JSpinner spinnerHabitaciones = new JSpinner(habitacionesModel);

        int banos = clamp(deptoAModificar.getBanos(), 1, 5);
        SpinnerNumberModel banosModel = new SpinnerNumberModel(banos, 1, 5, 1);
        JSpinner spinnerBanos = new JSpinner(banosModel);

        double precio = clamp(deptoAModificar.getGestorPrecios().getPrecioActual(), 1000, 100000);
        SpinnerNumberModel precioModel = new SpinnerNumberModel(precio, 1000, 100000, 50);
        JSpinner spinnerPrecio = new JSpinner(precioModel);

        JPanel panelModificar = new JPanel(new GridLayout(0, 2, 10, 10));
	    panelModificar.add(new JLabel("Código (no editable):")); panelModificar.add(txtCodigo);
	    panelModificar.add(new JLabel("Piso:")); panelModificar.add(spinnerPiso);
	    panelModificar.add(new JLabel("Metros²:")); panelModificar.add(spinnerMetros);
	    panelModificar.add(new JLabel("Habitaciones:")); panelModificar.add(spinnerHabitaciones);
	    panelModificar.add(new JLabel("Baños:")); panelModificar.add(spinnerBanos);
	    panelModificar.add(new JLabel("Precio:")); panelModificar.add(spinnerPrecio);
        
        int result = JOptionPane.showConfirmDialog(modificarFrame, panelModificar, "Modificar Departamento", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // MEJORA: Obtener valores de forma más segura
            int nuevoPiso = (int) spinnerPiso.getValue();
            double nuevosMetros = ((Number) spinnerMetros.getValue()).doubleValue();
            int nuevasHabitaciones = (int) spinnerHabitaciones.getValue();
            int nuevosBanos = (int) spinnerBanos.getValue();
            double nuevoPrecio = ((Number) spinnerPrecio.getValue()).doubleValue();
            
            deptoAModificar.setNumeroPiso(nuevoPiso);
            deptoAModificar.setMetrosCuadrados(nuevosMetros);
            deptoAModificar.setHabitaciones(nuevasHabitaciones);
            deptoAModificar.setBanos(nuevosBanos);
            deptoAModificar.getGestorPrecios().setPrecio(nuevoPrecio);
            
            return deptoAModificar;
        }
        
        return null;
	}

	public FiltroBusqueda getFiltroDelBusquedaVisual() {
	    Double precioMin = null;
	    Double precioMax = null;
	    try {
	        if (!txtPrecioMinBusqueda.getText().trim().isEmpty()) {
	            precioMin = Double.parseDouble(txtPrecioMinBusqueda.getText().trim());
	        }
	        if (!txtPrecioMaxBusqueda.getText().trim().isEmpty()) {
	            precioMax = Double.parseDouble(txtPrecioMaxBusqueda.getText().trim());
	        }
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(buscarFrame, "El precio debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
	        return null; // Devolvemos null para indicar que hubo un error.
	    }

	    Integer habitaciones = (Integer) spinnerHabBusqueda.getValue();
	    Integer banios = (Integer) spinnerBaniosBusqueda.getValue();
	    
	    EstadoDepartamento estado = null;
	    if (comboEstadoBusqueda.getSelectedItem() != null && !comboEstadoBusqueda.getSelectedItem().toString().isEmpty()) {
	        estado = EstadoDepartamento.valueOf(comboEstadoBusqueda.getSelectedItem().toString());
	    }

	    String direccion = txtDireccionBusqueda.getText().trim();
	    Boolean conPiscina = chkPiscinaBusqueda.isSelected();
	    Boolean conEstacionamiento = chkEstacionamientoBusqueda.isSelected();

	    return new FiltroBusqueda(precioMin, precioMax, habitaciones, banios, estado, conPiscina, conEstacionamiento, direccion);
	}

	public void verOpcionEliminar(ProyectoInmobiliario proyecto) {
		int result = JOptionPane.showConfirmDialog(
                modificarFrame,
                "¿Desea Eliminar el Proyecto " + proyecto.getNombreProyecto()+"?", 
                "Eliminar Proyecto",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                quesRed); 
        
        if (result == JOptionPane.YES_OPTION) {
        	JOptionPane.showConfirmDialog(
        			modificarFrame,
        			"Proyecto " + proyecto.getNombreProyecto() + " eliminado correctamente", 
        			"Eliminar Proyecto",
        			JOptionPane.DEFAULT_OPTION,
        			JOptionPane.PLAIN_MESSAGE,
        			vanishRed); 
        	
        	controlador.eliminarProyecto(proyecto);
        }
	}
}