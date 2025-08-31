package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;


//Datos
import modelo.datos.DatosPersonales;
import modelo.datos.CuentaBancaria;

//Entidades
import modelo.entidades.Usuario;
import modelo.entidades.Identificador;
import modelo.entidades.Comprador;
import modelo.entidades.Vendedor;

//Gestor
import gestor.GestorInmobiliarioService;
import gestor.ProyectoInmobiliario;
import gestor.Reserva;

import modelo.ubicacion.Agregados;
import modelo.ubicacion.Departamento;
import modelo.ubicacion.Edificio;
import servicios.displayer.ConsoleDisplayer;
/**
 * @author honai
 */
public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		ConsoleDisplayer.menuPrincipal();
		
		
		
		//creando un Usuario 
		long idNuevo = 1L;
		
		DatosPersonales datosPersona1 = new DatosPersonales("11.222.333-4", "Hugo Alejandroid", "soyunemail@gmail.com", 912345678);  
		
		CuentaBancaria datosBancoP1 = new CuentaBancaria("HG-777");  
		
		Vendedor hugancioElVendedor = new Vendedor(idNuevo, datosPersona1, datosBancoP1);
		//Usuario hugancio = new Usuario(idNuevo, datosPersona1, datosBancoP1); 
		
		ProyectoInmobiliario proyectoUno = new ProyectoInmobiliario("Proyecto Prueba", hugancioElVendedor);
		
		
		Agregados agre = new Agregados("La pintana", true, false);
		Edificio edi = new Edificio("Faker", agre); 
		
		Departamento depa1 = new Departamento("1A", 2, 30, 3, 2);
		Departamento depa2 = new Departamento("2A", 2, 30, 3, 2);
		Departamento depa3 = new Departamento("3A", 2, 30, 3, 2);
		
		edi.agregarDepartamento(depa1);
		edi.agregarDepartamento(depa2);
		edi.agregarDepartamento(depa3);
		
		
		proyectoUno.agregarEdificio(edi);
		
		System.out.println("Quien compró el proyecto? "+ (proyectoUno.getVendedor()).getDatosPersonales().getNombre());
		
		bf.close();
	}
}
