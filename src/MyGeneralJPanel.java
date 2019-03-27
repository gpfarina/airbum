import javax.swing.JPanel;
import javax.swing.BorderFactory;

/**
 * Classe che estende JPanel e verrà usata come supporto 
 * per tutti i pannelli usati nel gioco.
 */
@SuppressWarnings("serial")
public class MyGeneralJPanel extends JPanel  {

	//
	//Attributi
	//

	//Si usano attributi privati e non protected per avere maggiore
	//distacco dalle classi che implementano questa classe e una maggiore possibilità
	//di interfacciamento e gestione di questi attributi.

	//Window root contenete questo pannello
	private WindowGame	rootJFrame;

	/**
	 * Costruttore, inizializza l'aspetto 
	 * e setta le impostazioni comuni a tutti i pannelli 
	 * @param root Root window conentente il pannello 
	 *
	 */
	public MyGeneralJPanel(WindowGame root){
		this.setRootJFrame(root);
		this.initPanelAspect();
	}

	/**
	 * Setta il WindowGame contenitore di questo pannello
	 * @param root WindowGame (JFrame) padre di questo pannello
	 */
	public void setRootJFrame(WindowGame root){
		rootJFrame=root;
	}

	/**
	 * Restituisce il WindowGame contenitore di questo pannello
	 * @return Riferimento al WindowGame (JFrame) padre
	 */
	public WindowGame getRootJFrame(){
		return this.rootJFrame;
	}

	/**
	 * Inizializza l'aspetto comune a tutti i pannelli (JPanel) del gioco
	 */
	public void initPanelAspect(){	
		this.setBorder(BorderFactory.createEtchedBorder(new java.awt.Color(0,0,0), new java.awt.Color(0,0,0)));
		this.setBackground(new java.awt.Color(236,255,236));
		this.setSize(this.getRootJFrame().getSize());
	}

}
