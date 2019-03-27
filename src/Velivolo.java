import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;
import java.net.URL;


import javax.swing.JPanel;

/**
 * 
 * Classe che gestisce la gestione del velivolo.
 * Si preoccupa di amministrare la gestione dell'immagine corrente
 * e della traiettoria del velivolo, generando un punto a caso sullo schermo in y
 * da dove questo dovrà partire in base alla grandezza della finestra dell'area di gioco. 
 *
 */
public class Velivolo extends OggettoInMovimento {
	
	//Variabili statiche usate come identificativi nelle chiamate esterne a funzioni di Velivolo
//	Se l'animazione che deve essere eseguita è quella iniziale (bouncing)
	public static final int ANIMAZIONE_INIZIALE=0, 
		//Tipi di rappresentazione del velivolo
		RAPP_STATICO=1, RAPP_ANIMATO=2, RAPP_SIMBOLICO=3, RAPP_ESPLOSO=4, RAPP_NULL=5;
	private Image immagineSx, immagineDx, immagineEsploso;
	//contiene l'identificativo della rappresentazione corrente dell'aereo
	private int rapprCorrente=Velivolo.RAPP_NULL,rapprPrimaDiEsploso=Velivolo.RAPP_NULL;
	private MediaTracker tracker = new MediaTracker(new JPanel());
	//Contiene il verso corrente dell'aereo, utile per non reimpostare ogni volta
	//l'immagine
	//imposto a non settato, la prima volta quindi verrà settato correttamente e richiamati
	//i metodi per lo switch dell'immagine verso sx o dx in modo da impostare correttamente
	//le variabili di loro compentenza
	private int versoCorrente = TraiettoriaVelivolo.VERSO_NONSETTATO;
	//Flag che determina lo stato dell'aereo in esploso se questa è impostata a vero
	private boolean FlagEsploso;
	//dimensioni correnti dell'aereo e dimensioni a cui settare la forma simbolica dell'aereo(wSAereo e hSAereo)
	private int wAereo,hAereo,wSAereo,hSAereo,fattoreImg;
	//posizioni della coda e dell'elica dell'aereo in forma simbolica
	private double poscodaX,poselicaX,fattoreScala;
	//Url per il caricamento delle immagini
	private URL imageUrl;

	/**
	 * Costruttore della classe
	 * @param h	passo necessario per l'algoritmo di Eulero
	 * @param hAreaGioco altezza finestra di disegno
	 * @param wAreaGioco larghezza finestra di disegno	
	 * @param imgObs ImageObserver Per l'immagine dell'aereo
	 * */
	public Velivolo( int h, int hAreaGioco, int wAreaGioco, ImageObserver imgObs){
		this.fattoreScala=this.fattoreImg=1;
		//Decido un fattore di scala per l'aereo in base alle dimensioni dello schermo
		if(wAreaGioco>=1024){
			this.fattoreScala=1.1;
			this.fattoreImg=2;
		}
		//per schermi grandi
	
		if (wAreaGioco >=1280 && (hAreaGioco+160) >= 1024){
			this.fattoreImg=3;
			this.fattoreScala=1.3;
		}
		
		wSAereo=wAereo=(int)(80.0*(this.fattoreScala));
		hSAereo=hAereo=(int)(30.0*(this.fattoreScala));

		this.setImageObserver(imgObs);
		
		hAreaGioco = hAreaGioco/2 - this.getImageHeight();
		wAreaGioco = wAreaGioco - this.getImageWidth();
		this.setTraiettoria(new TraiettoriaVelivolo(h, hAreaGioco, wAreaGioco));
		
		this.cambiaTipoRappresentazione(Velivolo.RAPP_SIMBOLICO);
		
		this.setEsploso(false);
	}

