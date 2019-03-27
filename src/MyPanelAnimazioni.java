import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLayeredPane;
import javax.swing.Timer;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.Math;
import java.util.Random;

/**
 * Pannello in cui saranno visualizzate le animazioni di gioco
 * (velivolo in movimento , colpi , cannone ecc), 
 * saranno controllate le collisioni e sarà aumentato il punteggio
 * del giocatore nel caso queste avvengano. 
 */
@SuppressWarnings("serial")
public class MyPanelAnimazioni extends MyGeneralJPanel implements ActionListener  {

	//
	//Attributi
	//

	private static float SHOT_TIME_LIMIT;
	private static int BULLET_DELAY_MS;
	//Vari timer
	//tDelayColpi timer per il delay tra un colpo e l'altro
	//tVelivolo timer per l'animazione del velivolo
	//tProiettile timer per l'animazione dei proiettili
	//tStoppaVelivolo timer per gestire la pausa dell'aereo durante il suo moto
	//tPausaEsploso timer per gestire la pausa dopo l'esplosione 
	private Timer 		tDelayColpi,tVelivolo,tProiettile,tStoppaVelivolo,tPausaEsploso;
	private Velivolo	aereo;
	private Cannone cannon;
	private Proiettile[]	proiettili;
	//posizione iniziale dei colpi
	private Point2D.Double	posIniz;
	//Discriminanti per controllare, rispettivamente,
	//se l'animazione è in movimento (isInMovimento)
	//se l'aereo è in sosta di 5 secondo (aereoFermo)
	private boolean		isInMovimento,aereoFermo;
	private Impact		impatto;
	private int		numProiettili; 
	//pDaStampare serve a capire fino a che proiettile disegnare sullo schermo per il delay
	//xrand[1,2] sono le coordinate casuali per stoppare l'aereo
	//nulli rappresenta il numero di proiettili uscuti dallo schermo
	//verso aereo è il verso corrente dell'aereo, utile per ricalcolare le coordinate xrand[1,2]
	private int pDaStampare,xrand1,xrand2 ,h_vel=3 , nulli=0, versoAereo;
	//variabile per tenere il conto da quando sparo i colpi, e variabile per vedere quanto è durata lo stop dell'animazione
	//utili per stoppare l'animazione dei colpi dopo SHOT_TIME_LIMIT
	private long startTime,pausaTime;

	//Gestiscono il punteggio del giocatore
	private Punteggio 			pAttuale;
	private GestionePunteggi	gP;
	private MyAudioPlayer explosion;


	/**
	 * Costruttore di default
	 * @param rootFrame La WindowGame che conterrà il pannello
	 * @param Nome Nome del giocatore
	 * @param Cognome Cognome del giocatore
	 */
	public MyPanelAnimazioni(WindowGame rootFrame, String Nome, String Cognome){
		super(rootFrame);
		//IMPOSTAZIONI GRAGICHE FINESTRA
		//this.setOpaque(false);
		JLayeredPane asd = new JLayeredPane();
		this.add(asd);
		MyPanelAnimazioni.BULLET_DELAY_MS=this.getRootJFrame().getSettaggiGenerali().getDelayColpi();
		MyPanelAnimazioni.SHOT_TIME_LIMIT=this.getRootJFrame().getSettaggiGenerali().getLimiteTempoColpi();
		this.setBackground(Color.WHITE);
		Dimension dim = new Dimension();
		dim.setSize(rootFrame.getSettaggiGenerali().getDimensioniFinestra().getWidth(),rootFrame.getSettaggiGenerali().getDimensioniFinestra().getHeight()-160);
		this.setPreferredSize(dim);
		this.setBounds(0,(int)rootFrame.getSettaggiGenerali().getDimensioniFinestra().getHeight()-160,dim.width,(int)rootFrame.getSettaggiGenerali().getDimensioniFinestra().getHeight()-160);
		this.setDoubleBuffered(true);

		//Inizializzazione variabili per punteggio
		this.pAttuale= new Punteggio(Nome, Cognome);
		this.gP=new GestionePunteggi();
		this.gP.loadFromFile();

		//gestore per il controllo degli impatti
		impatto = new Impact();

		this.numProiettili=0;
		//Suoni
		this.explosion = new MyAudioPlayer("explos.ogg",false,this.getRootJFrame().getSettaggiGenerali().isSuonoOn());

		this.cannon= new Cannone((int)this.getRootJFrame().getPanelControls().getGradIniz(),this.getSize());

		//SETTAGGI DEL VELIVOLO
		this.aereo=new Velivolo(this.h_vel, dim.height,dim.width,this);
		versoAereo = this.aereo.getTraiettoria().getVerso();

		//Soste per il velivolo
		this.setXRand();

		//TIMER E ANIMAZIONI
		pDaStampare=0;
		this.tDelayColpi=new Timer(BULLET_DELAY_MS, this);
		this.tDelayColpi.setInitialDelay(0);
		this.tVelivolo=new Timer(20, this);
		this.tProiettile = new Timer(5, this);
		this.tPausaEsploso = new Timer(2000,this);
		this.tStoppaVelivolo=new Timer(5000, this);
		//Faccio partire l'animazione del velivolo
		this.setIsInMovimento(true);
		this.tVelivolo.start();


	}

