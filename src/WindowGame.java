import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
/**
 * Class WindowGame
 * Gestisce la main window del gioco. 
 * Frame principale, si occupa di caricare, in base al flusso di gioco
 * i vari pannelli.
 * 
 */
@SuppressWarnings("serial")
public class WindowGame extends JFrame{
	//
	//Attributi
	//
	//File xml per SynthLookAndFeel contenente le specifiche dell'aspetto grafico
	private static final String xml = "synth.xml";

	//Identificatori dei pannelli da inserire in questo frame

	public static final int PANEL_INIZIALE=0, PANEL_GIOCO=1, PANEL_SETTINGS=2;
	//Oggetto per la gestione dei settaggi generali di gioco
	private GestioneSettaggiGenerali gSettings; 
	//Oggetto per la gestione delle strategie di gioco
	private GestioneStrategia gStrategia;
	//Pannello iniziale
	private MyPanelIniziale panelSchermataIniziale;
	//Pannello in cui saranno visualizzate le animazioni del velivolo e dei proiettili
	private MyPanelAnimazioni panelGrafica;
	//Pannello per il controllo delle opzioni di gioco
	private MyPanelOption panelControls;
	//Panello per gestire le impostazioni generali di gioco
	private MyPanelSettings panelImpostation;

	//Nome e Cognome del giocatore
	private String nome=null, cognome=null;

	/**
	 * Costruttore con parametri
	 * Inizializza l'aspetto del gioco con il look and feel giusto,
	 * la grandezza del frame  
	 * e memorizza il gestore dei settaggi generali
	 * @param gestSettings Settaggi caricati dal file
	 */
	public WindowGame(GestioneSettaggiGenerali gestSettings){
		//Imposto l'attributo locale per memorizzare le impostazioni di gioco
		this.gSettings = gestSettings;
		//Imposto l'aspetto del Frame in base alle impostazioni caricate da file
		this.setFrameLayout(this.getSettaggiGenerali().getDimensioniFinestra());
		//Inizializzo il look and feel personalizzato (synthlookandfeel)
		this.initLookAndFeel();
	}



	protected void processWindowEvent(WindowEvent e1) {
		super.processWindowEvent(e1);
		if (e1.getID() == WindowEvent.WINDOW_CLOSING&&this.panelGrafica!=null) {
			this.getPanelAnimazioni().stopTimers();
			this.getPanelAnimazioni().getGp().aggiornaRisultati(this.getPanelAnimazioni().getPuntiAttuali());
			this.getPanelAnimazioni().getGp().saveToFile();
		} 	
		if(e1.getID() == WindowEvent.WINDOW_CLOSING){
			System.exit(0);
		}
	}

	//
	//Metodi set e get
	//

	/**
	 * Prepara il layout del frame; ovvero la grandezza della finestra
	 * e l'aspetto generale di questo frame
	 * @param dim Dimensione a cui impostare la finestra
	 */
	public void setFrameLayout(Dimension dim){

		//La finestra non può essere ridimensionata in qanto la dimensione della finestra è 
		//scelta dall'utente nel pannello di impostazioni
		this.setResizable(false);	

		//Setto il posizionamento della finestra a un limite accettabile
		//ovvero il trenta percento della differenza tra la risoluzione dello 
		//schermo e quella minima della finestra
		Dimension position = new Dimension();
		position = Toolkit.getDefaultToolkit().getScreenSize();
		position.setSize( ((position.getWidth() - dim.getWidth())/100)*30,
				((position.getHeight() - dim.getHeight())/100)*30 );

		//Imposto la dimensione del frame
		this.setBounds(position.width,position.height,dim.width,dim.height);
	}

