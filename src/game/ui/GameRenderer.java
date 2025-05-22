package game.ui;

import game.config.GameConstants;
import game.core.ActivePiece;
import game.core.GameController;
import game.core.Well;
import game.model.Tetramino;

import java.awt.*;
import java.util.List;

public class GameRenderer {

    public void render(Graphics g, GameController controller) {
        // Draw panel background
        g.setColor(GameConstants.PANEL_BACKGROUND_COLOR);
        g.fillRect(0, 0, GameConstants.WELL_WIDTH_BLOCKS * GameConstants.BLOCK_SIZE + 200, // Approximate width
                     GameConstants.WELL_HEIGHT_BLOCKS * GameConstants.BLOCK_SIZE + 50); // Approximate height

        drawWell(g, controller.getWell());
        drawActivePiece(g, controller.getActivePiece());
        drawSidebar(g, controller);

        if (controller.isGameOver()) {
            drawGameOverScreen(g);
        }
    }

    private void drawWell(Graphics g, Well well) {
        for (int i = 0; i < well.getGridWidth(); i++) {
            for (int j = 0; j < well.getGridHeight(); j++) {
                Color cellColor = well.getColorAt(i, j);
                int xPos = i * GameConstants.BLOCK_SIZE;
                int yPos = j * GameConstants.BLOCK_SIZE;

                if (cellColor == GameConstants.WELL_BORDER_COLOR) {
                    g.setColor(cellColor.darker()); // Darker gray for border
                    g.fillRect(xPos, yPos, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE);
                } else if (cellColor != GameConstants.WELL_BACKGROUND_COLOR) {
                    drawBlock(g, xPos, yPos, GameConstants.BLOCK_SIZE, cellColor, false);
                } else { // Background color
                    g.setColor(GameConstants.WELL_BACKGROUND_COLOR);
                    g.fillRect(xPos, yPos, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE);
                }
            }
        }
    }

    private void drawActivePiece(Graphics g, ActivePiece piece) {
        if (piece == null) return;

        Color pieceColor = piece.getColor();
        Point pieceOrigin = piece.getOrigin();
        Point[] shape = piece.getCurrentShapePoints();

        for (Point p : shape) {
            drawBlock(g, (pieceOrigin.x + p.x) * GameConstants.BLOCK_SIZE,
                      (pieceOrigin.y + p.y) * GameConstants.BLOCK_SIZE,
                      GameConstants.BLOCK_SIZE, pieceColor, false);
        }
    }

    private void drawSidebar(Graphics g, GameController controller) {
        int sidebarX = GameConstants.WELL_WIDTH_BLOCKS * GameConstants.BLOCK_SIZE + GameConstants.SIDEBAR_X_OFFSET_FACTOR;

        g.setColor(GameConstants.DEFAULT_TEXT_COLOR);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("Score: " + controller.getScoreManager().getScore(), sidebarX, GameConstants.SCORE_TEXT_Y_OFFSET);
        g.drawString("Level: " + controller.getScoreManager().getCurrentDisplayLevel(), sidebarX, GameConstants.BLOCK_SIZE * GameConstants.LEVEL_TEXT_Y_OFFSET_FACTOR + GameConstants.BLOCK_SIZE / 2);

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Next:", sidebarX, GameConstants.BLOCK_SIZE * GameConstants.NEXT_TEXT_Y_OFFSET_FACTOR + GameConstants.BLOCK_SIZE / 2);

        List<Integer> previewIds = controller.getPreviewPieceIds();
        int nextPieceY = GameConstants.BLOCK_SIZE * GameConstants.NEXT_PIECE_INITIAL_Y_OFFSET_FACTOR;

        for (int pieceId : previewIds) {
            Color nextPieceColor = Tetramino.getColor(pieceId);
            Point[] shapePoints = Tetramino.getShape(pieceId, 0); // Default rotation for preview

            // Calculate bounds for centering preview piece
            float minX = GameConstants.PREVIEW_AREA_WIDTH_BLOCKS, maxX = 0, minY = GameConstants.PREVIEW_AREA_WIDTH_BLOCKS, maxY = 0;
            for (Point pTemp : shapePoints) {
                minX = Math.min(minX, pTemp.x);
                maxX = Math.max(maxX, pTemp.x);
                minY = Math.min(minY, pTemp.y);
                maxY = Math.max(maxY, pTemp.y);
            }
            float pieceActualWidth = (maxX - minX + 1) * GameConstants.PREVIEW_BLOCK_SIZE;
            float offsetX = ((GameConstants.PREVIEW_AREA_WIDTH_BLOCKS * GameConstants.PREVIEW_BLOCK_SIZE) - pieceActualWidth) / 2f;

            for (Point pLocal : shapePoints) {
                drawBlock(g, (int) (sidebarX + offsetX + (pLocal.x - minX) * GameConstants.PREVIEW_BLOCK_SIZE),
                          (int) (nextPieceY + (pLocal.y - minY) * GameConstants.PREVIEW_BLOCK_SIZE),
                          GameConstants.PREVIEW_BLOCK_SIZE, nextPieceColor, true);
            }
            nextPieceY += GameConstants.PREVIEW_AREA_WIDTH_BLOCKS * GameConstants.PREVIEW_BLOCK_SIZE + GameConstants.BLOCK_SIZE / 2;
        }
    }

    private void drawBlock(Graphics g, int x, int y, int size, Color color, boolean isPreview) {
        g.setColor(color);
        int fillSize = isPreview ? size - 2 : size - 1;
        g.fillRect(x + (isPreview ? 1 : 0), y + (isPreview ? 1 : 0), fillSize, fillSize);

        g.setColor(color.darker());
        g.drawRect(x, y, size - 1, size - 1);
    }

    private void drawGameOverScreen(Graphics g) {
        int wellPixelWidth = GameConstants.WELL_WIDTH_BLOCKS * GameConstants.BLOCK_SIZE;
        int wellPixelHeight = GameConstants.WELL_HEIGHT_BLOCKS * GameConstants.BLOCK_SIZE;
        
        g.setColor(GameConstants.GAME_OVER_OVERLAY_COLOR);
        g.fillRect(0, 0, wellPixelWidth, wellPixelHeight);

        String gameOverMsg = "GAME OVER";
        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        g.setColor(GameConstants.GAME_OVER_TEXT_COLOR);
        FontMetrics fm = g.getFontMetrics();
        int msgWidth = fm.stringWidth(gameOverMsg);
        g.drawString(gameOverMsg, (wellPixelWidth - msgWidth) / 2, wellPixelHeight / 2 + GameConstants.GAME_OVER_MSG_Y_OFFSET);

        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g.setColor(GameConstants.DEFAULT_TEXT_COLOR);
        
        String continueMsg1 = "Premi Invio";
        fm = g.getFontMetrics(); // Re-get for new font
        msgWidth = fm.stringWidth(continueMsg1);
        g.drawString(continueMsg1, (wellPixelWidth - msgWidth) / 2, wellPixelHeight / 2 + GameConstants.CONTINUE_MSG_1_Y_OFFSET);
        
        String continueMsg2 = "per continuare";
        msgWidth = fm.stringWidth(continueMsg2);
        g.drawString(continueMsg2, (wellPixelWidth - msgWidth) / 2, wellPixelHeight / 2 + GameConstants.CONTINUE_MSG_2_Y_OFFSET);
    }
}