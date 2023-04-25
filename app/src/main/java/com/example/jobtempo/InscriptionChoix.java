package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InscriptionChoix extends AppCompatActivity {

    Button chercheur;
    Button entreprise;
    Button agence;
    Button retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_choix_utilisateur);

        chercheur = findViewById(R.id.recherche);
        chercheur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionChoix.this, InscriptionChercheur.class);
                startActivity(intent);
            }
        });

        entreprise = findViewById(R.id.employeur);
        entreprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionChoix.this, InscriptionEntreprise.class);
                startActivity(intent);
            }
        });

        agence = findViewById(R.id.agence);
        agence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionChoix.this, InscriptionEntreprise.class);
                startActivity(intent);
            }
        });

        retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionChoix.this, Connexion.class);
                startActivity(intent);
            }
        });
    }
}
