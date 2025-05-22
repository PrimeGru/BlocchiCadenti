package game.core;

import game.config.GameConstants;
import game.model.Tetramino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceQueue {
    private final ArrayList<Integer> nextPieceIds;
    private final int bagSize; // Number of unique tetraminos

    public PieceQueue() {
        this.nextPieceIds = new ArrayList<>();
        this.bagSize = Tetramino.getNumberOfShapes();
        ensureSufficientPieces();
    }

    private void ensureSufficientPieces() {
        // Ensure there are always enough pieces for display + the current one
        while (nextPieceIds.size() <= GameConstants.NEXT_PIECES_QUEUE_DISPLAY_SIZE + 1) {
            addNewBag();
        }
    }

    private void addNewBag() {
        ArrayList<Integer> sevenPieces = new ArrayList<>();
        for (int i = 0; i < bagSize; i++) {
            sevenPieces.add(i);
        }
        Collections.shuffle(sevenPieces);
        nextPieceIds.addAll(sevenPieces);
    }

    public int spawnNextPieceId() {
        if (nextPieceIds.isEmpty()) {
            ensureSufficientPieces(); // Should not happen if ensureSufficientPieces is called correctly
        }
        int spawnedId = nextPieceIds.remove(0);
        ensureSufficientPieces(); // Make sure queue is refilled for next spawn/preview
        return spawnedId;
    }

    public List<Integer> getPreviewPieceIds() {
        int count = Math.min(GameConstants.NEXT_PIECES_QUEUE_DISPLAY_SIZE, nextPieceIds.size());
        return new ArrayList<>(nextPieceIds.subList(0, count));
    }
    
    public void reset() {
        nextPieceIds.clear();
        ensureSufficientPieces();
    }
}