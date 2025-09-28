package servicios.displayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.FlowLayout; //pa botones?
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
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
import javax.swing.SwingConstants;
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

import gestion.database.DatabaseManager;
import gestion.opciones.OpcionesModificar;
import gestion.opciones.OpcionesProyecto;
import gestion.opciones.OpcionesRegistrar;
import gestion.opciones.OpcionesVer;
import gestion.FiltroBusqueda;
import gestion.GestorInmobiliarioService;
import gestion.TextFileExporter;
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
	private LinkedList<Departamento> departamentosPorEdificio = new LinkedList<Departamento>();
	private LinkedList<Edificio> edificiosPorProyecto = new LinkedList<Edificio>(); //igual ojo con esto, ni idea si esté bien
	
	//Sirve para establecer "limites" par alos spiners <>
	private int clamp(int value, int min, int max) {
	    return Math.max(min, Math.min(max, value));
	}

	private double clamp(double value, double min, double max) {
	    return Math.max(min, Math.min(max, value));
	}
	
	//Modificar 
	private JButton botonModificarE;
	private JButton botonModificarD;
	private List<Long> edificiosAEliminar = new LinkedList<>();
	private List<Long> departamentosAEliminar = new LinkedList<>();

	
	
    private final GestorInmobiliarioService gestorService; 
    
    public VisualDisplayer(GestorInmobiliarioService service) {
		this.gestorService = service;
		
		mainFrame.setIconImage(icono.getImage());
		buscarFrame.setIconImage(icono.getImage());
		
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
			opcionBoton.addActionListener(lambda -> {
				accionOpcionesProyecto(o);
			});
		}
		panel.add(panelBajo, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel mainProyectorPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(700, 200));
		
		// Tabla con datos.
		String[] columnas = {"ID", "Nombre Proyecto", "Vendedor", "Fecha Ingreso"};
		this.defaultMain = new DefaultTableModel(columnas, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		this.tablaProyecto = new JTable(defaultMain);
		
		formatearTablaPro();
		
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
		
		case MODIFICAR:{
			mainFrame.setVisible(false);
			modificarProyectoPanel();
			break;
		}
		case BUSCAR: {
			mainFrame.setVisible(false);
			buscarEdificioPanel();
			break;
		}
		case GUARDAR:{
			try {				
				gestorService.guardarCambiosDelPrograma();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(mainFrame, e);
			}
			System.exit(0);
			
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
		visualFrame.setIconImage(icono.getImage());
		
		visualFrame.setResizable(false);
		
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
	
	private void registrarUsuario(Edificio e, Departamento d, boolean comprar) {
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
	    if (result == JOptionPane.OK_OPTION) {
	        String nombre = txtNombre.getText().trim();
	        String rut = txtRut.getText().trim();
	        String email = txtEmail.getText().trim();
	        String numero = txtNumero.getText().trim();

	        if (!nombre.isEmpty() && !rut.isEmpty() && !email.isEmpty() && !numero.isEmpty()) {
	        	
	        	//EXCEPCIONES
	        	//RUT
	        	try {
		            ValidadorRut.validarRut(rut);
		        } catch (RutInvalidoException ex) {
		        	JOptionPane.showMessageDialog(
			                registrarFrame, 
			                ex.getMessage(), 
			                "Error", 
			                JOptionPane.PLAIN_MESSAGE,
	        		        nonoRed
			            );
		            return;
		        }
	        	//NOMBRE
	        	try {
		            ValidadorNombre.validarNombre(nombre);
		        } catch (NombreInvalidoException ex) {
		        	JOptionPane.showMessageDialog(
			                registrarFrame, 
			                ex.getMessage(), 
			                "Error", 
			                JOptionPane.PLAIN_MESSAGE,
	        		        nonoRed
			            );
		            return;
		        }
	        	//EMAIL
	        	try {
		            ValidadorEmail.validarEmail(email);
		        } catch (EmailInvalidoException ex) {
		        	JOptionPane.showMessageDialog(
			                registrarFrame, 
			                ex.getMessage(), 
			                "Error", 
			                JOptionPane.PLAIN_MESSAGE,
	        		        nonoRed
			            );
		            return;
		        }
	        	//NUMERO
	        	try {
		            ValidadorTelefono.validarTelefono(numero);
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
	        	
	        	
	            
	            Comprador nuevoUsuario = new Comprador(nombre, rut, email, numero);
	            
	            try {
					gestorService.guardarUsuarioEnDatabase(nuevoUsuario);
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
	            
	            d.setRutReserva(rut);
	            d.setComprador(nuevoUsuario);
	            
	            if(comprar) {
	            	d.setEstado(EstadoDepartamento.VENDIDO);
	            }else {
	            	d.setEstado(EstadoDepartamento.RESERVADO);
	            }
	            
	            JOptionPane.showMessageDialog(
        		        registrarFrame,
        		        "Exito",
        		        "RUT Correcto",
        		        JOptionPane.PLAIN_MESSAGE,
        		        likeRed
        		);
	            
	            cargarDepartamentosEnTabla(e);
	            
	            gestorService.getDatabaseManager().marcarProyectoParaModificar(e.getProyectoPadre().getId());
	            
	        } else {
	            JOptionPane.showMessageDialog(
	                registrarFrame, 
	                "Debe ingresar todos los campos", 
	                "Error", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
	        }
	    }
	    
	}
	
	private void verificarUsuario(Edificio e, Departamento d) {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		
		JTextField txtRut = new JTextField(20);
		panel.add(new JLabel("Ingrese su rut"));
	    panel.add(txtRut);
	    
	    int result = JOptionPane.showConfirmDialog(
	            visualFrame,   
	            panel, 
	            "Verificar Usuario", 
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE,
	            quesRed
	    );
	    if (result == JOptionPane.OK_OPTION) {
	        String rut = txtRut.getText().trim();
	        
	        //EXCEPCIÓN 
	        try {
	            ValidadorRut.validarRut(rut);
	        } catch (RutInvalidoException ex) {
	        	JOptionPane.showMessageDialog(
		                registrarFrame, 
		                ex.getMessage(), 
		                "Error", 
		                JOptionPane.PLAIN_MESSAGE,
        		        nonoRed
		            );
	            return;
	        }
	        
	        
	        if (!rut.isEmpty()) {
	            
	        	String rutReserva = d.getRutReserva();
	        	
	        	
	        	if(!rutReserva.equals(rut)) {
	        		JOptionPane.showMessageDialog(
			                registrarFrame, 
			                "RUT Incorrecto", 
			                "Error", 
			                JOptionPane.PLAIN_MESSAGE,
	        		        sadRed
			            );
	        	}else {
	        		
	        		JOptionPane.showMessageDialog(
	        		        registrarFrame,
	        		        "Exito",
	        		        "RUT Correcto",
	        		        JOptionPane.PLAIN_MESSAGE,
	        		        likeRed
	        		);
	        		
	        		Comprador comprador = gestorService.getCompradorPorRut(rut);
	        		if (comprador != null) {
	        		    d.setComprador(comprador);
	        		}

	        		d.setRutReserva(rut);
	        		
	        		d.setEstado(EstadoDepartamento.VENDIDO);
		            cargarDepartamentosEnTabla(e);
		            
		            gestorService.getDatabaseManager().marcarProyectoParaModificar(e.getProyectoPadre().getId());
	        	}
	            
	            
	            
	        } else {
	            JOptionPane.showMessageDialog(
	                registrarFrame, 
	                "Debe ingresar todos los campos", 
	                "Error", 
	                JOptionPane.PLAIN_MESSAGE,
    		        nonoRed
	            );
	        }
	    }
	}
	
	private void comprarDepartamento() {
		int filaSelEdi = tablaEdificio.getSelectedRow();
		if (filaSelEdi == -1) return;
        
        long idEdificio = (long) defaultEdi.getValueAt(filaSelEdi, 0);
        Edificio edificioSel = gestorService.getMapEdificios()
				.get(idEdificio);
        
		int filaSelDepa = tablaDepartamento.getSelectedRow();
		String estado = defaultDepa.getValueAt(filaSelDepa, 5).toString();
		
		//buscar departamento
		LinkedList<Departamento> departamentos = edificioSel.getDepartamentos();
		Departamento depa = null;
        
        for (Departamento d : departamentos) {
        	if (d.getCodigo().equals(defaultDepa.getValueAt(filaSelDepa, 0).toString())) {
        		depa = d;
        	}
        }
		
		
		if (estado.equals(EstadoDepartamento.DISPONIBLE.toString())) {
			registrarUsuario(edificioSel, depa, true);
		    
		}else {
			verificarUsuario(edificioSel, depa);
		}
		
		estado = defaultDepa.getValueAt(filaSelDepa, 5).toString();
		
		//TODO imprimir recibo como txt
		if (estado.equals(EstadoDepartamento.VENDIDO.toString())) {
			
			//Actualizar los precios por demanda
			int departamentosTotales = edificioSel.getDepartamentos().size();
			int departamentosVendidos = 0;
			
			LinkedList<Departamento> depas = edificioSel.getDepartamentos();
			
			for (Departamento d: depas) {
				if(d.getEstado().name().equals(EstadoDepartamento.VENDIDO.toString())) {
					departamentosVendidos++;
				}
			}
			
			for (Departamento d: depas) {
				if(!d.getEstado().name().equals(EstadoDepartamento.VENDIDO.toString())) {
					d.getGestorPrecios().actualizarPrecioPorDemanda(departamentosTotales, departamentosVendidos, edificioSel);
				}
			}
			
			cargarDepartamentosEnTabla(edificioSel);
            
            gestorService.getDatabaseManager().marcarProyectoParaModificar(edificioSel.getProyectoPadre().getId());
            
			int result = JOptionPane.showConfirmDialog(
		            visualFrame,
		            "¿Desea recibo?", 
		            "Compra Realizada",
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.PLAIN_MESSAGE,
		            quesRed
			);
			
			if (result == JOptionPane.YES_OPTION) {
				try {
					TextFileExporter.exportarReciboCompra(depa.getComprador(), depa);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			
					
		}
	}
	
	private void reservarDepartamento() {
		int filaSelEdi = tablaEdificio.getSelectedRow();
		if (filaSelEdi == -1) return;
        
        long idEdificio = (long) defaultEdi.getValueAt(filaSelEdi, 0);
        Edificio edificioSel = gestorService.getMapEdificios()
				.get(idEdificio);
        
		int filaSelDepa = tablaDepartamento.getSelectedRow();
		String estado = defaultDepa.getValueAt(filaSelDepa, 5).toString();
		
		//buscar departamento
		LinkedList<Departamento> departamentos = edificioSel.getDepartamentos();
		Departamento depa = null;
        
        for (Departamento d : departamentos) {
        	if (d.getCodigo().equals(defaultDepa.getValueAt(filaSelDepa, 0).toString())) {
        		depa = d;
        	}
        }
        
		registrarUsuario(edificioSel, depa, false);
	}
	
	private void accionOpcionesVer(OpcionesVer opcion) {
		switch (opcion) {
		case COMPRAR:{
			comprarDepartamento(); 
			break;
		}
		case RESERVAR: {
			reservarDepartamento();
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
	    
	}
	
	private void cargarDepartamentosEnTabla(List<Departamento> listaDepartamentos) {
	    // Limpiar la tabla primero
	    defaultDepa.setRowCount(0);

	    // Recorrer la lista y añadir filas
	    for (Departamento d : listaDepartamentos) {
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
	        
	        defaultDepa.addRow(fila);
	    }
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
		
		//Formatear Tabla
		formatearTablaEdi();
		
		cargarEdificiosEnTabla(filaSel);
		comprarBoton.setEnabled(false);
        reservarBoton.setEnabled(false);
		
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
		String[] DepaCols = {"Código", "Piso", "metros²", "Habitaciones", "Baños", "Estado", "Precio"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		this.tablaDepartamento = new JTable(defaultDepa);
		
		formatearTablaDep();
		
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
		
		registrarFrame = new JFrame("Registrar Proyecto");
		registrarFrame.setIconImage(icono.getImage());

		registrarFrame.setResizable(false);
		
		
		edificiosPorProyecto.clear();
		departamentosPorEdificio.clear();
		
		//visualFrame.setPreferredSize(new Dimension(400, 300));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
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
	    
	    //Nombre del proyecto 
	    datosPanel.add(new JLabel("Nombre: "));
	    txtNombreProyecto = new JTextField(25);   // <<<<< global, no local
	    datosPanel.add(txtNombreProyecto);
	    
	    //Nombre del vendedor
	    datosPanel.add(new JLabel("Vendedor: "));
	    txtVendedorProyecto = new JTextField(25); // <<<<< global, no local
	    datosPanel.add(txtVendedorProyecto);

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
	    botonAgregarD.setEnabled(false);
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
	
	private void agregarEdificio() {
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
	    	long idTemporal = -System.currentTimeMillis();
	        String nombre = txtNombre.getText().trim();
	        String direccion = txtDireccion.getText().trim();
	        String piscina = puntoPiscinaSi.isSelected() ? "Sí" : "No";
	        boolean tienePiscina = puntoPiscinaSi.isSelected() ? true : false;
	        String estacionamiento = puntoEstacionamientoSi.isSelected() ? "Sí" : "No";
	        boolean tieneestacionamiento = puntoEstacionamientoSi.isSelected() ? true : false;

	        if (!nombre.isEmpty() && !direccion.isEmpty()) {
	            Object[] nuevaFila = {
	            	idTemporal,
	                nombre,
	                direccion,
	                piscina,  
	                estacionamiento   
	            };
	            defaultEdi.addRow(nuevaFila);
	            
	            
	            //Crear un edificio y lo agrego a la lista de edificios?
	            Edificio nuevoEdificio = new Edificio(idTemporal, nombre, direccion, tienePiscina, tieneestacionamiento);
	            edificiosPorProyecto.add(nuevoEdificio);
	            //Asignamos una nueva lista de departamentos para este edificio?//esto nuevo
	            //departamentosPorEdificio.
	            
	        } else {
	            JOptionPane.showMessageDialog(
	                registrarFrame, 
	                "Debe ingresar todos los campos", 
	                "Error", 
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	    }
	}
	
	private void removerEdificio() {
		int filaSeleccionada = tablaEdificio.getSelectedRow();
	    if (filaSeleccionada == -1) return;
        Object valor = defaultEdi.getValueAt(filaSeleccionada, 0);
        long idEdificio = Long.parseLong(valor.toString());

        // Si el edificio ya existe en la DB (ID > 0), lo marcamos para eliminar.
        if (idEdificio > 0) {
            gestorService.getDatabaseManager().marcarEdificioParaEliminar(idEdificio);
        }

        // Lo quitamos de la lista temporal en memoria para que la UI se actualice.
        edificiosPorProyecto.removeIf(e -> e.getId() == idEdificio);
        // Y lo quitamos de la tabla visual.
        defaultEdi.removeRow(filaSeleccionada);
	}
	
	private void agregarDepartamento() {
		//nueva que modifica base de datos?
		int filaSleccionada = tablaEdificio.getSelectedRow();
	    if (filaSleccionada == -1) return;

	    // Recuperamos el ID del edificio desde la tabla
	    Object valor = defaultEdi.getValueAt(filaSleccionada, 0);
	    long idEdificio = Long.parseLong(valor.toString());

	    Edificio edificioSel = null;

	    for (Edificio edificio : edificiosPorProyecto) {
	        if (edificio.getId() == idEdificio) {
	            edificioSel = edificio; // Guárdalo cuando lo encuentres.
	            break;
	        }
	    }

	    if (edificioSel != null) {
	        ///"Código", "Piso", "metros²","Habitacion", "Baños", "Estado", "Precio"
	        JTextField txtCodigo = new JTextField(20);
	        JTextField txtEstado = new JTextField("Disponible");//inicializamos el depa en disponbible

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

	        if (result == JOptionPane.OK_OPTION) {
	            String codigo = txtCodigo.getText().trim();
	            int piso = ((Number) spinnerPiso.getValue()).intValue();
	            int metros = ((Number) spinnerMetros.getValue()).intValue();
	            int habitaciones = ((Number) spinnerHabitaciones.getValue()).intValue();
	            int banos = ((Number) spinnerBanos.getValue()).intValue();
	            String estado = txtEstado.getText().trim();
	            int precio = ((Number) spinnerPrecio.getValue()).intValue();

	            if (!codigo.isEmpty() && !estado.isEmpty()) {

	                // random a aprobar
	                long id = -System.currentTimeMillis();  
	                EstadoDepartamento estadoBase = EstadoDepartamento.DISPONIBLE;

	                // creamos el objeto departamento (todavía no en DB)
	                Departamento nuevoDepartemento = new Departamento(
	                    id, 
	                    codigo, 
	                    piso, 
	                    metros, 
	                    habitaciones, 
	                    banos, 
	                    estadoBase, 
	                    precio, 
	                    precio, 
	                    null);

	                edificioSel.agregarDepartamento(nuevoDepartemento);
	                nuevoDepartemento.setEdificioPadre(edificioSel);
	                // ======================
	                // cambio: persistencia inmediata si el edificio ya existe en DB
	                // ======================
	                try {
	                    DatabaseManager database = gestorService.getDatabaseManager();
	                    if (edificioSel.getId() > 0) { 
	                        database.agregarNuevoEdificio(edificioSel); // cambio
	                        // insertarDepartamento ya setea el id real y lo agrega al edificio en cache // cambio
	                    } else {
	                        // edificio todavía no está en DB → solo en memoria
	                        edificioSel.agregarDepartamento(nuevoDepartemento); // cambio
	                    }
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(registrarFrame, 
	                        "Error al guardar en la base de datos: " + ex.getMessage(),
	                        "Error DB", JOptionPane.PLAIN_MESSAGE, vanishRed);
	                    ex.printStackTrace();
	                    return; // salir si hubo error
	                }

	                // ======================
	                // cambio: añadir fila a la tabla con los datos del depto
	                // ======================
	                Object[] nuevaFila = {
	                    codigo, piso, metros, habitaciones, banos, estado, precio
	                };
	                defaultDepa.addRow(nuevaFila);

	            } else {
	                JOptionPane.showMessageDialog(
	                    registrarFrame,
	                    "Debe ingresar todos los campos",
	                    "Error",
	                    JOptionPane.PLAIN_MESSAGE,
	                    nonoRed
	                );
	            }
	        }
	    }
	}
	
	
	private void removerDepartamento() {
	    int filaSeleccionada = tablaDepartamento.getSelectedRow();
	    if (filaSeleccionada == -1) return;

	    String codigoDepa = defaultDepa.getValueAt(filaSeleccionada, 0).toString();
	    int filaEdificio = tablaEdificio.getSelectedRow();
	    if (filaEdificio == -1) return;
	    
	    long idEdificio = Long.parseLong(defaultEdi.getValueAt(filaEdificio, 0).toString());
	    
	    // Buscamos el edificio padre en la lista en memoria
	    Edificio edificioPadre = edificiosPorProyecto.stream()
	        .filter(e -> e.getId() == idEdificio)
	        .findFirst().orElse(null);

	    defaultDepa.removeRow(filaSeleccionada);
	    
	    if (edificioPadre == null) return; 
        // Buscamos el departamento para obtener su ID
        Departamento deptoARemover = edificioPadre.getDepartamentos().stream()
            .filter(d -> d.getCodigo().equals(codigoDepa))
            .findFirst().orElse(null);

        if (deptoARemover != null) {
            if (deptoARemover.getId() != null && deptoARemover.getId() > 0) {
                gestorService.getDatabaseManager().marcarDepartamentoParaEliminar(deptoARemover.getId());
            }
            edificioPadre.getDepartamentos().remove(deptoARemover);
        }
	}
	
	private void registrarProyecto() {
		String nombreProyecto = txtNombreProyecto.getText().trim();
	    String vendedor = txtVendedorProyecto.getText().trim();
	    LocalDate fecha = java.time.LocalDate.now();

	    if (nombreProyecto.isEmpty() || vendedor.isEmpty() || edificiosPorProyecto.isEmpty()) {
	        JOptionPane.showMessageDialog(registrarFrame,
	            "Debe ingresar Nombre, Vendedor y al menos un Edificio.",
	            "Error", JOptionPane.PLAIN_MESSAGE, nonoRed);
	        return;
	    }

	    // Crear el proyecto con un ID nuevo
	    //long nuevoId = gestorService.getAllProyectos().size() + 1; 
	    ProyectoInmobiliario nuevoProyecto = new ProyectoInmobiliario(nombreProyecto, vendedor, fecha);

	    // Asociar edificios
	    for (Edificio e : edificiosPorProyecto) {
	        nuevoProyecto.addEdificio(e);
	    }

	    // Guardarlo en el gestor //nachoid?
	    gestorService.getDatabaseManager().agregarNuevoProyecto(nuevoProyecto);
	    
	    
	    // Refrescar tabla de la ventana principal
	    cargarProyectosEnTabla();

	    // Mensaje de éxito
	    JOptionPane.showMessageDialog(registrarFrame,
	        "Proyecto registrado con éxito.",
	        "Éxito", JOptionPane.PLAIN_MESSAGE, likeRed);
	    
	    mainFrame.setVisible(true);
	    registrarFrame.dispose(); // cerrar la ventana de registrar
	}
	
	private void accionOpcionesRegistrar(OpcionesRegistrar opcion) {
		switch (opcion) {
		case AGREGAR_E:{
			agregarEdificio();
			break;
		}
		case REMOVER_E:{
			removerEdificio();
		    break;
		}
		case AGREGAR_D:{
			agregarDepartamento();
			break;
		}
		case REMOVER_D:{
			removerDepartamento();
		    break;
		}
		
		case REGISTRAR:{ //falta la logica para agregarlo a la hora de Registrar
			registrarProyecto();
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
		
		formatearTablaEdi();
		
		// Para añadir funcionalidad al elegir una fila
		tablaEdificio.getSelectionModel().addListSelectionListener(lambda -> {
		    if (!lambda.getValueIsAdjusting()) {
		        int filaSel = tablaEdificio.getSelectedRow();
		        boolean filaSeleccionada = filaSel != -1;

		        botonRemoverE.setEnabled(filaSeleccionada);
		        botonAgregarD.setEnabled(filaSeleccionada);

		        if (filaSeleccionada) {
		            // Obtenemos el ID del edificio
		            long idEdificio = Long.parseLong(defaultEdi.getValueAt(filaSel, 0).toString());

		            // Buscamos el edificio en la lista temporal de registro
		           /* Edificio edificioSel = edificiosPorProyecto.stream()
		                    .filter(e -> e.getId() == idEdificio)
		                    .findFirst()
		                    .orElse(null);*/

		            // Si existe, cargamos los departamentos asociados
		            //Edificio edificioSel = gestorService.getMapEdificios().get(idEdificio);
		            Edificio edificioSel = null;
		            
		            for (Edificio edificio : edificiosPorProyecto) {
		                if (edificio.getId() == idEdificio) {
		                	edificioSel = edificio; // Guárdalo cuando lo encuentres.
		                    break; // 3. Sal del bucle, ya no necesitas seguir buscando.
		                }
		            }
		            
		            if (edificioSel != null) {
		                cargarDepartamentosEnTabla(edificioSel);
		            }
		        } else {
		            // Limpiar tabla de departamentos si no hay edificio seleccionado
		            defaultDepa.setRowCount(0);
		        }
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
		
		formatearTablaDep();
		
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
	
	private JPanel buscarProyectorPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(900, 300));
		
		//Tabla Departamento
		String[] DepaCols = {"Nombre Proyecto", "Código", "Piso", "Metros²", "Habitaciones", "Baños",
				"Estado", "Precio", "Dirección", "Estacionamiento", "Piscina"};
		this.defaultDepa = new DefaultTableModel(DepaCols, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // ninguna celda editable
		    
			};
		};
		
		this.tablaDepartamentosFiltrados = new JTable(defaultDepa);
	
		//Formatear Columnas
		formatearTablaDepBus();
		
		DatabaseManager database = gestorService.getDatabaseManager();
		List<Departamento> listaDepas = database.getDepartamentosPorFiltro(new FiltroBusqueda());
	    cargarDepartamentosEnTabla(listaDepas);
	    
	    
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
	    JSpinner spinnerHab = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
	    habPanel.add(spinnerHab);

	    // --- Baños ---
	    JPanel banioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    banioPanel.add(new JLabel("Baños:"));
	    JSpinner spinnerBanios = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
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
	    JButton btnCancelar = new JButton("Cancelar");
	    botonPanel.add(btnBuscar);
	    botonPanel.add(btnCancelar);
	    
	    btnCancelar.addActionListener(e -> {
	        buscarFrame.dispose();
	        mainFrame.setVisible(true);
	    });
	    
	    
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
	
	//FORMATEAR TABLAS
	
	private void formatearTablaPro() {
		for (int i = 0; i < tablaProyecto.getColumnCount(); i++) {
			tablaProyecto.getColumnModel().getColumn(i).setResizable(false);
			
		}
		
		tablaProyecto.getColumnModel().getColumn(0).setCellRenderer(rendererColumna);
		
		tablaProyecto.getTableHeader().setReorderingAllowed(false);
		tablaProyecto.getColumnModel().getColumn(0).setPreferredWidth(1);  // ID
		//tablaProyecto.getColumnModel().getColumn(1).setPreferredWidth(120);  // ID
		//tablaProyecto.getColumnModel().getColumn(2).setPreferredWidth(120);  // ID
		//tablaProyecto.getColumnModel().getColumn(3).setPreferredWidth(120);  // ID
		
		
	}
	
	private void formatearTablaDepBus() {
		for (int i = 0; i < tablaDepartamentosFiltrados.getColumnCount(); i++) {
			tablaDepartamentosFiltrados.getColumnModel().getColumn(i).setResizable(false);
			
		}
		
		tablaDepartamentosFiltrados.getColumnModel().getColumn(0).setCellRenderer(rendererColumna);
		
		tablaDepartamentosFiltrados.getTableHeader().setReorderingAllowed(false);
		tablaDepartamentosFiltrados.getColumnModel().getColumn(0).setPreferredWidth(120);  // NOMBRE PROYECTO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(1).setPreferredWidth(50);  // CODIGO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(2).setPreferredWidth(30);  // PISO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(3).setPreferredWidth(50); // METROS²
		tablaDepartamentosFiltrados.getColumnModel().getColumn(4).setPreferredWidth(60);  // HABITACIONES
		tablaDepartamentosFiltrados.getColumnModel().getColumn(5).setPreferredWidth(30);  // BAÑOS
		tablaDepartamentosFiltrados.getColumnModel().getColumn(6).setPreferredWidth(70); // ESTADO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(7).setPreferredWidth(60); // PRECIO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(8).setPreferredWidth(120); // DIRECCION
		tablaDepartamentosFiltrados.getColumnModel().getColumn(9).setPreferredWidth(100); // ESTACIONAMIENTO
		tablaDepartamentosFiltrados.getColumnModel().getColumn(10).setPreferredWidth(50); // PISCINA
	}
	
	private void formatearTablaEdi() {
		for (int i = 0; i < tablaEdificio.getColumnCount(); i++) {
			tablaEdificio.getColumnModel().getColumn(i).setResizable(false);
			
		}
		
		tablaEdificio.getColumnModel().getColumn(0).setCellRenderer(rendererColumna);
		
		tablaEdificio.getTableHeader().setReorderingAllowed(false);
		tablaEdificio.getColumnModel().getColumn(0).setPreferredWidth(5); // ID
		tablaEdificio.getColumnModel().getColumn(1).setPreferredWidth(100); // EDIFICIO
		tablaEdificio.getColumnModel().getColumn(2).setPreferredWidth(120); // DIRECCION
		tablaEdificio.getColumnModel().getColumn(3).setPreferredWidth(30); // PISCINA
		tablaEdificio.getColumnModel().getColumn(4).setPreferredWidth(80); // ESTACIONAMIENTO
		
	}
	
	private void formatearTablaDep() {
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
	
	private void modificarProyectoPanel() {
		
		int filaSeleccionada = tablaProyecto.getSelectedRow();
	    if (filaSeleccionada == -1) return; // seguridad por si no hay fila seleccionada

	    // sacar ID del proyecto de la tabla (columna 0)
	    long idProyecto = (long) defaultMain.getValueAt(filaSeleccionada, 0);
	    ProyectoInmobiliario proyectoSel = gestorService.getDatabaseManager()
	                                .getMapProyectos()
	                                .get(idProyecto);

	    modificarFrame = new JFrame("Modificar Proyecto");
	    modificarFrame.setIconImage(icono.getImage());
	    modificarFrame.setResizable(false);

	    // llenar tus listas temporales con los datos del proyecto
	    edificiosPorProyecto.clear();
	    edificiosPorProyecto.addAll(proyectoSel.getEdificios());

	    departamentosPorEdificio.clear();
	    for (Edificio e : proyectoSel.getEdificios()) {
	        departamentosPorEdificio.addAll(e.getDepartamentos());
	    }
	    
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // subpaneles en el mismo estilo que "registrar"
	    JPanel headerPanel = modificarHeaderPanel(proyectoSel);
	    JPanel opcionesPanel = modificarOpcionesPanel(proyectoSel);
	    JPanel proyectorPanel = modificarProyectorPanel(proyectoSel);

	    mainPanel.add(headerPanel, BorderLayout.NORTH);
	    mainPanel.add(opcionesPanel, BorderLayout.SOUTH);
	    mainPanel.add(proyectorPanel, BorderLayout.CENTER);

	    modificarFrame.add(mainPanel);
	    modificarFrame.pack();
	    modificarFrame.setLocationRelativeTo(null);
	    modificarFrame.setVisible(true);

	    modificarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent e) {
	            mainFrame.setVisible(true);
	        }
	    });
		
	}
	
	private JPanel modificarHeaderPanel(ProyectoInmobiliario  proyectoSel) {
		JPanel panel = new JPanel(new BorderLayout());
	    panel.setPreferredSize(new Dimension(600, 150));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // El título usa el nombre del proyecto
	    JLabel titulo = new JLabel("Modificar Proyecto: " + proyectoSel.getNombreProyecto(), SwingConstants.CENTER);
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
	    panelFecha.setHorizontalAlignment(SwingConstants.RIGHT);
	    panelFecha.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	    panelCentral.add(panelFecha, BorderLayout.EAST);

	    panel.add(panelCentral, BorderLayout.CENTER);

	    JLabel marcaAgua = new JLabel("🄯 Los Bien Corporation. All lefts reserved");
	    panel.add(marcaAgua, BorderLayout.SOUTH);

	    return panel;

	}
	
	private JPanel modificarOpcionesPanel(ProyectoInmobiliario proyectoSel) {
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

	    // --- Listeners ---
	    botonAgregarE.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.AGREGAR_E, proyectoSel));
	    botonRemoverE.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.REMOVER_E, proyectoSel));
	    botonModificarE.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.MODIFICAR_E, proyectoSel));
	    botonAgregarD.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.AGREGAR_D, proyectoSel));
	    botonRemoverD.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.REMOVER_D, proyectoSel));
	    botonModificarD.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.MODIFICAR_D, proyectoSel));
	    botonGuardar.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.GUARDAR_CAMBIOS, proyectoSel));
	    botonCancelar.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.CANCELAR, proyectoSel));
	    botonEliminar.addActionListener(e -> accionOpcionesModificar(OpcionesModificar.ELIMINAR_PROYECTO, proyectoSel));

	    return panel;
	}
	
	private void modificarEdificio() {
		int filaSel = tablaEdificio.getSelectedRow();
        if (filaSel == -1) return; // Salir si no hay nada seleccionado

        // 1. Obtener el objeto Edificio de la lista temporal
        long idEdificio = (long) defaultEdi.getValueAt(filaSel, 0);
        Edificio edificioAModificar = edificiosPorProyecto.stream()
            .filter(e -> e.getId() == idEdificio).findFirst().orElse(null);

        if (edificioAModificar != null) {
            // 2. Crear el panel de diálogo y PRE-RELLENARLO con los datos actuales
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

                // 4. Actualizar la fila en la tabla para reflejar los cambios al instante
                defaultEdi.setValueAt(nuevoNombre, filaSel, 1);
                defaultEdi.setValueAt(nuevaDireccion, filaSel, 2);
                defaultEdi.setValueAt(nuevaPiscina ? "Sí" : "No", filaSel, 3);
                defaultEdi.setValueAt(nuevoEstacionamiento ? "Sí" : "No", filaSel, 4);
            }
        }
		
	}
	
	private void modificarDepartamento() {
		int filaDeptoSel = tablaDepartamento.getSelectedRow();
        int filaEdificioSel = tablaEdificio.getSelectedRow();
        if (filaDeptoSel == -1 || filaEdificioSel == -1) return;

        long idEdificio = (long) defaultEdi.getValueAt(filaEdificioSel, 0);
        String codigoDepto = defaultDepa.getValueAt(filaDeptoSel, 0).toString();
        
        Edificio edificioPadre = edificiosPorProyecto.stream().filter(e -> e.getId() == idEdificio).findFirst().orElse(null);
        Departamento deptoAModificar = null;
        if (edificioPadre != null) {
            deptoAModificar = edificioPadre.getDepartamentos().stream()
                .filter(d -> d.getCodigo().equals(codigoDepto)).findFirst().orElse(null);
        }

        if (deptoAModificar != null) {
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

                // Actualizar la fila en la tabla
                defaultDepa.setValueAt(nuevoPiso, filaDeptoSel, 1);
                defaultDepa.setValueAt(nuevosMetros, filaDeptoSel, 2);
                defaultDepa.setValueAt(nuevasHabitaciones, filaDeptoSel, 3);
                defaultDepa.setValueAt(nuevosBanos, filaDeptoSel, 4);
                defaultDepa.setValueAt(nuevoPrecio, filaDeptoSel, 6);
                
                
            }
        }
	}
	
	private void guardarCambios(ProyectoInmobiliario proyecto) {
		/*String nuevoNombre = txtNombreProyecto.getText().trim();
        String nuevoVendedor = txtVendedorProyecto.getText().trim();

        if (nuevoNombre.isEmpty() || nuevoVendedor.isEmpty() || edificiosPorProyecto.isEmpty()) {
            JOptionPane.showMessageDialog(modificarFrame, "Nombre, Vendedor y al menos un Edificio son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Actualizar el objeto Proyecto original
        proyecto.setNombreProyecto(nuevoNombre);
        proyecto.setVendedor(nuevoVendedor);
        proyecto.getEdificios().clear();
        proyecto.getEdificios().addAll(edificiosPorProyecto);

        // Guardar en la "base de datos"
        gestorService.getDatabaseManager().actualizarDatosDatabase();

        // Refrescar tabla principal
        cargarProyectosEnTabla();

        JOptionPane.showMessageDialog(modificarFrame, "Proyecto actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        modificarFrame.dispose();
        mainFrame.setVisible(true);*/
		String nuevoNombre = txtNombreProyecto.getText().trim();
	    String nuevoVendedor = txtVendedorProyecto.getText().trim();

	    if (nuevoNombre.isEmpty() || nuevoVendedor.isEmpty() || edificiosPorProyecto.isEmpty()) {
	        JOptionPane.showMessageDialog(modificarFrame, "Nombre, Vendedor y al menos un Edificio son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    // Actualizar objeto en memoria
	    proyecto.setNombreProyecto(nuevoNombre);
	    proyecto.setVendedor(nuevoVendedor);
	    
	    
	    proyecto.getEdificios().clear();
	    proyecto.getEdificios().addAll(edificiosPorProyecto);

	    //gestorService.getDatabaseManager().agregarNuevoProyecto(proyecto);
	    //gestorService.getDatabaseManager().modificarProyecto(proyecto.getId(), proyecto);
	    gestorService.getDatabaseManager().marcarProyectoParaModificar(proyecto.getId());
	    // Refrescar tabla
	    cargarProyectosEnTabla();

	    JOptionPane.showMessageDialog(modificarFrame,
	    		"Proyecto actualizado con éxito.", 
	    		"Éxito", 
	    		JOptionPane.PLAIN_MESSAGE,
	    		likeRed);

	    // Limpiar las listas de eliminados
	    edificiosAEliminar.clear();
	    departamentosAEliminar.clear();

	    modificarFrame.dispose();
	    mainFrame.setVisible(true);
	}
	
	private void eliminarProyecto(ProyectoInmobiliario proyecto) {
		int result = JOptionPane.showConfirmDialog(
	            modificarFrame,
	            "¿Desea Eliminar el Proyecto " +proyecto.getNombreProyecto()+"?", 
	            "Eliminar Proyecto",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.PLAIN_MESSAGE,
	            quesRed); 
		
		if (result == JOptionPane.YES_OPTION) {
			gestorService.getDatabaseManager().eliminarProyecto(proyecto.getId());
			
			JOptionPane.showConfirmDialog(
		            modificarFrame,
		            "Proyecto " + proyecto.getNombreProyecto() + " eliminado correctamente", 
		            "Eliminar Proyecto",
		            JOptionPane.DEFAULT_OPTION,
		            JOptionPane.PLAIN_MESSAGE,
		            vanishRed); 
			cargarProyectosEnTabla();
			
			modificarFrame.dispose();
		    mainFrame.setVisible(true);
		}
	}
	
	private void accionOpcionesModificar(OpcionesModificar opcion, ProyectoInmobiliario proyecto) {
	    switch (opcion) {
	    
	        case AGREGAR_E: {
	        	agregarEdificio();    	
	            break;
	        }
	        
	        case REMOVER_E: {
	        	removerEdificio();
	            break;
	        }
	        
	        case MODIFICAR_E:{
	        	modificarEdificio();
	            break;
	        }
	        
	        case AGREGAR_D: {
	        	agregarDepartamento();
	            break;
	        }
	        
	        case REMOVER_D: {
	        	removerDepartamento();
	            break;
	        }
	        
	        case MODIFICAR_D:{
	        	modificarDepartamento();
	            break;
	        }
	        
	        case GUARDAR_CAMBIOS: { 
	        	guardarCambios(proyecto);
	            break;
	        }
	        
	        case ELIMINAR_PROYECTO:{
	        	eliminarProyecto(proyecto);
	        	break;
	        }
	        case CANCELAR: {
	            modificarFrame.dispose();
	            mainFrame.setVisible(true);
	            break;
	        }
	    }
	}
	
	private JPanel  modificarProyectorPanel(ProyectoInmobiliario proyectoSel) {
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
	    panel.setPreferredSize(new Dimension(900, 350));

	    // --- Tabla Edificio ---
	    String[] ediCols = {"ID", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
	    this.defaultEdi = new DefaultTableModel(ediCols, 0);
	    this.tablaEdificio = new JTable(defaultEdi);
	    
	    // Rellenamos la tabla con los edificios existentes del proyecto
	    for (Edificio e : edificiosPorProyecto) {
	        Object[] fila = {
	            e.getId(), e.getNombre(), e.getInformacion().getDireccion(),
	            e.getInformacion().isTienePiscina() ? "Sí" : "No",
	            e.getInformacion().isTieneEstacionamiento() ? "Sí" : "No"
	        };
	        defaultEdi.addRow(fila);
	    }

	    // --- Tabla Departamento ---
	    String[] DepaCols = {"Código", "Piso", "m²", "Hab.", "Baños", "Estado", "Precio"};
	    this.defaultDepa = new DefaultTableModel(DepaCols, 0);
	    this.tablaDepartamento = new JTable(defaultDepa);
	    
	    // --- Listeners (Lógica de Interacción) ---
	    tablaEdificio.getSelectionModel().addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) {
	            int filaSel = tablaEdificio.getSelectedRow();
	            boolean seleccionado = filaSel != -1;

	            botonRemoverE.setEnabled(seleccionado);
	            botonModificarE.setEnabled(seleccionado);
	            botonAgregarD.setEnabled(seleccionado);

	            if (seleccionado) {
	                long idEdificio = (long) defaultEdi.getValueAt(filaSel, 0);
	                Edificio edificioSeleccionado = edificiosPorProyecto.stream()
	                    .filter(ed -> ed.getId() == idEdificio)
	                    .findFirst().orElse(null);
	                
	                if (edificioSeleccionado != null) {
	                    cargarDepartamentosEnTabla(edificioSeleccionado);
	                }
	            } else {
	                defaultDepa.setRowCount(0); // Limpia la tabla de deptos si no hay edificio seleccionado
	            }
	        }
	    });

	    tablaDepartamento.getSelectionModel().addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) {
	            botonRemoverD.setEnabled(tablaDepartamento.getSelectedRow() != -1);
	            botonModificarD.setEnabled(tablaDepartamento.getSelectedRow() != -1); 
	        }
	    });

	    // Añadir tablas al panel
	    panel.add(new JScrollPane(tablaEdificio));
	    panel.add(new JScrollPane(tablaDepartamento));

	    return panel;
	}
	
}