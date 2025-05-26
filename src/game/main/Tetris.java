package game.main;

import game.core.GameController;
import game.model.Highscores;
import game.ui.GamePanel;
import game.config.GameConstants; // For window sizing

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import game.config.GameConstants;

public class Tetris {
    private GamePanel gamePanel; // Changed from Game to GamePanel
    private GameController gameController;
    public JFrame frame;
    private Timer timer;
    private int initialDelay;
    private String[] commandLineArgs; // Store command line arguments for nickname

    public void run(String[] args) {
        this.commandLineArgs = args;

        String selectedLevel = (args.length > 0) ? args[0] : "Medio"; // Default if no arg
        this.initialDelay = getDelayForDifficulty(selectedLevel);

        // Initialize core game logic
        this.gameController = new GameController();
        // Initialize UI Panel with the controller
        this.gamePanel = new GamePanel(gameController);

        // Setup JFrame
        frame = new JFrame("BlocchiCadenti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Adjust size to match GamePanel's preference or a calculated one
        // The original calculation: 17 * 26 width, 26 * 24 + 14 height
        // New preferred size from GamePanel is (WELL_WIDTH + 6) * BLOCK_SIZE, WELL_HEIGHT * BLOCK_SIZE
        // Example: (12+6)*26 = 18*26 = 468. (23)*26 = 598.
        // Original: 17*26 = 442.  26*24+14 = 624+14 = 638.
        // Let's use GamePanel's preferred size and let pack() handle it or set explicitly.
        // For consistency with original visual size:
        frame.setSize(17 * GameConstants.BLOCK_SIZE, 23 * GameConstants.BLOCK_SIZE + 40); // Adjusted to be closer, +40 for title bar
        frame.setLocationRelativeTo(null);

        frame.add(gamePanel);
        frame.setVisible(true); // Set visible before requesting focus

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gamePanel.isGameOver()) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        Highscores scores = new Highscores();
                        String nickname = (commandLineArgs.length > 1) ? commandLineArgs[1] : "Player";
                        scores.updateScore(nickname, gamePanel.getCurrentScore());
                        frame.dispose(); // Close the game window
                    }
                    return;
                }

                // Game controls only if not game over
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        gamePanel.rotatePiece(-1); // Counter-clockwise
                        break;
                    case KeyEvent.VK_DOWN:
                         // Original had rotate(+1), some prefer soft drop. Keeping original.
                        gamePanel.rotatePiece(1); // Clockwise 
                        break;
                    case KeyEvent.VK_LEFT:
                        gamePanel.movePiece(-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        gamePanel.movePiece(+1);
                        break;
                    case KeyEvent.VK_SPACE:
                        gamePanel.dropPieceDown(); // This is the hard drop in original, one step down.
                                                 // If hard drop to bottom is desired, GameController needs a method.
                                                 // The timer also calls this, so space is like a manual tick.
                        break;
                }
            }
        });

        timer = new Timer(initialDelay, e -> {
            if (!gamePanel.isGameOver()) {
                gamePanel.dropPieceDown(); // This is the periodic downward movement
                updateGameSpeed();
            } else {
                timer.stop(); // Stop the timer when game is over
                gamePanel.repaint(); // Ensure game over screen is shown
            }
        });
        timer.start();
    }

    private int getDelayForDifficulty(String levelName) {
        switch (levelName) {
            case "Facile": return 1000;
            case "Medio": return 500;
            case "Difficile": return 300;
            case "Esperto": return 200;
            case "Incubo": return 75;
            default:
                System.err.println("Livello non riconosciuto: " + levelName + ". Defaulting to Medio.");
                return 500;
        }
    }

    private void updateGameSpeed() {
        // Speed up based on score. Ensure delay doesn't become too small.
        int scoreBasedReduction = (gameController.getScoreManager().getScore() / GameConstants.SCORE_PER_LEVEL_UP) * 100;
        timer.setDelay(Math.max(50, initialDelay - scoreBasedReduction)); // Minimum delay of 50ms
    }

    // To run the game (assuming you have a main method somewhere that calls this)
    // public static void main(String[] args) {
    //     ModularTetris tetris = new ModularTetris();
    //     SwingUtilities.invokeLater(() -> tetris.run(args));
    // }
}