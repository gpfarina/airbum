import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
/**
 * Pannello contenente le impostazioni generali di gioco
 * come grandezza della finestra, suoni e altro.
 * 
 */
@SuppressWarnings("serial")
public class MyPanelSettings extends MyGeneralJPanel implements ActionListener{
	
	//barra a scorrimento per impostare la grandezza della finestra di gioco
	private JSlider		grandezzaFinestraGioco, delayColpi,limiteSparo;
	private JLabel		dimensioni,  intervalloColpi, labelLimiteSparo;
	private JCheckBox	suono;
	private JButton		bBack, bApplica;
	private GestioneSettaggiGenerali gSettings;
	private Hashtable<Integer,JLabel> labelTable;
	
	/**
	 * Costruttore , imposta il layout del pannello
	 * @param rootFrame Root frame che contiene il pannello
	 * @param gestSettings Gestore dei settaggi generali
	 */
	public MyPanelSettings(WindowGame rootFrame,GestioneSettaggiGenerali gestSettings){
		super(rootFrame);
		this.gSettings = gestSettings;
		this.getRootJFrame().setTitle("Finestra di Impostazioni Generali");
		this.setLayout(null);
		//Dimensioni
		Dimension dim = new Dimension();
		dim.setSize(rootFrame.getSettaggiGenerali().getDimensioniFinestra().getWidth(),rootFrame.getSettaggiGenerali().getDimensioniFinestra().getHeight());
		this.setPreferredSize(dim);
		this.setBounds(rootFrame.getBounds().x, rootFrame.getBounds().y, dim.width, dim.height);
		this.dimensioni= new JLabel("DIMENSIONI FINESTRA GIOCO");
		this.dimensioni.setFont(new Font("Times", Font.TRUETYPE_FONT, 18));
		this.dimensioni.setBounds((this.getRootJFrame().getWidth()/2) - (this.dimensioni.getText().length()*5), 10, 345, 20);
		this.add(dimensioni);
		
		//Slide
		this.grandezzaFinestraGioco= new JSlider(JSlider.HORIZONTAL, 1 , 6, 1);
        this.grandezzaFinestraGioco.setMinorTickSpacing(1);
		this.grandezzaFinestraGioco.setPaintTicks(true);
		labelTable = new Hashtable<Integer,JLabel>();
		labelTable.put( new Integer("1"), new JLabel("800x600") );
		labelTable.put( new Integer("2"), new JLabel("900x600") );
		labelTable.put( new Integer("3"), new JLabel("900x800") );
		labelTable.put( new Integer("4"), new JLabel("1024x768") );
		labelTable.put( new Integer("5"), new JLabel("1280x800") );
		labelTable.put( new Integer("6"), new JLabel("1280x1024") );
		
		this.intervalloColpi= new JLabel("INTERVALLO FRA I COLPI (ms)");
		this.intervalloColpi.setFont(new Font("Times", Font.TRUETYPE_FONT, 18));
		this.intervalloColpi.setBounds((this.getRootJFrame().getWidth()/2) - 
								(this.intervalloColpi.getText().length()*4), 200, 345, 20);
		this.intervalloColpi.setFont(new Font("Times",Font.TRUETYPE_FONT, 18));
		this.add(intervalloColpi);
		
		this.delayColpi= new JSlider(JSlider.HORIZONTAL, 80, 3000, this.gSettings.getSettings().getDelayColpi());
		this.delayColpi.setMajorTickSpacing(1000);
		this.delayColpi.setMinorTickSpacing(100);
		this.delayColpi.setPaintTicks(true);
		this.delayColpi.setPaintLabels(true);
		this.delayColpi.setBounds(40, 230, this.getBounds().width-80, 80);
		this.delayColpi.setBorder(
                BorderFactory.createEtchedBorder());
		this.add(delayColpi);
		
		this.labelLimiteSparo= new JLabel("Limite tempo totale sparo colpi (s)");
		this.labelLimiteSparo.setFont(new Font("Times", Font.TRUETYPE_FONT, 18));
		this.labelLimiteSparo.setBounds((this.getRootJFrame().getWidth()/2) - 
								(this.intervalloColpi.getText().length()*4), 330, 345, 20);
		this.labelLimiteSparo.setFont(new Font("Times",Font.TRUETYPE_FONT, 18));
		this.add(labelLimiteSparo);
		
		this.limiteSparo= new JSlider(JSlider.HORIZONTAL, 5, 25, this.gSettings.getSettings().getLimiteTempoColpi());
		this.limiteSparo.setMajorTickSpacing(1);
		this.limiteSparo.setMinorTickSpacing(1);
		this.limiteSparo.setPaintTicks(true);
		this.limiteSparo.setPaintLabels(true);
		this.limiteSparo.setBounds(40, 360, this.getBounds().width-80, 80);
		this.limiteSparo.setBorder(
                BorderFactory.createEtchedBorder());
		this.add(limiteSparo);
		
		String currentDim = new String (String.valueOf((int)this.gSettings.getSettings().getDimensioniFinestra().getWidth())+"x"+
				String.valueOf((int)this.gSettings.getSettings().getDimensioniFinestra().getHeight()));	
		for (int i=1;i<=this.labelTable.size();i++){
			//Setto il puntatore dello JSlider alla dimensione corrente della finestra
			if (  currentDim.equals(this.labelTable.get(i).getText())){
				this.grandezzaFinestraGioco.setValue(i);
			}
		}
			
		this.grandezzaFinestraGioco.setLabelTable( labelTable );
		this.grandezzaFinestraGioco.setPaintLabels(true);
		this.grandezzaFinestraGioco.setSnapToTicks(true);
		this.grandezzaFinestraGioco.setBounds(40, 40, this.getBounds().width-80, 80);
		this.grandezzaFinestraGioco.setBorder(
                BorderFactory.createEtchedBorder());
        Font font = new Font("Serif", Font.ITALIC, 15);
        this.grandezzaFinestraGioco.setFont(font);
        this.add(this.grandezzaFinestraGioco);
		
		this.suono	= new JCheckBox("Presenza suoni");
		this.suono.setMnemonic(KeyEvent.VK_A);
		this.suono.setSelected(this.gSettings.getSettings().isSuonoOn());
		this.suono.setBounds((this.getWidth()/2)- 50 , 150, 160, 20);
		this.suono.setFont(new Font("Times", Font.TRUETYPE_FONT, 18));
		this.add(this.suono);
		
		this.bBack= new JButton("Annulla");
		this.bBack.addActionListener(this);
		this.bBack.setBounds(this.getBounds().width-250,this.getBounds().height-120, 100, 20 );
		this.bApplica = new JButton("Salva");
		this.bApplica.setMnemonic(KeyEvent.VK_S);
		this.bApplica.addActionListener(this);
		this.bApplica.setBounds(this.getBounds().width-120,this.getBounds().height-120, 100, 20 );		
		this.add(this.bBack);
		this.add(this.bApplica);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		

		if (e.getSource().equals(bBack)){
			this.backToInitialPanel();
		}
		
		else if (e.getSource().equals(bApplica)){
			//Applico i cambiamenti e serializzo su file delle impostazioni
			this.gSettings.getSettings().setDimensioniFinestra(this.getDimensioniFinestraSettate());
			this.getRootJFrame().setBounds((int)this.getRootJFrame().getBounds().getLocation().getX(), 
					(int)this.getRootJFrame().getBounds().getLocation().getY(),
					(int)this.getDimensioniFinestraSettate().getWidth(), 
					(int)this.getDimensioniFinestraSettate().getHeight());

			this.gSettings.getSettings().setSuono(this.suono.isSelected());
			this.gSettings.getSettings().setLimiteTempoColpi(this.limiteSparo.getValue());
			this.gSettings.getSettings().setDelayColpi(this.delayColpi.getValue());
			this.gSettings.saveToFile();
			this.backToInitialPanel();
		}
		
	}

	/**
	 * Ritorna le dimensioni settate, nello jslide, della finestra di gioco
	 * @return Dimension contenente le dimensioni settate
	 */
	private Dimension getDimensioniFinestraSettate(){
		Dimension dim = new Dimension();
		String width,height,dimString;
		dimString = this.labelTable.get(this.grandezzaFinestraGioco.getValue()).getText();
		width = dimString.substring(0,dimString.indexOf("x"));
		height = dimString.substring(dimString.indexOf("x")+1,dimString.length());
		dim.setSize(Double.valueOf(width), Double.valueOf(height));
		return dim;
	}

	/**
	 * Titorna al pannello iniziale
	 *
	 */
	private void backToInitialPanel(){
		this.getRootJFrame().setTitle(this.getRootJFrame().getTitle());
		this.getRootJFrame().initLookAndFeel();
		this.getRootJFrame().setPanel(WindowGame.PANEL_INIZIALE);	
	}

}
