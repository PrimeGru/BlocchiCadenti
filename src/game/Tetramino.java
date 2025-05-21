package game;

import java.awt.Color;
import java.awt.Point;

public class Tetramino {
    public static final Point[][][] SHAPES = {
        // I-Piece
        {
            
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) }
        },
        // J-Piece
        {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
        },
        // L-Piece
        {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
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
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
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
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
        }
    };

    public static final Color[] COLORS = {
        Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
    };
}