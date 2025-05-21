package game;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Highscores {

    private static final String FILENAME = "highscores.txt";
    private Map<String, Integer> highscores;

    public Highscores() {
        highscores = new HashMap<>();
        loadHighscores();
    }

    /**
     * Loads high scores from the file into the highscores map.
     */
    private void loadHighscores() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            // File doesn't exist, no high scores to load yet.
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String nickname = parts[0].trim();
                    try {
                        int score = Integer.parseInt(parts[1].trim());
                        highscores.put(nickname, score);
                    } catch (NumberFormatException e) {
                        // Silently skip invalid score format
                    }
                } else {
                    // Silently skip invalid line format
                }
            }
        } catch (IOException e) {
            // Silently handle file reading errors
        }
    }

    /**
     * Updates the score for a given nickname. If the nickname exists and the new score
     * is higher, the score is updated. If the nickname does not exist, it's added.
     * After updating, the high scores are saved back to the file.
     *
     * @param nickname The nickname of the player.
     * @param newScore The new score to potentially update.
     */
    public void updateScore(String nickname, int newScore) {
        boolean updated = false;
        if (highscores.containsKey(nickname)) {
            int currentScore = highscores.get(nickname);
            if (newScore > currentScore) {
                highscores.put(nickname, newScore);
                updated = true;
            }
        } else {
            highscores.put(nickname, newScore);
            updated = true;
        }

        if (updated) {
            saveHighscores();
        }
    }

    /**
     * Saves the current high scores from the map back to the file, overwriting the existing file.
     */
    private void saveHighscores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Map.Entry<String, Integer> entry : highscores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            // Silently handle file writing errors
        }
    }

}