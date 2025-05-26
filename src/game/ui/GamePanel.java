package game.ui;

import game.core.GameController;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import game.config.GameConstants; // For panel preferred size

public class GamePanel extends JPanel {

    private final GameController gameController;
    private final GameRenderer gameRenderer;

    public GamePanel(GameController gameController) {
        this.gameController = gameController;
        this.gameRenderer = new GameRenderer();
        
        // Set preferred size for the panel based on well and some sidebar space
        int preferredWidth = (GameConstants.WELL_WIDTH_BLOCKS + 6) * GameConstants.BLOCK_SIZE; // Approx 6 blocks for sidebar
        int preferredHeight = GameConstants.WELL_HEIGHT_BLOCKS * GameConstants.BLOCK_SIZE;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        // setFocusable(true) is handled by Tetris before adding KeyListener
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.render(g, gameController);
    }

    // Methods to delegate game actions from KeyListener/Timer to GameController
    public void rotatePiece(int direction) {
        gameController.handleRotateInput(direction);
        repaint();
    }

    public void movePiece(int dx) {
        gameController.handleMoveInput(dx);
        repaint();
    }

    public void dropPieceDown() {
        gameController.handleDropDownInput();
        repaint();
    }

    public boolean isGameOver() {
        return gameController.isGameOver();
    }

    public int getCurrentScore() {
        return gameController.getScoreManager().getScore();
    }
    
    public void resetGame() {
        gameController.resetGame();
        repaint();
    }
}