import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Rappresenta una classe astratta per quelle classi che vogliono  
 * serializzare degli oggetti su file
 * I file vengono salvati nella directory home dell'utente.
 */
public abstract class ToFile {
	//	Path al file di salvataggio
	private String filepath;
	//Nome file di salvataggio
	private String filenome;
	//File handler
	private File savefile;
	//File separator in base al sistema in uso es: "/" o "\"
	//per esempio Linux o Windows
	protected String separator;	


	/**
	 * Costruttore con parametri, si preoccupa di controllare se l'ambiente
	 * per il salvataggio del file è impostato, avendo cura di inizializzare
	 * le variabili private di questa classe.
	 * @param nomeFile Nome del file da creare per il salvataggio
	 */
	public ToFile(String nomeFile){
		this.separator = System.getProperty("file.separator");
		this.setFilePath(System.getProperty("user.home")+this.separator+".airbum");
		this.setFileNome(nomeFile);
		this.setFileHandler();
		//creo il file se non esiste
		boolean bool = this.createFileIfNotExists();
		//se il file non esisteva lo riempio con dati di default
		if(bool){ 
			this.createDefaultData();
			this.saveToFile();
		}else{
			//Carico i dati dal file
			this.loadFromFile();
		}
	}

	/**
	 * Costruttore con parametri, si preoccupa di controllare se l'ambiente
	 * per il salvataggio del file è impostato, avendo cura di inizializzare
	 * le variabili private di questa classe.
	 * @param path path alla cartella del file di salvataggio
	 * @param nomeFile Nome del file da creare per il salvataggio
	 */
	public ToFile(String path,String nomeFile){
		this(nomeFile);
		this.setFilePath(path);
		this.setFileNome(nomeFile);
		this.setFileHandler();
		//creo il file se non esiste
		boolean bool = this.createFileIfNotExists();
		//se il file non esisteva lo riempio con dati di default
		if(bool){ 
			this.createDefaultData();
			this.saveToFile();
		}
		//Carico i dati dal file
		this.loadFromFile();
	}
	/**
	 * Costruttore con parametri, si preoccupa di controllare se l'ambiente
	 * per il salvataggio del file è impostato, avendo cura di inizializzare
	 * le variabili private di questa classe.
	 * @param path path alla cartella del file di salvataggio
	 * @param nomeFile Nome del file da creare per il salvataggio
	 * @param h serve solo per differenziarlo dagli altri costruttori che settano anche l'handler
	 */
	public ToFile(String path, String nomeFile, boolean h){
		this.setFilePath(path);
		this.setFileNome(nomeFile);
	}
	//
	//Metodi set e get
	//

	/**
	 * Setta il path per il file
	 * @param path Path al file
	 */
	public void setFilePath(String path){
		this.filepath = path;
	}

	/**
	 * Ritorna il path del file
	 * @return Stringa contenente il path al file
	 */
	public String getFilePath(){
		return this.filepath;
	}

	/**
	 * Ritorna il nome del file
	 * @return Stringa contenente il nome al file
	 */
	public String getFileNome(){
		return this.filenome;
	}

	/**
	 * Setta nome per il file di salvataggio
	 * @param nome Nome del file
	 */
	public void setFileNome(String nome){
		this.filenome = nome;
	}

	/**
	 * Setta l'handler del file ad un diverso handler
	 * @param fh File handler
	 */
	public void setFileHandler(File fh){
		this.savefile= fh;
	}

	/**
	 * Setta l'handler del file con i parametri di default della classe
	 */
	public void setFileHandler(){

		this.savefile = new File(this.getFilePath()+ separator +this.getFileNome());
	}

	/**
	 * Ritorna il file handler
	 * @return File handler al file di salvataggio
	 */
	public File getFileHandler(){
		return this.savefile;
	}

	//
	//Altri metodi
	//

	/**
	 * Crea il nel caso questo non esista
	 * di default
	 * @return false se il file esiste, true se è stato creato
	 */
	public boolean createFileIfNotExists(){
		boolean ritorno = false;
		try{
			File dir = new File(this.getFilePath());
			if(! dir.exists()){
				dir.mkdir();
			}
			if(!this.getFileHandler().exists()){
				ritorno  = true;
				this.getFileHandler().createNewFile();
			}

		}
		catch (IOException e) {
			GestioneErrori.error(GestioneErrori.ERROR_GENERAL_IO_EXCEPTION, e);
		}
		return ritorno; 
	}

	/**
	 * Serializza un oggetto
	 */
	protected void saveToFile(){
		try 
		{

			//provo a creare il file 
			FileOutputStream outFileStream = new FileOutputStream(this.getFileHandler());
			ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
			//Serializzo gli oggetti
			writeObjects(outObjectStream);

			outObjectStream.close();
			outFileStream.close();
		} 
		catch (FileNotFoundException e) 
		{
			GestioneErrori.error(GestioneErrori.ERROR_FILE_NOT_FOUND_EXCEPTION, e);
		} 
		catch (IOException e) 		{
			GestioneErrori.error(GestioneErrori.ERROR_GENERAL_IO_EXCEPTION, e);

		} 		
	}

	/**
	 * 	Legge il file e lo deserializza richiamando la funzione da specializzare 
	 *  restoreObjects
	 */
	protected void loadFromFile(){
		try  
		{	//Apro il file e mi preparo per la deserializzazione	
			FileInputStream inFileStream = new FileInputStream(this.getFileHandler());
			//ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
			ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
			//Ripristino gli oggetti dal file richiamando il metodo
			//che la classe che estende ToFile dovrà specializzare
			this.restoreObjects(inObjectStream);


			inObjectStream.close();
			inFileStream.close();
		} 
		catch (FileNotFoundException e) 
		{
			GestioneErrori.error(GestioneErrori.ERROR_FILE_NOT_FOUND_EXCEPTION, e);


	} 
		catch (IOException e) 
		{
			GestioneErrori.error(GestioneErrori.ERROR_GENERAL_IO_EXCEPTION, e);

		} 
		catch (ClassNotFoundException e) 
		{
			GestioneErrori.error(GestioneErrori.ERROR_CLASS_NOT_FOUND_EXCEPTION, e);

		}

	}

	//
	//Metodi astratti
	//

	/**
	 * Crea valori di deafault per la classe in uso
	 * ovvero dati di default da serializzare, usato nel caso in cui
	 * il file per la memorizzazione non esista.
	 */
	protected abstract void createDefaultData();

	/**
	 * Ripristina un oggetto letto dallo stream
	 * la classe che estende ToFile deve sovrascrivere e specializzare questo 
	 * metodo  
	 * @param in ObjectInputStream da cui leggere per deserializzare
	 * @throws IOException 
	 * @throws ClassNotFoundException  
	 */
	protected abstract void restoreObjects(ObjectInputStream in) throws IOException, ClassNotFoundException;

	/**
	 * Scrive un oggetto su file
	 * la classe che estende ToFile deve sovrascrivere e specializzare questo 
	 * metodo  
	 * @param out ObjectInputStream da cui leggere per serializzare
	 * @throws IOException 
	 */
	protected abstract void writeObjects(ObjectOutputStream out) throws IOException;



}
