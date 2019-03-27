import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Classe per gestire i risultati dei giocatori, ovvero caricarli da
 * un file serializzato o salvare nuovi migliori punteggi.
 * Per salvare un nuovo punteggio questo deve essere precedentemente creato
 * (classe Punteggio) e passato come parametro a aggiornaRisultati, dopo 
 * di che richiamare saveToFile.
 * Questa classe estende ToFile per avere metodi comuni per la scrittura su file
 */
public class GestionePunteggi extends ToFile {

	//
	// Attributi
	// 

	//punteggi caricati dall'ultima apertura del file
	//NB l'array è in modo ordinato decrescente.
	private Punteggio[] risultati;

	/**
	 * Costruttore di default senza parametri
	 * Richiama il costruttore di ToFile per creare il file se non esiste
	 * e caricare i dati.
	 */
	public GestionePunteggi () { 
		super("punteggi.score");
	}

	//
	// Metodi
	//

	//
	// Metodi set e get
	//

	/**
	 * Setta (aggiorna) l'array dei risultati con i punteggi del giocatore
	 * @param score Punteggio del giocatore da aggiornare
	 */
	public void aggiornaRisultati (Punteggio score ) {

		//sfrutto l'ordinamento dell'array per inserire il nuovo punteggio
		int i = 0;
		while ((i<10) && (score.getPunteggio() <= risultati[i].getPunteggio())){
			i++;
		}
		if (i<10){
			//allora ho un punteggio migliore da aggiungere
			//copio quindi i risultati da i cancellando il peggiore
			for (int j = 9; j>i;j--){
				risultati[j] = risultati[j-1];
			}
			risultati[i]=score;
		}

	}


	/**
	 * Setta (aggiorna) l'array dei risultati con un punteggio
	 * @param i	Set the result number i
	 * @param score Punteggio del giocatore da aggiornare
	 */
	public void setRisultati (int i , Punteggio score ) {
		risultati [i] = score ;

	}

	/**
	 * Ritorna un punteggio memorizzati dopo l'apertura del file
	 * @param i Offset dell'array dei Punteggi
	 * @return the value of risultati
	 */
	public Punteggio getRisultati (int i ) {
		return this.risultati[i];
	}

	//
	// Altri metodi
	//
	/**
	 * Riempie l'array risultati con dati di default per i Punteggi
	 * ovvero con stringe vuote e punteggi a zero
	 */
	@Override
	protected void createDefaultData() {
		this.risultati = new Punteggio[10];
		for (int i=0;i<10;i++){
			this.risultati[i] = new Punteggio();
		}
	}
	/**
	 * Ripristina un oggetto letto dallo stream
	 * la classe che estende ToFile deve sovrascrivere e specializzare questo 
	 * metodo  
	 * @param in ObjectInputStream da cui leggere per deserializzare
	 */
	@Override
	public void restoreObjects(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		int toRead = in.readInt();
		risultati = new Punteggio[toRead];
		for (int i = 0; i < toRead; ++i)
		{

			this.setRisultati(i,(Punteggio)in.readObject());
		}

	}

	/**
	 * Scrive l'array di risultati sul file 
	 * @param out ObjectInputStream da cui leggere per serializzare
	 */
	@Override
	public void writeObjects(ObjectOutputStream out) throws IOException
	{
		//Scrivo prima quanti oggetti sto andando a memorizzare
		out.writeInt(risultati.length);
		for (Punteggio o : risultati){
			out.writeObject(o);
		}

	}

}
