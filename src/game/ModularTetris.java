package game;

 import java.awt.event.*;
 import javax.swing.*;

 public class ModularTetris {
     Game game = new Game();
     public JFrame frame = new JFrame("Tetris");
     private Timer tmr;
     private int delay;
     private String[] commandLineArgs; // Store command line arguments

     public void run(String[] args) {
         this.commandLineArgs = args; // Store for later use by KeyListener

         String livelloSelezionato = args[0];
         delay = handleLevelDifficulty(livelloSelezionato);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(17 * 26, 26 * 24 + 14);
         frame.setLocationRelativeTo(null);

         // Add the game panel to the frame
         frame.add(game);
         // Set the frame visible BEFORE requesting focus on a component within it
         frame.setVisible(true);

         // Make the Game panel focusable and request focus for it
         game.setFocusable(true);
         game.requestFocusInWindow();

         // Add KeyListener directly to the game panel
         game.addKeyListener(new KeyAdapter() {
             @Override
             public void keyPressed(KeyEvent e) {
                 if (game.isGameOver()) {
                     if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                         Highscores scores = new Highscores();
                         String nickname = (commandLineArgs.length > 1) ? commandLineArgs[1] : "Player";
                         scores.updateScore(nickname, game.score);
                         frame.dispose(); // 'frame' is accessible from inner class
                     }
                     return; // No other game controls if game over
                 }

                 // Game controls only if not game over
                 switch (e.getKeyCode()) {
                     case KeyEvent.VK_UP:
                         game.rotate(-1);
                         break;
                     case KeyEvent.VK_DOWN:
                         game.rotate(+1); // Or game.dropDown(); for soft drop, current is rotate
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

         tmr = new Timer(delay, e -> {
             if (!game.isGameOver()) {
                 game.dropDown();
                 updateSpeed(); // Speed update based on score
             }
         });
         tmr.start();
     }

     int handleLevelDifficulty(String livelloSelezionato) {
         if ("Facile".equals(livelloSelezionato)) {
             return 1000;
         }
         if ("Medio".equals(livelloSelezionato)) {
             return 500;
         }
         if ("Difficile".equals(livelloSelezionato)) {
             return 300;
         }
         if ("Esperto".equals(livelloSelezionato)) {
             return 200;
         }
         if ("Incubo".equals(livelloSelezionato)) {
             return 75;
         }
         System.err.println("Livello non riconosciuto: " + livelloSelezionato + ". Defaulting to Medio.");
         return 500;
     }

     void updateSpeed() {
         tmr.setDelay(Math.max(10, delay - (game.score / 500) * 10));
     }
 }