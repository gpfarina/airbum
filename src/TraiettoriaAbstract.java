import java.awt.geom.Point2D;

/**
 * Classe astratta rappresentante una traiettoria
 * offre metodi comuni a traiettoriaVelivolo e TraiettoriaProiettile
 */
public abstract class TraiettoriaAbstract  {
	//
	//Attributi
	//
	
	//punto di posizione istantanea
	private Point2D.Double posizione;
	//Passo per il calcolo del moto dell'oggetto
	private double PassoH;

	/**
	 * Costruttore di default
	 * setta il passo e inizializza l'oggetto posione
	 * @param h Passo della traiettoria
	 */
	public TraiettoriaAbstract(double h){
		this.setPassoH(h);
		posizione = new Point2D.Double();
	}
	

	/**
	 * Set the value of PassoH
	 * passo del moto per il calcolo del moto orizzontale uniforme del velivolo
	 * @param newVar the new value of PassoH
	 */
	protected void setPassoH ( double newVar ) {
		PassoH = newVar;
	}
	
	/**
	 * Get the value of PassoH
	 * passo del moto per il calcolo del moto orizzontale uniforme del velivolo
	 * @return the value of PassoH
	 */
	protected double getPassoH ( ) {
		return PassoH;
	}
	
	/**
	 * Setta il valore del punto di posizione istantanea
	 * @param x nuovo valore asse x
	 * @param y nuovo valore asse y
	 */
	protected void setPosizione (double x, double y) {
		posizione.setLocation(x, y);
	}  
	
	/**
	 * Get the value of posizione
	 * punto di posizione istantanea
	 * @return the value of posizione
	 */
	protected Point2D.Double getPosizione ( ) {
		return posizione;
	}
	/**
	 * Ritorna la posizione successiva dell'oggetto rispetto alla posizione attuale
	 *	@return posizione successiva
	 */
	public Point2D.Double 	getSucc(){
		return null;
	}


	/**
	 * Ritorna il verso del moto dell'oggetto che implementa Traiettoria
	 * @return verso
	 */
	public int getVerso(){
		return 0;
	}
	
}
