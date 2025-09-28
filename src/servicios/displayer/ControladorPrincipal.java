package servicios.displayer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import gestion.FiltroBusqueda;
import gestion.GestorInmobiliarioService;
import gestion.TextFileExporter;
import gestion.opciones.OpcionesModificar;
import gestion.opciones.OpcionesProyecto;
import gestion.opciones.OpcionesRegistrar;
import gestion.opciones.OpcionesVer;
import modelo.entidades.Comprador;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import modelo.ubicacion.EstadoDepartamento;
import modelo.ubicacion.ProyectoInmobiliario;

public class ControladorPrincipal {
	private final VisualDisplayerV2 vista;
    private final GestorInmobiliarioService gestor;
	
	private LinkedList<Edificio> edificiosTemporales = new LinkedList<Edificio>();
    
    public ControladorPrincipal(GestorInmobiliarioService gestor, VisualDisplayerV2 vista) {
        this.gestor = gestor;
        this.vista = vista;
    }

    public Collection<ProyectoInmobiliario> obtenerTodosLosProyectos() {
		return gestor.obtenerTodosLosProyectos();
    }
	
	public void manejarOpcionesProyecto(OpcionesProyecto opcion) {
		switch (opcion) {
			case REGISTRAR: {
			    this.edificiosTemporales.clear(); 
			    
				vista.ocultarVentanaPrincipal();
				vista.mostrarVentanaRegistrar();
				break;
			}		
			case MODIFICAR: {
				vista.ocultarVentanaPrincipal();
				this.solicitarModificacionProyecto();
				break;
			}
			case VER: {
				long idProyecto = vista.getSelectedProyectoId();
	            if (idProyecto == -1) return; // Si no hay nada seleccionado, no hace nada.

	            // El controlador usa el gestor para buscar el proyecto.
	            ProyectoInmobiliario proyecto = gestor.getProyectoPorId(idProyecto);

	            // Si no se encontró en la base de datos (porque es nuevo)...
	            if (proyecto == null) {
	                // lo buscamos entre todos los proyectos que la vista tiene en su tabla. :3
	                proyecto = this.obtenerTodosLosProyectos().stream()
	                        .filter(p -> p.getId() == idProyecto)
	                        .findFirst()
	                        .orElse(null);
	            }
	            
	            // Si despues de eso sigue siendo null, se termina la llamada.
	            if (proyecto == null) return;
	            
				vista.ocultarVentanaPrincipal();
				vista.mostrarVentanaVerDatos(proyecto.getNombreProyecto(), proyecto.getEdificios());
				break;
			}
			case BUSCAR: {
				vista.ocultarVentanaPrincipal();
				vista.mostrarVentanaBuscar();
				this.realizarBusquedaDepartamentos(); 
				break;
			}
			case GUARDAR: {
				try {
					gestor.guardarCambiosDelPrograma();
				} catch(SQLException e) {
					vista.mostrarMensajeDeError(e);
				}
				System.exit(0);
			}
			case SALIR: {
				System.exit(0); 
				break;
			}	
		}
	}

	public void iniciarPrograma() {
		vista.iniciarVentanaPrincipal();
	}

	public void manejarOpcionesVerDatos(OpcionesVer opcion) {
		switch (opcion) {
			case COMPRAR:{
				this.comprarDepartamento();
				break;
			}
			case RESERVAR: {
				this.reservarDepartamento();
				//vista.reservarDepartamento();
				break;
			}
			case SALIR:{
				vista.cerrarVentanaVerDatos();
				vista.mostrarVentanaPrincipal();
				break;
			}
		}
	}

	private void reservarDepartamento() {
	    long edificioId = vista.getSelectedEdificioId();
	    String deptoCodigo = vista.getSelectedDepartamentoCodigo();

	    if (edificioId == -1 || deptoCodigo == null) return;

	    Edificio edificioSel = gestor.getMapEdificios().get(edificioId);
	    Departamento deptoSel = edificioSel.getDepartamentos().stream()
	            .filter(d -> d.getCodigo().equals(deptoCodigo))
	            .findFirst().orElse(null);

	    if (deptoSel != null && deptoSel.getEstado().equals(EstadoDepartamento.DISPONIBLE)) {
	        // El controlador le pide a la vista que inicie el proceso de registro para una reserva.
	        vista.solicitarRegistroUsuario(edificioSel, deptoSel, false);
	    }
	}

