package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InscriptionEntreprise extends AppCompatActivity {
    Button valider;
    Button retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_entreprise);

        retour = findViewById(R.id.bouton_retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionEntreprise.this, InscriptionChoix.class);
                startActivity(intent);
            }
        });

        valider = findViewById(R.id.bouton_valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vérification dans la bdd
            }
        });
    }
}
