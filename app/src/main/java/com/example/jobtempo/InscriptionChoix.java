package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InscriptionChoix extends AppCompatActivity {

    Button chercheur;
    Button entreprise;
    Button agence;
    Button retour;
    boolean isChercheur;
    FirebaseAuth mAuth;

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_choix_utilisateur);

        chercheur = findViewById(R.id.recherche);
        entreprise = findViewById(R.id.employeur);
        agence = findViewById(R.id.agence);

        chercheur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChercheur = true;
                Intent intent = new Intent(InscriptionChoix.this, InscriptionChercheur.class);
                intent.putExtra("IsChercheur", isChercheur);
                startActivity(intent);
                finish();
            }
        });

        entreprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChercheur = false;
                Intent intent = new Intent(InscriptionChoix.this, InscriptionEntreprise.class);
                intent.putExtra("IsChercheur", isChercheur);
                startActivity(intent);
                finish();
            }
        });

        agence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChercheur = false;
                Intent intent = new Intent(InscriptionChoix.this, InscriptionEntreprise.class);
                intent.putExtra("IsChercheur", isChercheur);
                startActivity(intent);
                finish();
            }
        });

        retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionChoix.this, Connexion.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
