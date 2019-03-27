import javax.swing.JDialog;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * 
 * Finestra di dialogo che si occupa di mostrare i 10 migliori risultati fin'ora
 * raggiunti e salvati
 */
@SuppressWarnings("serial")
public class MyDialogPunteggi extends JDialog implements ActionListener {
	
	//
	//Attributi
	//
	
	private	JPanel				panelCentrale;
	private JLabel				nomi[], cognomi[], punteggi[];
	private GestionePunteggi	gP;
	private	JButton				bOk;

	/**
	 * Costruttore di default , crea il layout del dialog e carica i punteggi
	 *
	 */
	public MyDialogPunteggi(){
		//layout
		this.getContentPane().setLayout(new BorderLayout());
		this.panelCentrale=new JPanel();
		this.panelCentrale.setLayout(new GridLayout(11, 3));
		this.punteggi=new JLabel[11];
		this.nomi=new JLabel[11];
		this.cognomi=new JLabel[11];
		this.bOk=new JButton("OK");
		this.bOk.addActionListener(this);
		//gestione punteggi, carico da file
		this.gP=new GestionePunteggi();

		//Titoli della tabella
		this.nomi[0] = new JLabel("Nome");
		this.cognomi[0] = new JLabel("Cognome");
		this.punteggi[0] = new JLabel("Punteggio");
		//Carico i risultati dall'array nel gestore
		for(int i=0; i<10; ++i){
			this.nomi[i+1]= new JLabel(gP.getRisultati(i).getNome());
			this.cognomi[i+1]= new JLabel(gP.getRisultati(i).getCognome());
			this.punteggi[i+1]= new JLabel(gP.getRisultati(i).getPunteggio()+"");
		}
		//aggiungo le label dei punteggi al pannello
		for(int i=0; i<=10; ++i){
			this.panelCentrale.add(this.punteggi[i]);
			this.panelCentrale.add(this.nomi[i]);
			this.panelCentrale.add(this.cognomi[i]);
		}

		this.getContentPane().add(this.panelCentrale, BorderLayout.CENTER);	
		this.getContentPane().add(this.bOk, BorderLayout.SOUTH);	
		this.setModal(true);
		this.setBounds(200, 100, 400, 600);

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(bOk))
			this.dispose();
	}

}

