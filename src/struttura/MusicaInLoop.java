package struttura;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicaInLoop {

    // Metodo per caricare e riprodurre la musica in loop
    public static void riproduciMusicaInLoop(String filePath) {
        try {
            // Carica il file audio
            File audioFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();

            // Apre il clip con il flusso audio
            clip.open(audioInputStream);

            // Imposta il clip in loop continuo
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Avvia la riproduzione
            clip.start();

            System.out.println("Musica in loop... Premi Ctrl+C per fermare.");

            // Il programma rimane in attesa della fine della musica
            while (true) {
                // Qui puoi aggiungere altre logiche per il gioco, come:
                // - Aggiornamenti delle variabili del gioco
                // - Controllo dell'input dell'utente
                // - Movimenti dei personaggi
                // - Gestione degli eventi
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Errore nel caricamento o nella riproduzione del file audio: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Specifica il percorso del file audio
        String filePath = "path/to/your/music/file/music.wav"; // Modifica il percorso in base al tuo file

        // Avvia la riproduzione della musica
        riproduciMusicaInLoop(filePath);
    }
}
