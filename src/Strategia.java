import java.io.Serializable;
/**
 * Class Strategia
 * Memorizza la strategia di tiro attuale, ovvero 
 * numero colpi per la raffica e le variazioni
 * di velocità e angolo per le salve di colpi.
 * Verrà usata per gestire meglio la strategia di sparo attuale
 */
public class Strategia implements Serializable {

	/**
	 * Identificatore per la serializzazione
	 */
	private static final long	serialVersionUID	= 8701385873647343672L;
	//
	// Attributi
	//
	//Numero raffica colpi	
	private int nColpi;
	//Variazioni, per i colpi da 2 a 4, dell'angolo di tiro
	private double[] deltaGrad;
	//Variazioni, per i colpi da 2 a 4, della velocità di tiro
	private double[] deltaVel;

	/**
	 * Costruttore di default, imposta il numero colpi 
	 * a uno e le variazioni delta a zero
	 */
	public Strategia () { 
		this.setNColpi(1);
		this.setDeltaGrad(new double[]{0.0,0.0,0.0});
		this.setDeltaVel(new double[]{0.0,0.0,0.0});
	}

	/**
	 * Costruttore con parametri per settare la strategia attuale
	 * @param	nC	numero colpi per la salva
	 * @param 	dG	array  contenente le variazioni dell'angolo per ogni proiettile
	 * @param	dV	array  contenente le variazioni della velocità per ogni proiettile
	 */
	public Strategia (int nC, double[] dG, double[] dV) { 
		this.setNColpi(nC);
		this.setDeltaGrad(dG);
		this.setDeltaVel(dV);
	}
	//
	// Metodi
	//


	//
	// Metodi get e set
	//

	/**
	 * Setta il numero di colpi da sparare (raffica)
	 * @param nC numero di colpi
	 */
	public void setNColpi ( int nC ) {
		nColpi = nC;
	}

	/**
	 * Ritorna il numero di colpi settati
	 * @return Numero di colpi da sparare
	 */
	public int getNColpi ( ) {
		return nColpi;
	}


	/**
	 * Setta, per un colpo, la variazione d'angolo
	 * @param index iesimo proiettile su cui settare al variazione di angolo
	 * @param iVal	valore da impostare
	 */
	public void setDeltaGrad ( int index, double iVal ) {
		deltaGrad[index]=iVal;
	}

	/**
	 * Setta, per un colpo, la variazione di velocità
	 * @param index iesimo proiettile su cui settare al variazione di velocità
	 * @param iVal	valore da impostare
	 */
	public void setDeltaVel ( int index, double iVal ) {
		deltaVel[index]=iVal;
	}

	/**
	 * Setta, per ogni colpo, la variazione d'angolo
	 * @param dG Array variazioni angoli per ogni colpo
	 */
	public void setDeltaGrad ( double[] dG ) {
		deltaGrad = dG;
	}


	/**
	 * Ritorna il settaggio attuale della variazione di angolo settata
	 * per un colpo
	 * @param index	indice del colpo di cui ritornare la variazione d'angolo
	 * @return Variazione angolo per il colpo 
	 */
	public double getDeltaGrad ( int index ) {
		return deltaGrad[index];
	}
	/**
	 * Setta per ogni colpo, la variazione di velocità
	 * @param dV Array variazioni velocità per ogni colpo
	 */
	public void setDeltaVel ( double[] dV ) {
		deltaVel = dV;
	}

	/**
	 * Ritorna il settaggio attuale della variazione di velocità settata
	 * per un colpo
	 * @param index	indice del colpo di cui ritornare la variazione di velocità
	 * @return Variazione velocità per il colpo 
	 */
	public double getDeltaVel ( int index ) {
		return deltaVel[index];
	}

	/**
	 * Ritorna il settaggio attuale della variazione di velocità settata
	 * per ogni colpo
	 * @return Variazioni velocità per i colpi
	 */
	public double[] getDeltaVel ( ) {
		return deltaVel;
	}

	//
	// Altri metodi
	//


}
