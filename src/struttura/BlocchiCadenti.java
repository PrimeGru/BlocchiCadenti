package struttura;
import game.main.Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.io.*; // Import for file operations
import java.nio.charset.StandardCharsets; // For consistent character encoding
import java.security.MessageDigest; // For hashing
import java.security.NoSuchAlgorithmException; // For hashing exception

// Classe principale che contiene tutto
public class BlocchiCadenti implements ActionListener {

    private static final String LOGIN_PANEL = "Login";
    private static final String REGISTER_PANEL = "Register";
    private static final String LEVEL_SELECT_PANEL = "LevelSelect"; // Costante per il pannello di selezione livello

    private JFrame window;
    private JPanel mainPanel; // Pannello che usa CardLayout
    private CardLayout cardLayout;
    private JLabel titleLabel;
    private JLabel statusLabel; // Etichetta per messaggi di stato/output

    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private SelezioneLivello selezioneLivelloPanel; // Istanza del pannello di selezione livello

    private final AuthService authService; // Servizio per logica auth/reg
    private String loggedInUserNickname = null; // Memorizza il nickname dell'utente loggato

    protected static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 16);
    protected static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 16);
    protected static final Color TEXT_FOREGROUND_COLOR = Color.WHITE;
    protected static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public static void main(String[] args) {
        // Imposta un look and feel più moderno (opzionale)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Nimbus L&F non trovato, usando default.");
        }

        // Crea e mostra la GUI sull'Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new BlocchiCadenti().createAndShowGUI());
    }

    public BlocchiCadenti() {
        authService = new AuthService(); // Istanzia il servizio (ora classe interna)
    }

    private void createAndShowGUI() {
        window = new JFrame("BlocchiCadenti");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(600, 600)); // Dimensione minima aggiornata, ora quadrata
        window.setLocationRelativeTo(null); // Centra la finestra

        // Colore di sfondo generale
        window.getContentPane().setBackground(new Color(51, 51, 51));
        window.setLayout(new BorderLayout(10, 10)); // Layout principale con spaziature

        // --- Titolo ---
        titleLabel = new JLabel("BlocchiCadenti", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Ridotto il font per adattarsi meglio
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Padding sopra/sotto
        window.add(titleLabel, BorderLayout.NORTH);

        // --- Pannello Centrale con CardLayout ---
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false); // Rende trasparente il contenitore dei pannelli

        // Crea i pannelli passando 'this' come ActionListener
        // Ora usano le classi interne
        loginPanel = new LoginPanel(this, this);
        registrationPanel = new RegistrationPanel(this, this);
        selezioneLivelloPanel = new SelezioneLivello(this); // Crea il pannello selezione livello

        // Aggiunge i pannelli al CardLayout
        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(registrationPanel, REGISTER_PANEL);
        mainPanel.add(selezioneLivelloPanel, LEVEL_SELECT_PANEL); // Aggiungi il pannello selezione livello

        // Aggiunge un bordo vuoto intorno al pannello centrale per spaziatura
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Per centrare mainPanel se è più piccolo
        centerWrapper.setOpaque(false);
        centerWrapper.add(mainPanel); // Metti il pannello cardlayout dentro il wrapper
        window.add(centerWrapper, BorderLayout.CENTER);

        // --- Area Messaggi / Stato ---
        statusLabel = new JLabel("Inserisci le tue credenziali per accedere.", SwingConstants.CENTER);
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Padding
        window.add(statusLabel, BorderLayout.SOUTH);

        // Mostra il pannello di login all'inizio
        cardLayout.show(mainPanel, LOGIN_PANEL);

        window.pack(); // Adatta la finestra ai componenti
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "SHOW_REGISTER":
                cardLayout.show(mainPanel, REGISTER_PANEL);
                statusLabel.setText("Inserisci i dati per la registrazione.");
                registrationPanel.clearFields(); // Pulisci campi quando mostri
                window.pack(); // Riadatta la dimensione se necessario
                break;

            case "SHOW_LOGIN":
                cardLayout.show(mainPanel, LOGIN_PANEL);
                statusLabel.setText("Inserisci le tue credenziali per accedere.");
                loginPanel.clearFields(); // Pulisci campi quando mostri
                window.pack(); // Riadatta la dimensione se necessario
                break;

            case "LOGIN_ATTEMPT":
                handleLogin();
                break;

            case "REGISTER_CONFIRM":
                handleRegistration();
                break;
            case "LEVEL_SELECTED": // Gestisci la selezione del livello
                handleLevelSelection();
                break;
            default:
                System.out.println("Comando non riconosciuto: " + command);
                break;
        }
    }

    private void handleLogin() {
        String nickname = loginPanel.getNickname();
        char[] passwordChars = loginPanel.getPassword();
        String password = new String(passwordChars);

        // Usa il servizio interno
        AuthService.LoginResult result = authService.login(nickname, password);

        switch (result) {
            case SUCCESS:
                statusLabel.setText("Benvenuto, " + nickname + "! Accesso effettuato.");
                statusLabel.setForeground(new Color(0, 180, 0)); // Verde successo
                loggedInUserNickname = nickname; // Salva il nickname
                // Mostra il pannello di selezione del livello dopo il login
                cardLayout.show(mainPanel, LEVEL_SELECT_PANEL);
                window.pack();
                break;
            case USER_NOT_FOUND:
                statusLabel.setText("Nickname non trovato. Riprova o registrati.");
                statusLabel.setForeground(Color.ORANGE);
                loginPanel.clearFields();
                break;
            case INVALID_PASSWORD:
                statusLabel.setText("Password errata. Riprova.");
                statusLabel.setForeground(Color.RED);
                // Non pulire il nickname, ma pulisci la password
                loginPanel.passwordField.setText(""); // Accesso diretto al campo (ora possibile perché classe interna)
                break;
        }

        // Pulisci l'array della password per sicurezza
        Arrays.fill(passwordChars, ' ');
        window.pack(); // Adatta la finestra al messaggio
    }

    private void handleRegistration() {
        String nickname = registrationPanel.getNickname();
        char[] passwordChars = registrationPanel.getPassword();
        char[] confirmPasswordChars = registrationPanel.getConfirmPassword();

        // Validazione aggiuntiva: le password devono corrispondere
        if (!Arrays.equals(passwordChars, confirmPasswordChars)) {
            statusLabel.setText("Le password non corrispondono. Riprova.");
            statusLabel.setForeground(Color.RED);
            // Pulisci solo i campi password
            registrationPanel.passwordField.setText(""); // Accesso diretto
            registrationPanel.confirmPasswordField.setText(""); // Accesso diretto
            Arrays.fill(passwordChars, ' ');
            Arrays.fill(confirmPasswordChars, ' ');
            window.pack();
            return; // Esce dalla funzione
        }

        String password = new String(passwordChars);
        // Usa il servizio interno
        AuthService.RegistrationResult result = authService.register(nickname, password);

        switch (result) {
            case SUCCESS:
                statusLabel.setText("Registrazione completata! Ora puoi effettuare il login.");
                statusLabel.setForeground(new Color(0, 180, 0));
                registrationPanel.clearFields();
                // Torna automaticamente al pannello di login dopo successo
                cardLayout.show(mainPanel, LOGIN_PANEL);
                break;
            case NICKNAME_TAKEN:
                statusLabel.setText("Nickname già in uso. Scegline un altro.");
                statusLabel.setForeground(Color.ORANGE);
                registrationPanel.nicknameField.requestFocusInWindow(); // Accesso diretto
                registrationPanel.nicknameField.selectAll(); // Accesso diretto
                break;
            case EMPTY_FIELDS:
                statusLabel.setText("Tutti i campi sono obbligatori.");
                statusLabel.setForeground(Color.RED);
                break;
            case FAILURE:
            default:
                statusLabel.setText("Errore durante la registrazione. Riprova.");
                statusLabel.setForeground(Color.RED);
                break;
        }

        // Pulisci gli array delle password per sicurezza
        Arrays.fill(passwordChars, ' ');
        Arrays.fill(confirmPasswordChars, ' ');
        window.pack(); // Adatta la finestra al messaggio
    }

    private void handleLevelSelection() {
        String livelloSelezionato = selezioneLivelloPanel.getLivelloSelezionato();
        statusLabel.setForeground(new Color(0, 180, 0));
        // Qui dovresti avviare il gioco con il livello selezionato
        // Puoi creare una nuova finestra per il gioco, o cambiare il contenuto del frame esistente.
        //JOptionPane.showMessageDialog(window, "Avvio del gioco al livello " + livelloSelezionato + " per l'utente " + loggedInUserNickname + "!", "Inizio Partita", JOptionPane.INFORMATION_MESSAGE);
        window.setVisible(false); //chiude la finestra precedente
        // Creare una nuova classe che estende JFrame per visualizzare il gioco.
        String[] args = new String[2];
        args[0] = livelloSelezionato;
        args[1] = loggedInUserNickname;
        Tetris tetris = new Tetris();
        tetris.run(args);
        tetris.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                window.setVisible(true);
            }
        });
        
        
        return;
    }

    // ============================================================
    // CLASSI INTERNE
    // ============================================================

    // Classe semplice per gestire la logica di autenticazione e registrazione
    // In un'app reale, interagirebbe con un database o un file.
    // Resa statica perché non necessita dello stato dell'istanza di BlocchiCadentiApp
    static class AuthService {

        private static final String USERS_FILE = "users.txt"; // Nome del file
        // Sostituisce la mappa 'users' nel vecchio codice
        private final Map<String, String> registeredUsers = new HashMap<>();

        public enum LoginResult {
            SUCCESS,
            USER_NOT_FOUND,
            INVALID_PASSWORD
        }

        public enum RegistrationResult {
            SUCCESS,
            NICKNAME_TAKEN,
            EMPTY_FIELDS,
            FAILURE // Generic failure
        }

        // Costruttore (potrebbe caricare utenti da file/db)
        public AuthService() {
            loadUsersFromFile(); // Carica gli utenti all'avvio
        }

        // Metodo per caricare gli utenti dal file all'avvio dell'applicazione
        private void loadUsersFromFile() {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                try {
                    file.createNewFile(); // Crea il file se non esiste
                    System.out.println("Created users.txt file.");
                } catch (IOException e) {
                    System.err.println("Error creating users.txt: " + e.getMessage());
                }
                return; // Se il file non esiste, non ci sono utenti da caricare
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        registeredUsers.put(parts[0], parts[1]); // Nickname:HashedPassword
                    }
                }
                System.out.println("Loaded " + registeredUsers.size() + " users from " + USERS_FILE);
            } catch (IOException e) {
                System.err.println("Error loading users from file: " + e.getMessage());
            }
        }

        // Metodo per scrivere un nuovo utente nel file
        private void saveUserToFile(String nickname, String hashedPassword) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) { // 'true' per appendere
                writer.write(nickname + ":" + hashedPassword);
                writer.newLine();
                System.out.println("Saved user to file: " + nickname);
            } catch (IOException e) {
                System.err.println("Error saving user to file: " + e.getMessage());
            }
        }

        // Metodo per hashare la password (usando SHA-256)
        private String hashPassword(String password) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                return bytesToHex(encodedhash);
            } catch (NoSuchAlgorithmException e) {
                System.err.println("SHA-256 algorithm not found: " + e.getMessage());
                return null; // Should not happen in modern JVMs
            }
        }

        // Helper per convertire un array di byte in stringa esadecimale
        private String bytesToHex(byte[] hash) {
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }

        // Tenta di autenticare un utente.
        public LoginResult login(String nickname, String password) {
            String hashedPassword = hashPassword(password); // Hash della password inserita per il confronto

            if (!registeredUsers.containsKey(nickname)) {
                return LoginResult.USER_NOT_FOUND;
            }
            // Confronta la password hashata con quella hashata memorizzata
            if (registeredUsers.get(nickname).equals(hashedPassword)) {
                return LoginResult.SUCCESS;
            } else {
                return LoginResult.INVALID_PASSWORD;
            }
        }

        // Tenta di registrare un nuovo utente.
        public RegistrationResult register(String nickname, String password) {
            if (nickname == null || nickname.trim().isEmpty() || password == null || password.isEmpty()) {
                return RegistrationResult.EMPTY_FIELDS;
            }
            String trimmedNickname = nickname.trim();
            if (registeredUsers.containsKey(trimmedNickname)) {
                return RegistrationResult.NICKNAME_TAKEN;
            }

            String hashedPassword = hashPassword(password);
            if (hashedPassword == null) { // Fallback if hashing failed
                return RegistrationResult.FAILURE;
            }

            registeredUsers.put(trimmedNickname, hashedPassword); // Aggiungi alla mappa in memoria
            saveUserToFile(trimmedNickname, hashedPassword); // Salva su file
            System.out.println("Utente registrato: " + trimmedNickname); // Log per debug
            return RegistrationResult.SUCCESS;
        }

        // Metodo opzionale per ottenere il nickname (non usato in questa UI semplice)
        public Optional<String> getNicknameIfValid(String nickname, String password) {
            if (login(nickname, password) == LoginResult.SUCCESS) {
                return Optional.of(nickname);
            }
            return Optional.empty();
        }
    }
}