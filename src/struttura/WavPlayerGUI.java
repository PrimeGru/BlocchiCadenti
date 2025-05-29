package struttura;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class WavPlayerGUI extends JFrame {

    public WavPlayerGUI() {
        super("WAV Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 100);
        setLocationRelativeTo(null);
        JLabel label = new JLabel("Riproduzione automatica del file .wav...", SwingConstants.CENTER);
        add(label);
        setVisible(true);

        File currentDir = new File(".");
        File[] wavFiles = currentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

        if (wavFiles != null && wavFiles.length == 1) {
            playWavFile(wavFiles[0]);
        } else if (wavFiles == null || wavFiles.length == 0) {
            JOptionPane.showMessageDialog(this, "Nessun file .wav trovato nella cartella corrente.", "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Trovati più file .wav. Lascia solo uno nella cartella.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void playWavFile(File wavFile) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            JOptionPane.showMessageDialog(this, "Riproduzione in corso...\nChiudi questo messaggio per fermare il suono.");
            clip.stop();
            clip.close();
            audioStream.close();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(this, "Formato audio non supportato.", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore durante la lettura del file.", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, "Linea audio non disponibile.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WavPlayerGUI::new);
    }
}