	private void comprarDepartamento() {
	    long edificioId 	= vista.getSelectedEdificioId();	    
	    String deptoCodigo	= vista.getSelectedDepartamentoCodigo();
		if (edificioId == -1 || deptoCodigo == null) return;
        
		Edificio edificioSel = gestor.getMapEdificios().get(edificioId);
		Departamento deptoSel = edificioSel.getDepartamentos().stream()
	            .filter(d -> d.getCodigo().equals(deptoCodigo))
	            .findFirst().orElse(null);
		
		if (deptoSel == null) return;
        
	    if (deptoSel.getEstado().equals(EstadoDepartamento.DISPONIBLE)) {
	        vista.solicitarRegistroUsuario(edificioSel, deptoSel, true);
	    } else {
	        vista.solicitarVerificacionUsuario(edificioSel, deptoSel);
	    }
	}

	public void generarNuevoRegistroUsuario(Comprador nuevoUsuario, Edificio edificioSel, Departamento deptoSel, boolean comprarSeleccionado) throws SQLException {
        try {
			gestor.guardarUsuarioEnDatabase(nuevoUsuario);
		} catch (SQLException e) {
			throw new SQLException("No se pudo registrar al usuario en la DB.");
		}
        
        deptoSel.setRutReserva(nuevoUsuario.getRut());
        deptoSel.setComprador(nuevoUsuario);
        
        if (comprarSeleccionado) deptoSel.setEstado(EstadoDepartamento.VENDIDO);
        else deptoSel.setEstado(EstadoDepartamento.RESERVADO);
        
        gestor.marcarProyectoParaModificar(edificioSel.getProyectoPadre().getId());
        
	    if (deptoSel.getEstado().equals(EstadoDepartamento.VENDIDO)) {
	        boolean deseaRecibo = vista.mostrarDialogoRecibo();
	    	if (deseaRecibo) {
                try {
					TextFileExporter.exportarReciboCompra(deptoSel.getComprador(), deptoSel);
				} catch (IOException e) {
					vista.mostrarMensajeDeError(e);
				}
	    	}
		}
	}
	
	public void confirmarVerificacionUsuario(String rut, Departamento deptoSel, Edificio edificioSel) {
		String rutReserva = deptoSel.getRutReserva();

		if (rutReserva == null || !rutReserva.equals(rut)) {
        	vista.mostrarMensajeRutInvalido();
        	return;
        }
		
		Comprador comprador = gestor.getCompradorPorRut(rut);
		if (comprador != null) deptoSel.setComprador(comprador);
		
		deptoSel.setRutReserva(rut);
		deptoSel.setEstado(EstadoDepartamento.VENDIDO);
        
        gestor.marcarProyectoParaModificar(edificioSel.getProyectoPadre().getId());
        vista.mostrarExitoRutCorrecto();
        
        boolean deseaRecibo = vista.mostrarDialogoRecibo();
        if (deseaRecibo) {
            try {
                TextFileExporter.exportarReciboCompra(deptoSel.getComprador(), deptoSel);
            } catch (IOException e) {
                vista.mostrarMensajeDeError(e);
            }
        }
	}
	
	public void iniciarProcesoDeRegistro() {
	    this.edificiosTemporales.clear();
	    vista.mostrarVentanaRegistrar();
	}
	
	public void manejarOpcionesRegistrar(OpcionesRegistrar opcion) {
		switch (opcion) {
			case AGREGAR_E:{
				this.agregarEdificioTemporal();
				break;
			}
			case REMOVER_E:{
				this.removerEdificioTemporal();
			    break;
			}
			case AGREGAR_D:{
				this.agregarDepartamentoTemporal();
				break;
			}
			case REMOVER_D:{
				this.removerDepartamentoTemporal();
			    break;
			}
			case REGISTRAR: {
				this.registrarProyecto();
			    break;
			}
			case SALIR:{
				vista.cerrarVentanaRegistrar();
				vista.mostrarVentanaPrincipal();
				break;
			}
		}
	}
	
	

