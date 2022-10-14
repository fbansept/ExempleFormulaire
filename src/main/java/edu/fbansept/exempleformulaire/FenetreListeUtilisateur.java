package edu.fbansept.exempleformulaire;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.fbansept.exempleformulaire.models.Pays;
import edu.fbansept.exempleformulaire.models.Utilisateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;

public class FenetreListeUtilisateur extends JFrame implements WindowListener {

    protected boolean themeSombreActif = true;
    protected DefaultTableModel model;

    protected ArrayList<Utilisateur> listeUtilisateur = new ArrayList<>();

    public FenetreListeUtilisateur() {
        setSize(800, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(this);

        //ajout du panneau principal avec un layout de 5 zones
        // (NORTH, SOUTH, EAST, WEST, CENTER)
        JPanel panneau = new JPanel(new BorderLayout());
        setContentPane(panneau);

        //--------- BOUTON THEME -----------

        JButton boutonTheme = new JButton("Changer le theme");

        boutonTheme.addActionListener(
                e -> {
                    try {
                        if (themeSombreActif) {
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


        JButton boutonAjoutUtilisateur = new JButton("Ajouter un utilisateur");
        boutonAjoutUtilisateur.addActionListener(
                e -> {

                    JOptionPane.showOptionDialog(
                            this,
                            new FenetreFormulaire((nouvelUtilisateur) -> {
                                listeUtilisateur.add(nouvelUtilisateur);

                                ObjectOutputStream oos = null;

                                try {
                                    FileOutputStream fichier = new FileOutputStream("personne.eesc");

                                    oos = new ObjectOutputStream(fichier);
                                    oos.writeObject(listeUtilisateur);
                                    oos.flush();
                                    oos.close();

                                    model.addRow(nouvelUtilisateur.getLigneTableau());
                                    model.fireTableDataChanged();

                                    JOptionPane.showMessageDialog(
                                            this,
                                            "L'utilisateur " + nouvelUtilisateur.getNom() + " a bien été ajouté");

                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(
                                            this,
                                            "Impossible d'enregistrer l'utilisateur");
                                }
                            }),
                            "Ajouter un utilisateur",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            new Object[]{},
                            null
                    );
                }
        );

        //---------- BOUTONS DU HAUT -------

        Box boxBoutonHaut = Box.createVerticalBox();

        boxBoutonHaut.add(
                HelperForm.generateRow(boutonTheme, 10, 10, 0, 0, HelperForm.ALIGN_RIGHT)
        );
        boxBoutonHaut.add(
                HelperForm.generateRow(boutonAjoutUtilisateur, 10, 0, 0, 10, HelperForm.ALIGN_LEFT)
        );

        panneau.add(boxBoutonHaut, BorderLayout.NORTH);

        //----------- TABLE UTILISATEUR ------------

        model = new DefaultTableModel();
        model.addColumn("Civilité");
        model.addColumn("Nom");
        model.addColumn("Prénom");
        model.addColumn("Email");
        model.addColumn("Pays");
        model.addColumn("Age");
        model.addColumn("Marié");
        model.addColumn("Actions");



        JTable tableUtilisateur = new JTable(model);



        ImageIcon errorIcon;

        try {
            errorIcon = new ImageIcon(
                    ImageIO.read(new File("src/main/resources/icones/error.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tableUtilisateur.getColumn("Actions").setCellRenderer(
                (table, value, isSelected, hasFocus, row, column) -> {
                    JLabel label = new JLabel();
                    label.setIcon(errorIcon);
                    return label;
                }
        );

        tableUtilisateur.getColumn("Actions").setCellEditor(
                new DefaultCellEditor(new JCheckBox()) {
                    public Component getTableCellEditorComponent(
                            JTable table, Object value, boolean isSelected, int row, int column) {
                        JLabel icone = new JLabel();
                        icone.setIcon(errorIcon);
                        icone.addMouseListener(new MouseListener() {
                            @Override
                            public void mouseReleased(MouseEvent e) {
                                System.out.println(listeUtilisateur.get(row).getNom());
                            }

                            public void mouseClicked(MouseEvent e) { }
                            public void mousePressed(MouseEvent e) { }
                            public void mouseEntered(MouseEvent e) {}
                            public void mouseExited(MouseEvent e) {}
                        });
                        return icone;
                    }
                }
        );

        panneau.add(new JScrollPane(tableUtilisateur), BorderLayout.CENTER);
        ouvrirFichier();
        setVisible(true);
    }

    public void ouvrirFichier() {

        ObjectInputStream ois = null;

        try {
            FileInputStream fichier = new FileInputStream("personne.eesc");
            ois = new ObjectInputStream(fichier);
            listeUtilisateur = (ArrayList<Utilisateur>) ois.readObject();

            for (Utilisateur utilisateurFichier : listeUtilisateur) {
                model.addRow(utilisateurFichier.getLigneTableau());
            }

            ois.close();

        } catch (FileNotFoundException e) {
            System.out.println("Première fois qu'on ouvre l'application");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Impossible d'ouvrir le fichier");
        } catch (ClassNotFoundException | ClassCastException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fichier corrompu");
        }
    }


    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new FenetreListeUtilisateur();
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

        if (choixUtilisateur == JOptionPane.YES_OPTION) {
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
