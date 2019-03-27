/**
 * Class MainWindow
 * contiene l'entry-point del programma
 * 
 * */
public class Main{

	/**
	 * Main
	 * @param args Argodenti da linea di comandi che non saranno comunque utilizzati
	 * 		   
	 */	
	public static void main(String args[]){
		//Creo un oggetto per la gestione dei settaggi di gioco
		GestioneSettaggiGenerali genSettingsHandler =  new GestioneSettaggiGenerali();
		//Creo la finestra principale (JFrame contenitore generale)
		WindowGame finestraGioco = new	WindowGame(genSettingsHandler);
		finestraGioco.startGame();

	}
}