	private void registrarProyecto() {
	    String nombreProyecto = vista.getNombreProyectoRegistrar();
	    String vendedor = vista.getVendedorRegistrar();
	    
	    if (nombreProyecto.isEmpty() || vendedor.isEmpty() || this.edificiosTemporales.isEmpty()) {
	        return;
	    }
	    
	    ProyectoInmobiliario nuevoProyecto = new ProyectoInmobiliario(nombreProyecto, vendedor, LocalDate.now());
	    for (Edificio e : this.edificiosTemporales) {
	        nuevoProyecto.addEdificio(e);
	    }
	    
	    gestor.agregarProyectoADatabase(nuevoProyecto);
	    
	    vista.mostrarMensajeExito("Proyecto '" + nombreProyecto + "' registrado con éxito.");
	    vista.cerrarVentanaRegistrar();
	    vista.cargarProyectosEnModeloTabla();
	    vista.mostrarVentanaPrincipal();
	}

	private void removerDepartamentoTemporal() {
		long edificioPadreId = vista.getSelectedEdificioId();
	    String deptoCodigoParaEliminar = vista.getSelectedDepartamentoCodigo();

	    if (edificioPadreId == -1 || deptoCodigoParaEliminar == null) return;
	    
	    Edificio edificioPadre = this.edificiosTemporales.stream()
	            .filter(e -> e.getId() == edificioPadreId)
	            .findFirst().orElse(null);
	    
	    if (edificioPadre == null) return;
	    
	    Departamento deptoAEliminar = edificioPadre.getDepartamentos().stream()
                .filter(d -> d.getCodigo().equals(deptoCodigoParaEliminar))
                .findFirst().orElse(null);
	    
	    if (deptoAEliminar == null) return;
	    if (deptoAEliminar.getId() > 0) {
            gestor.marcarDepartamentoParaEliminar(deptoAEliminar.getId());
        }
	    
        edificioPadre.getDepartamentos().remove(deptoAEliminar);
        vista.actualizarTablaDepartamentosRegistrar(edificioPadre.getDepartamentos());
	}

	private void agregarDepartamentoTemporal() {
	    long edificioPadreId = vista.getSelectedEdificioId();
	    if (edificioPadreId == -1) return;
	    
		Edificio edificioPadre = this.edificiosTemporales.stream()
		        .filter(e -> e.getId() == edificioPadreId)
		        .findFirst().orElse(null);
		
		if (edificioPadre == null) return;
		
		Departamento nuevoDepartamento = vista.solicitarDatosNuevoDepartamento();
		if (nuevoDepartamento == null) return;
		
        edificioPadre.agregarDepartamento(nuevoDepartamento);
        nuevoDepartamento.setEdificioPadre(edificioPadre);
        vista.actualizarTablaDepartamentosRegistrar(edificioPadre.getDepartamentos());
		
	}

	private void removerEdificioTemporal() {
	    long edificioId = vista.getSelectedEdificioId();
	    if (edificioId == -1) return;

	    // Se marca para eliminar (ya que es un ID valido)
		if (edificioId > 0) gestor.marcarEdificioParaEliminar(edificioId);

        // Lo quitamos de la lista temporal en memoria para que la UI se actualice.
		this.edificiosTemporales.removeIf(e -> e.getId() == edificioId);        
		// Y lo quitamos de la tabla visual.
        vista.actualizarTablaEdificiosRegistrar(this.edificiosTemporales);        
	}

	private void agregarEdificioTemporal() {
		Edificio nuevoEdificio = vista.solicitarDatosNuevoEdificio();
		if (nuevoEdificio == null) return;
		
		this.edificiosTemporales.add(nuevoEdificio);
        vista.actualizarTablaEdificiosRegistrar(this.edificiosTemporales);
	}

	public void edificioSeleccionadoEnRegistrar(long idEdificio) {
	    if (idEdificio == -1) return;
	    
	    Edificio edificioSeleccionado = this.edificiosTemporales.stream()
	            .filter(e -> e.getId() == idEdificio)
	            .findFirst().orElse(null);
	    if (edificioSeleccionado == null) return;
	    
        vista.actualizarTablaDepartamentosRegistrar(edificioSeleccionado.getDepartamentos());
	}