	/**
	 * Setter per impostare l'animazione del pannello
	 * se f vale vero allora saranno abilitate le animazioni,
	 * se f vale valso sono stoppate (quelle del velivolo e dei proiettili)
	 * @param val Se a falso stoppa l'animazione, la fa partire in caso contrario
	 */
	public void setIsInMovimento(boolean val){
		if(val){
			//Aggiungo il tempo atteso durante lo stop delle animazioni al contatore per l'abort dell'animazione
			this.startTime += System.currentTimeMillis()-this.pausaTime;
		}else{
			//Memorizzo il tempo in cui ho stoppato le animazioni
			this.pausaTime = System.currentTimeMillis();
		}
		isInMovimento=val;
	}

	/**
	 * Controlla se sono attive le animazioni per il pannello
	 * @return vero se sono abilitate, falso in caso contrario
	 */
	public boolean getIsInMovimento(){
		return isInMovimento;
	}

	/**
	 * Ritorna l'oggetto velivolo contenuto nel pannello
	 * @return Riferimento di tipo velivolo
	 */
	public Velivolo getVelivolo(){
		return this.aereo;
	}

	/**
	 * stoppa i timer presenti in questo pannello
	 * in quanto se non lo si facesse, uscendo e rientrando dalla schermata
	 * di gioco al menu iniziale, l'applicazione risente di rallentamenti
	 *
	 */
	public void stopTimers(){
		this.tProiettile.stop();
		this.tVelivolo.stop();
	}

	/**
	 * Calcola due nuove coordinate casuali per far stoppare il velivolo in una
	 * pausa di 5 secondi
	 */
	private void setXRand(){
		int width = this.getWidth() - aereo.getImageHeight();
		Random generator = new Random();
		this.xrand1=generator.nextInt(width - h_vel) ;
		this.xrand2=generator.nextInt( width - h_vel);

		if((xrand1 % h_vel)!=0) xrand1=(xrand1/h_vel)*h_vel;
		if((xrand2 % h_vel)!=0) xrand2=(xrand2/h_vel)*h_vel;
	}

	/**
	 * Memorizza il tempo da quando si sparano i colpi
	 *
	 */
	private void recordStartSparoTime(){
		startTime = System.currentTimeMillis();
	}

	/**
	 * Restituisce in secondi il tempo trascorso da quando si
	 * sono sparati i colpi.
	 * @return float Rappresentante i secondi trascorsi
	 */
	private float getElapsedTimeInSecond(long fromVar){
		return (float)((System.currentTimeMillis()-fromVar) / 1000);
	}

	/**
	 * Restituisce il gestore dei punteggi attuale 
	 * @return GestionePunteggi gestore punteggi
	 */
	public GestionePunteggi getGp(){
		return this.gP;
	}

	/**
	 * Ritorna l'oggetto per la memorizzazione dei punteggi attuali 
	 * del giocatore
	 * @return Punteggio punteggio attuale del giocatore
	 */
	public Punteggio getPuntiAttuali(){
		return this.pAttuale;
	}
	/**
	 * Disegna tutti i componenti di questo pannello
	 * l'aereo verrà disegnato in base al suo stato (da se stesso)
	 * verranno poi disegnati i colpi e gestit
	 */
	public void paintComponent(Graphics screen){
		Graphics2D screen2D = (Graphics2D) screen; 
		super.paintComponent(screen2D);

		this.drawSfondo(screen2D);
		
		//Disegno l'aereo nella sua rappresentazione corrente
		aereo.draw(screen2D, this);

		//SPIEGAZIONE DEL BLOCCO
		//Se è attivo il timer per i proiettili li disegno e nel caso siano usciti tutti azzero il 
		//loro numero ; nel caso l'aereo sia esploso lo faccio ripartire dopo una pausa contata da 
		//quando sono usciti tutti i proiettili dallo schermo.
		if(this.tProiettile.isRunning()){
			//disegno i proiettili
			for (int i=0; i<numProiettili;i++) {
				if (this.proiettili[i] != null) {
					this.proiettili[i].draw(screen2D, this);
				}
			}
		}

		//Disegno il cannone
		this.cannon.draw(screen2D, this);

	}

