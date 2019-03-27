import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
/**
 * Questa classe gestisce l'input del nome e del cognome dell'utente
 * attraverso un dialogo modale
 */
@SuppressWarnings("serial")
public class MyDialogInputPlayer extends JDialog implements ActionListener {
	
	//
	//Attributi
	//
	
	JButton		jbOk, jbAnnulla;
	JTextField	jtNome, jtCognome;
	JLabel		jlNome, jlCognome;
	//riferimento alla finestra madre
	WindowGame	wGame;

	/**
	 * Costruttore si occupa di istanziare le variabili di classe
	 * e di sistemarle nel layout
	 * @param	owner	Riferimento alla finestra principale
	 * */
	public MyDialogInputPlayer(WindowGame owner){

		this.setResizable(false);
		this.wGame=owner;
		this.jbOk=new JButton("OK");
		this.jbOk.addActionListener(this);
		this.jbOk.setMnemonic(KeyEvent.VK_O);
		this.jbAnnulla=new JButton("Annulla");
		this.jbAnnulla.addActionListener(this);
		this.jbAnnulla.setMnemonic(KeyEvent.VK_A);
		this.jtNome= new JTextField();
		this.jtCognome= new JTextField();
		this.jlNome=new JLabel("Nome: ");
		this.jlCognome= new JLabel("Cognome: ");
		this.getContentPane().setLayout(new GridLayout(3, 2));
		this.getContentPane().add(this.jlNome);
		this.getContentPane().add(this.jtNome);
		this.getContentPane().add(this.jlCognome);
		this.getContentPane().add(this.jtCognome);
		this.getContentPane().add(this.jbAnnulla);
		this.getContentPane().add(this.jbOk);

		//posizioniamo il dialogo nel mezzo della finestra principale
		this.setBounds(this.wGame.getBounds().x+this.wGame.getBounds().width/2-100,
				this.wGame.getBounds().y+this.wGame.getBounds().height/2-50,
				210, 120);

		//rendiamo modale e quindi bloccante il dialogo
		this.setModal(true);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jbOk)){
			//se clickiamo ok impostiamo il nome e il cognome della finestra principale
			//ma se il textfield è vuoto (length()=0) allora lasciamo a null il nome e cognome
			this.wGame.setNomePlayer(this.jtNome.getText().length()!=0?this.jtNome.getText(): null);
			this.wGame.setCognomePlayer(this.jtCognome.getText().length()!=0?this.jtCognome.getText(): null);
		}
		this.dispose();
	}

}