	public void edificioSeleccionadoEnVer(long idEdificio) {
	    if (idEdificio == -1) return;
	    
	    Edificio edificioSeleccionado = gestor.getEdificioPorId(idEdificio);
	    if (edificioSeleccionado == null) return;
	    
        vista.cargarDepartamentosEnTabla(edificioSeleccionado.getDepartamentos());
	}
	
	public void solicitarModificacionProyecto() {
	    long idProyecto = vista.getSelectedProyectoId();
	    if (idProyecto == -1) return;

	    ProyectoInmobiliario proyectoAModificar = gestor.getProyectoPorId(idProyecto);
	    if (proyectoAModificar == null) return; // Seguridad

	    this.edificiosTemporales.clear();
	    this.edificiosTemporales.addAll(proyectoAModificar.getEdificios());

	    vista.iniciarVentanaModificar(proyectoAModificar);
	}
	
	public void guardarCambiosModificacion(ProyectoInmobiliario proyectoOriginal) {
	    // 1. Obtener datos actualizados de la vista
	    String nuevoNombre = vista.getNombreProyectoRegistrar();
	    String nuevoVendedor = vista.getVendedorRegistrar();

	    // 2. Actualizar el objeto original en memoria
	    proyectoOriginal.setNombreProyecto(nuevoNombre);
	    proyectoOriginal.setVendedor(nuevoVendedor);
	    proyectoOriginal.getEdificios().clear();
	    proyectoOriginal.getEdificios().addAll(this.edificiosTemporales); // Usa la lista temporal

	    // 3. Marcar el proyecto para ser actualizado en la base de datos al salir
	    gestor.marcarProyectoParaModificar(proyectoOriginal.getId());

	    // 4. Cerrar y actualizar la UI
	    vista.mostrarMensajeExito("Proyecto actualizado correctamente.");
	    vista.cerrarVentanaModificar();
	    vista.cargarProyectosEnModeloTabla();
	    vista.mostrarVentanaPrincipal();
	}
	
	public void manejarOpcionesModificar(OpcionesModificar opcion, ProyectoInmobiliario proyecto) {
	    switch (opcion) {
	        case AGREGAR_E: {
	        	agregarEdificioTemporal();    	
	            break;
	        }
	        case REMOVER_E: {
	        	removerEdificioModificar();
	            break;
	        }
	        case MODIFICAR_E:{
	        	this.modificarEdificio();
	            break;
	        }
	        case AGREGAR_D: {
	        	agregarDepartamentoTemporal();
	            break;
	        }
	        case REMOVER_D: {
	        	removerDepartamentoModificar();
	            break;
	        }
	        case MODIFICAR_D:{
	        	this.modificarDepartamento();
	            break;
	        }
	        case GUARDAR_CAMBIOS: { 
	        	this.guardarCambiosModificacion(proyecto);
	            break;
	        }
	        case CANCELAR: {
	    	    vista.cerrarVentanaModificar();
	    	    vista.mostrarVentanaPrincipal();
	            break;
	        }
	    }
	}
	
	private void removerEdificioModificar() {
	    // Llama al getter correcto para la ventana de modificar
	    long edificioId = vista.getEdificioIdFromTablaModificar();
	    if (edificioId == -1) return;

	    if (edificioId > 0) {
	        gestor.marcarEdificioParaEliminar(edificioId);
	    }

	    this.edificiosTemporales.removeIf(e -> e.getId() == edificioId);        
	    
	    // Llama al método de actualización correcto para la ventana de modificar
	    vista.actualizarTablaEdificiosModificar(this.edificiosTemporales);        
	}

	
	private void removerDepartamentoModificar() {
	    long edificioPadreId = vista.getEdificioIdFromTablaModificar();
	    // Llama al nuevo getter que creamos
	    String deptoCodigoParaEliminar = vista.getSelectedDepartamentoCodigoFromTablaModificar();

	    if (edificioPadreId == -1 || deptoCodigoParaEliminar == null) return;
	    
	    Edificio edificioPadre = this.edificiosTemporales.stream()
	            .filter(e -> e.getId() == edificioPadreId)
	            .findFirst().orElse(null);
	    
	    if (edificioPadre == null) return;
	    
	    Departamento deptoAEliminar = edificioPadre.getDepartamentos().stream()
	            .filter(d -> d.getCodigo().equals(deptoCodigoParaEliminar))
	            .findFirst().orElse(null);
	    
	    if (deptoAEliminar == null) return;
	    if (deptoAEliminar.getId() > 0) {
	        gestor.marcarDepartamentoParaEliminar(deptoAEliminar.getId());
	    }
	    
	    edificioPadre.getDepartamentos().remove(deptoAEliminar);
	    
	    // Llama al método de actualización correcto
	    vista.actualizarTablaDepartamentosModificar(edificioPadre.getDepartamentos());
	}

