import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;



/**
 * 
 * Pannello che gestisce le opzioni di gioco, velocità iniziale, angolo iniziale
 * pusalnte spara, e il quale contiene la barra di menu per gli altri controlli
 * di gioco
 */
@SuppressWarnings("serial")
public class MyPanelOption extends MyGeneralJPanel implements ActionListener {

	//
	//Attributi
	//
	
	private JMenuBar 	jBar;
	private JMenu		jMenuStrategia,jMenuTipoVelivolo,jMenuMenu,
	jMenuNumeroColpi;
	private JMenuItem	jiTatticheSparo, jiEsciMenu, jiPunteggi;
	private JRadioButtonMenuItem [] jrNumeroColpi,jrFormaVelivolo;
	private ButtonGroup gFormaVelivolo, gNumeroColpi;

	private JButton	bSpara;
	private JLabel	lSpeed, lAngolo, lPunteggio;
	private JTextField  tSpeed, tAngolo;
	private MyGeneralJPanel pannelloG;
	private Strategia	strategiaCorrente;

	/**
	 * Costruttore della classe: istanzia tutti i componenti grafici del pannello
	 * e li mette nella loro posizione usando un layout assoluto
	 * @param rootFrame La WindowGame che contiene il pannello
	 * */
	public MyPanelOption(WindowGame rootFrame){
		super(rootFrame);
		this.setLayout(new BorderLayout());
		pannelloG= new MyGeneralJPanel(this.getRootJFrame());
		//creo una nuova strategia vuota
		this.strategiaCorrente=new Strategia();
		Dimension dim = new Dimension(rootFrame.getSettaggiGenerali().getDimensioniFinestra().width,
				160);
		this.setPreferredSize(dim);
		this.setBounds(0, 0, dim.width,130);
		jBar=new JMenuBar();
		jMenuStrategia=new JMenu("Strategie");
		jMenuTipoVelivolo=new JMenu("Tipo velivolo");
		jMenuMenu=new JMenu("Menu");
		jMenuNumeroColpi=new JMenu("Numero colpi");

		jiTatticheSparo=new JMenuItem("Tattiche di sparo");
		//di default siamo ad un colpo => nessuna strategia
		jiTatticheSparo.setEnabled(false); 
		jiTatticheSparo.addActionListener(this);

		jiEsciMenu=new JMenuItem("Esci al menu principale");
		jiEsciMenu.addActionListener(this);
		jiPunteggi=new JMenuItem("Punteggi");
		jiPunteggi.addActionListener(this);
		gFormaVelivolo=new ButtonGroup();
		gNumeroColpi=new ButtonGroup();

		//Per Scelta numero colpi
		jrNumeroColpi=new JRadioButtonMenuItem[4];
		for(int i=0;i<4;i++){
			jrNumeroColpi[i]=new JRadioButtonMenuItem(i+1 +" colpi           ");
			gNumeroColpi.add(jrNumeroColpi[i]);
			jMenuNumeroColpi.add(jrNumeroColpi[i]);
			jrNumeroColpi[i].setHorizontalTextPosition(JRadioButtonMenuItem.RIGHT);
			jrNumeroColpi[i].addActionListener(this);
		}
		jrNumeroColpi[0].setText("1 colpo             ");
		jrNumeroColpi[0].setSelected(true);

		//per scelta forma velivolo
		jrFormaVelivolo=new JRadioButtonMenuItem[3];
		for(int i=0; i<3;i++){
			jrFormaVelivolo[i]=new JRadioButtonMenuItem();
			gFormaVelivolo.add(jrFormaVelivolo[i]);
			jMenuTipoVelivolo.add(jrFormaVelivolo[i]);
		}
		
		jrFormaVelivolo[0].setText("Forma simbolica");
		jrFormaVelivolo[1].setText("Forma statica");
		jrFormaVelivolo[2].setText("Forma animata");
		jrFormaVelivolo[0].setSelected(true);
		for(int i=0; i< 3;i++){
			jrFormaVelivolo[i].addActionListener(this);
		}

		//Aggiungo i componenti
		
		jMenuStrategia.add(jMenuNumeroColpi);
		jMenuStrategia.addSeparator();
		jMenuStrategia.add(jiTatticheSparo);

		jMenuMenu.add(jiPunteggi);
		jMenuMenu.addSeparator();
		jMenuMenu.add(jiEsciMenu);


		jBar.add(jMenuStrategia);
		jBar.add(jMenuTipoVelivolo);
		jBar.add(jMenuMenu);

		lSpeed=new JLabel("Velocità:");
		lAngolo=new JLabel("Angolo:");
		tSpeed=new JTextField("100.0");
		tSpeed.setToolTipText("Velocità del colpo");
		tAngolo=new JTextField("45.0");
		tAngolo.setToolTipText("Angolo iniziale del colpo");

		bSpara=new JButton("SPARA");
		bSpara.setMnemonic(KeyEvent.VK_S);
		bSpara.addActionListener(this);

		pannelloG.setLayout(null);
		this.add(jBar, BorderLayout.NORTH);

		lSpeed.setBounds(pannelloG.getBounds().x+30,
				pannelloG.getBounds().y+30,
				lSpeed.getText().length()*8,
				20);

		lAngolo.setBounds(pannelloG.getBounds().x+30,
				lSpeed.getBounds().y+24,
				lSpeed.getBounds().width,
				20);

		tSpeed.setBounds(lSpeed.getBounds().x+
				lSpeed.getBounds().width,
				lSpeed.getBounds().y,
				lSpeed.getBounds().width,
				lSpeed.getBounds().height);

		tAngolo.setBounds(lAngolo.getBounds().x+
				lAngolo.getBounds().width,
				lAngolo.getBounds().y,
				lAngolo.getBounds().width,
				lAngolo.getBounds().height);

		tSpeed.setHorizontalAlignment(JTextField.RIGHT);
		tAngolo.setHorizontalAlignment(JTextField.RIGHT);

		bSpara.setBounds(this.getWidth()-180,
				lSpeed.getBounds().y,
				100,
				50);

		this.lPunteggio=new JLabel("Punteggio: 0");
		this.lPunteggio.setFont(new Font("Arial", Font.PLAIN, 20));
		this.lPunteggio.setBounds(this.tSpeed.getBounds().x+200,
									this.tSpeed.getBounds().y+10, 180, 30);
		
		this.lPunteggio.setHorizontalTextPosition(JLabel.CENTER);
		this.lPunteggio.setVerticalTextPosition(JLabel.CENTER);
		
		
		this.pannelloG.add(this.lPunteggio);
		this.pannelloG.add(lSpeed);
		this.pannelloG.add(lAngolo);
		this.pannelloG.add(tSpeed);
		this.pannelloG.add(tAngolo);
		this.pannelloG.add(bSpara);
		this.add(pannelloG, BorderLayout.CENTER);




	}


