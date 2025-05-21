package struttura;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerAudio {

    private static boolean testingMode = false;
    private static String suonoBloccoPath = "";
    private static String suonoLineaCompletaPath = "";

    public static void setTestingMode(boolean testing) {
        testingMode = testing;
    }

    public static void setSuonoBlocco(String filePath) {
        suonoBloccoPath = filePath;
    }

    public static void setSuonoLineaCompleta(String filePath) {
        suonoLineaCompletaPath = filePath;
    }

    public static void riproduciMusicaInLoop(String filePath) {
        if (testingMode) {
            System.out.println("[TEST MODE] Simulazione della riproduzione in loop del file: " + filePath);
            return;
        }

        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            while (true) {
                try {
                    Thread.sleep(1000); // Pausa per non consumare CPU inutilmente
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interruzione del thread di riproduzione.");
                    break;
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Errore nel caricamento o nella riproduzione del file audio: " + e.getMessage());
        }
    }

    public static void avviaRiproduzionePlaylist(List<String> playlist) {
        if (testingMode) {
            System.out.println("[TEST MODE] Simulazione della riproduzione playlist:");
            for (String filePath : playlist) {
                System.out.println("Simulazione riproduzione: " + filePath);
            }
            return;
        }

        for (String filePath : playlist) {
            try {
                File audioFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                while (clip.isRunning()) {
                    Thread.sleep(100);
                }

                clip.close();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                System.err.println("Errore durante la riproduzione del brano: " + filePath + " -> " + e.getMessage());
            }
        }
    }

    public static void riproduciSuonoEffetto(String filePath) {
        if (testingMode) {
            System.out.println("[TEST MODE] Suono effetto simulato: " + filePath);
            return;
        }

        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Errore nel riprodurre il suono effetto: " + e.getMessage());
        }
    }

    public static void suonoBloccoToccato() {
        riproduciSuonoEffetto(suonoBloccoPath);
    }

    public static void suonoLineaCompletata() {
        riproduciSuonoEffetto(suonoLineaCompletaPath);
    }

    public static void main(String[] args) {
        // Specifica il percorso del file audio
        String filePath = "path/to/your/music/file/music.wav"; // Modifica il percorso in base al tuo file

        // Specifica i percorsi dei file audio per la playlist
        List<String> gameSoundtracks = new ArrayList<>();
        gameSoundtracks.add("path/to/your/music/file/music1.wav");
        gameSoundtracks.add("path/to/your/music/file/music2.wav");
        gameSoundtracks.add("path/to/your/music/file/music3.wav");
        // Aggiungi tutti i brani che desideri

        // Imposta i suoni personalizzati
        setSuonoBlocco("path/to/your/sound/effect/hit.wav");
        setSuonoLineaCompleta("path/to/your/sound/effect/line_clear.wav");

        // *** Per testare senza audio, abilita la modalità di test: ***
        setTestingMode(true);

        // Avvia la riproduzione della musica
        riproduciMusicaInLoop(filePath);

        // Avvia la riproduzione della playlist
        avviaRiproduzionePlaylist(gameSoundtracks);

        // Simula un evento dove un blocco tocca terra
        System.out.println("Simulazione evento: blocco tocca il terreno");
        suonoBloccoToccato();

        // Simula un evento di linea completata
        System.out.println("Simulazione evento: linea completata");
        suonoLineaCompletata();
    }
}
