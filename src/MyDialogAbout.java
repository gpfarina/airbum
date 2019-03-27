import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StringBufferInputStream;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Si occupa di gestire l'about del programma
 * mostrando delle scritte scorrevoli su un pannello interno
 */
@SuppressWarnings("serial")
public class MyDialogAbout extends JDialog implements ActionListener {
	
	//
	//Attributi
	//
	
	private MyAboutPanel	myAboutPanel;
	private JButton			bOk;

	/**
	 * Costruttore, inizializza il layout del pannello 
	 * @param owner finestra da cui viene richiamato
	 * */
	public MyDialogAbout(WindowGame owner){

		this.setBounds(500, 200,
				220, 350
		);



		this.setTitle("About Airbum");
		this.setModal(true);
		this.setResizable(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.bOk=new JButton("Chiudi");
		this.bOk.addActionListener(this);
		this.bOk.setMnemonic(KeyEvent.VK_C);
		this.getContentPane().add(this.bOk, BorderLayout.SOUTH);
		
		this.myAboutPanel= new MyAboutPanel();
		this.getContentPane().add(this.myAboutPanel, BorderLayout.CENTER);
		this.myAboutPanel.startTimer();

	}

	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}

	/**
	 * Classe privata
	 * pannello del dialog per l'about dove verranno
	 * stampate le stringhe da un file di testo about.txt
	 *
	 */
	@SuppressWarnings("serial")
	private class MyAboutPanel extends JPanel implements ActionListener{
		private Timer 	tDelay;
		//vettore dove verranno caricate le stringhe prese
		//dal file about.txt
		private Vector<String>	stringheAbout;
		private int 	firstLine, counterLineToPrint;
		private Font fontAbout;
		private Graphics2D screen;

		/**
		 * Costruttore di default 
		 * crea il pannello del dialo, imposta i font e le dimensioni
		 * e crea il vettore di stringhe da stampare
		 */
		public MyAboutPanel(){
			//timer per lo scorrimento verticale
			tDelay=new Timer(500, this);
			stringheAbout= new Vector <String>();
			//imposto un font personalizzato monospaced
			try {
				InputStream is = ClassLoader.getSystemResourceAsStream("whtrabbit.ttf");
				fontAbout = Font.createFont(Font.TRUETYPE_FONT, is);
				fontAbout=fontAbout.deriveFont((float)14.0);
			} catch (Exception ex) {
				GestioneErrori.error(GestioneErrori.ERROR_FONT_NOT_PRESENT, ex);
				fontAbout = new Font("serif", Font.PLAIN, 60);

			}
			try{
				InputStream in = ClassLoader.getSystemResourceAsStream("about.txt");
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
			      while (null != (line = br.readLine())) {
			         stringheAbout.add(line);
			    }
			}
			catch(Exception e){
				GestioneErrori.error(GestioneErrori.ERROR_GENERAL_EXCEPTION, e);
			}

		}

		/**
		 * Fa partire il timer per lo scorrimento
		 * delle scritte sul pannello
		 *
		 */
		public void startTimer(){
			this.firstLine=290;
			this.counterLineToPrint=0;
			this.tDelay.start();
		}


		public void actionPerformed(ActionEvent e) {
			if(this.counterLineToPrint < this.stringheAbout.size())
				this.counterLineToPrint++;
			else
				this.tDelay.stop();
			this.repaint();
		}	

		public void paintComponent(Graphics g){
			screen = (Graphics2D)g;
			super.paintComponent (screen);
			screen.setFont(fontAbout);
			for(int i=0; i<this.counterLineToPrint; ++i)
				screen.drawString(this.stringheAbout.elementAt(i).toString(), 10, this.firstLine+(i+1)*20);

			this.firstLine-=20;
		}
	}

}
