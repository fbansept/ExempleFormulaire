package edu.fbansept.exempleformulaire;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.fbansept.exempleformulaire.models.Pays;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FenetrePrincipale extends JFrame implements WindowListener {

    protected boolean themeSombreActif = true;
    protected int defaultMargin = 10;

    public FenetrePrincipale() {
        setSize(500,500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(this);

        //ajout du panneau principal avec un layout de 5 zones
        // (NORTH, SOUTH, EAST, WEST, CENTER)
        JPanel panneau = new JPanel(new BorderLayout());
        setContentPane(panneau);

        ChampsSaisie boite = new ChampsSaisie();

        //--------- BOUTON THEME -----------

        JButton boutonTheme = new JButton("Changer le theme");


        boutonTheme.addActionListener(
            e -> {
                try {
                    if(themeSombreActif) {
                        UIManager.setLookAndFeel(new FlatLightLaf());
                    } else {
                        UIManager.setLookAndFeel(new FlatDarculaLaf());
                    }
                    SwingUtilities.updateComponentTreeUI(this);
                    themeSombreActif = !themeSombreActif;

                } catch (UnsupportedLookAndFeelException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );



        //---------- BOUTONS DU HAUT -------
        panneau.add(
                HelperForm.generateRow(boutonTheme,10,10,0,0, HelperForm.ALIGN_RIGHT),
                BorderLayout.NORTH);



        //---------------- FORMULAIRE ------------------


        Box formulaire = Box.createVerticalBox();
        //formulaire.setBorder(BorderFactory.createLineBorder(Color.RED));

        panneau.add(formulaire, BorderLayout.CENTER);


        //---------------- LISTE CIVILITE ------------------

        String[] listeCivilites = {"Monsieur","Madame","Mademoiselle","Autre"};
        JComboBox<String> selectCivilite = new JComboBox<>(listeCivilites);

        formulaire.add(HelperForm.generateField(
                "Civilité",selectCivilite));

        //---------------- CHAMPS TEXT : NOM ---------------

        ChampsSaisie champsNom = new ChampsSaisie("[\\p{L}\s'-]");
        formulaire.add(
                HelperForm.generateField("Nom", champsNom)
        );

        //---------------- CHAMPS TEXT : PRENOM ---------------

        ChampsSaisie champsPrenom = new ChampsSaisie("[\\p{L}\s'-]");
        formulaire.add(
                HelperForm.generateField("Prénom", champsPrenom)
        );

        //---------------- CHAMPS TEXT : EMAIL ---------------

        ChampsSaisie champsEmail = new ChampsSaisie("[a-zA-Z0-9@\\.-]");
        formulaire.add(
                HelperForm.generateField("Email", champsEmail)
        );

        champsEmail.getTextField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {

                if(champsEmail.getText().equals("")) {
                    champsEmail.erreur("Champs obligatoire");
                } else {

                    String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(champsEmail.getText());

                    if(!matcher.matches()){
                        champsEmail.erreur("Format invalide");
                    }
                }
            }
        });


        //---------------- LISTE PAYS ------------------
        Pays[] listePays = {
                new Pays("France", "FR", "fr.png"),
                new Pays("Royaume-unis", "GBR", "gb.png"),
                new Pays("Allemagne", "DE", "de.png")
        };

        JComboBox<Pays> selectPays = new JComboBox<>(listePays);

        selectPays.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Pays pays = (Pays)value;
                setText(pays.getNom());

                try {
                    //on charge l'image de drapeau correspond au pays
                    Image image = ImageIO.read(new File("src/main/resources/drapeaux/" + pays.getImage()));

                    //on redimensionne l'image
                    Image resizedImage = image.getScaledInstance(20, 16, Image.SCALE_SMOOTH);

                    setIconTextGap(10);

                    //on change l'icone du JLabel par l'image redimensionnée
                    setIcon(new ImageIcon(resizedImage));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return this;
            }
        });

        formulaire.add(
                HelperForm.generateField("Pays",selectPays)
        );


        //---------------- CHAMPS TEXT : AGE ---------------

        ChampsSaisie champsAge = new ChampsSaisie("[0-9]");
        formulaire.add(
                HelperForm.generateField("Age", champsAge,50));

        //---------------- CHAMPS CHECKBOX : MARIE/PACSE ---------------

        JCheckBox champsMarie = new JCheckBox();
        formulaire.add(
                HelperForm.generateField("Marie/pacse", champsMarie));


        //--------- BOUTON VALIDER FORMULAIRE -----------

        JButton boutonValider = new JButton("Enregistrer");

        boutonValider.addActionListener(e -> {

            boolean erreurNom = false;
            boolean erreurPrenom = false;
            boolean erreurAge = false;
            boolean erreurEmail = false;

            String message = "Le formulaire comporte des erreurs : ";

            champsNom.resetMessage();
            champsPrenom.resetMessage();

            //------ validateur nom -------
            if(champsNom.getText().equals("")) {
                erreurNom = true;
                message += "\n - Nom obligatoire,";
                champsNom.erreur("Champs obligatoire");
            }

            //------ validateur prenom -------
            if(champsPrenom.getText().equals("")) {
                erreurPrenom = true;
                message += "\n - Prénom obligatoire,";
                champsPrenom.erreur("Champs obligatoire");
            }

            //------ validateur age -------
            if(champsAge.getText().equals("")) {
                erreurAge = true;
                message += "\n - Age obligatoire,";
                champsAge.erreur("Champs obligatoire");
            } else {

                int age = Integer.parseInt(champsAge.getText());

                if(age <= 0 || age > 150) {
                    erreurAge = true;
                    message += "\n - Age avec une valeur impossible,";
                    champsAge.erreur("Valeur impossible");
                }
            }

            //------ validateur email -------
            if(champsEmail.getText().equals("")) {
                erreurEmail = true;
                message += "\n - Email obligatoire,";
                champsEmail.erreur("Champs obligatoire");
            } else {

                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(champsEmail.getText());

                if(!matcher.matches()){
                    erreurEmail = true;
                    message += "\n - Email format invalide,";
                    champsEmail.erreur("Format invalide");
                }

            }

            //on supprime la dernière des virgules
            message = message.substring(0,message.length()-1);

            if(erreurNom || erreurPrenom || erreurAge || erreurEmail) {

                JOptionPane.showMessageDialog(
                        this,
                        message,
                        "Erreur de saisie",
                        JOptionPane.WARNING_MESSAGE);
            }
        });



        //---------- BOUTONS DU BAS -------

        boutonValider.setSize(new Dimension(100, 30));

        panneau.add(
                HelperForm.generateRow(boutonValider,0,10,10,0, HelperForm.ALIGN_RIGHT),
                BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new FenetrePrincipale();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        String[] choix = {"Oui", "Ne pas fermer l'application"};
        int choixUtilisateur = JOptionPane.showOptionDialog(
                this,
                "Voulez-vous vraiment fermer l'application",
                "Confirmer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choix,
                choix[1]);

        if(choixUtilisateur == JOptionPane.YES_OPTION) {
            System.exit(1);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
