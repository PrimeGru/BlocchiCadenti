package struttura;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MusicaInLoop {

    private static Clip clip;
    private static List<String> playlist;
    private static List<String> shuffledPlaylist;
    private static int currentSongIndex = 0;
    private static String lastPlayedSong = null;
    private static Random random = new Random();
    private static boolean isTestingMode = false; // Flag per la modalità di test

    // Metodo per impostare la modalità di test
    public static void setTestingMode(boolean testingMode) {
        isTestingMode = testingMode;
        if (isTestingMode) {
            System.out.println("Modalità di test attivata. L'audio non verrà riprodotto.");
        } else {
            System.out.println("Modalità di test disattivata. L'audio verrà riprodotto.");
        }
    }

    // Metodo per caricare e riprodurre il brano corrente (o simularlo in modalità test)
    private static void playSong(String filePath) {
        if (isTestingMode) {
            System.out.println("TEST MODE: Simulazione riproduzione di: " + filePath);
            lastPlayedSong = filePath; // Aggiorna l'ultimo brano riprodotto anche in test mode
            // Simula la durata del brano
            try {
                Thread.sleep(1000); // Simula 1 secondo di riproduzione
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interruzione nella simulazione della riproduzione.");
            }
            playNextSong(); // Passa al brano successivo dopo la simulazione
        } else {
            try {
                File audioFile = new File(filePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // Aggiunge un listener per sapere quando il brano è finito
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        playNextSong(); // Passa al brano successivo quando il corrente finisce
                    }
                });

                clip.start();
                System.out.println("Riproduzione: " + filePath);
                lastPlayedSong = filePath; // Aggiorna l'ultimo brano riprodotto

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Errore nel caricamento o nella riproduzione del file audio: " + e.getMessage());
                playNextSong(); // Prova a riprodurre il brano successivo in caso di errore
            }
        }
    }

    // Metodo per passare al brano successivo nella playlist casuale
    private static void playNextSong() {
        currentSongIndex++;

        // Se abbiamo riprodotto tutti i brani nella playlist casuale corrente
        if (currentSongIndex >= shuffledPlaylist.size()) {
            shufflePlaylist(); // Mischia di nuovo la playlist
            currentSongIndex = 0; // Ricomincia dall'inizio della nuova playlist casuale
        }

        // Riproduci il brano successivo
        playSong(shuffledPlaylist.get(currentSongIndex));
    }

    // Metodo per mischiare la playlist assicurandosi che il primo brano non sia lo stesso dell'ultimo riprodotto
    private static void shufflePlaylist() {
        shuffledPlaylist = new ArrayList<>(playlist); // Crea una copia della playlist originale
        Collections.shuffle(shuffledPlaylist, random); // Mischia la copia

        // Assicurati che il primo brano della nuova playlist mischiata non sia l'ultimo riprodotto
        if (lastPlayedSong != null && shuffledPlaylist.get(0).equals(lastPlayedSong)) {
            // Trova un altro brano con cui scambiare il primo
            for (int i = 1; i < shuffledPlaylist.size(); i++) {
                if (!shuffledPlaylist.get(i).equals(lastPlayedSong)) {
                    Collections.swap(shuffledPlaylist, 0, i);
                    break;
                }
            }
            // Se la playlist ha solo un elemento e questo è l'ultimo riprodotto, non possiamo fare altro che ripeterlo.
            // In un caso reale con più brani, questo swap dovrebbe sempre trovare un'alternativa.
        }
         System.out.println("Playlist mischiata. Nuovo ordine:");
         shuffledPlaylist.forEach(System.out::println);
    }

    // Metodo per avviare la riproduzione in loop dalla playlist
    public static void avviaRiproduzionePlaylist(List<String> musicFiles) {
        playlist = new ArrayList<>(musicFiles); // Inizializza la playlist con i file forniti

        if (playlist.isEmpty()) {
            System.out.println("La playlist è vuota. Nessun brano da riprodurre.");
            return;
        }

        shufflePlaylist(); // Mischia la playlist iniziale
        playSong(shuffledPlaylist.get(currentSongIndex)); // Avvia la riproduzione del primo brano

        System.out.println("Riproduzione playlist in loop... Premi Ctrl+C per fermare.");

        // Il programma rimane in attesa della fine della musica (gestita dal LineListener o dalla simulazione in test mode)
        // In un'applicazione di gioco reale, qui ci sarebbe il loop principale del gioco.
        // Questo while(true) è solo per mantenere il programma in esecuzione per la riproduzione audio/simulazione.
        while (true) {
            try {
                Thread.sleep(1000); // Pausa per non consumare CPU inutilmente
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interruzione del thread di riproduzione.");
                break;
            }
        }
    }

    public static void main(String[] args) {
        // Specifica i percorsi dei file audio per la playlist
        List<String> gameSoundtracks = new ArrayList<>();
        gameSoundtracks.add("path/to/your/music/file/music1.wav"); // Modifica i percorsi
        gameSoundtracks.add("path/to/your/music/file/music2.wav");
        gameSoundtracks.add("path/to/your/music/file/music3.wav");
        // Aggiungi tutti i brani che desideri

        // *** Per testare senza audio, decommenta la riga seguente: ***
        setTestingMode(true);

        // Avvia la riproduzione della playlist
        avviaRiproduzionePlaylist(gameSoundtracks);
    }
}