	private void modificarDepartamento() {
	    // 1. Pedir a la vista los identificadores necesarios
	    long edificioId = vista.getEdificioIdFromTablaModificar();
	    String deptoCodigo = vista.getSelectedDepartamentoCodigo(); // Puedes reutilizar este método

	    if (edificioId == -1 || deptoCodigo == null) return;

	    // 2. Buscar el edificio y el departamento en la lista temporal
	    Edificio edificioPadre = this.edificiosTemporales.stream()
	            .filter(e -> e.getId() == edificioId)
	            .findFirst().orElse(null);

	    if (edificioPadre == null) return;

	    Departamento deptoAModificar = edificioPadre.getDepartamentos().stream()
	            .filter(d -> d.getCodigo().equals(deptoCodigo))
	            .findFirst().orElse(null);

	    if (deptoAModificar == null) return;

	    // 3. Pedir a la vista que muestre un diálogo y devuelva el objeto modificado
	    Departamento deptoModificado = vista.solicitarDatosModificarDepartamento(deptoAModificar);
	    
	    // 4. Si no se canceló, actualizar el objeto
	    if (deptoModificado != null) {
	        int index = edificioPadre.getDepartamentos().indexOf(deptoAModificar);
	        if (index != -1) {
	            edificioPadre.getDepartamentos().set(index, deptoModificado);
	        }

	        // 5. Ordenar a la vista que actualice la tabla de departamentos
	        vista.actualizarTablaDepartamentosModificar(edificioPadre.getDepartamentos());
	    }
	}

	public List<Edificio> obtenerEdificiosTemporales() {
		return edificiosTemporales;
	}

	public void edificioSeleccionadoEnModificar(long idEdificio) {
	    if (idEdificio == -1) return;

	    Edificio edificioSeleccionado = this.edificiosTemporales.stream()
	        .filter(e -> e.getId() == idEdificio)
	        .findFirst().orElse(null);

	    if (edificioSeleccionado != null) {
	        vista.actualizarTablaDepartamentosModificar(edificioSeleccionado.getDepartamentos());
	    }
	}
	
	private void modificarEdificio() {
	    // 1. Pedir el ID del edificio seleccionado a la vista
	    long edificioId = vista.getEdificioIdFromTablaModificar();
	    if (edificioId == -1) return;

	    // 2. Buscar el edificio en la lista temporal
	    Edificio edificioAModificar = this.edificiosTemporales.stream()
	        .filter(e -> e.getId() == edificioId)
	        .findFirst().orElse(null);

	    if (edificioAModificar == null) return;

	    // 3. Pedir a la vista que muestre un diálogo y nos devuelva el edificio con los nuevos datos
	    Edificio edificioModificado = vista.solicitarDatosModificarEdificio(edificioAModificar);

	    // 4. Si el usuario no canceló, actualizamos el objeto en nuestra lista temporal
	    if (edificioModificado != null) {
	        // Reemplazamos el objeto antiguo por el nuevo en la lista
	        int index = this.edificiosTemporales.indexOf(edificioAModificar);
	        if (index != -1) {
	            this.edificiosTemporales.set(index, edificioModificado);
	        }
	        
	        // 5. Ordenar a la vista que se actualice
	        vista.actualizarTablaEdificiosModificar(this.edificiosTemporales);
	    }
	}
	
	public void realizarBusquedaDepartamentos() {
	    FiltroBusqueda filtro = vista.getFiltroDelBusquedaVisual();
	    
	    if (filtro == null) {
	        return;
	    }

	    List<Departamento> departamentosEncontrados = gestor.buscarDepartamentosPorFiltro(filtro);

	    vista.actualizarTablaDepartamentosBusqueda(departamentosEncontrados);
	}
}
