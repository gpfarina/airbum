import java.awt.geom.Point2D;
import java.lang.Math;

/**
 * Classe TraiettoriaProiettile per la traiettoria del proiettile.cacca
 * L'inizializzazione dell'oggetto di tipo TraiettoriaProiettile dovra' essere
 * fatta passando il passo h, il punto iniziale di lancio, la velocita' e l'angolo di tiro.
 */

@SuppressWarnings("unused")
public class TraiettoriaProiettile extends TraiettoriaAbstract{

	//
	// Fields
	//

	/**
	 * costante che definisce il passo per il calcolo della funzione di Eulero
	 */                
	private static double AccelG = 9.8665;

	/**
	 * variabile per la velocita' orizzontale
	 */
	private double vx;
	/**
	 * variabile per la velocita' verticale
	 */
	private double vy;

	//
	// Constructors
	//
	/**
	 * @param h passo
	 * @param posIniz punto iniziale
	 * @param vIniz velocita' iniziale
	 * @param aIniz angolo iniziale
	 */
	public TraiettoriaProiettile( double h, Point2D.Double posIniz, double vIniz, double aIniz ){
		super(h);
		//inizializzazione delle variabili iniziali
		this.setPosizione(posIniz.getX(),posIniz.getY());
		
		this.setVx(Math.cos(aIniz)*vIniz);  //velocita' orizzontale
		this.setVy(Math.sin(aIniz)*vIniz);  //velocita' verticale

	}

	//
	// Accessor Methods
	//

	/**
	 * Restituisce il valore dell'accelerazione di gravita'
	 * @return valore dell'accelerazione di gravita'
	 */
	private double getAccelG ( ) {
		return AccelG;
	}

	/**
	 * Ritorna il valore della variabile per la velocita' orizzontale
	 * @return valore della velocita' orizzontale
	 */
	private double getVx () {
		return vx;
	}

	/**
	 * Ritorna il valore della variabile per la velocita' verticale
	 * @return valore della velocita' verticale
	 */
	private double getVy ( ) {
		return vy;
	}
	//
	// Setter Methods
	//                                

	/**
	 * Setta il valore per la variabile per la velocita' orizzontale
	 * @param newVy nuovo valore per la velocita' orizzontale
	 */
	private void setVx ( double newVx ) {
		this.vx = newVx;
	}

	/**
	 * Setta il valore per la variabile per la velocita' verticale
	 * @param newVy nuovo valore per la velocita' verticale
	 */
	private void setVy ( double newVy ) {
		this.vy = newVy;
	}    


	//
	// Other methods
	//


	/**
	 * calcola il punto successivo tramite la formula di Eulero.
	 * restituisce un dato di tipo Point
	 * @return nuova posizione
	 */
	public Point2D.Double getSucc(){

		//aumento la coordinata X e la coordinata Y secondo il metodo di Eulero
		this.setPosizione(
				(this.getPosizione().getX() + this.getPassoH() * this.getVx()),
				(this.getPosizione().getY() - this.getPassoH() * this.getVy()));
		//diminuisco la Vy
		this.setVy(this.getVy() - this.getPassoH() * this.getAccelG());
		//ritorno la nuova posizione
		return this.getPosizione();

	}

	public int getVerso(){
		return 0;
	}
}