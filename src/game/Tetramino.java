package game;

 import java.awt.Color;
 import java.awt.Point;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.List;

 public class Tetramino {
     // Using the SHAPES definition from the previous iteration which had some adjustments
     public static final Point[][][] SHAPES = {
         // I-Piece
         {
             { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) }, // horizontal
             { new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) }, // vertical (centered better)
             { new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) }, // horizontal (shifted)
             { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }  // vertical (standard)
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
             { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) }, // Adjusted for distinct rotation
             { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) }, // Repeat of 0
             { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) }  // Repeat of 1
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
             { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }, // Repeat of 0
             { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }  // Repeat of 1
         }
     };

     // COLORS array is final, but its contents (the Color objects) can be reordered.
     public static final Color[] COLORS = {
         Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, new Color(128, 0, 128), Color.red // Original Purple
     };
     // Store the original order to allow resetting
     private static final Color[] ORIGINAL_COLORS = Arrays.copyOf(COLORS, COLORS.length);

     /**
      * Shuffles the order of colors in the COLORS array.
      */
     public static void shuffleColors() {
         List<Color> colorList = Arrays.asList(COLORS); // This list is backed by the COLORS array
         Collections.shuffle(colorList);
         // The COLORS array is now shuffled in place.
     }

     /**
      * Resets the COLORS array to its original, default order.
      */
     public static void resetColors() {
         for (int i = 0; i < COLORS.length; i++) {
             COLORS[i] = ORIGINAL_COLORS[i];
         }
     }
 }