import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
/**
 * 
 * La classe gestisce il caricamento, il salvataggio di una strategia
 * e l'impostazione di una strategia corrente	
 */
@SuppressWarnings("serial")
public class MyDialogStrategie extends JDialog implements ActionListener{
	
	//
	//Attributi
	//
	
	private WindowGame parent;
	private Dimension dim;
	private JTextField	tDeltaV[], tDeltaA[];
	private JLabel		lDeltaV[], lDeltaA[];
	private JButton		bCaricaStrategia, bSalvaStrategia, bUsaStrategia;
	private Strategia	cs;
	private	GestioneStrategia	gCs;
	//numero di colpi su cui operare (sarà ncolpi-1 perchè non si considera il primo)
	private int 		nC;
	//
	//Costruttori
	//

	/**
	 * imposta le variabili di classe e imposta a modale il dialogo
	 * @param parent	la finestra del gioco a cui il dialogo è associato
	 * @param s			La strategia da impostare
	 * */
	public MyDialogStrategie(WindowGame parent, Strategia s){
		super(parent, "Tattiche di Sparo");
		this.parent=parent;
		cs=s;
		//parto dal secondo colpo in poi a settare i delta
		nC=s.getNColpi()-1;
		this.dim = new Dimension(parent.getSize());
		this.setModal(true);
		//Creo l'array  per contenere un numero massimo di colpi in modo che
		//se faccio carica strategia ho caricate anche le impostazioni
		//dei colpi che attualmente non visualizzo
		tDeltaV	= 	new JTextField[3];
		tDeltaA	=	new JTextField[3];
		lDeltaV	=	new JLabel[3];
		lDeltaA	=	new JLabel[3];

		//Imposto le label e i textfield con i valori di default
		//o quelli della strategia corrente
		//si noti che la posizione 0 corrisponde al secondo colpo
		for(int i=0; i<nC; ++i){
			lDeltaA[i]	=	new JLabel("Delta Angolo colpo n° "+(i+2));
			tDeltaA[i]	= 	new JTextField(Double.toString(parent.getPanelControls().getStrategiaCorrente().getDeltaGrad(i)));
			tDeltaA[i].setToolTipText("Variazione rispetto all'angolo iniziale");
			tDeltaA[i].setHorizontalAlignment(JTextField.RIGHT);
			
			lDeltaV[i]	=	new JLabel("Delta Velocità(in %) colpo n° "+(i+2));
			tDeltaV[i]	= 	new JTextField(Double.toString(parent.getPanelControls().getStrategiaCorrente().getDeltaVel(i)));
			tDeltaV[i].setToolTipText("Variazione della velocità, in percentuale, rispetto a quella iniziale");
			tDeltaV[i].setHorizontalAlignment(JTextField.RIGHT);
		}

		//Larga il 42% della window grande
		//e alta il numero di textfield (+3 per la legenda) per una costante
		//piu un certo spazio per andare sul sicuro e non sforare
		this.setBounds(parent.getBounds().x*150/100,
				parent.getBounds().y*150/100,
				dim.width*42/100, (nC+3)*20+200);	
		
		//layout assoluto
		this.setLayout(null);					
		JLabel legenda = new JLabel("Legenda (valori ammessi):");
		legenda.setBounds(15,0,220,20);
		legenda.setOpaque(false);
		JLabel legAngolo = new JLabel("-0.5 <= Delta angoli <= +0.5");
		legAngolo.setBounds(15, 20, 220, 20);
		JLabel legVel = new JLabel("-10 <= Delta velocità <= +10");
		legVel.setBounds(15, 40, 220, 20);
		this.add(legenda);
		this.add(legAngolo);
		this.add(legVel);
		//Impostiamo i bounds del dialog
		for(int i=0; i<this.nC; ++i){				
			lDeltaA[i].setBounds(15, 20+((i+2)*40), 220, 20);
			tDeltaA[i].setBounds(250, 20+((i+2)*40), 60, 20);
			this.add(lDeltaA[i]);
			this.add(tDeltaA[i]);
			lDeltaV[i].setBounds(15, 40+((i+2)*40), 220, 20);
			tDeltaV[i].setBounds(250, 40+((i+2)*40), 60, 20);
			this.add(lDeltaV[i]);
			this.add(tDeltaV[i]);
		}

		bCaricaStrategia	=	new JButton("Carica");
		bSalvaStrategia		=	new JButton("Salva");
		bUsaStrategia		=	new JButton("Usa");

		bCaricaStrategia.setBounds(5, this.getBounds().height-95, 100, 50);
		bSalvaStrategia.setBounds(110, this.getBounds().height-95, 100, 50);
		bUsaStrategia.setBounds(215, this.getBounds().height-95, 100, 50);

		bCaricaStrategia.addActionListener(this);
		bSalvaStrategia.addActionListener(this);
		bUsaStrategia.addActionListener(this);
		this.add(bCaricaStrategia);
		this.add(bSalvaStrategia);
		this.add(bUsaStrategia);

	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(bCaricaStrategia)){
			JFileChooser chooserLoad = new JFileChooser();
			//Impostiamo il lookandfeel di default del sistema per il dialogo
			makeCrossPlatformLookAndFeel(chooserLoad);
			//carichiamo la strategia
			this.LoadStrategia(chooserLoad);	
			this.parent.initLookAndFeel();
		}
		else	
			if(e.getSource().equals(bSalvaStrategia)){		
				JFileChooser chooserSave = new JFileChooser();
				makeCrossPlatformLookAndFeel(chooserSave);	
				//Impostiamo il lookandfeel di default del sistema per il dialogo
				if(isInputValid())
					//salviamo la strategia se l'input è corretto
					this.SaveStrategia(chooserSave);		
				else
					this.printDialogError();
				this.parent.initLookAndFeel();
			}
			else	
				if(e.getSource().equals(bUsaStrategia)){
					if(isInputValid()){
						for(int i=0; i<this.nC; ++i){
							parent.getPanelControls().getStrategiaCorrente().setDeltaGrad(i, Double.valueOf(tDeltaA[i].getText()));
							parent.getPanelControls().getStrategiaCorrente().setDeltaVel(i,  Double.valueOf(tDeltaV[i].getText()));
						} 
						this.dispose();
					}
					else{
						printDialogError();
					}


				}
	}

	/**
	 * Stampa un messaggio di errore 
	 * */

	public void printDialogError(){
		JOptionPane.showMessageDialog(this, "Uno o più valori in input non sono corretti", 
				"ERRORE", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Verifica che l'input immesso dall'utente rientri
	 * nel range di valori richiesto
	 * @return vero se l'input immesso è valido
	 * */
	public boolean isInputValid(){
		for(int i=0; i<this.nC; ++i){
			try{
				if(Double.valueOf(tDeltaA[i].getText())>0.5 ||
						Double.valueOf(tDeltaA[i].getText())<-0.5)
					return false;
				if(Double.valueOf(tDeltaV[i].getText())>10 ||
						Double.valueOf(tDeltaV[i].getText())<-10)
					return false;
				
		}catch(Exception e){
			return false; //ritorna falso in caso di errore			
		}
	}
		return true;
	}
	//Metodo per impostare il lookandfeel di sistema per i dialoghi JFIleChooser
	private void makeCrossPlatformLookAndFeel(JFileChooser chooser){
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			GestioneErrori.error(GestioneErrori.ERROR_CLASS_NOT_FOUND_EXCEPTION, e1);
		} catch (InstantiationException e1) {
			GestioneErrori.error(GestioneErrori.ERROR_LOOK_AND_FEEL_CREATING_CLASS, e1);
		} catch (IllegalAccessException e1) {
			GestioneErrori.error(GestioneErrori.ERROR_LOOK_AND_FEEL_ILLEGAL_CLASS_ACCESS, e1);
		} catch (UnsupportedLookAndFeelException e1) {
			GestioneErrori.error(GestioneErrori.ERROR_LOOK_AND_FEEL_UNSUPPORTED_STYLE, e1);
		}
		SwingUtilities.updateComponentTreeUI(chooser);
	}


	/**
	 * Il metodo si occupa di prendere i dati dai textfield 
	 * inserirli in un oggetto strategia. SI occupa poi di 
	 * creare un oggetto GestioneStrategia con la strategia creata precedentemente
	 * Infine serilizza la GestioneStrategia
	 * @param chooserSave	Dialogo di salvataggio
	 * */
	public void SaveStrategia(JFileChooser chooserSave){
		ExFileFilter filter = new ExFileFilter("dat", "File di Salvataggio strategia");
		chooserSave.removeChoosableFileFilter(chooserSave.getFileFilter());//tolgo il filtro per all files
		chooserSave.addChoosableFileFilter(filter);
		String separator=System.getProperty("file.separator");	//stringa di separazione directory
		chooserSave.setCurrentDirectory(new File(System.getProperty("user.home")+
				separator+".airbum"));
		
		int returnVal=chooserSave.showSaveDialog(this);		//returnVal=costante che indica cosa ho clickato nel dialogo
		if (returnVal == JFileChooser.APPROVE_OPTION) {	//se ho clickato apri...
			cs.setNColpi(this.cs.getNColpi());			//Impostiamo il numero di colpi nella strategia
			for(int i=0; i<this.nC; ++i){		//Leggiamo i vari delta e li settiamo in strategia
				cs.setDeltaGrad(i,  Double.valueOf(tDeltaA[i].getText()));
				cs.setDeltaVel(i,   Double.valueOf(tDeltaV[i].getText()));
			}
			File file = chooserSave.getSelectedFile();		//Ritorniamo in file il file selezionato nel dialogo per salvare
			String path=file.getPath();						//ne prendiamo il path (percorso+nome)
			String nome=file.getName();						//ne prendiamo il nome
			path=path.replaceAll(nome, "");		//togliamo dal path il nome
			if(!nome.endsWith(".dat"))		
				nome=nome.concat(".dat");

			file=new File(path+nome);
			gCs=new GestioneStrategia(path, nome);			//Istanziamo un oggetto gestionestrategia
			gCs.setFileHandler(file);					//ne settiamo l'handler
			gCs.setStrategia(0, cs);				//aggiungiamo una strategia 
			gCs.saveToFile();						//lo salviamo sul disco
		}

	}

	/**
	 * Il metodo si occupa di deserializzare i dati dal file selezionato 
	 * inserirli nella corrente Strategia (cs) .  
	 * @param chooserLoad	Dialogo di caricamento
	 * */
	public void LoadStrategia(JFileChooser chooserLoad){
		ExFileFilter filter = new ExFileFilter("dat", "File di Salvataggio strategia");
		chooserLoad.removeChoosableFileFilter(chooserLoad.getFileFilter());//tolgo il filtro per all files
		chooserLoad.addChoosableFileFilter(filter);
		String separator=System.getProperty("file.separator");	//stringa di separazione directory
		chooserLoad.setCurrentDirectory(new File(System.getProperty("user.home")+
				separator+".airbum"));
		int returnVal = chooserLoad.showOpenDialog(this);//returnVal=costante che indica cosa ho clickato nel dialogo
		if (returnVal == JFileChooser.APPROVE_OPTION){	//se ho clickato apri..
			File file = chooserLoad.getSelectedFile();	//file contiene l'handler del file selezionato nel dialogo
			String path=file.getPath();
			String nome=file.getName();
			path=new String(path.substring(path.length()-nome.length()));
			gCs=new GestioneStrategia(path, nome);
			gCs.setFileHandler(file);
			gCs.loadFromFile();
			for(int i=0; i<gCs.getStrategia(0).getNColpi()-1; ++i){
				if(tDeltaV[i]!=null){
					tDeltaV[i].setText(Double.toString(gCs.getStrategia(0).getDeltaVel(i)));	//inseriamo i dati letti nei textfield
					tDeltaA[i].setText(Double.toString(gCs.getStrategia(0).getDeltaGrad(i)));
				}
				parent.getPanelControls().getStrategiaCorrente().setDeltaGrad(i, gCs.getStrategia(0).getDeltaGrad(i));
				parent.getPanelControls().getStrategiaCorrente().setDeltaVel(i, gCs.getStrategia(0).getDeltaVel(i));
			}
			//I colpi non settati nella strategia caricata li imposto a zero
			for(int i=gCs.getStrategia(0).getNColpi()-1;i<3;++i){
				if(tDeltaV[i]!=null){
					tDeltaV[i].setText("0");	
					tDeltaA[i].setText("0");
				}
				parent.getPanelControls().getStrategiaCorrente().setDeltaGrad(i, 0);
				parent.getPanelControls().getStrategiaCorrente().setDeltaVel(i, 0);
			
			}
			
		}
	}		
	


	/**
	 * Classe privata per gestire il filtro sui file *.dat 
	 * */
	private class ExFileFilter extends FileFilter{
		private Hashtable<String, ExFileFilter> filters = null;
		private String description = null;
		private String fullDescription = null;
		private boolean useExtensionsInDescription = true;


		/**
		 * Costruttore di default
		 * */
		public ExFileFilter(){
			this.filters = new Hashtable<String, ExFileFilter>();
		}

		/**
		 * Costruttore
		 * @param extension estensione da impostare come filtro
		 * */
		public ExFileFilter(String extension) {
			this(extension, null);
		}
		/**
		 * Costruttore
		 * @param extension estensione da impostare come filtro
		 * @param description descrizione dell'estensione
		 * */
		public ExFileFilter(String extension, String description) {
			this();
			if(extension!=null) this.addExtension(extension);
			if(description!=null) this.setDescription(description);
		}


		public boolean accept(File f) {
			if(f != null) {
				if(f.isDirectory()) {
					return true;
				}
				String extension = getExtension(f);
				if(extension != null && filters.get(getExtension(f)) != null) {
					return true;
				};
			}
			return false;
		}

		/**
		 * Ritorna l'estensione del file selezionato
		 * @param f file selezionato
		 * @return estensione file selezionato
		 * */
		public String getExtension(File f) {
			if(f != null) {
				String filename = f.getName();
				int i = filename.lastIndexOf('.');
				if(i>0 && i<filename.length()-1) {
					return filename.substring(i+1).toLowerCase();
				};
			}
			return null;
		}

		/**
		 * Aggiunge un estensione al filtro
		 * @param extension estensione da aggiungere
		 * */

		public void addExtension(String extension) {
			if(filters == null) {
				filters = new Hashtable<String, ExFileFilter>(5);
			}
			filters.put(extension.toLowerCase(), this);
			fullDescription = null;
		}


		/**
		 * Ritorna la descrizione dell'estensione
		 * @return description
		 * */
		public String getDescription() {
			if(fullDescription == null) {
				if(description == null || isExtensionListInDescription()) {
					fullDescription = description==null ? "(" : description + " (";
					// build the description from the extension list
					Enumeration extensions = filters.keys();
					if(extensions != null) {
						fullDescription += "." + (String) extensions.nextElement();
						while (extensions.hasMoreElements()) {
							fullDescription += ", ." + (String) extensions.nextElement();
						}
					}
					fullDescription += ")";
				} else {
					fullDescription = description;
				}
			}
			return fullDescription;
		}
		
		/**
		 * Imposta una descrizione per una estensione
		 * @param description	descrizione da impostare
		 * */
		public void setDescription(String description) {
			this.description = description;
			fullDescription = null;
		}

		/**
		 * @return useExtensionsInDescription
		 */
		public boolean isExtensionListInDescription() {
			return useExtensionsInDescription;
		}
	}
}