	/**
	 * Metodo getter per la variabile strategiaCorrente
	 * @return strategiaCorrente
	 * */
	public Strategia getStrategiaCorrente(){
		return strategiaCorrente;
	}


	/**
	 * Ritorna la velocità iniziale dei colpi
	 * @return double della velocità iniziale dei colpi
	 */
	public double getVelIniz ( ) {
		double ritorno;
		try{
			ritorno= Double.valueOf(this.tSpeed.getText());
			if ((ritorno<0)||(ritorno>350)){
				JOptionPane.showMessageDialog(this, "Attenzione! Immettere un valore di velocita' compreso tra 0 e 350",
						"errore", JOptionPane.INFORMATION_MESSAGE);
				ritorno=-1;
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "I campi di testo velocità o angolo contengono uno o piu caratteri non validi",
					"errore", JOptionPane.INFORMATION_MESSAGE);
			ritorno = -1; //ritorna -1 in caso di errore
		}
		return ritorno;
	}

	/**
	 * Ritorna l'angolo iniziale dei colpi
	 * @return double dell'angolo iniziale dei colpi
	 */
	public double getGradIniz ( ){
		double ritorno;
		try{
			ritorno= Double.valueOf(this.tAngolo.getText());
			if ((ritorno<0)||(ritorno>90)){
				JOptionPane.showMessageDialog(this, "Attenzione! Immettere un valore di angolo compreso tra 0 e 90",
						"errore", JOptionPane.INFORMATION_MESSAGE);
				ritorno=-1;
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "I campi di testo velocità o angolo contengono uno o piu caratteri non validi",
					"errore", JOptionPane.INFORMATION_MESSAGE);
			ritorno = -1; //ritorna -1 in caso di errore
		}
		return ritorno;
	}

