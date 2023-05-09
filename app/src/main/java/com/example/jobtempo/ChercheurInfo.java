package com.example.jobtempo;

public class ChercheurInfo {
    public boolean isChercheur;
    public String nomFamille;
    public String prenom;
    public String email;
    public String adresse;
    public String dateNaissance;
    public String telephone;

    public ChercheurInfo(boolean isChercheur, String nomFamille, String prenom, String email, String adresse, String dateNaissance, String telephone){
        this.isChercheur = isChercheur;
        this.nomFamille = nomFamille;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
    }
}
