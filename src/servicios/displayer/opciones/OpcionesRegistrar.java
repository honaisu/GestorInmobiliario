package servicios.displayer.opciones;

public enum OpcionesRegistrar{
	AGREGAR_E("Agregar Edificio"),
	REMOVER_E("Remover Edificio"),
	AGREGAR_D("Agregar Departamento"),
	REMOVER_D("Remover Departamento"),
	REGISTRAR("Registrar Proyecto"),
	SALIR("Cancelar");
	
	private String nombre;
	private OpcionesRegistrar(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}

}


/*
private JPanel registrarProyectorPanel() {
		
		
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		panel.setPreferredSize(new Dimension(900, 350));
				
		
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
		///------------------------///
	}
	
	*/