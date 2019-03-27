import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;


/**
 * Pannello principale di gioco da cui verranno scelte:
 * startgame
 * impostazioni
 * About
 * Exit
 * 
 */
@SuppressWarnings("serial")
public class MyPanelIniziale  extends MyGeneralJPanel  implements ActionListener  {

	//Attributi
	private BufferedImage logo;			
	private Velivolo aereo;

	
    //Altezza dell'immagine aereo
    int aereoHeight;
    //ampiezza dell'immagine aereo
    int aereoWidth;  
    private String imageNome;
	private String imagePath;
	private int imageLogoX,imageLogoY;
	private JButton [] buttons; 
	//Oggetto necessario per la riproduzione del file audio
	private MyAudioPlayer introSound; 
	private Timer tim;
	private Graphics2D screen2D;
	/**
	 * Costruttore di default carica l'immagine del logo
	 * e setta il layout manager
	 * @param root Window contenitore di riferimento
	 */
	public MyPanelIniziale(WindowGame root){
		super(root);
		int i;
		this.getRootJFrame().setTitle("Airbum Game");
		this.setLogoNome("logo.png");
		//path relativo al build path
		this.setLogoPath("images");
		String separator = java.lang.System.getProperty("file.separator");
		try {
		    //Uso getSystemResourceAsStream per rendere il codice portabile
		    //anche nel caso il programma sia distrubuito in un .jar
		    logo = ImageIO.read(ClassLoader.getSystemResourceAsStream(this.getLogoPath() + separator + this.getLogoNome()));
		    MediaTracker tracker = new MediaTracker(new JPanel());
		    tracker.addImage(logo, 0);
		    try{
		    	tracker.waitForAll();
		    }catch(InterruptedException e){
		    	//TODO GIAMP qua stampa impossibile caricare immagine. però puoi continuare tanto non è grave
		    	e.printStackTrace();
		    }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Aereo per l'animazione iniziale (bouncing)
		aereo = new Velivolo((int)2.4,(int)this.getSize().getHeight(),
				(int)this.getSize().getWidth(),this,Velivolo.ANIMAZIONE_INIZIALE);

		//coordinate del logo
		this.imageLogoX = 80;
		this.imageLogoY = 100;

		this.setLayout(null);
		//Inizializzo i bottoni
		this.buttons = new JButton[4];
		for (i=0;i< 4;i++){
			this.buttons[i] = new JButton();
			this.buttons[i].addActionListener(this);
			this.buttons[i].setVerticalTextPosition(AbstractButton.CENTER);
			this.buttons[i].setHorizontalTextPosition(AbstractButton.LEADING);
			this.buttons[i].setPreferredSize(new java.awt.Dimension(112, 31));
		}
		this.buttons[0].setText("Start Game");
		this.buttons[0].setMnemonic(KeyEvent.VK_S);

		this.buttons[1].setText("Impostazioni");
		this.buttons[1].setMnemonic(KeyEvent.VK_I);

		this.buttons[2].setText("About");
		this.buttons[2].setMnemonic(KeyEvent.VK_B);       

		this.buttons[3].setText("Exit");
		this.buttons[3].setMnemonic(KeyEvent.VK_E);

		this.setButtons();
		for (int j=0; j<4; ++j)
			this.add(this.buttons[j]);

		this.setDoubleBuffered(true);

		this.tim = new Timer(60,this);
		this.tim.setInitialDelay(35300);
		this.tim.start();

		//faccio partire l'audio
		this.introSound = new MyAudioPlayer("song_intro.ogg",true,this.getRootJFrame().getSettaggiGenerali().isSuonoOn());
		this.introSound.start();
	}

	//
	//Metodi set e get
	//

	/**
	 * Imposta le coordinate e le dimensioni dei pulsanti
	 * */   
	public void setButtons(){
		for (int i=0; i<4; ++i)
			this.buttons[i].setBounds((this.getRootJFrame().getWidth()-(this.getRootJFrame().getWidth()/10))-200, 280+60*i, 150, 45);
	}

	/**
	 * Ritorna il nome dell'immagine logo
	 * @return il nome dell'immagine
	 */
	public String getLogoNome(){
		return this.imageNome;
	}

	/**
	 * Setta il nome del file logo
	 * @param nome nome file
	 */
	public void setLogoNome(String nome){
		this.imageNome = nome;
	}

	/**
	 * Ritorna il path dell'immagine logo
	 * @return il path all'immagine
	 */
	public String getLogoPath(){
		return this.imagePath;
	}

	/**
	 * Setta il path all'immagine logo
	 * @param nome Path all'immagine
	 */
	public void setLogoPath(String nome){
		this.imagePath = nome;
	}

	//
	//Altri metodi
	//

	public void paintComponent(Graphics screen){ 
		screen2D = (Graphics2D) screen; 
		super.paintComponent(screen2D);
		screen2D.drawImage(this.logo,this.imageLogoX,this.imageLogoY, this);
		this.aereo.draw(screen2D,this);

	}


	/**
	 * Gestore eventi dei pulsanti principali
	 */
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(this.tim)){
			this.aereo.setSucc(Velivolo.ANIMAZIONE_INIZIALE);
			this.repaint();  
		}else
			if( e.getSource().equals(this.buttons[0])){
				//premuto start game
				this.introSound.stop();
				this.tim.stop();
				this.getRootJFrame().setPanel(WindowGame.PANEL_GIOCO);

			}
			else{
				if( e.getSource().equals(this.buttons[1])){
					//premuto impostazioni
					this.introSound.stop();
					this.tim.stop();
					super.getRootJFrame().setPanel(WindowGame.PANEL_SETTINGS);
				}
				else{
					if( e.getSource().equals(this.buttons[2])){
						//premuto about
						MyDialogAbout about;
						about=new MyDialogAbout(this.getRootJFrame());
						about.setVisible(true);
					}
					else{
						if( e.getSource().equals(this.buttons[3])){
							//premuto exit
							System.exit(0);
						}
					}
				}
			}

	} 
}
