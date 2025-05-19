package struttura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// TODO: pull registered users file and check login

public class LoginPanel extends JPanel {

        // Campi resi accessibili dalla classe esterna se necessario
        final JTextField nicknameField;
        final JPasswordField passwordField;
        private final JButton loginButton;
        private final JButton registerButton;

        public LoginPanel(ActionListener loginAction, ActionListener showRegisterAction) {
            setLayout(new GridBagLayout());
            setBackground(new Color(10, 10, 10, 255)); // Sfondo
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