	/**
	 * Disegna lo sfondo 
	 * @param screen contesto grafico
	 */
	public void drawSfondo(Graphics2D screen){
		//disegno il mare
		screen.setColor(Color.decode("#00ffff"));
		Rectangle2D mare = new Rectangle2D.Double(0,(this.getHeight()/2)+(this.getHeight()/10),this.getWidth(),
				(this.getHeight()/2) - (this.getHeight()/10));
		screen.fill(mare);
		screen.draw(mare);
		
		screen.setColor(Color.decode("#00ff00"));
		//Disegno l'isolotto
		double widthIsolotto=this.getWidth()/2+(this.getWidth()/3);
		double heightIsolotto=this.getHeight()/4;
		Ellipse2D isolotto = new Ellipse2D.Double(-(widthIsolotto/3),(double)this.getHeight()-(heightIsolotto/2),
				widthIsolotto,heightIsolotto);
		screen.fill(isolotto);
		screen.draw(isolotto);
		screen.setColor(Color.decode("#ffff00"));
		//disegno il sole
		double soleWidth=this.getWidth()/7;
		Ellipse2D sole = new Ellipse2D.Double(this.getWidth()-soleWidth+(soleWidth/5),-(soleWidth/10),soleWidth,soleWidth); 
		screen.fill(sole);
		screen.draw(sole);
	}
	/**
	 * In base al timer che ha generato l'evento, calcolo le coordinata per le figure
	 * aereo o colpi e controllo se avviene un impatto.
	 * Altre azioni conseguite sono:
	 * - il calcolo delle coordinate per stoppare l'aereo per 5
	 *  secondi e conseguentemente gestirne la pausa.
	 * - Aumentare il punteggio del giocatore se avviene un impatto e segnare
	 *  quanti colpi sono stati sparati
	 */
	public void actionPerformed(ActionEvent e) {
		//Ignoro i fire dei timer se l'animazione è bloccata
		if(this.getIsInMovimento()){
			//CONTROLLI PER GESTIRE SEPARATAMENTE I VARI TIMER
			//timer del velivolo
			if(e.getSource() == this.tVelivolo){
				//Se l'aereo non è esploso
				//allora calcolo delle coordinate casuali in cui farlo stoppare
				//o la sua coordinata successiva nel moto rettilineo uniforme
				if (!aereo.isEsploso()){
					//Vedo se devo stoppare l'aereo
					if ( (aereo.getPosizione().getX() == this.xrand1 ) ||
							(aereo.getPosizione().getX() == this.xrand2  )) {
						//Stoppo il velivolo per 5 secondi
						this.tStoppaVelivolo.setDelay(5000);
						this.tStoppaVelivolo.start();
						this.aereoFermo=true;
					} else {
						//Calcolo coordinata successiva dell'aereo se questo non è fermo nella pausa di 5 secondi
						if(!this.aereoFermo){
							aereo.setSucc();
						}
						if(aereo.getVerso()!=versoAereo){
							//se il verso dell'aereo cambia calcolo nuovi punti di fermata
							versoAereo = aereo.getVerso();
							this.setXRand();
						}
					}
				}
			}
			//Timer per far ripartire il velivolo nella sua pausa di 5 secondi
			else if(e.getSource().equals(tStoppaVelivolo)) {
				//controllo che l'aereo non sia esploso per calcolare la coordinata
				//successiva altrimenti muoverei l'immagine dell'esplosione
				if(!aereo.isEsploso()){
					aereo.setSucc();
				}
				this.aereoFermo=false;
				this.tStoppaVelivolo.stop();

			}
			//Timer per gestire la pausa subito dopo dell'esplosione
			else if(e.getSource().equals(tPausaEsploso)){
				this.tPausaEsploso.stop();
				//faccio ripartire l'aereo e calcolo nuove soste
				this.setXRand();
				this.aereo.riparti();
				this.getRootJFrame().getPanelControls().setEnabledButtonSpara(true);
			}
			//timer del proiettile
			else if(e.getSource().equals(tProiettile)) {

				//Calcolo la traiettoria successiva per ogni proiettile
				//uso pDaStampare in modo che calcolo la traiettoria con un delay per ogni colpo
				for (int i=0; i<pDaStampare;i++) {
					if (this.proiettili[i] != null ) {
						this.proiettili[i].setSucc();
						//se il proiettile è uscito dallo schermo lo setto a null
						if ( ! ((this.proiettili[i].getPosizione().getY()<=this.getHeight()) &&
								(this.proiettili[i].getPosizione().getX() <= this.getWidth()))) {
							this.proiettili[i]=null; 
							nulli+=1;
						}

					}
				}

				//se sono usciti tutti i colpi.. o è trascorso troppo tempo da quando si sono sparati(sommo pausatime per togliere il tempo atteso in 
				//				pausa animazione)
				if (numProiettili == nulli || (this.getElapsedTimeInSecond(this.startTime)) >
				(MyPanelAnimazioni.SHOT_TIME_LIMIT+((this.tDelayColpi.getDelay()*this.numProiettili)/1000))){
					//Faccio ripartire la fase di sparo resettando tutto

					numProiettili = nulli = pDaStampare = 0;
					//aggiorno il punteggio in base ai velivoli abbattuti e i colpi sparati 
					//quando questi sono usciti dallo schermo
					this.pAttuale.setPunteggio();
					this.getRootJFrame().getPanelControls().getLabelPunteggio().setText(
							"Punteggio: "+this.pAttuale.getPunteggio());
					this.tProiettile.stop();
					this.tDelayColpi.stop();

					//se l'aereo è esploso allora faccio partire il timer per la pausa
					if(aereo.isEsploso()){
						this.tPausaEsploso.start();
					}
					else{
						this.getRootJFrame().getPanelControls().setEnabledButtonSpara(true);
					}

				}

			}
			//Timer delay sparo colpi
			//Quando arriva il fire del timer aumento pDaStampare
			//la quale mi indica il colpo ennesimo che sto considerando da stampare
			//dopo un delay dettato da tDelayColpi
			//aumento il numero di colpi utilizzati nel punteggio
			else if(e.getSource().equals(tDelayColpi)) {
				if ((pDaStampare!=this.numProiettili)){
					++pDaStampare;

				}
				else
					tDelayColpi.stop();
			}

			//CONTROLLO IMPATTO INDIPENDENTEMENTE DAL TIMER CHE HA GENERATO IL FIRE
			if(!aereo.isEsploso()){
				//Se l'aereo non è esploso controllo per ogni colpo se è avvenuto l'impatto
				for (int i=0; i<numProiettili;i++) {
					if ((this.proiettili[i] != null) && (this.aereo != null)) 
						if (impatto.checkImpact(this.aereo, this.proiettili[i])) {
							this.explosion.start();
							//elimino il proiettile che ha colpito l'aereo 
							this.proiettili[i] = null;
							//aumento il numero dei proiettili usciti dallo schermo
							nulli+=1;
							//setto l'aereo a esploso
							aereo.setEsploso(true);
							//Qua richiamare qualcosa per gestire i punteggi e aumentarli
							this.pAttuale.incVelivoli();
							//Aggiorno il punteggio appena colpisco l'aereo
							//in modo che se esco prima che tutti i colpi 
							//siano usciti dallo schermo non perdo il risultato
							this.pAttuale.setPunteggio();	
						}
				}
			}

			this.repaint();
		}

	}

