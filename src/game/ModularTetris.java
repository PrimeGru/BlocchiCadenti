package Java_Programs.src.Tetris;

import java.awt.event.*;
import javax.swing.*;

public class ModularTetris {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(12 * 26 + 10, 26 * 23 + 25);

        Game game = new Game();
        frame.add(game);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (game.isGameOver()) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> game.rotate(-1);
                    case KeyEvent.VK_DOWN -> game.rotate(+1);
                    case KeyEvent.VK_LEFT -> game.move(-1);
                    case KeyEvent.VK_RIGHT -> game.move(+1);
                    case KeyEvent.VK_SPACE -> game.dropDown();
                }
            }
        });

        new Timer(1000, e -> {
            if (!game.isGameOver()) {
                game.dropDown();
            }
        }).start();
    }
}