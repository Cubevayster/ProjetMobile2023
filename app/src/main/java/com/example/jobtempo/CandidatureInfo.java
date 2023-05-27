package com.example.jobtempo;

public class CandidatureInfo {
    public String offreId;
    public String nomFamille;
    public String prenom;
    public String email;
    public String adresse;
    public String dateNaissance;
    public String telephone;
    public String cvFileId;
    public String lmFileId;

    public CandidatureInfo(){
        // Requis pour la désérialisation depuis Firebase
    }

    public CandidatureInfo(String offreId, String nomFamille, String prenom, String email, String adresse, String dateNaissance, String telephone, String cvFileId, String lmFileId){
        this.offreId = offreId;
        this.nomFamille = nomFamille;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.cvFileId = cvFileId;
        this.lmFileId = lmFileId;
    }

    public String getNom(){
        return this.nomFamille;
    }

    public String getPrenom(){
        return this.prenom;
    }

}
