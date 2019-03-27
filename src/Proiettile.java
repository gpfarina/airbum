
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
/**
 * La classe rappresenta un proiettile ed estende la classe OggettoInMovimento
 * che contiene le caratteristiche generali degli oggetti in movimento
 * */
public class Proiettile extends OggettoInMovimento {
	/**
	 * Costruttore della classe, imposta traiettoria e posizione iniziale del proiettile
	 * @param h		passo del proiettile necessario per applicare il metodo di Eulero
	 * @param posInit	posizione iniziale del proiettile (in generale in basso a sinistra della finestra)
	 * @param vInit 	velocità iniziale del proiettile
	 * @param aInit	angolo iniziale in radianti rispetto all'asse x 
	 * @param who ImageObserver da settare per l'immgine di questo colpo (sarà il pannello che lo contiene)
	 * */
	public Proiettile(double h, Point2D.Double posInit, double vInit, double aInit,ImageObserver who){
		super("images","proiettile.gif",who);
		this.setTraiettoria(new TraiettoriaProiettile(
				h, posInit, vInit, aInit));
		this.setPosizioneIniziale();
	}

	/**
	 * @see OggettoInMovimento#getImageHeight()
	 */
	@Override
	public int getImageHeight() {
		return this.getImmagine().getHeight(this.getImageObserver());
	}

	/**
	 * @see OggettoInMovimento#getImageWidth()
	 */
	@Override
	public int getImageWidth() {
		return this.getImmagine().getWidth(this.getImageObserver());
	}
}
