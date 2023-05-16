package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainConnecteEntreprise extends Activity {

    Button monCompte;
    Button gestionOffres;
    Button deconnexion;
    Button ajoutOffre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_connecte_entreprise);

        monCompte = findViewById(R.id.mon_compte_entreprise);
        gestionOffres = findViewById(R.id.gestion_offre);
        deconnexion = findViewById(R.id.deconnexion_entreprise);
        ajoutOffre = findViewById(R.id.ajout_offre);

        ajoutOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