	/**
	 * Costruttore per l'aereo nella animazione iniziale bouncing
	 * setta l'immagine e la traiettoria
	 * @param h passo in x
	 * @param hAreaGioco altezza finestra
	 * @param wAreaGioco larghezza finestra
	 * @param imgObs image observer per l'immagine del velivolo
	 * @param iniz identificatore per sapere che vogliamo l'animazione iniziale, deve essereVelivolo.ANIMAZIONE_INIZIALE
	 */
	public Velivolo (int h, int hAreaGioco, int wAreaGioco, ImageObserver imgObs,int iniz){
		assert( iniz == Velivolo.ANIMAZIONE_INIZIALE);
		this.fattoreImg=3;
		this.setImageObserver(imgObs);
		
		this.cambiaTipoRappresentazione(Velivolo.RAPP_ANIMATO);
		hAreaGioco = hAreaGioco - this.getImageHeight();
		wAreaGioco = wAreaGioco - this.getImageWidth();
		this.setTraiettoria(new TraiettoriaVelivolo(h, hAreaGioco,
				wAreaGioco,Velivolo.ANIMAZIONE_INIZIALE));
		this.setEsploso(false);
		this.checkAndSwitchDirection();
	}
	//
	//Metodi set e get
	//

	/**
	 * Imposta la posizione alla posizione successiva per l'animazione del 
	 * pannello iniziale (bouncing)
	 * @param iniz identificatore per sapere che vogliamo l'animazione iniziale, deve essere iniz == Velivolo.ANIMAZIONE_INIZIALE
	 * */
	public void setSucc(int iniz){
		assert( iniz == Velivolo.ANIMAZIONE_INIZIALE );
		TraiettoriaVelivolo tmp = ((TraiettoriaVelivolo)this.getTraiettoria());
		
		this.setPosizione(tmp.getSucc(Velivolo.ANIMAZIONE_INIZIALE));   

	}
	
	/**
	 * Ritorna la larghezza dell'aereo
	 * @return intero contenente la larghezza dell'aereo
	 */
	public int getImageWidth(){
		return this.wAereo;
	}

	/**
	 * Setta la larghezza dell'aereo
	 * @param toSet larghezza a cui settare l'aereo
	 */
	public void setImageWidth(int toSet){
		this.wAereo=toSet;
	}

	/**
	 * Ritorna l'altezza dell'aereo
	 * @return intero contenente l'altezza dell'aereo
	 */
	public int getImageHeight(){
		return this.hAereo;
	}


	/**
	 * Setta l'altezza dell'aereo
	 * @param toSet altezza a cui settare l'aereo
	 */
	public void setImageHeight(int toSet){
		this.hAereo=toSet;
	}

	/**
	 * Cambia lo stato "esploso" dell'aereo al valore del parametro passato.
	 * (es: l'aereo è esploso se il parametro è true)
	 * @param value valore booleano da impostare come stato esploso
	 */
	public void setEsploso(boolean value){
		
		if(value){
			this.cambiaTipoRappresentazione(Velivolo.RAPP_ESPLOSO);
		}
		
		this.FlagEsploso = value;
		
		if(!value){
			this.cambiaTipoRappresentazione(this.rapprPrimaDiEsploso);
		}
	}

	/**
	 * Ritorna il valore dello stato dell'aereo riguardo l'essere esploso
	 * @return booleano true se l'aereo è sullo stato esploso, false altrimenti.
	 */
	public boolean isEsploso(){
		return this.FlagEsploso;
	}

	/**
	 * Ritorna il verso corrente del velivolo
	 * @return int Verso della traiettoria corrente vedere TraiettoriaVelivolo.Verso_*
	 */
	public int getVerso(){
		return this.getTraiettoria().getVerso();
	}
	
	//
	//Cambi dell'immagine in base alla sua direzione
	//

	/**
	 * Cambia l'immagine dell'oggetto in movimento corrente a quella dell'aereo verso sinistra
	 *
	 */
	private void switchImageToSx(){
		if(this.rapprCorrente != Velivolo.RAPP_SIMBOLICO){
			this.setImmagine(this.immagineSx);
		}
		else{
			poscodaX=this.getImageWidth()-(this.getImageWidth()/5);
			poselicaX=0;
		}
	}


