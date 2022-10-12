package edu.fbansept.exempleformulaire;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ChampsSaisie extends Box {
    protected JTextField textField = new JTextField();
    protected JLabel icone = new JLabel();
    protected JLabel message = new JLabel();

    protected Border borderOriginal;

    public ChampsSaisie() {
        super(BoxLayout.Y_AXIS);
        Box ligne1 = Box.createHorizontalBox();
        ligne1.add(textField);
        ligne1.add(icone);
        ligne1.add(Box.createHorizontalGlue());
        add(ligne1);

        Box ligne2 = Box.createHorizontalBox();
        ligne2.add(message);
        ligne2.add(Box.createHorizontalGlue());
        add(ligne2);

        borderOriginal = textField.getBorder();
    }

    public void erreur(String texte) {
        textField.setBorder(BorderFactory.createLineBorder(Color.red));
        message.setForeground(Color.RED);
        message.setText(texte);
    }

    public void resetMessage() {
        textField.setBorder(borderOriginal);
        message.setText("");
    }

    public String getText() {
        return textField.getText();
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JLabel getIcone() {
        return icone;
    }

    public void setIcone(JLabel icone) {
        this.icone = icone;
    }

    public JLabel getMessage() {
        return message;
    }

    public void setMessage(JLabel message) {
        this.message = message;
    }
}
