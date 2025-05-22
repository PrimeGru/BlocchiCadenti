package game.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Highscores {

    private static final String FILENAME = "highscores.txt";
    private Map<String, Integer> highscores;

    public Highscores() {
        highscores = new HashMap<>();
        loadHighscores();
    }

    private void loadHighscores() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            return; // No high scores to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2); // Split only on the first colon
                if (parts.length == 2) {
                    String nickname = parts[0].trim();
                    try {
                        int score = Integer.parseInt(parts[1].trim());
                        highscores.put(nickname, score);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid score for nickname '" + nickname + "' in " + FILENAME);
                    }
                } else {
                    System.err.println("Skipping invalid line format in " + FILENAME + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading highscores from " + FILENAME + ": " + e.getMessage());
        }
    }

    public void updateScore(String nickname, int newScore) {
        if (nickname == null || nickname.trim().isEmpty()) {
            System.err.println("Nickname cannot be empty. Score not saved.");
            return;
        }
        // Only update if new score is higher, or if player is not yet in scores
        if (!highscores.containsKey(nickname) || newScore > highscores.getOrDefault(nickname, Integer.MIN_VALUE)) {
            highscores.put(nickname, newScore);
            saveHighscores();
        }
    }

    private void saveHighscores() {
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(highscores.entrySet());
        // Sort in descending order of score
        sortedScores.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Map.Entry<String, Integer> entry : sortedScores) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving highscores to " + FILENAME + ": " + e.getMessage());
        }
    }

    public List<Map.Entry<String, Integer>> getSortedHighscores(int limit) {
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(highscores.entrySet());
        sortedScores.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return sortedScores.subList(0, Math.min(limit, sortedScores.size()));
    }
}