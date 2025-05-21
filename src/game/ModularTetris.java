package game;

import java.awt.event.*;
import javax.swing.*;

import struttura.BlocchiCadenti;

public class ModularTetris {
    Game game = new Game();
    public JFrame frame = new JFrame("Tetris");
    private Timer tmr;
    private int delay;
    
    public void run(String[] args) {
        
        String livelloSelezionato = args[0];
        delay = handleLevelDifficulty(livelloSelezionato);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(12 * 26, 26 * 24 + 14);
        frame.setLocationRelativeTo(null);

        frame.add(game);
        frame.setVisible(true);

        
        tmr = new Timer(delay, e -> {
            if (!game.isGameOver()) {
                game.dropDown();
                updateSpeed();
            }
        });
        tmr.start();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (game.isGameOver()) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        Highscores scores = new Highscores();
                        scores.updateScore(args[1], game.score);
                        frame.dispose();
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

        return;
    }

    int handleLevelDifficulty(String livelloSelezionato) {
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

    void updateSpeed() {
        tmr.setDelay(Math.max(10, delay - (game.score / 500) * 10));
    }
}