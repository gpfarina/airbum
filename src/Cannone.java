import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.net.URL;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * Classe Cannone gestisce l'immagine e la rotazione del cannnone
 */
public class Cannone {
	//
	//Attributi
	//

	//Immagini per il cannone
	private Image imgCannone,imgRuotata;
	private URL imageUrl;
	//immagine per la rotazione
	private BufferedImage destinationBI = null;
	private Dimension screenDimension;
	//gradi di rotazione
	private double rotazione;

	/**
	 * Costruttore del cannone
	 * imposta l'immagine e le variabili locali
	 * @param gradi gradi di cui deve essere ruotato di default
	 * @param scDim dimensioni dello schermo
	 */
	public Cannone(int gradi,Dimension scDim){
		//carico le immagini
		imageUrl=ClassLoader.getSystemResource("images" + System.getProperty("file.separator")+ "1cannone.png");
		this.imgCannone=Toolkit.getDefaultToolkit().getImage(imageUrl);
		imageUrl=ClassLoader.getSystemResource("images" + System.getProperty("file.separator")+ "ruota.png");
		this.imgRuotata=Toolkit.getDefaultToolkit().getImage(imageUrl);

		//aspettiamo il caricamento di tutta l'immagine
		MediaTracker mt = new MediaTracker(new JPanel());
		mt.addImage(imgCannone, 0);
		mt.addImage(this.imgRuotata,1);
		try {
			mt.waitForAll();
		} 
		catch (InterruptedException ie) {
			GestioneErrori.error(GestioneErrori.ERROR_LOADING_IMAGE, ie);
		}
		//ruoto subito il cannone dei gradi di default 
		//presenti nella textfield dei controlli utente
		this.rotate(gradi);
		this.screenDimension=scDim;
	}

	//
	//Metodi set e get
	//

	/**
	 * Ritorna le coordinate, rispetto l'immagine del cannone,
	 * della sua bocca (utile per vedere da dove far uscire i colpi)
	 * @return coordinate della bocca del cannone
	 * */
	public Point.Double getCoordBocca(){
		double h = Math.sqrt(Math.pow(this.imgCannone.getWidth(null), 2)+Math.pow(this.imgCannone.getHeight(null), 2));
		return new Point.Double( Math.cos(Math.toRadians(this.rotazione))*h, Math.sin(Math.toRadians(this.rotazione))* h );
	}

	//
	//Altri metodi
	//

	/**
	 * Ruota l'immagine dei gradi specificati
	 * L'immagine di default non è toccata, ma
	 * viene modificata ogni volta una immagine d'appoggio
	 * che sarà poi quella che verrà disegnata  a schermo
	 * @param gradi di tot gradi
	 */
	public void rotate(int gradi){
		this.rotazione=gradi;

		BufferedImage sourceBI = new BufferedImage(imgCannone.getWidth(null), imgCannone
				.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) sourceBI.getGraphics();
		g.drawImage(imgCannone, 0, 0, null);

		AffineTransform at = new AffineTransform();

		// ruoto l'immagine di tot gradi nel suo contesto grafico
		at.rotate((90-gradi) * Math.PI / 180.0, sourceBI.getWidth() / 2, sourceBI
				.getHeight() );

		BufferedImageOp bio;
		bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

		destinationBI = bio.filter(sourceBI, null);

	}

	/**
	 * Disegna il cannone e la ruota in basso a sinistra dello schermo
	 * @param g 	Contesto grafico in cui disegnare
	 * @param obj 	Osservatore per disegnare l'immagine
	 * */
	public void draw(Graphics2D g, ImageObserver obj){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(this.destinationBI,
				0,
				(int)this.screenDimension.getHeight()-this.imgCannone.getHeight(obj)-10,
				obj); 
		g.drawImage(this.imgRuotata,
				-(this.imgRuotata.getWidth(obj)/2)+10,
				(int)this.screenDimension.getHeight()-(this.imgRuotata.getHeight(obj)/2),
				obj); 
	}

}
