package servicios.displayer;

import gestion.GestorInmobiliarioService;
import servicios.displayer.frames.MainFrame;

public class VisualDisplayerV2 {
	private GestorInmobiliarioService gestorService;
	private MainFrame mainFrame;
	
	public VisualDisplayerV2(GestorInmobiliarioService gestorService) {
		this.gestorService = gestorService;
		
		mainFrame = new MainFrame(gestorService);
	}
	
	public void initialize() {
		mainFrame.inicializarComponentes();
	}
}
