package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonCompteChercheur extends AppCompatActivity {
    Button modifier;
    Button retour;
    TextView titre;
    TextView nom;
    TextView prenom;
    TextView nationalite;
    TextView telephone;
    TextView email;
    TextView date;
    TextView adresse;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mon_compte_chercheur);

        titre = findViewById(R.id.titre);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        nationalite = findViewById(R.id.nationalite);
        telephone = findViewById(R.id.telephone);
        email = findViewById(R.id.email);
        date = findViewById(R.id.date);
        adresse = findViewById(R.id.adresse);


        auth = FirebaseAuth.getInstance();
        retour = findViewById(R.id.retour);
        modifier = findViewById(R.id.modifier);
        user = auth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titre.setText("Profil");
                nom.setText(dataSnapshot.child("nomFamille").getValue(String.class));
                prenom.setText(dataSnapshot.child("prenom").getValue(String.class));
                telephone.setText(dataSnapshot.child("telephone").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                date.setText(dataSnapshot.child("dateNaissance").getValue(String.class));
                adresse.setText(dataSnapshot.child("adresse").getValue(String.class));
                //Toast.makeText(MonCompteChercheur.this, dataSnapshot.child("nomFamille").getValue(String.class), Toast.LENGTH_SHORT).show();
                if(dataSnapshot.child("nationalite").getValue(String.class) == null){
                    nationalite.setText("non spécifiée");
                }else{
                    nationalite.setText(dataSnapshot.child("nationalite").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(MonCompteChercheur.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonCompteChercheur.this, MainConnecte.class);
                startActivity(intent);
                finish();
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonCompteChercheur.this, ModifierChercheur.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
