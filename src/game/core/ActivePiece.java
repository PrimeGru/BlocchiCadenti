package game.core;

import game.config.GameConstants;
import game.model.Tetramino;

import java.awt.Color;
import java.awt.Point;

public class ActivePiece {
    private Point origin;
    private int pieceId;
    private int rotation;

    public ActivePiece(int pieceId) {
        this.pieceId = pieceId;
        this.origin = new Point(GameConstants.INITIAL_PIECE_SPAWN_X, GameConstants.INITIAL_PIECE_SPAWN_Y);
        this.rotation = 0; // Default initial rotation
    }

    public Point getOrigin() {
        return new Point(origin); // Return a copy
    }

    public int getPieceId() {
        return pieceId;
    }

    public int getRotation() {
        return rotation;
    }

    public Color getColor() {
        return Tetramino.getColor(pieceId);
    }

    public Point[] getCurrentShapePoints() {
        return Tetramino.getShape(pieceId, rotation);
    }

    public void rotate(int direction, Well well) {
        int newRotation = (this.rotation + direction + 4) % 4; // +4 ensures positive before modulo
        if (!well.isCollision(origin.x, origin.y, newRotation, pieceId)) {
            this.rotation = newRotation;
        }
    }

    public void move(int dx, Well well) {
        if (!well.isCollision(origin.x + dx, origin.y, rotation, pieceId)) {
            origin.x += dx;
        }
    }

    public boolean canMoveDown(Well well) {
        return !well.isCollision(origin.x, origin.y + 1, rotation, pieceId);
    }

    public void moveDown() {
        origin.y++;
    }
}