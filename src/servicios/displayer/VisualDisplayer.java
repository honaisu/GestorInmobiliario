package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.FlowLayout; //pa botones?
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton; //boton de puntito
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;


import javax.swing.SwingConstants;
import javax.swing.Timer;

import gestor.DatabaseManager;
import gestor.FiltroBusqueda;
import gestor.GestorInmobiliarioService;
import gestor.ProyectoInmobiliario;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.EstadoDepartamento;

/**
 * Clase encargada de mostrar todos los componentes visuales referentes
 * a la interfaz gráfica de nuestro programa (implementada con Java Swing).
 */
public class VisualDisplayer {
	private static JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
	private JFrame registrarFrame = new JFrame("Registrar Proyecto");
	private JFrame buscarFrame = new JFrame("Filtrar Edificios");
	private JFrame visualFrame;
	
	private DefaultTableModel defaultMain;
	private JTable tablaProyecto;
	
	private DefaultTableModel defaultEdi;
	private JTable tablaEdificio;
	
	private DefaultTableModel defaultDepa;
	private JTable tablaDepartamento;
	private JTable tablaDepartamentosFiltrados;
	
	private JButton reservarBoton;
	private JButton verBoton;
	private JButton comprarBoton;
	private JButton eliminarBoton;
	
	//Botones para el registrarProyecto
	private JButton botonRemoverE;
	private JButton botonRemoverD;
	private JButton botonAgregarE;
	private JButton botonAgregarD;
	//private JButton botonRegistrar;
	
	
	
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
	
	private void accionOpcionesProyecto(OpcionesProyecto opcion) {
		switch (opcion) {
		case VER: {
			mainFrame.setVisible(false);
			verProyectoPanel();
			break;
		}
		case REGISTRAR: {
			mainFrame.setVisible(false);
			registrarProyectoPanel();
			break;
		}
		case BUSCAR: {
			mainFrame.setVisible(false);
			buscarEdificioPanel();
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
	
	
	//----------------------
	//	Frame Ver Proyecto
	//----------------------
	
	private void verProyectoPanel() {
		int filaSeleccionada = tablaProyecto.getSelectedRow();
		String titulo = tablaProyecto.getValueAt(filaSeleccionada, 1).toString();
		
		visualFrame = new JFrame("Ver Proyecto");
		//visualFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		visualFrame.setResizable(false);
		//visualFrame.setPreferredSize(new Dimension(400, 300));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		JPanel headerPanel = verHeaderPanel(titulo);
		JPanel opcionesPanel = verOpcionesPanel();
		JPanel proyectorPanel = verProyectorPanel(filaSeleccionada);
		
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		mainPanel.add(proyectorPanel, BorderLayout.WEST);
		
		visualFrame.add(mainPanel);
		visualFrame.pack();
		visualFrame.setLocationRelativeTo(null);
		visualFrame.setVisible(true);
		
		visualFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	mainFrame.setVisible(true);
		        
		    }
		});
		
	}
	