	/**
	 * Cambia l'immagine dell'oggetto in movimento corrente a quella dell'aereo verso destra
	 *
	 */
	private void switchImageToDx(){
		if(this.rapprCorrente != Velivolo.RAPP_SIMBOLICO ){
			this.setImmagine(this.immagineDx);
		}
		else{
			this.poscodaX = 0;
			this.poselicaX = this.getImageWidth()-(this.getImageWidth()/15);
		}
	}


	
	/**
	 * Controlla il verso dell'aereo e nel caso richiama il metodo per 
	 * cambiare l'immagine della rappresentazione corrente in modo che questa
	 * segua il verso giusto.
	 */
	public void checkAndSwitchDirection(){
		assert(!this.isEsploso());
		if(this.versoCorrente!=this.getTraiettoria().getVerso()){
			this.versoCorrente=this.getTraiettoria().getVerso();
			if(this.getTraiettoria().getVerso()
					==TraiettoriaVelivolo.VERSO_SINISTRA){
				this.switchImageToSx();
			}
			else{
				this.switchImageToDx();
			}
		}
	}

	//
	//Altri metodi
	//

	/**
	 * reimposta le coordinate dell'aereo dopo un'esplosione e reseta il flag esploso
	 * facendo ripartire l'aereo in una posizione a caso
	 */
	public void riparti(){
		this.setTraiettoria(((TraiettoriaVelivolo)this.getTraiettoria()).initTraiettoriaOrizz());
		this.setEsploso(false);
	}
	
	/**
	 * Cambia il tipo di rappresentazione dell'aereo
	 * @param tipo valore tra statico ,simbolico o animato
	 */
	public void cambiaTipoRappresentazione(int tipo){
		//con la condizione && tipo!=this.statSimAnim evito di ricaricare il tipo di 
		//rappresentazione se questa è stata precedentemente settata
		if(tipo != Velivolo.RAPP_NULL && tipo!=this.rapprCorrente){
			if( !this.isEsploso()){
				if(tipo == Velivolo.RAPP_SIMBOLICO ){
					//Rappresentazione simbolica
					this.setImageWidth(this.wSAereo);
					this.setImageHeight(this.hSAereo);
		
				}
				else if(tipo == Velivolo.RAPP_ESPLOSO ){
					this.rapprPrimaDiEsploso = this.rapprCorrente;
					imageUrl= ClassLoader.getSystemResource("images" + System.getProperty("file.separator")+ "boom.png");
					this.immagineEsploso=Toolkit.getDefaultToolkit().getImage(imageUrl);
					tracker.addImage(this.immagineEsploso, 0);
				    try {
				           tracker.waitForAll();
				       }
				       catch(InterruptedException e) {
				       	e.printStackTrace();
				    }
					this.setImmagine(this.immagineEsploso);

				}
				else if( (tipo== Velivolo.RAPP_ANIMATO || tipo== Velivolo.RAPP_STATICO )){
					//Uso immagini già scalate per non appesantire troppo l'appicazione
					String tipologiaAereo;
					if(tipo == Velivolo.RAPP_ANIMATO)
						tipologiaAereo = "animato";
					else
						tipologiaAereo = "statico";
					//Rappresentazione statica o animata
					imageUrl=ClassLoader.getSystemResource("images" + System.getProperty("file.separator")+
							String.valueOf(this.fattoreImg)+"aereodx_"+tipologiaAereo+".gif");
					this.immagineDx=Toolkit.getDefaultToolkit().getImage(imageUrl);
					imageUrl= ClassLoader.getSystemResource("images" + System.getProperty("file.separator")+ 
							String.valueOf(this.fattoreImg)+"aereosx_"+tipologiaAereo+".gif");
					this.immagineSx=Toolkit.getDefaultToolkit().getImage(imageUrl);
			
					//Il seguente stralcio di codice serve
					//a far si che l'immagine e ledimensioni di questa
					//vengano impostate solo una volta caricata completamente l'immagine
					//È stato necessario fare ciò a causa di alcuni problemi
					//di caricamanto che si incontravano con l'immagine statica
		
					//aspetto il caricamento delle immagini prima di caricarne la dimensione
					//altrimenti potrei settare una dimensione -1
					tracker.addImage(immagineSx, 0);
					tracker.addImage(immagineDx, 1);
				    try {
				           tracker.waitForAll();
				       }
				       catch(InterruptedException e) {
				       	e.printStackTrace();
				    }
					
					this.setImmagine(this.immagineDx);
					
					this.setImageWidth(this.getImmagine().getWidth(this.getImageObserver()));
					this.setImageHeight(this.getImmagine().getHeight(this.getImageObserver()));

				}
		
				this.rapprCorrente=tipo;
				if(!this.isEsploso()&& tipo!=Velivolo.RAPP_ESPLOSO){
					//Imposto l'immagine caricata al verso giusto resettando prima il verso
					this.versoCorrente = TraiettoriaVelivolo.VERSO_NONSETTATO;
					if(this.getTraiettoria()!=null)
						this.checkAndSwitchDirection();
				}
			}
			else{
				this.rapprPrimaDiEsploso=tipo;
			}
		}
		
	}
	/**
	 * Override del metodo derivato da oggetto in movimento, si preoccupa di settare
	 * l'immagine dell'aereo in base alla direzione sullo schermo e di non cambiarla nel caso
	 * l'aereo sia esploso;quindi di disegnarla sullo schermo in base alle coordinate correnti
	 * 
	 */
	@Override
	public void draw(Graphics2D g, ImageObserver obj){

		if(!this.isEsploso()){
			//Giro l'aereo nel verso giusto
			this.checkAndSwitchDirection();
		}
		
		if(this.rapprCorrente!= Velivolo.RAPP_SIMBOLICO || this.rapprCorrente == Velivolo.RAPP_ESPLOSO ){
			//disegno lo sprite
			
			super.draw(g, obj);
		}
		//altrimenti disegno una forma statica attraverso i metodi 2D di java
		else{	
			this.drawSimbolico(g);
		}
		
	}
	
