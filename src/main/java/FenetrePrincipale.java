import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class FenetrePrincipale extends JFrame {

    protected boolean themeSombreActif = true;
    public FenetrePrincipale() {
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panneau = new JPanel();
        setContentPane(panneau);

        JButton boutonTheme = new JButton("Changer le theme");
        panneau.add(boutonTheme);

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


        setVisible(true);
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new FenetrePrincipale();
    }

}
