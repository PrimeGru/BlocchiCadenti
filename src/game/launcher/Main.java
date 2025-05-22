package game.launcher;

import game.main.Tetris;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Tetris tetrisGame = new Tetris();
        // Example command line arguments: "Medio" "MyNickName"
        // String[] gameArgs = {"Medio", "Player1"}; 
        // String[] gameArgs = {"Incubo"};
        String[] gameArgs = args; // Use actual command line args passed to main

        SwingUtilities.invokeLater(() -> tetrisGame.run(gameArgs));
    }
}