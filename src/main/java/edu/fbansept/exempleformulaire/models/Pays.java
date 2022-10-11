package edu.fbansept.exempleformulaire.models;

public class Pays {
    protected String nom;
    protected String iso;
    protected String image;

    public Pays(String nom, String iso, String image) {
        this.nom = nom;
        this.iso = iso;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
