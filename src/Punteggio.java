import java.io.Serializable;
import java.lang.StrictMath; //per l'arrotondamento
import java.lang.String;
/**
 * Class Punteggio
 * Gestisce la memorizzazione temporanea e il calcolo del punteggio della 
 * partita attuale.
 */
public class Punteggio implements Serializable{

	/**
	 * Usato per riconoscere la "versione" della classe
	 */
	private static final long	serialVersionUID	= -6521546412907773152L;
	//
	// Attributi
	//
	//Nome Giocatore	
	private String Nome = new String(); ;
	//Cognome Giocatore
	private String Cognome = new String();
	//Punteggio conseguito 
	private int punteggio;
	//Numero colpi sparati
	private transient int nColpi;
	//Numero velivoli abbattuti
	private transient int velivoliAbbattuti;

	/**
	 * Costruttore di default senza parametri
	 * 
	 */
	public Punteggio () {
		this.setNColpi(0);
		this.setVelivoliAbbattuti(0);
		this.setPunteggio(0);
		this.setNome("");
		this.setCognome("");
	}

	/**
	 * Costruttore di con parametri, settando il numero di colpi sparati
	 * e il numero di veicoli abbattuti inizialmente a zero
	 * @param nome Nome giocatore
	 * @param cognome Cognome giocatore
	 */
	public Punteggio (String nome, String cognome) {
		this.setNColpi(0);
		this.setVelivoliAbbattuti(0);
		this.setPunteggio(0);
		this.setNome(nome);
		this.setCognome(cognome);
	}

	//
	// Metodi
	//


	//
	// Metodi set e get
	//

	/**
	 * Setta il nome giocatore
	 * @param nome Nome del giocatore
	 */
	public void setNome ( String nome ) {
		this.Nome = nome;
	}

	/**
	 * Ritorna il nome del giocatore
	 * @return nome giocatore
	 */
	public String getNome ( ) {
		return this.Nome;
	}

	/**
	 * Setta il cognome del giocatore
	 * @param cognome Cognome del giocatore
	 */
	public void setCognome ( String cognome ) {
		this.Cognome = cognome;
	}

	/**
	 * Ritorna il cognome del giocatore
	 * @return cognome giocatore
	 */
	public String getCognome ( ) {
		return this.Cognome;
	}

	/**
	 * Aggiorna il punteggio del giocatore in modo dinamico
	 * ovvero in base al numero di veicoli colpiti e i colpi sprecati
	 */
	public void setPunteggio () {
		this.punteggio = (int) StrictMath.round((this.getVelivoliAbbattuti() * 1000) /
				this.getNColpiSparati());
	}

	/**
	 * Aggiorna il punteggio del giocatore con valore fisso
	 * @param val Valore da settare come punteggio giocatore
	 */
	public void setPunteggio (int val) {
		this.punteggio = val;
	}

	/**
	 * Ritorna il punteggio del giocatore
	 * @return Punteggio del giocatore
	 */
	public int getPunteggio ( ) {
		return this.punteggio;
	}

	/**
	 * Setta il numero di colpi sparati
	 * @param nC Numero colpi sparati
	 */
	private void setNColpi ( int nC ) {
		this.nColpi = nC;
	}

	/**
	 * Ritorna il numero di colpi sparati dall'utente
	 * @return Numero di colpi sparati
	 */
	public int getNColpiSparati ( ) {
		return this.nColpi;
	}

	/**
	 * Setta il numero di velivoli abbattuti
	 * @param nV numero velivoli abbattuti
	 */
	private void setVelivoliAbbattuti ( int nV ) {
		this.velivoliAbbattuti = nV;
	}

	/**
	 * Ritorna il numero di velivoli abbattuti
	 * @return Numero velivoli abbattuti
	 */
	public int getVelivoliAbbattuti ( ) {
		return this.velivoliAbbattuti;
	}

	//
	// Altri metodi
	//

	/**
	 * Incrementa il numero di velivoli abbattuti di 1 
	 */
	public void incVelivoli(  )
	{
		this.setVelivoliAbbattuti(this.getVelivoliAbbattuti() + 1);
	}


	/**
	 * Incrementa il numero di colpi sparati di 1
	 */
	public void incColpi(  )
	{
		this.setNColpi(this.getNColpiSparati() + 1);
	}


}
