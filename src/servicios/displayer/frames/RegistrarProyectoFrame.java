package servicios.displayer.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import gestion.GestorInmobiliarioService;
import gestion.opciones.OpcionesRegistrar;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.EstadoDepartamento;
import modelo.ubicacion.ProyectoInmobiliario;

public class RegistrarProyectoFrame extends JFrame {
    private GestorInmobiliarioService gestorService;
	private List<Long> idProyectos = new ArrayList<>();
	
	private DefaultTableModel defaultMain;
	private JTable tablaProyecto;
    
	private JTextField txtNombreProyecto;
	private JTextField txtVendedorProyecto;
	private JButton botonRemoverE;
	private JButton botonRemoverD;
	private JButton botonAgregarE;
	private JButton botonAgregarD;
	
	private LinkedList<Departamento> departamentosPorEdificio = new LinkedList<Departamento>();
	private LinkedList<Edificio> edificiosPorProyecto = new LinkedList<Edificio>(); //igual ojo con esto, ni idea si esté bien
	
	private DefaultTableModel defaultEdi;
	private JTable tablaEdificio;
	
	private DefaultTableModel defaultDepa;
	private JTable tablaDepartamento;
	
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
    
    public RegistrarProyectoFrame(GestorInmobiliarioService service) {
        super("Registrar Proyecto"); // Título de la ventana
        this.gestorService = service;

        JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//JPanel headerPanelDerecho = verHeaderPanel(filaSeleccionada);// una cosa así
		JPanel headerPanel = registrarHeaderPanel();//arriba
		JPanel opcionesPanel = registrarOpcionesPanel();//abajo
		JPanel proyectorPanel = registrarProyectorPanel(); //parte "central"
		
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(proyectorPanel, BorderLayout.CENTER);
		mainPanel.add(opcionesPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null); 
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
    
private JPanel registrarProyectorPanel() {
		
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(900, 350));
				
		
		// Tabla Edificio.
		String[] ediCols = {"N°", "Edificio", "Dirección", "Piscina", "Estacionamiento"};
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
		            
		            // TODO revisar esto??
		            for (Edificio edificio : edificiosPorProyecto) {
		                if (edificio.getId() == idEdificio) {
		                	edificioSel = edificio; // Guárdalo cuando lo encuentres.
		                    break; // 3. Sal del bucle, ya no necesitas seguir buscando.
		                }
		            } // que es esto?
		            
		            if (edificioSel != null) {
		                //cargarDepartamentosEnTabla(edificioSel);
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
		String[] DepaCols = {"Código", "Piso", "Metros²","Habitacion", "Baños", "Estado", "Precio"};

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
			this.setVisible(true);
			this.dispose();
			break;
		}
		
		}
		
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
	            this,   // ventana padre que vamos a usar
	            panel, 
	            "Registrar Edificio", 
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE
	    );
	    
	 // Si el usuario presionó OK
	    if (result == JOptionPane.OK_OPTION) {
	    	long id = defaultEdi.getRowCount() + 1;
	        String nombre = txtNombre.getText().trim();
	        String direccion = txtDireccion.getText().trim();
	        String piscina = puntoPiscinaSi.isSelected() ? "Sí" : "No";
	        boolean tienePiscina = puntoPiscinaSi.isSelected() ? true : false;
	        String estacionamiento = puntoEstacionamientoSi.isSelected() ? "Sí" : "No";
	        boolean tieneestacionamiento = puntoEstacionamientoSi.isSelected() ? true : false;

	        if (!nombre.isEmpty() && !direccion.isEmpty()) {
	            Object[] nuevaFila = {
	                id,
	                nombre,
	                direccion,
	                piscina,  
	                estacionamiento   
	            };
	            defaultEdi.addRow(nuevaFila);
	            
	            
	            //Crear un edificio y lo agrego a la lista de edificios?
	            Edificio nuevoEdificio = new Edificio(id, nombre, direccion, tienePiscina, tieneestacionamiento);
	            edificiosPorProyecto.add(nuevoEdificio);
	            //Asignamos una nueva lista de departamentos para este edificio?//esto nuevo
	            //departamentosPorEdificio.
	            
	        } else {
	            JOptionPane.showMessageDialog(
	                this, 
	                "Debe ingresar todos los campos", 
	                "Error", 
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	    }
	}
	
	private void removerEdificio() {
		int filaSeleccionada = tablaEdificio.getSelectedRow();
	    if (filaSeleccionada != -1) {
	        // 1) Obtener el ID del edificio desde la tabla
	        Object valor = defaultEdi.getValueAt(filaSeleccionada, 0);
	        long idEdificio = Long.parseLong(valor.toString());

	        // 2) Buscar el edificio en la lista//mishh esta forma decirle a los papus
	        edificiosPorProyecto.removeIf(e -> e.getId() == idEdificio);

	        // 3) Eliminar la fila de la tabla
	        defaultEdi.removeRow(filaSeleccionada);
	    }
	}
	
	private void agregarDepartamento() {
		int filaSleccionada= tablaEdificio.getSelectedRow();
        if (filaSleccionada == -1) return;
        
        // Recuperamos el ID del edificio desde la tabla
        Object valor = defaultEdi.getValueAt(filaSleccionada, 0);
        long idEdificio = Long.parseLong(valor.toString());
        
        //Creamos la lista de departamentos en caso de no existir//nuevo creo k ta mal
        //if (!departamentosPorEdificio.containsKey(idEdificio)) {
          //  departamentosPorEdificio.put(idEdificio, new LinkedList<>());
        //}
        
        // Buscar el edificio en caché
        
        Edificio edificioSel = null;
        
        for (Edificio edificio : edificiosPorProyecto) {
            if (edificio.getId() == idEdificio) {
            	edificioSel = edificio; // Guárdalo cuando lo encuentres.
                break; // 3. Sal del bucle, ya no necesitas seguir buscando.
            }
        }
        
        if (edificioSel != null) {
        	//cargarDepartamentosEnTabla(edificioSel);
        	
        	///"Código", "Piso", "metros^2","Habitacion", "Baños", "Estado", "Precio"
			JTextField txtCodigo = new JTextField(20);
			JTextField txtEstado = new JTextField("Disponible");//inicializamos el depa en disponbible
			
			/*spinner funciona así
			SpinnerNumberModel(valorInicial, minimo, maximo, paso);
			paso es de cuanto en cuanto va avanzando
			*/
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
		            this,
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
		            Object[] nuevaFila = {
		                codigo, piso, metros, habitaciones, banos, estado, precio
		            };
		            defaultDepa.addRow(nuevaFila);
		            
		            //random a aprobar
		            long id = 1; //pa probar
		            EstadoDepartamento estadoBase = EstadoDepartamento.DISPONIBLE;
		            
		            
		            
		            //agregamos el depa a la lista del deparetamentos del edificio asociado//nuevo
		            // Agregamos el departamento a la lista del edificio
		            Departamento nuevoDepartemento = new Departamento(id, codigo, piso, metros, habitaciones, banos, estadoBase, precio, precio);

		            // 1️ Agregar a la lista dentro del objeto Edificio
		            edificioSel.agregarDepartamento(nuevoDepartemento);

		            
		        } else {
		            JOptionPane.showMessageDialog(
		                this,
		                "Debe ingresar todos los campos",
		                "Error",
		                JOptionPane.ERROR_MESSAGE
		            );
		        }
		    }
           
        }
		
	}
	
	private void removerDepartamento() {
		int filaSeleccionada = tablaDepartamento.getSelectedRow();
	    if (filaSeleccionada != -1) {
	        // 1) Recuperar el código del departamento de la tabla
	        String codigoDepa = defaultDepa.getValueAt(filaSeleccionada, 0).toString();

	        // 2) Obtener el edificio actualmente seleccionado en la tabla de edificios
	        int filaEdificio = tablaEdificio.getSelectedRow();
	        if (filaEdificio != -1) {
	            long idEdificio = Long.parseLong(defaultEdi.getValueAt(filaEdificio, 0).toString());

	            for (Edificio edificio : edificiosPorProyecto) {
	                if (edificio.getId() == idEdificio) {
	                    // Eliminar de la lista de departamentos del edificio
	                    edificio.getDepartamentos().removeIf(d -> d.getCodigo().equals(codigoDepa));
	                    break;
	                }
	            }
	        }

	        // 3) Eliminar de la tabla
	        defaultDepa.removeRow(filaSeleccionada);
	    }
	}
	
	private void registrarProyecto() {
		String nombreProyecto = txtNombreProyecto.getText().trim();
	    String vendedor = txtVendedorProyecto.getText().trim();
	    LocalDate fecha = java.time.LocalDate.now();

	    if (nombreProyecto.isEmpty() || vendedor.isEmpty() || edificiosPorProyecto.isEmpty()) {
	        JOptionPane.showMessageDialog(this,
	            "Debe ingresar Nombre, Vendedor y al menos un Edificio.",
	            "Error", JOptionPane.ERROR_MESSAGE);
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
	    JOptionPane.showMessageDialog(this,
	        "Proyecto registrado con éxito.",
	        "Éxito", JOptionPane.INFORMATION_MESSAGE);
	    
	    this.dispose(); // cerrar la ventana de registrar
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
		tablaDepartamento.getColumnModel().getColumn(2).setPreferredWidth(50); //METROS^2
		tablaDepartamento.getColumnModel().getColumn(3).setPreferredWidth(70); //HABITACIONES
		tablaDepartamento.getColumnModel().getColumn(4).setPreferredWidth(30); //BAÑOS
		tablaDepartamento.getColumnModel().getColumn(5).setPreferredWidth(70); //ESTADO
		tablaDepartamento.getColumnModel().getColumn(0).setPreferredWidth(60); //PRECIO
	}
	
	
	
	
}