	/**
	 * Fa partire l'animazione dello sparo
	 * Impostando le varie variabili in base alla strategia corrente
	 * 
	 */
	public void startSpara(){
		//settings del proiettile
		double	aInit=this.getRootJFrame().getPanelControls().getGradIniz();
		double	vInit=this.getRootJFrame().getPanelControls().getVelIniz();
		if (( vInit != -1)&&(aInit != -1)) { //non vi e' errore nel textfield
			this.numProiettili = this.getRootJFrame().getPanelControls().getStrategiaCorrente().getNColpi();
			this.nulli=0;
			//ruoto il cannone in base all'angolo corrente di sparo
			this.cannon.rotate((int)this.getRootJFrame().getPanelControls().getGradIniz());

			//posizione iniziale dei colpi in base all'inclinazione del cannone
			Point.Double bocca = this.cannon.getCoordBocca(); 
			this.posIniz = new Point2D.Double(bocca.getX()+5,this.getSize().getHeight() - bocca.getY());

			this.proiettili = new Proiettile[this.numProiettili];
			this.proiettili[0] =  new Proiettile( 0.04,posIniz,vInit,Math.toRadians(aInit),this);
			this.pAttuale.incColpi();
			for (int i=1; i<numProiettili;i++){
				//aumento i colpi usati nel punteggio
				this.pAttuale.incColpi();
				this.proiettili[i] = new Proiettile(0.04,posIniz,vInit + (vInit* this.getRootJFrame().getPanelControls().getStrategiaCorrente().getDeltaVel(i-1)) / 100
						,Math.toRadians( aInit + this.getRootJFrame().getPanelControls().getStrategiaCorrente().getDeltaGrad(i-1) ),this);
			}
			this.recordStartSparoTime();
			this.tProiettile.start();
			this.tDelayColpi.start();
		} else {
			this.numProiettili=-1;
			this.getRootJFrame().getPanelControls().setEnabledButtonSpara(true);
		}

	}



}

