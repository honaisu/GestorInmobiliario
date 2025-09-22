package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout; //pa botones?
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton; 
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton; //boton de puntito
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

import gestor.GestorInmobiliarioService;
import gestor.ProyectoInmobiliario;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;

/**
 * Clase encargada de mostrar todos los componentes visuales referentes
 * a la interfaz gráfica de nuestro programa (implementada con Java Swing).
 */
public class VisualDisplayer {
	private static JFrame mainFrame = new JFrame("Gestor de Inmobiliaria");
	private JFrame registrarFrame = new JFrame("Registrar Proyecto");
	private JFrame visualFrame;
	
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
			
			/*
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
	            
	            //JOptionPane.showMessageDialog(mainFrame, mensaje, "Detalles Proyecto", JOptionPane.PLAIN_MESSAGE);
			}
			*/
			verProyectoPanel();
			break;
		}
		case REGISTRAR: {
			/*String nombreProyecto = JOptionPane.showInputDialog(
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
			}*/
			
			registrarProyectoPanel();
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
	
	public static JFrame getFrame() {
		return mainFrame;
	}
	
	//Frame Ver Proyecto
	
	private void verProyectoPanel() {
		int filaSeleccionada = tablaProyecto.getSelectedRow();
		String titulo = tablaProyecto.getValueAt(filaSeleccionada, 1).toString();
		
		this.visualFrame = new JFrame("Ver Proyecto");
		//visualFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		visualFrame.setResizable(false);
		//visualFrame.setPreferredSize(new Dimension(400, 300));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		JPanel headerPanel = verHeaderPanel(titulo);
		JPanel opcionesPanel = verOpcionesPanel();
		JPanel proyectorPanel = verProyectorPanel(filaSeleccionada);
		
		
		mainPanel.add(proyectorPanel, BorderLayout.WEST);
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(opcionesPanel, BorderLayout.EAST);
		
		visualFrame.add(mainPanel);
		visualFrame.pack();
		visualFrame.setLocationRelativeTo(null);
		visualFrame.setVisible(true);
		
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
			
			break;
		}
		case RESERVAR:{
			int filaSelDepa = tablaDepartamento.getSelectedRow();
			
			
			break;
		}
		case SALIR:{
			visualFrame.dispose();
			break;
		}
		
		}
		
	}
	
	public void cargarEdificiosEnTabla(int filaSel) {
		Collection<Edificio> edificios = gestorService.getAllEdificios();
		String id = defaultMain.getValueAt(filaSel, 0).toString();
		int idProyectoSeleccionado = Integer.parseInt(id);
		
		for (Edificio e : edificios) {
		    if (e.getProyectoPadre().getId() == idProyectoSeleccionado) {
		        Object[] fila = {
		            e.getId(),
		            e.getNombre(),
		            e.getInformacion().getDireccion(),
		            e.getInformacion().isTienePiscina() ? "Sí" : "No",
		            e.getInformacion().isTieneEstacionamiento() ? "Sí" : "No"
		        };
		        defaultEdi.addRow(fila);
		        //configurarListenerEdificios(e.getDepartamentos());
		    }
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
		        if (filaSelEdi != -1) {
		            // Recuperamos el ID del edificio desde la tabla
		            long idEdificio = (long) defaultEdi.getValueAt(filaSelEdi, 0);
		            
		            // Buscar el edificio en cache
		            Edificio edificioSel = gestorService.getAllEdificios()
		                                .stream()
		                                .filter(e -> e.getId() == idEdificio)
		                                .findFirst()
		                                .orElse(null);

		            if (edificioSel != null) {
		                cargarDepartamentosEnTabla(edificioSel);
		            }
		        }
				
			}
		});
		
		//Tabla Departamento
		String[] DepaCols = {"Código", "Piso", "metros^2", "Estado", "Precio"};
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
					String estado = defaultDepa.getValueAt(filaSelDepa, 3).toString();

		            boolean disponible = estado.toString().equals("DISPONIBLE");
		            boolean reservado = estado.toString().equals("RESERVADO");
		            if(disponible) {
		            	comprarBoton.setEnabled(disponible);
			            reservarBoton.setEnabled(disponible);
		            }else if(reservado) {
		            	comprarBoton.setEnabled(reservado);
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
	
	
	//header-> parte arriba
	//proyector-> las tablas y lo que queremos ver
	//acciones-> los botones que tenemos disponible abajo
	
	/// Parte de registrar ///
	private void registrarProyectoPanel() {
		
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
	    javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
	        java.time.LocalDateTime now = java.time.LocalDateTime.now();
	        panelHora.setText("Fecha Ingreso: " + now.format(
	            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yy")
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
	
}
