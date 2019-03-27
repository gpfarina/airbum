import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe per gestire la memorizzazione delle impostazioni
 * generali di gioco su file e contenete un oggetto per la memorizzazione 
 * di queste.
 * Se il file non esiste viene creato con settaggi di default
 * altrimenti viene caricato e le impostazioni sono settate correttamente.
 *
 */
public class GestioneSettaggiGenerali extends ToFile{

	//
	//Attributi
	//
	
	//Oggetto per memorizzare i settaggi generali
	private SettaggiGenerali settings;

	/**
	 * Costruttore di default
	 * Richiama il costruttore di ToFile per creare il file se non esiste
	 * e caricare i dati.
	 *
	 */
	public GestioneSettaggiGenerali(){
		super("genSettings.set");

	}

	//
	//Metodi set e Get
	//

	/**
	 * Ritorna l'oggetto interno che memorizza i settaggi giocatore
	 * @return Riferimento all'oggetto che memorizza i settaggi generali
	 */
	public SettaggiGenerali getSettings(){
		return this.settings;
	}

	//
	//Altri metodi
	//

	/**
	 * Crea impostazioni base di gioco
	 * ovvero dati di default da serializzare, usato nel caso in cui
	 * il file per la memorizzazione non esista.
	 */
	@Override
	protected void createDefaultData(){
		this.settings = new SettaggiGenerali();
	}

	/**
	 * Ripristina un oggetto letto dallo stream
	 * la classe che estende ToFile deve sovrascrivere e specializzare questo 
	 * metodo  
	 * @param in ObjectInputStream da cui leggere per deserializzare
	 */
	@Override
	protected void restoreObjects(ObjectInputStream in) throws IOException, ClassNotFoundException
	{ 
		this.settings = (SettaggiGenerali)in.readObject();

	}

	/**
	 * Serializza l'oggetto su file
	 * @param out ObjectInputStream in cui scrivere per serializzare
	 */
	@Override
	protected void writeObjects(ObjectOutputStream out) throws IOException
	{
		out.writeObject(this.settings);

	}


}
