import java.awt.Dimension;
import java.io.Serializable;
import java.awt.Toolkit;

/**
 * Classe per memorizzare i settaggi di gioco 
 * come Dimensione minima dello schermo, suoni ecc..
 * Le dimensioni di default dello schermo sono impostate a 800x600
 * ma se lo schermo della macchina ha una risoluzione minore allora la
 * risoluzione di default sarà impostata a quella grandezza.
 */
public class SettaggiGenerali implements Serializable{
	/**
	 * Identificatore per la serializzazione
	 */
	private static final long	serialVersionUID	= 5884591959160405715L;
	//	Settaggi per la dimensione minima della finestra di gioco
	private Dimension dimensioniSchermo;
	//presenza del suono
	private boolean suono;
	//Dimensione di default dello schermo
	private Dimension def = new Dimension(800, 600);
	//delay da un colpo all'altro in ms
	private int		tDelayColpi;
	//limite di tempo allo sparo dei colpi
	private int limiteTempoColpi;
	/**
	 * Costruttore di default
	 * Setta le variabili private dei settaggi generali
	 */
	public SettaggiGenerali(){
		Dimension screen = new Dimension();
		//se le dimenioni attuali dello schermo sono minori della
		//nostra di default allora settiamo come default quelle dello
		//schermo dell'utente
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		if ((screen.width < def.width) ||
				(screen.height<def.height)){
			def=screen;
		}
		this.tDelayColpi=200;	//200 ms di default
		this.setDimensioniFinestra(def);
		this.setSuono(true);
		this.setLimiteTempoColpi(15);
	}

	/**
	 * Setta la dimensione minima schermo
	 * @param dim Dimensione schermo
	 */
	public void setDimensioniFinestra(Dimension dim){
		this.dimensioniSchermo = dim;
	}

	/**
	 * Ritorna la dimensione minima schermo
	 * @return dim Dimensione schermo
	 */
	public Dimension getDimensioniFinestra(){
		return this.dimensioniSchermo ;
	}	
	
	/**
	 * Ritorna vero se il suono è abilitato
	 * @return vero se il suono è abilitato
	 */
	public boolean isSuonoOn(){
		return this.suono;
	}
	
	/**
	 * Imposta il suono a abilitato se passato true come parametro
	 * dibabilitato se passato false
	 * @param value booleano true se settare a on il suono
	 */
	public void setSuono(boolean value){
		this.suono = value;
	}
	
	/**
	 * Ritorna il delay settato tra un colpo e l'altro
	 * @return int Delay tra i colpi
	 */
	public int getDelayColpi(){
		return this.tDelayColpi;
	}
	
	/**
	 * Setta il delay per lo sparo tra un colpo e l'altro
	 * @param iVal delay da settare
	 */
	public void setDelayColpi(int iVal){
		this.tDelayColpi = iVal;
	}
	
	/**
	 * Ritorna il limite, in secondi, per l'attesa 
	 * dopo lo sparo; tempo dopo il quale la fase di
	 * sparo sarà resettata e i colpi in gioco fatti scomparire
	 * @return intero Limite tempo di sparo colpi totale
	 */
	public int getLimiteTempoColpi(){
		return this.limiteTempoColpi;
	}
	
	/**
	 * Setta il limite in secondi, per l'attesa 
	 * dopo lo sparo; tempo dopo il quale la fase di
	 * sparo sarà resettata e i colpi in gioco fatti scomparire
	 * @param iVal limite in secondi da settare
	 */
	public void setLimiteTempoColpi(int iVal){
		this.limiteTempoColpi = iVal;
	}
}
