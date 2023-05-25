package com.example.jobtempo;

public class OffreInfo {

    public String idDepositaire;
    public String nomOffre;
    public String employeurString;
    public String metierCible;
    public String localisationString;
    public String periodeString;
    public String remunerationString;
    public String descriptionString;

    public OffreInfo(String idDepositaire, String nomOffre, String employeurString, String metierCible, String localisationString, String periodeString, String remunerationString, String descriptionString){
        this.idDepositaire = idDepositaire;
        this.nomOffre = nomOffre;
        this.employeurString = employeurString;
        this.metierCible = metierCible;
        this.localisationString = localisationString;
        this.periodeString = periodeString;
        this.remunerationString = remunerationString;
        this.descriptionString = descriptionString;
    }
}