	/**
	 * Setta la l'angolo iniziale dei colpi
	 * @param grad double rappresentante l'angolo iniziale dei colpi
	 * @throws Exception Eccezione riguardo il tipo del testo inserito
	 */
	public void setGradIniz ( double grad ) throws Exception{
		this.tAngolo.setText(Double.toString(grad));
	}

	/**
	 * Setta la velocità iniziale dei colpi
	 * @param v double rappresentante la velocità iniziale dei colpi
	 */
	public void setVelIniz ( double v ) {
		this.tSpeed.setText(Double.toString(v));
	}

	/**
	 * Abilita e disabilita il bottone spara, in modo che non posso sparare colpi quando
	 * questi non hanno raggiunto l'obiettivo o non sono caduti tutti
	 * @param value true se abilitarlo, false altrimenti
	 */
	public void setEnabledButtonSpara(boolean value){
		this.bSpara.setEnabled(value);
	}

	public void actionPerformed(ActionEvent e){
		//Pulsante per uscire al pannello principale
		if(e.getSource().equals(this.jiEsciMenu)){
			//Stoppo i timer delle animazioni, vedere commento del metodo per la motivazione
			this.getRootJFrame().getPanelAnimazioni().stopTimers();
			this.getRootJFrame().getPanelAnimazioni().getGp().aggiornaRisultati(this.getRootJFrame().getPanelAnimazioni().getPuntiAttuali());
			this.getRootJFrame().getPanelAnimazioni().getGp().saveToFile();
			this.getRootJFrame().setPanel(WindowGame.PANEL_INIZIALE );	
		}
		//Mostra il dialogo dei punteggi
		if(e.getSource().equals(this.jiPunteggi)){

			MyDialogPunteggi showPunteggi;
			showPunteggi=new MyDialogPunteggi();
			showPunteggi.setVisible(true);
		}
		//Pulsante spara
		else if(e.getSource().equals(bSpara)){
			this.setEnabledButtonSpara(false);
			this.tAngolo.setText(this.tAngolo.getText().replaceAll(",", "."));
			this.tSpeed.setText(this.tSpeed.getText().replaceAll(",", "."));
			this.getRootJFrame().getPanelAnimazioni().startSpara();
		}
		//Imposta le tattiche di sparo
		else if(e.getSource().equals(this.jiTatticheSparo)){

			//Stoppo l'animazione 
			this.getRootJFrame().getPanelAnimazioni().setIsInMovimento(false);

			MyDialogStrategie strategia = new MyDialogStrategie(this.getRootJFrame(), this.getStrategiaCorrente());
			strategia.setVisible(true);
			//facciamo ripartire l'animazione
			this.getRootJFrame().getPanelAnimazioni().setIsInMovimento(true);
		}
		//Cambio della rappresentazione del velivolo al volo
		else if(e.getSource().equals(this.jrFormaVelivolo[0])){
			this.getRootJFrame().getPanelAnimazioni().getVelivolo().cambiaTipoRappresentazione(Velivolo.RAPP_SIMBOLICO);
		}else if(e.getSource().equals(this.jrFormaVelivolo[1])){
			this.getRootJFrame().getPanelAnimazioni().getVelivolo().cambiaTipoRappresentazione(Velivolo.RAPP_STATICO);
		}
		else if(e.getSource().equals(this.jrFormaVelivolo[2])){
			this.getRootJFrame().getPanelAnimazioni().getVelivolo().cambiaTipoRappresentazione(Velivolo.RAPP_ANIMATO);
		}
		//Setto il numero colpi per la strategia corrente
		else if(e.getSource().equals(this.jrNumeroColpi[0])){
			//se c'è solo un colpo la strategia non esiste
			this.jiTatticheSparo.setEnabled(false);	
			this.getStrategiaCorrente().setNColpi(1);
		}
		else{
			//>1 colpo esiste la strategia
			this.jiTatticheSparo.setEnabled(true);	

			if(e.getSource().equals(this.jrNumeroColpi[1])){
				this.getStrategiaCorrente().setNColpi(2);
			}
			else if(e.getSource().equals(this.jrNumeroColpi[2])){
				this.getStrategiaCorrente().setNColpi(3);
			}
			else if(e.getSource().equals(this.jrNumeroColpi[3])){
				this.getStrategiaCorrente().setNColpi(4);
			}
		}
	}
	
	public JLabel getLabelPunteggio(){
		return this.lPunteggio;
	}
	
}
