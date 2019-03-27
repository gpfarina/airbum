import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
/**
 * 
 * La seguente classe permette di salvare e recuperare le strategie
 * di sparo settate dal giocatore serializzate su file.
 */
@SuppressWarnings("serial")
public class GestioneStrategia extends ToFile{
	//
	//Attributi
	//
	/**
	 * Per memorizzare le Strategie utilizzeremo un'istanza della
	 * classe Vector in modo da rendere più flessibile la 
	 * quantità di informazione memorizzabile, anche se per la prima
	 * versione del gioco si è deciso di utilizzare un singolo
	 * file per una singola strategia, di conseguenza si avrà un solo elemento
	 * nel vector.
	 */
	private Vector<Strategia> strategies;

	/**
	 * Costruttore con parametri
	 * @param path Path al file
	 * @param nome nome del file
	 */
	public GestioneStrategia(String path, String nome){
		super(path, nome, true);
		strategies=new Vector<Strategia>();
	}


	/**
	 * Crea dati di default per le strategie
	 * Di default non inseriamo nessuna strategia
	 * */
	protected void createDefaultData(){
		strategies= new Vector<Strategia>();
	}

	/**
	 * Imposta l'iesima strategia nel vector di strategie
	 * @param	index 	elemento
	 * @param	st		strategia da aggiungere 
	 * */
	public void setStrategia(int index, Strategia st){
		strategies.add(index, st);
	}

	/**
	 * Ritorna la strategia di indice index
	 * @param	index indice della strategia da ritornare
	 * @return	iesima strategia
	 * */
	public Strategia getStrategia(int index){
		return strategies.elementAt(index);
	}


	/**
	 * Serializzo l'oggetto contenente le strategie.
	 * La prima cosa che scrive è la lunghezza del Vector
	 * cosi quando leggeremo sapremo quanto leggere
	 * @param out  l' ObjectOutputStream che usiamo per scrivere nel file
	 * */
	protected void writeObjects(ObjectOutputStream out)throws IOException{
		//scrivo il numero di strategie serializzate
		out.writeInt(strategies.size());
		for (int i=0; i<strategies.size(); ++i)
			out.writeObject(strategies.elementAt(i));

	}

	/**
	 * Recupera le strategie scritte precedentemente
	 * la prima cosa scritta è stato il numero delle strategies
	 * perciò questo numero sarà il primo ad essere letto in modo
	 * da sapere per quanto continuare la lettura
	 * @param in l'ObjectInputStream da dove leggeremo il file
	 * */
	protected void restoreObjects(ObjectInputStream in)throws IOException, ClassNotFoundException{
		int length=in.readInt();
		//cancello le strategie precedentemente memorizzate nel Vector 
		strategies=new Vector<Strategia>(length);
		for (int i=0; i<length; ++i)
			strategies.add((Strategia) in.readObject());
	}
}