	/**
	 * Imposta nel Frame WindowGame il pannello richiesto
	 * NB: si noti che il metodo removeAll
	 * su this.getContentPane serve a rimuovere tutti i componenti fino ad ora 
	 * inseriti, mentre dopo ogni inserimento di un nuovo pannello nel WindowGame
	 * bisogna chiamare un revalidate sulla componente inserita per mostrare i
	 * cambiamenti.
	 * @param i indice del pannello da mostrare (0- pannello iniziale, 1-pannelli di gioco,
	 * 		2-impostazioni, 3-about)
	 */
	public void setPanel(int i){
		//Tolgo tutti i pannelli fino ad ora impostati
		//this.setLayout(null);
		this.getContentPane().removeAll();
		switch(i){
			case WindowGame.PANEL_INIZIALE :
				//Aggiungo il pannello per la schermata iniziale
				panelSchermataIniziale = new MyPanelIniziale(this);
				this.getContentPane().add(panelSchermataIniziale);
				this.panelSchermataIniziale.revalidate();
				break;
			case WindowGame.PANEL_GIOCO :
				this.setLayout(new BorderLayout());
				//MyPanelSfondo prova = new MyPanelSfondo(this);
				//this.getContentPane().add(prova, BorderLayout.NORTH);
				//prova.revalidate();
				//Pannello dove verranno disegnate le animazioni
				MyDialogInputPlayer input;
				input= new MyDialogInputPlayer(this);
				if (this.getNomePlayer()!=null && this.getCognomePlayer()!=null){
					//Pannello per i controlli di gioco
					this.panelControls = new MyPanelOption(this);
					this.getContentPane().add(panelControls,BorderLayout.CENTER);
					this.panelControls.revalidate();


					this.panelGrafica = new MyPanelAnimazioni(this, this.getNomePlayer(), this.getCognomePlayer());
					//this.panelGrafica.setOpaque(false);
					this.getContentPane().add(panelGrafica,BorderLayout.NORTH);
					this.panelGrafica.revalidate();
				}
				else
					this.setPanel(WindowGame.PANEL_INIZIALE);

				break;
			case WindowGame.PANEL_SETTINGS :
				this.panelImpostation = new MyPanelSettings(this,gSettings); 
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				}
				catch (ClassNotFoundException e1) {
					GestioneErrori.error(GestioneErrori.ERROR_CLASS_NOT_FOUND_EXCEPTION, e1);
				} catch (InstantiationException e1) {
					GestioneErrori.error(GestioneErrori.ERROR_LOOK_AND_FEEL_CREATING_CLASS, e1);
				} catch (IllegalAccessException e1) {
					GestioneErrori.error(GestioneErrori.ERROR_LOOK_AND_FEEL_ILLEGAL_CLASS_ACCESS, e1);
				} catch (UnsupportedLookAndFeelException e1) {
					GestioneErrori.error(GestioneErrori.ERROR_LOOK_AND_FEEL_UNSUPPORTED_STYLE, e1);
				}
				SwingUtilities.updateComponentTreeUI(panelImpostation);

				this.getContentPane().add(panelImpostation);
				this.panelImpostation.revalidate();
				break;
		}

		this.repaint();
	}

	/**
	 * Ritorna i l'oggetto per la gestione delle impostazioni generali di gioco
	 * @return Riferimento all'oggetto contenente i settaggi generali di gioco
	 */
	public SettaggiGenerali getSettaggiGenerali(){
		return this.gSettings.getSettings();
	}

	/**
	 * Ritorna i l'oggetto per la gestione delle strategie di gioco
	 * @return Riferimento all'oggetto contenente la gestione delle strategie di gioco
	 */
	public GestioneStrategia getStrategiaGioco(){
		return this.gStrategia;
	}

	/**
	 * Ritorna il nome del file xml per il SynthLookAndFeel
	 * @return Stringa contenente il nome del file
	 */
	public static String getXmlNome(){
		return WindowGame.xml;
	}

	/**
	 * Ritorna il pannello dove vengono disegnate le animazioni
	 * metodo utile all'interno di mypanelOption
	 * @return MypanelGame rappresentante il pannello dove avvengono le animazioni
	 */
	public MyPanelAnimazioni getPanelAnimazioni(){
		return this.panelGrafica;
	}

	/**
	 * Ritorna il pannello dove vengono impostate le opzioni durante il gioco
	 * come numero colpi e le tattiche di tiro
	 * @return MypanelOption rappresentante il pannello delle opzioni di gioco
	 */
	public MyPanelOption getPanelControls(){
		return this.panelControls;
	}

	/**
	 * Setta il nome del giocatore
	 * @param nome
	 */
	public void setNomePlayer(String nome){
		this.nome=nome;
	}

	/**
	 * Setta il cognome del giocatore
	 * @param cognome
	 */
	public void setCognomePlayer(String cognome){
		this.cognome=cognome;
	}

	/**
	 * Ritorna il nome del giocatore
	 * @return String nome giocatore
	 */
	public String getNomePlayer(){
		return this.nome;
	}

	/**
	 * Ritorna il cognome giocatore
	 * @return String cognome giocatore
	 */
	public String getCognomePlayer(){
		return this.cognome;
	}
	//
	//Altri metodi
	//

	/**
	 * Carica il primo pannello e imposta come attivo il flusso dell'applicazione
	 */
	public void startGame(){
		//Schermata iniziale
		this.setPanel(WindowGame.PANEL_INIZIALE);
		this.setVisible(true);
	}

	/**
	 * Inizializza l'aspetto  del gioco
	 * utilizzando uno stile personalizzato di nome SynthLookAndFeel.
	 * Se questo non viene trovato o vi sono degli errori nel caricamento
	 * dei file xml si fa un revert all LookAndFeel cross platform metal.
	 */
	public void initLookAndFeel(){
		try {
			SynthLookAndFeel laf = new SynthLookAndFeel();
			laf.load(MyGeneralJPanel.class.getResourceAsStream(getXmlNome()),
					MyGeneralJPanel.class);
			UIManager.setLookAndFeel(laf);
		} catch(Exception e) {

			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch(Exception f){
				f.getStackTrace();
			}
		}
	}


}

