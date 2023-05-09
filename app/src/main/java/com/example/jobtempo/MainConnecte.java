package com.example.jobtempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainConnecte extends AppCompatActivity {
    Button deconnexion;
    Button monCompte;
    TextView utilisateurNom;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_connecte);

        auth = FirebaseAuth.getInstance();
        deconnexion = findViewById(R.id.deconnexion);
        monCompte = findViewById(R.id.mon_compte);
        utilisateurNom = findViewById(R.id.utilisateur_nom);
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            utilisateurNom.setText(user.getEmail());
        }

        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainConnecte.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        monCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vers infos du compte
            }
        });
    }
}