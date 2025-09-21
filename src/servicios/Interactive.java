package servicios;

import java.io.BufferedReader;
import java.io.IOException;

import gestor.GestorInmobiliarioService;
import modelo.entidades.Vendedor;
import modelo.ubicacion.Agregados;
import modelo.ubicacion.Edificio;
import servicios.displayer.ConsoleDisplayer;

/**
 * Clase encargada de manejar la lógica de interactividad entre
 * el programa y el usuario. :3
 */
public class Interactive {
	/**
	 * Menú utilizado para poder ser capaz de mostrar las opciones disponibles
	 * del menú de vendedor.
	 */
	public static void menuVendedorOpciones(BufferedReader bf, Vendedor vendedor) throws IOException {
		/**
		 *  -- TEMPORAL --
		 *  Inicia en i = 2 por el proyecto ya creado en AVANCE
		 *  TODO: corregir.
		 */
		int i = 2;
		// Para nuevos proyectos.. Pivote
		String opcion;
		do {
			ConsoleDisplayer.limpiarTerminal();
			ConsoleDisplayer.menuVendedorProyectos(vendedor);
			opcion = bf.readLine();
			if (opcion.equals("0")) break;
			if (opcion.length() == 0) {
				System.out.println("Por favor, ingrese una opción");
				continue;
			}
			
			switch (opcion) {
			case "1":
				String nombreProyecto;
				System.out.print("Ingrese el nombre de su proyecto: ");
				nombreProyecto = bf.readLine(); // lectura simple
				System.out.println("Rellene con la información de su EDIFICIO asociado:");
				Edificio nuevoEdificio = crearEdificioPorInput(bf);
				if (GestorInmobiliarioService.crearNuevoProyecto(i, nombreProyecto, vendedor, nuevoEdificio)) {
					System.out.println("Creaste correctamente el proyecto "+ nombreProyecto + ".");
					i++;
				} else {
					System.out.println("Proyecto no aceptado.");
				}
				bf.readLine();
				break;
			case "2":
				GestorInmobiliarioService.mostrarProyectos();
				bf.readLine();
				break;
			default:
				System.out.println("Esa opción no está permitida.");
			}
		} while(true);
	}

	// TODO: Mejorar sistema :)
	public static Edificio crearEdificioPorInput(BufferedReader bf) throws IOException {
		String nombreEdificio, direccion;
		System.out.print("Nombre del edificio: ");
		nombreEdificio = bf.readLine();
		System.out.print("Dirección (nombre de la comuna): ");
		direccion = bf.readLine();
		Agregados agregados = new Agregados(direccion);
		Edificio edi = new Edificio(nombreEdificio, agregados);
		return edi;
	}
}
