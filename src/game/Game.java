package Java_Programs.src.Tetris;


import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Game extends JPanel {
    private static final long serialVersionUID = 1L;

    private Point pieceOrigin;
    private int currentPiece;
    private int rotation;
    private ArrayList<Integer> nextPieces = new ArrayList<>();
    private long score;
    private Color[][] well;
    private boolean gameOver = false;

    public Game() {
        init();
    }

    private void init() {
        well = new Color[12][24];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                well[i][j] = (i == 0 || i == 11 || j == 22) ? Color.GRAY : Color.BLACK;
            }
        }
        newPiece();
    }

    public void newPiece() {
        pieceOrigin = new Point(5, 2);
        rotation = 0;
        if (nextPieces.isEmpty()) {
            Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(nextPieces);
        }
        currentPiece = nextPieces.remove(0);

        if (collidesAt(pieceOrigin.x, pieceOrigin.y, rotation)) {
            gameOver = true;
        }
    }

    private boolean collidesAt(int x, int y, int rotation) {
        for (Point p : Tetramino.SHAPES[currentPiece][rotation]) {
            if (well[p.x + x][p.y + y] != Color.BLACK) {
                return true;
            }
        }
        return false;
    }

    public void rotate(int i) {
        if (gameOver) return;
        int newRotation = (rotation + i) % 4;
        if (newRotation < 0) newRotation = 3;
        if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
            rotation = newRotation;
        }
        repaint();
    }

    public void move(int i) {
        if (gameOver) return;
        if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
            pieceOrigin.x += i;
        }
        repaint();
    }

    public void dropDown() {
        if (gameOver) return;
        if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
            pieceOrigin.y++;
        } else {
            fixToWell();
        }
        repaint();
    }

    private void fixToWell() {
        for (Point p : Tetramino.SHAPES[currentPiece][rotation]) {
            well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = Tetramino.COLORS[currentPiece];
        }
        clearRows();
        newPiece();
    }

    private void deleteRow(int row) {
        for (int j = row - 1; j > 0; j--) {
            for (int i = 1; i < 11; i++) {
                well[i][j + 1] = well[i][j];
            }
        }
    }

    private void clearRows() {
        int numClears = 0;
        for (int j = 21; j > 0; j--) {
            boolean gap = false;
            for (int i = 1; i < 11; i++) {
                if (well[i][j] == Color.BLACK) {
                    gap = true;
                    break;
                }
            }
            if (!gap) {
                deleteRow(j);
                j++;
                numClears++;
            }
        }
        score += switch (numClears) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
    }

    private void drawPiece(Graphics g) {
        if (gameOver) return;
        g.setColor(Tetramino.COLORS[currentPiece]);
        for (Point p : Tetramino.SHAPES[currentPiece][rotation]) {
            g.fillRect((p.x + pieceOrigin.x) * 26, (p.y + pieceOrigin.y) * 26, 25, 25);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, 26 * 12, 26 * 23);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                g.setColor(well[i][j]);
                g.fillRect(26 * i, 26 * j, 25, 25);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 19 * 12, 25);
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 100, 200);
        }
        drawPiece(g);
    }

    public boolean isGameOver() {
        return gameOver;
    }
}