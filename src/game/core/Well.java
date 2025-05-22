package game.core;

import game.config.GameConstants;
import game.model.Tetramino; // For piece color

import java.awt.Color;
import java.awt.Point;

public class Well {
    private final Color[][] grid;
    private final int width;
    private final int height; // This is WELL_HEIGHT_BLOCKS, including the bottom border

    public Well() {
        this.width = GameConstants.WELL_WIDTH_BLOCKS;
        this.height = GameConstants.WELL_HEIGHT_BLOCKS;
        this.grid = new Color[width][height];
        initialize();
    }

    public void initialize() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0 || i == width - 1 || j == height - 1) { // Left, Right, Bottom borders
                    grid[i][j] = GameConstants.WELL_BORDER_COLOR;
                } else {
                    grid[i][j] = GameConstants.WELL_BACKGROUND_COLOR;
                }
            }
        }
        // Top row above visible play area is usually kept clear / black for spawning
        // The loop for j up to height-1 makes the last row (j = WELL_HEIGHT_BLOCKS -1) a border.
        // The playable area is effectively WELL_HEIGHT_BLOCKS - 1 blocks high.
    }

    public Color getColorAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return GameConstants.WELL_BORDER_COLOR; // Treat out of bounds as border for safety
    }

    public boolean isCollision(int pieceX, int pieceY, int rotation, int pieceId) {
        Point[] shape = Tetramino.getShape(pieceId, rotation);
        for (Point p : shape) {
            int checkX = pieceX + p.x;
            int checkY = pieceY + p.y;

            // Check boundaries (including left/right walls and floor)
            if (checkX < 0 || checkX >= width || checkY < 0 || checkY >= height) {
                return true; // Collision with outer boundary (should be caught by border color check too)
            }
            // Check against existing blocks or well borders
            if (grid[checkX][checkY] != GameConstants.WELL_BACKGROUND_COLOR) {
                return true;
            }
        }
        return false;
    }
    
    public void fixPiece(int pieceX, int pieceY, int rotation, int pieceId) {
        Color pieceColor = Tetramino.getColor(pieceId);
        Point[] shape = Tetramino.getShape(pieceId, rotation);
        for (Point p : shape) {
            int finalX = pieceX + p.x;
            int finalY = pieceY + p.y;
            // Ensure piece is fixed within the playable area (not over borders, though collision should prevent this)
            if (finalX > 0 && finalX < width - 1 && finalY < height - 1 && finalY >=0) {
                 grid[finalX][finalY] = pieceColor;
            } else if (finalX >=0 && finalX < width && finalY >=0 && finalY < height){
                 // This case handles fixing a piece that might be partially in the border zone due to spawn
                 // e.g. I-piece spawning horizontally might have a part at x=0 if not careful with spawn point.
                 // For safety, only fix if it's a valid part of the grid.
                 // The primary check should be that pieces only fix within the play area bounded by GRAY borders.
                 // If a piece point is at the border itself (e.g. x=0, x=width-1, y=height-1), it means Game Over usually.
                 // For robust fixing, only allow colors other than BORDER_COLOR or BACKGROUND_COLOR.
                 if(grid[finalX][finalY] == GameConstants.WELL_BACKGROUND_COLOR) { // Only fix on empty space.
                    grid[finalX][finalY] = pieceColor;
                 }
            }
        }
    }

    public int clearFullRows() {
        int numClears = 0;
        // Start checking from one row above the bottom border up to the top
        for (int j = height - 2; j >= 0; j--) {
            boolean fullRow = true;
            // Check cells within the playable area (i.e., not the side borders)
            for (int i = 1; i < width - 1; i++) {
                if (grid[i][j] == GameConstants.WELL_BACKGROUND_COLOR) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                deleteRow(j);
                numClears++;
                j++; // Re-check the current row index as rows above have shifted down
            }
        }
        return numClears;
    }

    private void deleteRow(int row) {
        // Shift all rows above this one, down one position
        // Only shift within the playable columns (1 to width-2)
        for (int j = row; j > 0; j--) {
            for (int i = 1; i < width - 1; i++) {
                grid[i][j] = grid[i][j - 1];
            }
        }
        // Clear the top playable row
        for (int i = 1; i < width - 1; i++) {
            grid[i][0] = GameConstants.WELL_BACKGROUND_COLOR;
        }
    }

    public int getGridWidth() {
        return width;
    }

    public int getGridHeight() {
        return height;
    }
}