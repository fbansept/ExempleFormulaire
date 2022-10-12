package edu.fbansept.exempleformulaire;

import javax.swing.*;
import java.awt.*;

public class ChampsSaisie extends Box {
    protected JTextField textField = new JTextField();
    protected JLabel icone = new JLabel();
    protected JLabel message = new JLabel();
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
    }

    public void erreur(String texte) {
        textField.setBorder(BorderFactory.createLineBorder(Color.red));
        message.setForeground(Color.RED);
        message.setText(texte);
    }

    public void resetMessage() {
        //supprimer la bordure du texfield
        //remettre le text du JLabel message à vide
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
