import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Classe per gestire la riproduzione di un file audio ogg.
 * uso JavaLayer ogg spi per la decodifica trasparente dei file ogg
 * http://www.javazoom.net/vorbisspi/vorbisspi.html
 * implemento l'interfaccia runnable per poi creare un thread che gestisce 
 * la riproduzione.
 */
public class MyAudioPlayer implements Runnable{  
	//
	//Attributi
	//
	//Stringhe per gestire il nome e il percorso del file audio
	private String audioFileNome, audioFilePath, separator;
	//File che conterra il file audio da suonare
	private	File toPlay;
	private 	AudioInputStream ais;
	//Destination input
	private AudioInputStream din;
	//Linea audio
	private SourceDataLine line;
	private boolean loop,isSuonoOn;
	//Thread utilizzato per la riproduzione
	private Thread thread;

	/**
	 * Costruttore con parametri, setta i percorsi per il file da riprodurre
	 * e prepara l'ambiente.
	 * @param nomeFile file da riprodurre
	 * @param ifLoop Parametro che indica se loopare il suono all'infinito
	 * @param suonoOn indica se nelle impostazioni è stato abilitato il suono
	 */
	public MyAudioPlayer(String nomeFile,boolean ifLoop,boolean suonoOn){
		//super(nomeFile);
		//imposto il separatore di sistema del nome file
		this.setSeparator();
		//Imposto il nome e il percorso del file audio da riprodurre
		this.setAudioFileNome(nomeFile);
		this.setAudioFilePath("sounds");
		//imposto il separatore di sistema del nome file
		this.setSeparator();
		//Creo il file handler del file da riprodurre
		this.setFileAudio();		
		this.loop = ifLoop;
		this.isSuonoOn = suonoOn;
	}

	//
	//Metodi set e get
	//

	/**
	 * Imposta il nome del file audio da riprodurre
	 * @param file il nome del file da riprodurre
	 * */
	public void setAudioFileNome(String file){
		this.audioFileNome=file;
	}

	/**
	 * Ritorna il nome del file audio da riporodurre
	 * @return audioFileNome
	 * */	
	public String getAudioFileNome(){
		return this.audioFileNome;
	}

	/**
	 * Imposta il percorso del file audio da riprodurre
	 * @param path il nome del file da riprodurre
	 * */
	public void setAudioFilePath(String path){
		this.audioFilePath=path;
	}
	/**
	 * Ritorna il percorso del file audio da riporodurre
	 * @return audioFilePath
	 * */	
	public String getAudioFilePath(){
		return this.audioFilePath;
	}


	/**
	 * Imposta il separatore del path \ o / a seconda del istema in cui 
	 * si esegue il programma
	 * */
	public void setSeparator(){
		this.separator = System.getProperty("file.separator");
	}

	/**
	 * Ritorna il separatore di default del sistema
	 * @return separator
	 * */
	public String getSeparator(){
		return this.separator;
	}


	/**
	 * Imposta l'handler del file audio
	 * */
	public void setFileAudio(){
		toPlay=new File(this.getAudioFilePath()+
				this.getSeparator()+
				this.getAudioFileNome());
	}

	/**
	 * Ritorna l'handler del file audio
	 * @return toPlay
	 * */

	public File getFileAudio(){
		return toPlay;
	}

	//
	//Altri metodi
	//

	/**
	 * riproduce il file audio tramite AudioSystem di java
	 * */
	private void playSound(){
		try{
			//Apro il file
			java.net.URL audioUrl = ClassLoader.getSystemResource(this.getAudioFilePath()+
					this.getSeparator()+
					this.getAudioFileNome());
			ais= AudioSystem.getAudioInputStream(audioUrl);
			din = null;

			if(ais != null){
				//Controllo la codifica del file per poi passare i
				//dati ottenuti a rawplay
				AudioFormat baseFormat = ais.getFormat();
				AudioFormat  decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(),
						false);

				din = AudioSystem.getAudioInputStream(decodedFormat, ais);
				//Suono realmente il file 
				rawplay(decodedFormat, din);
				ais.close();	

			}



		}
		catch(IOException e){
			GestioneErrori.error(GestioneErrori.ERROR_GENERAL_EXCEPTION, e);
		}
		catch(UnsupportedAudioFileException ee){
			GestioneErrori.error(GestioneErrori.ERROR_AUDIO_FILE_NOT_SUPPORTED, ee);
		}
		catch(LineUnavailableException Lue){
			GestioneErrori.error(GestioneErrori.ERROR_BUSY_AUDIO_LINE, Lue);
			this.stop();
		};
	}

	/**
	 * Si occupa di suonare decodificandolo in base al targetFormat precedentemente riconosciuto
	 * prima decodificato
	 * @param targetFormat formato del file riconosciuto
	 * @param din
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	private void rawplay(AudioFormat targetFormat, 
			AudioInputStream din) throws IOException, LineUnavailableException
			{
		byte[] data = new byte[4096];
		line = getLine(targetFormat);		
		//Se supporta il gain control abbasso il volume del suono
		if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)){
			double value = 0.1;
			try {
				FloatControl gainControl =
					(FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
				float dB = (float) (Math.log(value==0.0 ?
						0.0001:value)/Math.log(10.0)*20.0);
				gainControl.setValue(dB);
			} catch (Exception ex){
				GestioneErrori.error(GestioneErrori.ERROR_GENERAL_EXCEPTION, ex);
			}

		}
		if (line != null)
		{
			//Start
			line.start();
			int nBytesRead = 0;
			while (nBytesRead != -1 && this.isSuonoOn)
			{
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) line.write(data, 0, nBytesRead);
			}
			//Stop
			line.drain();
			line.stop();
			line.close();
			din.close();

		}		

			}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	/**
	 * Fa partire la riproduzione del file audio tramite un nuovo
	 * thread
	 */
	public void run(){
		if(this.isSuonoOn){
			do{
				this.playSound();
			}while(this.loop);
			this.stop();
		}
	}

	/**
	 * Stoppa il thread se è in loop
	 */
	private void stopIt(){
		this.isSuonoOn=this.loop=false;
	}

	/**
	 * crea un nuovo thread e fa
	 * partire il suono
	 *
	 */
	public void start() {
		thread = new Thread(this); 
		thread.start();
	}

	/**
	 * stoppa il suono e il thread 
	 * in modo corretto
	 *
	 */
	public void stop() {
		if(this.loop)this.stopIt();
		thread = null;
	}

}
