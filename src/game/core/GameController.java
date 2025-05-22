package game.core;

import java.util.List;
import game.model.Tetramino; // For checking pieceId validity

public class GameController {
    private Well well;
    private ActivePiece activePiece;
    private PieceQueue pieceQueue;
    private ScoreManager scoreManager;
    private boolean gameOver;

    public GameController() {
        initializeGame();
    }

    private void initializeGame() {
        this.well = new Well(); // Well initializes itself
        this.pieceQueue = new PieceQueue();
        this.scoreManager = new ScoreManager(); // ScoreManager resets itself and Tetramino colors
        this.gameOver = false;
        spawnNewPiece();
    }
    
    public void resetGame() {
        initializeGame();
    }

    private void spawnNewPiece() {
        int nextPieceId = pieceQueue.spawnNextPieceId();
        this.activePiece = new ActivePiece(nextPieceId);

        // Check for game over condition immediately upon spawning
        if (well.isCollision(activePiece.getOrigin().x, activePiece.getOrigin().y, activePiece.getRotation(), activePiece.getPieceId())) {
            this.gameOver = true;
            this.activePiece = null; // No active piece if game over on spawn
        }
    }

    public void handleRotateInput(int direction) {
        if (gameOver || activePiece == null) return;
        activePiece.rotate(direction, well);
    }

    public void handleMoveInput(int dx) {
        if (gameOver || activePiece == null) return;
        activePiece.move(dx, well);
    }

    public void handleDropDownInput() {
        if (gameOver || activePiece == null) return;

        if (activePiece.canMoveDown(well)) {
            activePiece.moveDown();
        } else {
            fixPieceToWell();
        }
    }

    private void fixPieceToWell() {
        if (activePiece == null) return; // Should not happen if called correctly

        well.fixPiece(activePiece.getOrigin().x, activePiece.getOrigin().y, activePiece.getRotation(), activePiece.getPieceId());
        int clearedRows = well.clearFullRows();
        scoreManager.processClearedRows(clearedRows);

        // Check if fixing the piece caused a game over state (e.g. top out)
        // This check is implicitly handled by spawnNewPiece's collision check.
        // If after clearing rows, the new piece cannot spawn, gameOver will be set.
        
        if (!gameOver) { // Only spawn new piece if game is not already over
            spawnNewPiece();
        } else {
            this.activePiece = null; // Ensure no piece is drawn if game ended during fixing.
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    // Getter methods for the renderer and game panel
    public Well getWell() {
        return well;
    }

    public ActivePiece getActivePiece() {
        return activePiece;
    }

    public List<Integer> getPreviewPieceIds() {
        return pieceQueue.getPreviewPieceIds();
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }
}