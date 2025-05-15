import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        statusLabel.setText("Livello selezionato: " + livelloSelezionato + ". Preparazione del gioco...");
        statusLabel.setForeground(new Color(0, 180, 0));
        // Qui dovresti avviare il gioco con il livello selezionato
        // Puoi creare una nuova finestra per il gioco, o cambiare il contenuto del frame esistente.
        JOptionPane.showMessageDialog(window, "Avvio del gioco al livello " + livelloSelezionato + " per l'utente " + loggedInUserNickname + "!", "Inizio Partita", JOptionPane.INFORMATION_MESSAGE);
        // window.dispose(); //chiude la finestra precedente
        // Creare una nuova classe che estende JFrame per visualizzare il gioco.
        // new FinestraGioco(livelloSelezionato, loggedInUserNickname).setVisible(true);
    }

    // ============================================================
    // CLASSI INTERNE
    // ============================================================

    // Classe semplice per gestire la logica di autenticazione e registrazione
    // In un'app reale, interagirebbe con un database o un file.
    // Resa statica perché non necessita dello stato dell'istanza di BlocchiCadentiApp
    static class AuthService {

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
            // Aggiungi utenti di esempio se necessario per test
            registeredUsers.put("test", "pw");
        }

        // Tenta di autenticare un utente.
        public LoginResult login(String nickname, String password) {
            if (!registeredUsers.containsKey(nickname)) {
                return LoginResult.USER_NOT_FOUND;
            }
            if (registeredUsers.get(nickname).equals(password)) {
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
            if (registeredUsers.containsKey(nickname.trim())) { // Trimma anche qui per consistenza
                return RegistrationResult.NICKNAME_TAKEN;
            }
            registeredUsers.put(nickname.trim(), password);
            System.out.println("Utente registrato: " + nickname.trim()); // Log per debug
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

    // Pannello Login (classe interna)
    class LoginPanel extends JPanel {

        // Campi resi accessibili dalla classe esterna se necessario
        final JTextField nicknameField;
        final JPasswordField passwordField;
        private final JButton loginButton;
        private final JButton registerButton;

        public LoginPanel(ActionListener loginAction, ActionListener showRegisterAction) {
            setLayout(new GridBagLayout());
            setBackground(new Color(0, 0, 0, 150)); // Sfondo semi-trasparente
            setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); // Spaziatura tra componenti
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER; // Centra tutto il contenuto del pannello

            // --- Nickname ---
            JLabel nicknameLabel = new JLabel("Nickname:");
            nicknameLabel.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            nicknameLabel.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER; // Era LINE_END, cambiato in CENTER
            gbc.fill = GridBagConstraints.NONE;
            add(nicknameLabel, gbc);

            nicknameField = new JTextField(20); // Larghezza suggerita
            nicknameField.setFont(BlocchiCadenti.TEXT_FIELD_FONT); // Modifica qui
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0; // Occupa spazio orizzontale
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(nicknameField, gbc);

            // --- Password ---
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            passwordLabel.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER; // Era LINE_END, cambiato in CENTER
            add(passwordLabel, gbc);

            passwordField = new JPasswordField(20);
            passwordField.setFont(BlocchiCadenti.TEXT_FIELD_FONT); // Modifica qui
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(passwordField, gbc);

            // --- Pulsanti ---
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); // Pannello per centrare i pulsanti
            buttonPanel.setOpaque(false); // Rende trasparente il pannello dei pulsanti

            loginButton = new JButton("Login");
            loginButton.setBackground(new Color(0, 153, 0));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFont(BlocchiCadenti.BUTTON_FONT); // Modifica qui
            loginButton.setActionCommand("LOGIN_ATTEMPT"); // Comando per l'action listener
            loginButton.addActionListener(loginAction);
            buttonPanel.add(loginButton);

            registerButton = new JButton("Registrati");
            registerButton.setBackground(new Color(51, 102, 255));
            registerButton.setForeground(Color.WHITE);
            registerButton.setFont(BlocchiCadenti.BUTTON_FONT); // Modifica qui
            registerButton.setActionCommand("SHOW_REGISTER"); // Comando per mostrare la registrazione
            registerButton.addActionListener(showRegisterAction);
            buttonPanel.add(registerButton);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2; // Occupa due colonne
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weighty = 1.0; // Spinge i pulsanti verso il basso se c'è spazio extra
            gbc.anchor = GridBagConstraints.PAGE_END;
            add(buttonPanel, gbc);
        }

        public String getNickname() {
            return nicknameField.getText();
        }

        public char[] getPassword() {
            return passwordField.getPassword();
        }

        public void clearFields() {
            nicknameField.setText("");
            passwordField.setText("");
        }
    }

    // Pannello Registrazione (classe interna)
    class RegistrationPanel extends JPanel {

        // Campi resi accessibili
        final JTextField nicknameField;
        final JPasswordField passwordField;
        final JPasswordField confirmPasswordField; // Campo conferma password
        private final JButton confirmRegisterButton;
        private final JButton cancelButton; // Pulsante per tornare al login

        public RegistrationPanel(ActionListener registerAction, ActionListener showLoginAction) {
            setLayout(new GridBagLayout());
            setBackground(new Color(0, 0, 0, 150));
            setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER; // Centra il contenuto


            // --- Nickname ---
            JLabel nicknameLabel = new JLabel("Scegli Nickname:");
            nicknameLabel.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            nicknameLabel.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER; // Era LINE_END, cambiato in CENTER
            gbc.fill = GridBagConstraints.NONE;
            add(nicknameLabel, gbc);

            nicknameField = new JTextField(20);
            nicknameField.setFont(BlocchiCadenti.TEXT_FIELD_FONT); // Modifica qui
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(nicknameField, gbc);

            // --- Password ---
            JLabel passwordLabel = new JLabel("Scegli Password:");
            passwordLabel.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            passwordLabel.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER; // Era LINE_END, cambiato in CENTER
            add(passwordLabel, gbc);

            passwordField = new JPasswordField(20);
            passwordField.setFont(BlocchiCadenti.TEXT_FIELD_FONT); // Modifica qui
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(passwordField, gbc);

            // --- Conferma Password ---
            JLabel confirmPasswordLabel = new JLabel("Conferma Password:");
            confirmPasswordLabel.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            confirmPasswordLabel.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            gbc.gridx = 0;
            gbc.gridy = 2; // Nuova riga
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER; // Era LINE_END, cambiato in CENTER
            add(confirmPasswordLabel, gbc);

            confirmPasswordField = new JPasswordField(20);
            confirmPasswordField.setFont(BlocchiCadenti.TEXT_FIELD_FONT); // Modifica qui
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(confirmPasswordField, gbc);


            // --- Pulsanti ---
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            buttonPanel.setOpaque(false);

            confirmRegisterButton = new JButton("Registra");
            confirmRegisterButton.setBackground(new Color(255, 165, 0));
            confirmRegisterButton.setForeground(Color.WHITE);
            confirmRegisterButton.setFont(BlocchiCadenti.BUTTON_FONT); // Modifica qui
            confirmRegisterButton.setActionCommand("REGISTER_CONFIRM");
            confirmRegisterButton.addActionListener(registerAction);
            buttonPanel.add(confirmRegisterButton);

            cancelButton = new JButton("Annulla");
            cancelButton.setBackground(new Color(200, 200, 200)); // Grigio
            cancelButton.setFont(BlocchiCadenti.BUTTON_FONT); // Modifica qui
            cancelButton.setActionCommand("SHOW_LOGIN"); // Comando per tornare al login
            cancelButton.addActionListener(showLoginAction);
            buttonPanel.add(cancelButton);

            gbc.gridx = 0;
            gbc.gridy = 3; // Nuova riga per i pulsanti
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.PAGE_END;
            add(buttonPanel, gbc);
        }

        public String getNickname() {
            return nicknameField.getText();
        }

        public char[] getPassword() {
            return passwordField.getPassword();
        }

        public char[] getConfirmPassword() {
            return confirmPasswordField.getPassword();
        }

        public void clearFields() {
            nicknameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    } // Fine RegistrationPanel interna

    // Pannello Selezione Livello (classe interna)
    class SelezioneLivello extends JPanel implements ActionListener {

        private JRadioButton livelloFacile;
        private JRadioButton livelloMedio;
        private JRadioButton livelloDifficile;
        private JRadioButton livelloEsperto;
        private JRadioButton livelloIncubo;
        private ButtonGroup gruppoLivelli;
        private JButton bottoneConferma;
        private JLabel etichettaSelezione;
        private JLabel etichettaIstruzioni;
        private String livelloSelezionato; //per ottenere il livello selezionato

        public SelezioneLivello(ActionListener levelSelectAction) {
            setLayout(new GridBagLayout()); // Cambiato in GridBagLayout per maggiore controllo
            setBackground(new Color(0, 0, 0, 150)); // Sfondo semi-trasparente
            setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER; // Ogni componente occupa una riga intera
            gbc.anchor = GridBagConstraints.CENTER; //centra tutto

            // Crea le etichette
            etichettaSelezione = new JLabel("Seleziona il livello di difficoltà:");
            etichettaSelezione.setFont(new Font("Arial", Font.BOLD, 16)); // Font leggermente ingrandito
            etichettaSelezione.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            add(etichettaSelezione, gbc);

            etichettaIstruzioni = new JLabel("Scegli un'opzione e premi 'Conferma'.");
            etichettaIstruzioni.setFont(new Font("Arial", Font.ITALIC, 14)); // Font leggermente ingrandito
            etichettaIstruzioni.setForeground(Color.LIGHT_GRAY);
            add(etichettaIstruzioni, gbc);

            // Crea i radio button per i livelli
            livelloFacile = new JRadioButton("Facile");
            livelloFacile.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            livelloFacile.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            add(livelloFacile, gbc);

            livelloMedio = new JRadioButton("Medio");
            livelloMedio.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            livelloMedio.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            add(livelloMedio, gbc);

            livelloDifficile = new JRadioButton("Difficile");
            livelloDifficile.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            livelloDifficile.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            add(livelloDifficile, gbc);

            livelloEsperto = new JRadioButton("Esperto");
            livelloEsperto.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            livelloEsperto.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            add(livelloEsperto, gbc);

            livelloIncubo = new JRadioButton("Incubo");
            livelloIncubo.setForeground(BlocchiCadenti.TEXT_FOREGROUND_COLOR); // Modifica qui
            livelloIncubo.setFont(BlocchiCadenti.LABEL_FONT); // Modifica qui
            add(livelloIncubo, gbc);

            // Crea il gruppo di bottoni per assicurare che solo un livello sia selezionato
            gruppoLivelli = new ButtonGroup();
            gruppoLivelli.add(livelloFacile);
            gruppoLivelli.add(livelloMedio);
            gruppoLivelli.add(livelloDifficile);
            gruppoLivelli.add(livelloEsperto);
            gruppoLivelli.add(livelloIncubo);

            // Seleziona il livello facile come predefinito
            livelloFacile.setSelected(true);
            livelloSelezionato = "Facile"; //inizializza

            // Crea il bottone di conferma
            bottoneConferma = new JButton("Conferma");
            bottoneConferma.setFont(BlocchiCadenti.BUTTON_FONT); // Modifica qui
            bottoneConferma.setBackground(new Color(0, 128, 128)); // Teal
            
            bottoneConferma.setForeground(Color.WHITE);
            bottoneConferma.setActionCommand("LEVEL_SELECTED");
            bottoneConferma.addActionListener(levelSelectAction); // Usa l'action listener passato
            bottoneConferma.addActionListener(this);
            gbc.anchor = GridBagConstraints.CENTER; // Centra il bottone
            add(bottoneConferma, gbc);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bottoneConferma) {
                if (livelloFacile.isSelected()) {
                    livelloSelezionato = "Facile";
                } else if (livelloMedio.isSelected()) {
                    livelloSelezionato = "Medio";
                } else if (livelloDifficile.isSelected()) {
                    livelloSelezionato = "Difficile";
                } else if (livelloEsperto.isSelected()) {
                    livelloSelezionato = "Esperto";
                } else if (livelloIncubo.isSelected()) {
                    livelloSelezionato = "Incubo";
                }
            }
        }

        public String getLivelloSelezionato() {
            return livelloSelezionato;
        }
    }
}
