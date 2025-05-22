package game.model;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tetramino {
    // SHAPES definition remains the same as provided in the original Game.java
    public static final Point[][][] SHAPES = {
        // I-Piece
        {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            { new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) },
            { new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
        },
        // J-Piece
        {
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
            { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(1, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(0, 2), new Point(1, 2) }
        },
        // L-Piece
        {
            { new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2) }
        },
        // O-Piece
        {
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
        },
        // S-Piece
        {
            { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
            { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) }
        },
        // T-Piece
        {
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
        },
        // Z-Piece
        {
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
            { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
            { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
        }
    };

    public static final Color[] COLORS = {
        Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, new Color(128, 0, 128), Color.red
    };
    private static final Color[] ORIGINAL_COLORS = Arrays.copyOf(COLORS, COLORS.length);

    public static int getNumberOfShapes() {
        return SHAPES.length;
    }

    public static Point[] getShape(int pieceId, int rotation) {
        if (pieceId < 0 || pieceId >= SHAPES.length) return new Point[0]; // Should not happen with valid ID
        return SHAPES[pieceId][rotation];
    }

    public static Color getColor(int pieceId) {
         if (pieceId < 0 || pieceId >= COLORS.length) return Color.PINK; // Error color
        return COLORS[pieceId];
    }

    public static void shuffleColors() {
        List<Color> colorList = Arrays.asList(COLORS);
        Collections.shuffle(colorList);
        // COLORS array is now shuffled.
    }

    public static void resetColors() {
        System.arraycopy(ORIGINAL_COLORS, 0, COLORS, 0, ORIGINAL_COLORS.length);
    }
}