package com.example.jobtempo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                String nom = dataSnapshot.child("nomFamille").getValue(String.class);
                utilisateurNom.setText(prenom + " " + nom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(MainConnecte.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
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
                Intent intent = new Intent(MainConnecte.this, MonCompteChercheur.class);
                startActivity(intent);
                finish();
            }
        });
    }
}