package game.config;

import java.awt.Color;

public final class GameConstants {
    private GameConstants() {} // Private constructor to prevent instantiation

    // Well dimensions
    public static final int WELL_WIDTH_BLOCKS = 12;
    public static final int WELL_HEIGHT_BLOCKS = 23; // Includes bottom border row

    // Visuals
    public static final int BLOCK_SIZE = 26;
    public static final int PREVIEW_BLOCK_SIZE = 20;

    // Gameplay
    public static final int NEXT_PIECES_QUEUE_DISPLAY_SIZE = 4;
    public static final int SCORE_PER_LEVEL_UP = 2000;

    // Colors
    public static final Color WELL_BACKGROUND_COLOR = Color.BLACK;
    public static final Color WELL_BORDER_COLOR = Color.GRAY;
    public static final Color DEFAULT_TEXT_COLOR = Color.WHITE;
    public static final Color GAME_OVER_TEXT_COLOR = Color.RED;
    public static final Color GAME_OVER_OVERLAY_COLOR = new Color(0, 0, 0, 180);
    public static final Color PANEL_BACKGROUND_COLOR = Color.DARK_GRAY.darker();

    // Piece Spawn
    public static final int INITIAL_PIECE_SPAWN_X = WELL_WIDTH_BLOCKS / 2 - 2;
    public static final int INITIAL_PIECE_SPAWN_Y = 0;

    // UI Layout ( Sidebar )
    public static final int SIDEBAR_X_OFFSET_FACTOR = BLOCK_SIZE / 2;
    public static final int SCORE_TEXT_Y_OFFSET = BLOCK_SIZE + BLOCK_SIZE / 2;
    public static final int LEVEL_TEXT_Y_OFFSET_FACTOR = 2;
    public static final int NEXT_TEXT_Y_OFFSET_FACTOR = 5;
    public static final int NEXT_PIECE_INITIAL_Y_OFFSET_FACTOR = 7;
    public static final int NEXT_PIECE_VERTICAL_SPACING_FACTOR = 4;
    public static final int PREVIEW_AREA_WIDTH_BLOCKS = 4; // Assumed width for centering preview pieces

    // Game Over Message Layout
    public static final int GAME_OVER_MSG_Y_OFFSET = -50;
    public static final int CONTINUE_MSG_1_Y_OFFSET = -10;
    public static final int CONTINUE_MSG_2_Y_OFFSET = 15;

}