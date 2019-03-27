import java.awt.geom.Point2D;
import java.lang.Math;

/**
 * Class TraiettoriaVelivolo
 * Classe di Traiettoria Velivolo. Si occupa di calcolare i punti per un moto
 * orizzontale uniforme di velocita' v.
 * 
 */
public class TraiettoriaVelivolo extends TraiettoriaAbstract{
    	/**
    	 * definisce se il verso e' verso destra
    	 */
	public static final int VERSO_DESTRA=1;
	/**
	 * definisce se il verso e' verso sinistra
	 */
	public static final int VERSO_SINISTRA=-1;
	
	/**
	 * Verso non ancora settato utile in velivolo per il check della direzione
	 */
	public static final int VERSO_NONSETTATO=-2;
	//
	// Fields
	//

	
	/**
	 * definisce il verso del moto. 1: verso delle x crescenti (da sinistra a dest ra); -1: verso delle x decresenti (da destra a sinistra)
	 */
	private int verso;

	/**
	 * x Massima definita da lAreaGioco
	 */
	private int xMax,yMax;
	//Per animazione iniziale posizione in y
	private float position;
	//
	// Constructors
	// 

	/**
	 * costruttore di Traiettoria Velivolo.
	 * @param        h passo H
	 * @param        hAreaGioco altezza dell'area di gioco in pixel - altezza
	 * dell'immagine
	 * @param        wAreaGioco lunghezza dell'area di gioco in pixel - larghezza
	 * dell'immagine
	 */
	public TraiettoriaVelivolo( double h, int hAreaGioco, int wAreaGioco ){
		super(h);
		//inizializzazione delle variabili iniziali
		//Faccio in modo che la grandezza sia multiplo di h_vel 
		//altrimenti se parte da destra l'aereo potrebbe non fermarsi nella sosta di 5 secondi
		this.setXMax(((int)(wAreaGioco/h))*(int)h);
		this.setYMax(hAreaGioco);
		this.setPassoH(h);
		this.initTraiettoriaOrizz();

	}
	
	/**
	 * Costruttore per la traiettoria dell'animazione iniziale (bouncing) 
	 * @param h passo traiettoria in x
	 * @param hAreaGioco altezza finestra
	 * @param wAreaGioco larghezza finestra
	 * @param animazioneIniziale identificativo per sapere che vogliamo l'animazione iniziale
	 * 	il quale deve essere Velivolo.ANIMAZIONE_INIZIALE
	 */
	public TraiettoriaVelivolo( double h, int hAreaGioco, int wAreaGioco,int animazioneIniziale ){
		super(h);
		assert(animazioneIniziale == Velivolo.ANIMAZIONE_INIZIALE);
		//Posizione relativa alla corrente y dell'immagine dell'aereo
		//(intesa quindi come salto in y)
		this.position = (float) 0;
		//inizializzazione delle variabili iniziali
		this.setXMax(wAreaGioco);
		this.setYMax(hAreaGioco);
		this.setPassoH(h);
		
		this.setPosizione( (int)200, 
				(int)this.getYMax()-200 );
		this.setVerso(TraiettoriaVelivolo.VERSO_DESTRA);
	}
	//
	// Setter Methods
	//

	/**
	 * Set the value of verso
	 * definisce il verso del moto. 1: verso delle x crescenti (da sinistra a dest ra);
	 * -1: verso delle x decresenti (da destra a sinistra)
	 * @param newVar the new value of verso
	 */
	public void setVerso ( int newVar ) {
		verso = newVar;
	}



	/**
	 * Set the value of xMax
	 * x Massima definita da lAreaGioco
	 * @param newVar the new value of xMax
	 */
	private void setXMax ( int newVar ) {
		xMax = newVar;
	}
	
	/**
	 * Set the value of yMax
	 * y Massima definita da hAreaGioco
	 * @param newVar the new value of xMax
	 */
	private void setYMax ( int newVar ) {
		yMax = newVar;
	}
	
