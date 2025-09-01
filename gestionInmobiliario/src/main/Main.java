package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Interactividad (menú opciones)
import servicios.Interactive;
/*
 * Cada paquete de abajo se puede reemplazar a futuro.
 * Pues solo son utilizados para el testing (usuario, proyecto) de la app. :D
 */
// Gestor
import gestor.GestorInmobiliarioService;
import gestor.ProyectoInmobiliario;
// Datos
import modelo.datos.DatosPersonales;
import modelo.entidades.Vendedor;
// Edificios
import modelo.ubicacion.Agregados;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
/**
 * @author honai
 */
public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		/**
		 *  -- FUNCIONAMIENTO PRELIMINAR DEL PROGRAMA --
		 *  Mostrando como funciona el sistema de "vendedor"
		 *  (parte importante de nuestro futuro sistema).
		 */
		
		// Vendedor pivote para mostrar funcionamiento :)
		Vendedor hugancio = testVendedor();
		// Crea al menos un proyecto dentro del menú
		testProyecto(hugancio);
		// Menú
		Interactive.menuVendedorOpciones(bf, hugancio);
		System.out.println("Hasta luego!");
		bf.close();
	}
	
	public static Vendedor testVendedor() {
		//creando un Usuario
		long idNuevo = 1L;
		
		DatosPersonales datosPersona1 = new DatosPersonales("11.222.333-4", "Hugo Alejandroid", "soyunemail@gmail.com", 912345678);  
		
		
		Vendedor hugancioElVendedor = new Vendedor(idNuevo, datosPersona1);
		return hugancioElVendedor;
	}
	
	public static void testProyecto(Vendedor vendedor) {
		Agregados agre = new Agregados("La Pintana", true, false);
		Edificio edi = new Edificio("Faker Club.", agre); 
		
		Departamento depa1 = new Departamento("1A", 2, 30, 3, 2);
		Departamento depa2 = new Departamento("2A", 2, 30, 3, 2);
		Departamento depa3 = new Departamento("3A", 2, 30, 3, 2);
		
		edi.agregarDepartamento(depa1);
		edi.agregarDepartamento(depa2);
		edi.agregarDepartamento(depa3);
		ProyectoInmobiliario proyecto = new ProyectoInmobiliario(1, "Testing", vendedor, edi);
		GestorInmobiliarioService.agregarProyecto(proyecto);
	}
}