	/**
	 * Disegna l'aereo in forma simbolica
	 * attraverso i metodi 2D di java
	 * @param g contesto grafigo sul quale disegnare 
	 */
	private void drawSimbolico(Graphics2D g){
		//Abilito l'antialiasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try{
			g.setColor(Color.decode("#000000"));
			//Disegno la coda dell'aereo
			Ellipse2D coda = new Ellipse2D.Double(this.getPosizione().x+poscodaX,this.getPosizione().y-(this.getImageHeight()/5),
					this.getImageWidth()/5,this.getImageHeight()/0.9);	
			g.fill(coda);
			g.draw(coda);
			//Disegno il corpo dell'aereo
			g.setColor(Color.decode("#000000"));			
			Ellipse2D corpo = new Ellipse2D.Double(this.getPosizione().x,this.getPosizione().y+(this.getImageHeight()/3.5),
					this.getImageWidth(),this.getImageHeight()-(this.getImageHeight()/3.5));	
			g.fill(corpo);
			g.draw(corpo);

			

			//Disegno l'lelica dell'aereo
			g.setColor(Color.decode("#00ffff"));
			Ellipse2D elica = new Ellipse2D.Double(this.getPosizione().x+this.poselicaX,this.getPosizione().y+(this.getImageHeight()/8),
					this.getImageWidth()/6,this.getImageHeight());	
			g.fill(elica);
			g.draw(elica);
			//Disegno l'ala dell'areo
			g.setColor(Color.decode("#292929"));
			Ellipse2D ala = new Ellipse2D.Double(this.getPosizione().x+(this.getImageWidth()/2.5),
					this.getPosizione().y+(this.getImageHeight()/2.4),this.getImageWidth()/4.3,this.getImageWidth()/3);
			g.fill(ala);
			g.draw(ala);

	
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
	}
	
}

