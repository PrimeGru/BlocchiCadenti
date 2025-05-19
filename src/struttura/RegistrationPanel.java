package struttura;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

// TODO: make this save to a file

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
            setBackground(new Color(10, 10, 10, 255));
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
