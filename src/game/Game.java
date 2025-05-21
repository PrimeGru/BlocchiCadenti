package game;

 import java.awt.*;
 import java.util.*;
 import javax.swing.*;

 public class Game extends JPanel {
     private static final long serialVersionUID = 1L;

     private Point pieceOrigin;
     private int currentPiece = -1; // Initialize to -1 or handle in newPiece carefully
     private int rotation;
     private ArrayList<Integer> nextPieces = new ArrayList<>();
     public int score;
     private Color[][] well;
     private boolean gameOver = false;

     private static final int WELL_WIDTH_BLOCKS = 12;
     private static final int WELL_HEIGHT_BLOCKS = 23;
     private static final int BLOCK_SIZE = 26;
     private static final int NEXT_PIECES_QUEUE_SIZE = 4;
     private static final int PREVIEW_BLOCK_SIZE = 20;

     private int currentScoreLevel = 0; // For tracking color shuffle levels
     private static final int SCORE_PER_LEVEL_UP = 2000;


     public Game() {
         init();
         // setFocusable(true) is now handled in ModularTetris.java before adding KeyListener
     }

     private void init() {
         well = new Color[WELL_WIDTH_BLOCKS][WELL_HEIGHT_BLOCKS + 1];
         for (int i = 0; i < WELL_WIDTH_BLOCKS; i++) {
             for (int j = 0; j < WELL_HEIGHT_BLOCKS; j++) {
                 if (i == 0 || i == WELL_WIDTH_BLOCKS - 1 || j == WELL_HEIGHT_BLOCKS - 1) {
                     well[i][j] = Color.GRAY;
                 } else {
                     well[i][j] = Color.BLACK;
                 }
             }
         }
         score = 0;
         currentScoreLevel = 0; // Reset score level
         Tetramino.resetColors(); // Reset Tetramino colors to original order

         gameOver = false;
         nextPieces.clear();
         ensureNextPiecesAvailable();
         newPiece(); // Spawns the first piece
     }

     private void ensureNextPiecesAvailable() {
         while (nextPieces.size() <= NEXT_PIECES_QUEUE_SIZE + 1) {
             ArrayList<Integer> sevenPieces = new ArrayList<>();
             for (int i = 0; i < Tetramino.SHAPES.length; i++) {
                 sevenPieces.add(i);
             }
             Collections.shuffle(sevenPieces);
             nextPieces.addAll(sevenPieces);
         }
     }

     public void newPiece() {
         pieceOrigin = new Point(WELL_WIDTH_BLOCKS / 2 - 2, 0); // Adjusted start for some wider pieces
         rotation = 0;

         ensureNextPiecesAvailable();

         if (nextPieces.isEmpty()) {
             gameOver = true;
             return;
         }
         currentPiece = nextPieces.remove(0);

         if (collidesAt(pieceOrigin.x, pieceOrigin.y, rotation)) {
             gameOver = true;
             // If game over on new piece spawn, clear current piece to avoid drawing invalid one
             currentPiece = -1;
         }
         repaint();
     }

     private boolean collidesAt(int x, int y, int rotation) {
         if (currentPiece < 0) return true; // No piece selected, treat as collision
         for (Point p : Tetramino.SHAPES[currentPiece][rotation]) {
             int checkX = x + p.x;
             int checkY = y + p.y;

             if (checkX < 0 || checkX >= WELL_WIDTH_BLOCKS || checkY < 0 || checkY >= WELL_HEIGHT_BLOCKS) {
                 return true;
             }
             if (well[checkX][checkY] != Color.BLACK) {
                 return true;
             }
         }
         return false;
     }

     public void rotate(int i) {
         if (gameOver || currentPiece < 0) return;
         int newRotation = (rotation + i + 4) % 4;
         if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
             rotation = newRotation;
         }
         repaint();
     }

     public void move(int i) {
         if (gameOver || currentPiece < 0) return;
         if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
             pieceOrigin.x += i;
         }
         repaint();
     }

     public void dropDown() {
         if (gameOver || currentPiece < 0) return;
         if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
             pieceOrigin.y++;
         } else {
             fixToWell();
         }
         repaint();
     }

     private void fixToWell() {
         if (currentPiece < 0) return; // Should not happen if dropDown was called with a valid piece
         for (Point p : Tetramino.SHAPES[currentPiece][rotation]) {
             int finalX = pieceOrigin.x + p.x;
             int finalY = pieceOrigin.y + p.y;
             if (finalX >= 0 && finalX < WELL_WIDTH_BLOCKS && finalY >= 0 && finalY < WELL_HEIGHT_BLOCKS) {
                 well[finalX][finalY] = Tetramino.COLORS[currentPiece];
             }
         }
         clearRows(); // This also calls checkLevelUp if rows are cleared
         if (!gameOver) {
             newPiece();
         } else {
            currentPiece = -1; // Ensure no piece is drawn after game over from fixing
         }
     }

     private void deleteRow(int row) {
         for (int j = row; j > 0; j--) {
             for (int i = 1; i < WELL_WIDTH_BLOCKS - 1; i++) {
                 well[i][j] = well[i][j - 1];
             }
         }
         for (int i = 1; i < WELL_WIDTH_BLOCKS - 1; i++) {
             well[i][0] = Color.BLACK;
         }
     }

     private void clearRows() {
         int numClears = 0;
         for (int j = WELL_HEIGHT_BLOCKS - 2; j >= 0; j--) {
             boolean fullRow = true;
             for (int i = 1; i < WELL_WIDTH_BLOCKS - 1; i++) {
                 if (well[i][j] == Color.BLACK) {
                     fullRow = false;
                     break;
                 }
             }
             if (fullRow) {
                 deleteRow(j);
                 numClears++;
                 j++;
             }
         }

         if (numClears > 0) {
             switch (numClears) {
                 case 1: score += 100; break;
                 case 2: score += 300; break;
                 case 3: score += 500; break;
                 case 4: score += 800; break;
             }
             checkLevelUp(); // Check for color shuffle level up
         }
     }

     private void checkLevelUp() {
         int newLevel = score / SCORE_PER_LEVEL_UP;
         if (newLevel > currentScoreLevel) {
             currentScoreLevel = newLevel;
             Tetramino.shuffleColors();
         }
     }


     private void drawBlock(Graphics g, int x, int y, int size, Color color, boolean isPreview) {
         g.setColor(color);
         int fillSize = isPreview ? size - 2 : size - 1; // Slightly smaller fill for preview for distinctness
         g.fillRect(x + (isPreview ? 1 : 0), y + (isPreview ? 1 : 0), fillSize, fillSize);

         g.setColor(color.darker());
         g.drawRect(x, y, size - 1, size - 1);
     }


     private void drawPiece(Graphics g) {
         if (gameOver || currentPiece < 0) return; // Do not draw if no current piece or game over
         Color pieceColor = Tetramino.COLORS[currentPiece];
         for (Point p : Tetramino.SHAPES[currentPiece][rotation]) {
             drawBlock(g, (pieceOrigin.x + p.x) * BLOCK_SIZE,
                        (pieceOrigin.y + p.y) * BLOCK_SIZE,
                        BLOCK_SIZE, pieceColor, false);
         }
     }

     @Override
     public void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.setColor(Color.DARK_GRAY.darker());
         g.fillRect(0, 0, getWidth(), getHeight());

         for (int i = 0; i < WELL_WIDTH_BLOCKS; i++) {
             for (int j = 0; j < WELL_HEIGHT_BLOCKS; j++) {
                 Color cellColor = well[i][j];
                 if (cellColor == Color.GRAY) {
                     g.setColor(Color.GRAY.darker());
                     g.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                 } else if (cellColor != Color.BLACK) {
                     drawBlock(g, i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, cellColor, false);
                 } else {
                     g.setColor(Color.BLACK);
                     g.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                 }
             }
         }

         drawPiece(g); // Will not draw if game over and currentPiece is -1

         int sidebarX = WELL_WIDTH_BLOCKS * BLOCK_SIZE + (BLOCK_SIZE / 2);
         g.setColor(Color.WHITE);
         g.setFont(new Font("SansSerif", Font.BOLD, 18));
         g.drawString("Score: " + score, sidebarX, BLOCK_SIZE + (BLOCK_SIZE/2));
         g.drawString("Level: " + currentScoreLevel, sidebarX, BLOCK_SIZE * 2 + (BLOCK_SIZE/2));
         g.setFont(new Font("SansSerif", Font.BOLD, 16));
         g.drawString("Next:", sidebarX, BLOCK_SIZE * 3 + (BLOCK_SIZE / 2));

         int nextPieceY = BLOCK_SIZE * 4;
         for (int k = 0; k < Math.min(NEXT_PIECES_QUEUE_SIZE, nextPieces.size()); k++) {
             int pieceId = nextPieces.get(k);
             Color nextPieceColor = Tetramino.COLORS[pieceId]; // Uses potentially shuffled colors
             Point[] shape = Tetramino.SHAPES[pieceId][0];

             float minX = 4, maxX = 0, minY = 4, maxY = 0;
             for(Point pTemp : shape) { // Renamed p to pTemp to avoid conflict if in same scope
                 minX = Math.min(minX, pTemp.x); maxX = Math.max(maxX, pTemp.x);
                 minY = Math.min(minY, pTemp.y); maxY = Math.max(maxY, pTemp.y);
             }
             float pieceActualWidth = (maxX - minX + 1) * PREVIEW_BLOCK_SIZE;
             float offsetX = ( (4 * PREVIEW_BLOCK_SIZE) - pieceActualWidth) / 2f;

             for (Point pLocal : shape) { // Renamed p to pLocal
                 drawBlock(g, (int)(sidebarX + offsetX + (pLocal.x - minX) * PREVIEW_BLOCK_SIZE),
                            (int)(nextPieceY + (pLocal.y - minY) * PREVIEW_BLOCK_SIZE), // Adjusted to use minY for relative preview
                            PREVIEW_BLOCK_SIZE, nextPieceColor, true);
             }
             nextPieceY += 4 * PREVIEW_BLOCK_SIZE + (BLOCK_SIZE/2);
         }

         if (gameOver) {
             g.setColor(new Color(0,0,0,180));
             g.fillRect(0,0, WELL_WIDTH_BLOCKS * BLOCK_SIZE, WELL_HEIGHT_BLOCKS * BLOCK_SIZE);
             g.setFont(new Font("SansSerif", Font.BOLD, 30));
             g.setColor(Color.RED);
             String gameOverMsg = "GAME OVER";
             FontMetrics fm = g.getFontMetrics();
             int msgWidth = fm.stringWidth(gameOverMsg);
             g.drawString(gameOverMsg, (WELL_WIDTH_BLOCKS * BLOCK_SIZE - msgWidth) / 2, WELL_HEIGHT_BLOCKS * BLOCK_SIZE / 2 - 50);
             g.setFont(new Font("SansSerif", Font.PLAIN, 20));
             g.setColor(Color.WHITE);
             String continueMsg = "Premi Invio";
             fm = g.getFontMetrics(); // Re-get FontMetrics for the new font
             msgWidth = fm.stringWidth(continueMsg);
             g.drawString(continueMsg, (WELL_WIDTH_BLOCKS * BLOCK_SIZE - msgWidth) / 2, WELL_HEIGHT_BLOCKS * BLOCK_SIZE / 2 -10);
             String continueMsg2 = "per continuare";
             msgWidth = fm.stringWidth(continueMsg2);
             g.drawString(continueMsg2, (WELL_WIDTH_BLOCKS * BLOCK_SIZE - msgWidth) / 2, WELL_HEIGHT_BLOCKS * BLOCK_SIZE / 2 + 15);
         }
     }

     public boolean isGameOver() {
         return gameOver;
     }

     public void resetGame() { // If you want an explicit reset option
         init();
     }
 }