package struttura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

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
        setBackground(new Color(10, 10, 10, 255)); // Sfondo semi-trasparente
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