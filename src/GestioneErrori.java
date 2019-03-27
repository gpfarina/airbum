import javax.swing.JOptionPane;

/**
 * Gestione degli errori
 * ogni elemento del vector contiene una stringa
 * nella forma: indexError, Description
 */
public class GestioneErrori {

	//
	//Attributi
	//
	//Indici degli errori utilizzati nell array errors

	/**
	 * Eccezione generale
	 */
	public final static int ERROR_GENERAL_EXCEPTION=0;
	/**
	 * Eccezione di input output
	 */
	public final static int ERROR_GENERAL_IO_EXCEPTION=1;
	/**
	 * Eccezione nel caricamento di una immagine
	 */
	public final static int ERROR_LOADING_IMAGE=2;
	/**
	 * Linea audio occupata
	 */
	public final static int ERROR_BUSY_AUDIO_LINE=3;
	/**
	 * File audio non supportato per la riproduzione
	 */
	public final static int ERROR_AUDIO_FILE_NOT_SUPPORTED=4;
	/**
	 * Classe look and feel non trovata
	 */
	public final static int ERROR_LOOK_AND_FEEL_CLASS_NOT_FOUND=5;
	/**
	 * Errore nella creazione della classe look and feel
	 */
	public final static int ERROR_LOOK_AND_FEEL_CREATING_CLASS=6;
	/**
	 * Accesso illegale alla classe look and feel
	 */
	public final static int ERROR_LOOK_AND_FEEL_ILLEGAL_CLASS_ACCESS=7;
	/**
	 * Stile non supportato per il look and feel
	 */
	public final static int ERROR_LOOK_AND_FEEL_UNSUPPORTED_STYLE=8;
	/**
	 * Font non presente
	 */
	public final static int ERROR_FONT_NOT_PRESENT=9;
	/**
	 * File non trovato
	 */
	public final static int ERROR_FILE_NOT_FOUND_EXCEPTION=10;
	/**
	 * Classe non trovata
	 */
	public final static int ERROR_CLASS_NOT_FOUND_EXCEPTION=11;


	private final static int MAX_ERRORS=12;
	private static String[] errori=null;


	/**
	 * Stampa l'errore con indice specificato, e nel caso non lo si sia gia fatto
	 * carica l'array di stringhe
	 * @param index indice dell'errore
	 * @param e copia dell'eccezione 
	 * */
	public static void error(int index, Exception e){
		if(errori==null) 
			loadErrors();
		JOptionPane.showMessageDialog(null, index+": "+errori[index], "Errore", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace(); 
	}

	/**
	 * Carica gli arrai degli errori
	 */
	public static void loadErrors(){
		errori=new String[MAX_ERRORS];

		//errori generali
		errori[ERROR_GENERAL_EXCEPTION]="Eccezione generale";
		errori[ERROR_GENERAL_IO_EXCEPTION]="Eccezione generale di I/O";
		errori[ERROR_LOADING_IMAGE]="Impossibile attendere il caricamento delle immagine relative al cannone";
		errori[ERROR_BUSY_AUDIO_LINE]="Linea audio occupata";
		errori[ERROR_AUDIO_FILE_NOT_SUPPORTED]="File audio in caricamento non supportato";

		//errori dialog strategie: stile
		errori[ERROR_LOOK_AND_FEEL_CLASS_NOT_FOUND]="Errore nell'impostazione del Look&Feel: classe non trovata";
		errori[ERROR_LOOK_AND_FEEL_CREATING_CLASS]="Errore nell'instanziamento della classe Look&Feel";
		errori[ERROR_LOOK_AND_FEEL_ILLEGAL_CLASS_ACCESS]="Accesso illegale alla classe dello stile";
		errori[ERROR_LOOK_AND_FEEL_UNSUPPORTED_STYLE]="Stile in caricamento non supportato";

		errori[ERROR_FONT_NOT_PRESENT]="Font non presente nel sistema";
		errori[ERROR_FILE_NOT_FOUND_EXCEPTION]="Uno o piu file necessari non trovati";

		errori[ERROR_CLASS_NOT_FOUND_EXCEPTION]="Classe in caricamento non riconosciuta";


	}


}