	//
	// Accessor methods
	//



	/**
	 * Get the value of verso
	 * definisce il verso del moto. 1: verso delle x crescenti (da sinistra a dest ra);
	 * -1: verso delle x decresenti (da destra a sinistra)
	 * @return the value of verso
	 */
	public int getVerso ( ) {
		return verso;
	}



	/**
	 * Get the value of xMax
	 * x Massima definita da lAreaGioco
	 * @return the value of xMax
	 */
	public int getXMax ( ) {
		return xMax;
	}  

	/**
	 * Get the value of yMax
	 * y Massima definita da hAreaGioco
	 * @return the value of yMax
	 */
	public int getYMax ( ) {
		return yMax;
	}  

	//
	// Other methods
	//

	/**
	 * Inizializza la posizione e il verso per il moto orizzontale del velivolo
	 * viene richiamato anche quando il velivolo ricompare in una posizione casuale
	 * dello schermo
	 * @return Ritorna l'oggetto traiettoria modificato
	 */
	public TraiettoriaVelivolo initTraiettoriaOrizz(){
		//il casting sul random mi restituisce o 0 o 1. Moltiplicando questo valore
		//per lAreaGioco la posizione iniziale mi verra' o a destra (0,yRand)
		//o a sinistra (lAreagioco,yRand)
		this.setPosizione( 
				(int)(this.getXMax()*(Math.round(Math.random()))), 
				(int) (this.getYMax()*Math.random()) );

		if (this.getPosizione().getX()==0) {
			this.setVerso(TraiettoriaVelivolo.VERSO_DESTRA);
		} else {
			this.setVerso(TraiettoriaVelivolo.VERSO_SINISTRA);
		}
		return this;
	}
	/**
	 * calcola il punto successivo per l'animazione orizzontale rettilinea
	 * e restituisce un valore di tipo punto 2D calcola una traiettoria rettilinea uniforme
	 * @return Point2D nuova posizione del velivolo
	 */
	public Point2D.Double getSucc(){

		//setto la nuova x. a seconda del verso verra' diminuita o aumentata
		this.setPosizione(this.getPosizione().getX() + 
				this.getPassoH() * this.getVerso(), 
				this.getPosizione().getY());

		//cambia il verso se e' arrivato a destra o a sinistra
		if (((int)this.getPosizione().getX() <= 0) || 
				((int)this.getPosizione().getX() >= this.getXMax())) {
			this.setVerso(this.getVerso()*(-1));
		}

		//ritorna la nuova posizione
		return this.getPosizione();

	}
	/**
	 * calcola il punto successivo e restituisce un valore di tipo punto 2D
	 * calcola una traiettoria per una animazione bouncing
	 * @param animIniziale identificativo per sapere che vogliamo l'animazione iniziale 
	 * 	deve essere Velivolo.ANIMAZIONE_INIZIALE
	 * @return       Point2D
	 */
	public Point2D.Double getSucc(int animIniziale){
		assert (animIniziale == Velivolo.ANIMAZIONE_INIZIALE);
    	position += (float) 0.08;
		if (position > 3) position = (float) 0;
	    
	  	double salto = Math.sin(position) *this.getYMax();
	  	this.setPosizione(this.getPosizione().getX()+this.getPassoH(), (int) (this.getYMax() - salto));
	  	int imageAereoY = (int) (this.getYMax() - salto);
	  	if (imageAereoY == -1) imageAereoY = this.getYMax() ; 
	    
	  	
	  	int imageAereoX = (int)this.getPosizione().getX();
	   
	  	if (imageAereoX > (this.getXMax())){
	  	    this.setPassoH(this.getPassoH()*-1);
	  	    this.setVerso(this.getVerso()*(-1));
	  		
		}
	  	if (imageAereoX < 1){
	  		this.setPassoH(this.getPassoH()*-1);
	  	  this.setVerso(this.getVerso()*(-1));
		}

		//ritorna la nuova posizione
		return this.getPosizione();

	}

}