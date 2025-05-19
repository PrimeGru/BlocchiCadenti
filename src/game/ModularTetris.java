package game;

import java.awt.event.*;
import javax.swing.*;

public class ModularTetris {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        String livelloSelezionato = args[0];
        int delay = 1;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(12 * 26 + 10, 26 * 23 + 25);

        Game game = new Game();
        frame.add(game);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (game.isGameOver()) {
                    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                        Highscores scores = new Highscores();
                        scores.updateScore(args[1], game.score);
                    }
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: 
                        game.rotate(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.rotate(+1);
                        break;
                    case KeyEvent.VK_LEFT:
                        game.move(-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.move(+1);
                        break;
                    case KeyEvent.VK_SPACE:
                        game.dropDown();
                        break;
                }
            }
        });

        delay = handleLevelDifficulty(livelloSelezionato);

        new Timer(delay, e -> {
            if (!game.isGameOver()) {
                game.dropDown();
            }
        }).start();
    }

    static int handleLevelDifficulty(String livelloSelezionato) {
        if(livelloSelezionato == "Facile") {
            return 1000;
        }
        if(livelloSelezionato == "Medio") {
            return 500;
        }
        if(livelloSelezionato == "Difficile") {
            return 300;
        }
        if(livelloSelezionato == "Esperto") {
            return 200;
        }
        if(livelloSelezionato == "Incubo") {
            return 75;
        }
        else return 1;
    }
}