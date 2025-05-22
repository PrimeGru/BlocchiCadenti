package game.core;

import game.config.GameConstants;
import game.model.Tetramino;

public class ScoreManager {
    private int score;
    private int currentLevel; // Tracks levels for color shuffling, distinct from difficulty level
    private final int scorePerLevelUp;

    public ScoreManager() {
        this.scorePerLevelUp = GameConstants.SCORE_PER_LEVEL_UP;
        reset();
    }

    public void reset() {
        this.score = 0;
        this.currentLevel = 0;
        Tetramino.resetColors(); // Reset colors on game/score reset
    }

    public void processClearedRows(int numClears) {
        if (numClears <= 0) {
            return;
        }
        int pointsEarned = 0;
        switch (numClears) {
            case 1: pointsEarned = 100; break;
            case 2: pointsEarned = 300; break;
            case 3: pointsEarned = 500; break;
            case 4: pointsEarned = 800; break; // Tetris!
            default: pointsEarned = 800 + (numClears - 4) * 200; // Bonus for more than 4 (if possible)
        }
        score += pointsEarned;
        checkAndApplyLevelUp();
    }

    private void checkAndApplyLevelUp() {
        int newPotentialLevel = score / scorePerLevelUp;
        if (newPotentialLevel > currentLevel) {
            currentLevel = newPotentialLevel;
            Tetramino.shuffleColors();
            // Potentially add more level up effects here if desired
        }
    }

    public int getScore() {
        return score;
    }

    public int getCurrentDisplayLevel() {
        return currentLevel; // This is the color shuffle level
    }
}