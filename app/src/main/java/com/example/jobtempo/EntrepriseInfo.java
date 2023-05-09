package com.example.jobtempo;

public class EntrepriseInfo {
    public boolean isChercheur;
    public String nom;
    public String numéro;
    public String email;
    public String adresse;
    public String telephone;

    public EntrepriseInfo(boolean isChercheur, String nom, String numéro, String email, String adresse, String telephone){
        this.isChercheur = isChercheur;
        this.nom = nom;
        this.numéro = numéro;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
    }
}
