import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.net.URL;

/**
 * Rappresenta un generale oggetto in movimento sullo schermo
 * ad esempio i colpi o il velivolo.
 * Si memorizzano qui l'immagine dell'oggetto e le sue coordinate attuali.
 * Questa classe contiene un oggetto di tipo Traiettoria il quale si preoccupa
 * di calcolare la traiettoria  di movimento dell'oggetto in considerazione.
 * 
 * */
public abstract class OggettoInMovimento{

	//immagine dell'oggetto
	private Image immagine;
	//traiettoria generica che segue l'oggetto (velivolo o proiettile)
	private TraiettoriaAbstract traiettoria;
	//Posizione attuale
	private Point2D.Double posizione;
	
	private ImageObserver panelObserver;
	
	/**
	 * Costruttore di default senza parametri
	 *
	 */
	public OggettoInMovimento(){
	}
	
	/**
	 *
	 * Costruttore con parametri, si preoccupa di impostare l'immagine dell'oggetto
	 * @param path path all'immagine
	 * @param nome path al nome del file immagine
	 * @param who Image observer da settare per l'immagine di questo velivolo in movimento 
	 */
	public OggettoInMovimento(String path, String nome, ImageObserver who){
		this.setImmagine(path, nome);
		this.setImageObserver(who);
	}
	
	/**
	 * Imposta la traiettoria dell'oggetto e inizializza
	 * la posizione iniziale dell'oggetto in base alla traiettoria calcolata
	 * @param tr Traiettoria da impostare
	 * */
	protected void setTraiettoria(TraiettoriaAbstract tr){
		this.traiettoria=tr;
		this.setPosizioneIniziale();
	}

	/**
	 * Imposta le coordinate iniziali dell'oggetto in base
	 * alla traiettoria appena calcolata.
	 * */
	protected void setPosizioneIniziale(){
		posizione= new Point2D.Double(
				this.getTraiettoria().getPosizione().getX(),
				this.getTraiettoria().getPosizione().getY());
	}

	/**
	 * Ritorna l'oggetto traiettoria che seguirà l'oggetto
	 * @return traiettoria
	 * */
	protected TraiettoriaAbstract getTraiettoria(){
		return this.traiettoria;
	}


	/**
	 * Ritorna l'immagine che rappresenta l'oggetto in movimento
	 * @return immagine
	 * */
	protected Image getImmagine(){
		return immagine;
	}


	/**
	 * Imposta l'immagine dell'oggetto
	 * @param path Path alla cartella contenente l'immagine
	 * @param nome nome dell'immagine

	 * */
	private void setImmagine(String path,String nome){
		URL imageUrl= ClassLoader.getSystemResource(path + System.getProperty("file.separator")+ nome);
		this.immagine=Toolkit.getDefaultToolkit().getImage(imageUrl);
		
	}
	
	/**
	 * Imposta l'immagine dell'oggetto a una immagine pre caricata
	 * @param toset immagine a cui settare quella in OggettoInMovimento
	 * 
	 **/
	protected void setImmagine(Image toset){
		this.immagine=toset;
	}

	/**
	 * Titorna la larghezza dell'immagine
	 * @return int Larghezza dell'immagine
	 */
	public abstract int getImageWidth();

	/**
	 * Titorna l'altezza dell'immagine
	 * @return int Altezza dell'immagine
	 */
	public abstract int getImageHeight();
	
	/**
	 * Ritorna la posizione attuale dell'oggetto nella traiettoria
	 * @return posizione
	 * */
	public Point2D.Double getPosizione(){
		return posizione;
	}
	
	/**
	 * Setta la posizione corrente del velivolo a una calcolata dalla traiettoria 
	 * */
	protected void setPosizione(Point2D.Double newPos){
		posizione = newPos;
	}
	
	/**
	 * Ritorna l'image observer corrente per l'immagine di questo velivolo in movimento
	 * @return Image observer corrente per l'immagine di questo oggetto in movimento
	 */
	protected ImageObserver getImageObserver(){
		return this.panelObserver;
	}
	
	/**
	 * Setta l'image observer per l'immagine di questo velivolo in movimento
	 * @param imgObs Image observer da settare
	 */
	protected void setImageObserver(ImageObserver imgObs){
		this.panelObserver = imgObs;
	}
	
	/**
	 * Imposta la posizione alla posizione successiva
	 * */
	public void setSucc(){
		this.posizione=traiettoria.getSucc();   
	}
	
	/**
	 * Disegna l'oggetto e imposta la posizione di questo alla posizione
	 * successiva nella sua traiettoria
	 * @param g 	Contesto grafico in cui disegnare
	 * @param obj 	Osservatore per disegnare l'immagine
	 * */
	public void draw(Graphics2D g, ImageObserver obj){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(this.getImmagine(),
				(int)this.getPosizione().getX(),
				(int)this.getPosizione().getY(),
				obj); 
	}
}