	private JPanel verHeaderPanel(String tit) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 50));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
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
			//TODO imprimir recibo como txt para 
			break;
		}
		case RESERVAR:{
			int filaSelDepa = tablaDepartamento.getSelectedRow();
			int filaSelEdi = tablaEdificio.getSelectedRow();
			
			
			break;
		}
		case SALIR:{
			visualFrame.dispose();
			mainFrame.setVisible(true);
			break;
		}
		
		}
		
	}
	
	public void cargarEdificiosEnTabla(int filaSel) {
		String id = defaultMain.getValueAt(filaSel, 0).toString();
		long idProyectoSeleccionado = Long.parseLong(id);
		
		ProyectoInmobiliario proyectoSeleccionado = gestorService.getDatabaseManager()
												.getMapProyectos()
												.get(idProyectoSeleccionado);
		
		for (Edificio e : proyectoSeleccionado.getEdificios()) {
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
	
	private void cargarDepartamentosEnTabla(Edificio edificio) {
	    // Limpiamos la tabla antes
	    defaultDepa.setRowCount(0);

	    for (Departamento d : edificio.getDepartamentos()) {
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
	    
	    comprarBoton.setEnabled(false);
        reservarBoton.setEnabled(false);
	}

	
	
	private JPanel verProyectorPanel(int filaSel) {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		//panel.setPreferredSize(new Dimension(500, 200));
		
		// Tabla Edificio.
		String[] ediCols = {"ID", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
		this.defaultEdi = new DefaultTableModel(ediCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		this.tablaEdificio = new JTable(defaultEdi);
		
		cargarEdificiosEnTabla(filaSel);
		
		// Para añadir funcionalidad al elegir una fila
		tablaEdificio.getSelectionModel().addListSelectionListener(lambda -> {
	        // Este código se ejecuta CADA VEZ que la selección cambia.
			//tablaDepartamento.clearSelection();
			if (!lambda.getValueIsAdjusting()) {
				
				int filaSelEdi = tablaEdificio.getSelectedRow();
		        if (filaSelEdi == -1) return;
		        
	            // Recuperamos el ID del edificio desde la tabla
	            long idEdificio = (long) defaultEdi.getValueAt(filaSelEdi, 0);
	            
	            // Buscar el edificio en caché
	            
	            Edificio edificioSel = gestorService.getMapEdificios()
	            					.get(idEdificio);
	            
	            if (edificioSel != null) {
	                cargarDepartamentosEnTabla(edificioSel);
	            }
			}
		});
		
		//Tabla Departamento
		String[] DepaCols = {"Código", "Piso", "metros^2", "Habitaciones", "Baños", "Estado", "Precio"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		this.tablaDepartamento = new JTable(defaultDepa);
		
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
	private void registrarProyectoPanel() {
		
		LinkedList<Edificio> listaEdi = new LinkedList<Edificio>();
		
		registrarFrame.setResizable(false);
		//visualFrame.setPreferredSize(new Dimension(400, 300));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		//ver si agrego una parte derecha y izquierda de cada cosa
		//JPanel headerPanelDerecho = verHeaderPanel(filaSeleccionada);// una cosa así
		JPanel headerPanel = registrarHeaderPanel();//arriba
		JPanel opcionesPanel = registrarOpcionesPanel();//abajo
		JPanel proyectorPanel = registrarProyectorPanel(); //parte "central"
		
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		mainPanel.add(opcionesPanel, BorderLayout.SOUTH);
		
		registrarFrame.add(mainPanel);
		registrarFrame.pack();
		registrarFrame.setLocationRelativeTo(null);
		registrarFrame.setVisible(true);
		
		registrarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	mainFrame.setVisible(true);
		        
		    }
		});
		
	}
	
	private JPanel registrarHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout()); 
		panel.setPreferredSize(new Dimension(600, 150));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel titulo = new JLabel("Registrar", SwingConstants.CENTER); //centramos el titulo
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		panel.add(titulo, BorderLayout.NORTH);
		
		///Parte central
		JPanel panelCentral = new JPanel(new BorderLayout(5,5));
		
		// Panel de inputs en el centro
	    JPanel datosPanel = new JPanel(new GridLayout(2, 2, 10, 10));
	    datosPanel.add(new JLabel("Nombre: "));
	    JTextField txtNombre = new JTextField(25);
	    datosPanel.add(txtNombre);

	    datosPanel.add(new JLabel("Vendedor: "));
	    JTextField txtRut = new JTextField(25);
	    datosPanel.add(txtRut);

	    panelCentral.add(datosPanel, BorderLayout.WEST);
	    
	    
	    
	    ///Parte derecha con la fecha (no se si implementemos hora)
	    JLabel panelHora = new JLabel("Fecha Ingreso: ");
	    panelHora.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
	    panelHora.setHorizontalAlignment(SwingConstants.RIGHT);
	    panelHora.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	    panelCentral.add(panelHora, BorderLayout.EAST);

	    // Timer para actualizar la hora cada segundo (aunque solo ocupamos la fecha)
	    Timer timer = new Timer(1000, e -> {
	        LocalDateTime now = LocalDateTime.now();
	        panelHora.setText("Fecha Ingreso: " + now.format(
	            DateTimeFormatter.ofPattern("dd-MM-yy")
	        ));
	    });
	    timer.start();

	    panel.add(panelCentral, BorderLayout.CENTER);
		
		
	    //Marca de agua abajo
		JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel; 
	}
	
	private JPanel registrarOpcionesPanel() {
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
	    botonRemoverD.setEnabled(false); //parte "desabilitado" ya que todavia no seleccionamos una fila
	    
	    panelDepartamento.add(botonAgregarD);
	    panelDepartamento.add(botonRemoverD);

	    panelCentral.add(panelEdificio);
	    panelCentral.add(panelDepartamento);

	    // Panel inferior para botones Registrar y Salir
	    JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JButton botonRegistrar = new JButton(OpcionesRegistrar.REGISTRAR.getNombre());
	    JButton botonSalir = new JButton(OpcionesRegistrar.SALIR.getNombre());
	    panelInferior.add(botonRegistrar);
	    panelInferior.add(botonSalir);

	    // Armar todo
	    panel.add(panelCentral, BorderLayout.CENTER);
	    panel.add(panelInferior, BorderLayout.SOUTH);

	    // --- Listeners --- //esto todavia ni lo entiendo, pero creo que es como el for de benjoid
	    botonAgregarE.addActionListener(e -> accionOpcionesRegistrar(OpcionesRegistrar.AGREGAR_E));
	    botonRemoverE.addActionListener(e -> accionOpcionesRegistrar(OpcionesRegistrar.REMOVER_E));
	    botonAgregarD.addActionListener(e -> accionOpcionesRegistrar(OpcionesRegistrar.AGREGAR_D));
	    botonRemoverD.addActionListener(e -> accionOpcionesRegistrar(OpcionesRegistrar.REMOVER_D));
	    botonSalir.addActionListener(e -> accionOpcionesRegistrar(OpcionesRegistrar.SALIR));
	    botonRegistrar.addActionListener(e -> accionOpcionesRegistrar(OpcionesRegistrar.REGISTRAR));
	    
	    return panel;
	}
	
	private void accionOpcionesRegistrar(OpcionesRegistrar opcion) {
		switch (opcion) {
		case AGREGAR_E:{
			//Campos para agregar Nombre y direccion;
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
		    
		 // Si el usuario presionó OK
		    if (result == JOptionPane.OK_OPTION) {
		        String nombre = txtNombre.getText().trim();
		        String direccion = txtDireccion.getText().trim();
		        String piscina = puntoPiscinaSi.isSelected() ? "Sí" : "No";
		        String estacionamiento = puntoEstacionamientoSi.isSelected() ? "Sí" : "No";

		        if (!nombre.isEmpty() && !direccion.isEmpty()) {
		            Object[] nuevaFila = {
		                defaultEdi.getRowCount() + 1,
		                nombre,
		                direccion,
		                piscina,  
		                estacionamiento   
		            };
		            defaultEdi.addRow(nuevaFila);
		        } else {
		            JOptionPane.showMessageDialog(
		                registrarFrame, 
		                "Debe ingresar todos los campos", 
		                "Error", 
		                JOptionPane.ERROR_MESSAGE
		            );
		        }
		    }
			break;
		}
		case REMOVER_E:{
			//Pruebas
			int fila = tablaEdificio.getSelectedRow();
		    if (fila != -1) {
		        defaultEdi.removeRow(fila);
		    }
			break;
		}
		case AGREGAR_D:{
			//es lo mismo que en lo de edifico pero en depa, pero igual diferente
			
			break;
		}
		case REMOVER_D:{
			int fila = tablaDepartamento.getSelectedRow();
		    if (fila != -1) {
		        defaultDepa.removeRow(fila);
		    }
			break;
		}
		
		case REGISTRAR:{ //falta la logica para agregarlo a la hora de Registrar
			
			
			break;
		}
		
		
		case SALIR:{
			mainFrame.setVisible(true);
			registrarFrame.dispose();
			break;
		}
		
		}
		
	}
	
	
	private JPanel registrarProyectorPanel() {
		
		
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(900, 350));
				
		
		// Tabla Edificio.
		String[] ediCols = {"ID", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
		this.defaultEdi = new DefaultTableModel(ediCols, 0);
		this.tablaEdificio = new JTable(defaultEdi);
		
		// Para añadir funcionalidad al elegir una fila
		tablaEdificio.getSelectionModel().addListSelectionListener(lambda -> {
		    if (!lambda.getValueIsAdjusting()) {
		        boolean filaSeleccionada = tablaEdificio.getSelectedRow() != -1;
		        botonRemoverE.setEnabled(filaSeleccionada);
		    }
		});
		
		// Encargado de mostrar la barrita vertical.
		JScrollPane scrollEdi = new JScrollPane(
				this.tablaEdificio,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		
		/// Tabla Departamento ///
		String[] DepaCols = {"Código", "Piso", "metros^2", "Estado", "Precio"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0);
		this.tablaDepartamento = new JTable(defaultDepa);
		
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
		///------------------------///
	}
	
	
	
	//-----------------------------
	//	Frame Busqueda por Filtro
	//-----------------------------
	
	
	private void buscarEdificioPanel() {
		buscarFrame.setResizable(false);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel headerPanel = buscarHeaderPanel();
		JPanel proyectorPanel = buscarProyectorPanel();
		JPanel opcionesPanel = buscarFiltrosPanel();
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		
		buscarFrame.add(mainPanel);
		buscarFrame.pack();
		buscarFrame.setLocationRelativeTo(null);
		buscarFrame.setVisible(true);
		
		buscarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	mainFrame.setVisible(true);
		        
		    }
		});
	}
	
	private JPanel buscarHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 50));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		JLabel titulo = new JLabel("Filtrar Edificios", JLabel.LEFT);
		titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");

		panel.add(titulo, BorderLayout.CENTER);
		panel.add(marcaAgua, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void cargarDepartamentosEnTabla(List<Departamento> listaDepartamentos) {
	    // Limpiar la tabla primero
	    defaultDepa.setRowCount(0);

	    // Recorrer la lista y añadir filas
	    for (Departamento d : listaDepartamentos) {
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
	}
	
	
	private JPanel buscarProyectorPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		
		//Tabla Departamento
		String[] DepaCols = {"Código", "Piso", "metros^2", "Habitaciones", "Baños", "Estado", "Precio"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		
		this.tablaDepartamentosFiltrados = new JTable(defaultDepa);
		
		for (int i = 0; i < tablaDepartamentosFiltrados.getColumnCount(); i++) {
			tablaDepartamentosFiltrados.getColumnModel().getColumn(i).setResizable(false);
		}
		
		tablaDepartamentosFiltrados.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
		tablaDepartamentosFiltrados.getColumnModel().getColumn(1).setPreferredWidth(40);  // Piso
		tablaDepartamentosFiltrados.getColumnModel().getColumn(2).setPreferredWidth(100); // Metros^2
		tablaDepartamentosFiltrados.getColumnModel().getColumn(3).setPreferredWidth(90);  // Habitaciones
		tablaDepartamentosFiltrados.getColumnModel().getColumn(4).setPreferredWidth(70);  // Baños
		tablaDepartamentosFiltrados.getColumnModel().getColumn(5).setPreferredWidth(100); // Estado
		tablaDepartamentosFiltrados.getColumnModel().getColumn(6).setPreferredWidth(60); // Precio
		
		JScrollPane scrollDepa = new JScrollPane(
				this.tablaDepartamentosFiltrados,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollDepa);
		return panel;
	}
	
	private JPanel buscarFiltrosPanel() {
		JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setPreferredSize(new Dimension(250, 100));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // --- Precio ---
	    JPanel precioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    precioPanel.add(new JLabel("Precio:"), BorderLayout.NORTH);
	    JTextField txtPrecioMin = new JTextField(6);
	    JTextField txtPrecioMax = new JTextField(6);
	    precioPanel.add(txtPrecioMin);
	    precioPanel.add(new JLabel("-"));
	    precioPanel.add(txtPrecioMax);

	    // --- Habitaciones ---
	    JPanel habPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    habPanel.add(new JLabel("Habitaciones:"));
	    JSpinner spinnerHab = new JSpinner(new SpinnerNumberModel(1, 0, 5, 1));
	    habPanel.add(spinnerHab);

	    // --- Baños ---
	    JPanel banioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    banioPanel.add(new JLabel("Baños:"));
	    JSpinner spinnerBanios = new JSpinner(new SpinnerNumberModel(1, 0, 5, 1));
	    banioPanel.add(spinnerBanios);

	    // --- Estado ---
	    JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    estadoPanel.add(new JLabel("Estado:"));
	    JComboBox<String> comboEstado = new JComboBox<>(new String[]{null,"DISPONIBLE", "RESERVADO", "VENDIDO"});
	    estadoPanel.add(comboEstado);

	    // --- Dirección ---
	    JPanel dirPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    dirPanel.add(new JLabel("Dirección:"));
	    JTextField txtDireccion = new JTextField(10);
	    dirPanel.add(txtDireccion);

	    // --- Extras ---
	    JPanel extrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    JCheckBox chkPiscina = new JCheckBox("Piscina");
	    JCheckBox chkEstacionamiento = new JCheckBox("Estacionamiento");
	    extrasPanel.add(chkPiscina);
	    extrasPanel.add(chkEstacionamiento);

	    // --- Botón de búsqueda ---
	    JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JButton btnBuscar = new JButton("Buscar");
	    botonPanel.add(btnBuscar);
	    
	    
	    //Obtener Valores
	    btnBuscar.addActionListener(e -> {
	        // Aquí va el código cuando se hace click en el botón
	    	System.out.flush(); 
	        // Precio
	        Double precioMin = null;
	        Double precioMax = null;
	        try {
	            if (!txtPrecioMin.getText().trim().isEmpty()) {
	                precioMin = Double.parseDouble(txtPrecioMin.getText().trim());
	            }
	            if (!txtPrecioMax.getText().trim().isEmpty()) {
	                precioMax = Double.parseDouble(txtPrecioMax.getText().trim());
	            }
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(panel, "El precio debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
	            return; // salir si hay error
	        }

	        // Habitaciones y baños
	        Integer habitaciones = (Integer) spinnerHab.getValue();
	        Integer banios = (Integer) spinnerBanios.getValue();

	        // Estado
	        EstadoDepartamento estado;
	        if (comboEstado.getSelectedItem() == null) {
	        	estado = null;
	        	
	        }else {
	        	estado = EstadoDepartamento.valueOf(comboEstado.getSelectedItem().toString());
	        }
	        

	        // Dirección
	        String direccion = txtDireccion.getText().trim();

	        // Extras
	        Boolean conPiscina = chkPiscina.isSelected();
	        Boolean conEstacionamiento = chkEstacionamiento.isSelected();

	        // --- Probar ---
	        FiltroBusqueda filtro = new FiltroBusqueda(precioMin, precioMax, habitaciones, banios,
					estado, conPiscina, conEstacionamiento, direccion);
		    
		    DatabaseManager database = gestorService.getDatabaseManager();
		    
		    List<Departamento> listaDepas = database.getDepartamentosPorFiltro(filtro);
		    cargarDepartamentosEnTabla(listaDepas);

	    });

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
	
	
}
