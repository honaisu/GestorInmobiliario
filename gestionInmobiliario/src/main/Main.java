package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import servicios.displayer.ConsoleDisplayer;

/**
 * @author honai
 */
public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		ConsoleDisplayer.menuPrincipal(bf);
		bf.close();
	}
}
