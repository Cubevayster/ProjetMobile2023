package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainConnecteEntreprise extends Activity {

    Button monCompte;
    Button gestionOffres;
    Button deconnexion;
    Button ajoutOffre;

    LinearLayout layoutScrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_connecte_entreprise);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // L'utilisateur n'est pas connecté, effectuer la gestion appropriée
            finish();
        }

        String userId = currentUser.getUid();
        DatabaseReference offreRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Offers");
        Query offreQuery = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Offers").orderByChild("idDepositaire").equalTo(userId);

        monCompte = findViewById(R.id.mon_compte_entreprise);
        gestionOffres = findViewById(R.id.gestion_offre);
        deconnexion = findViewById(R.id.deconnexion_entreprise);
        ajoutOffre = findViewById(R.id.ajout_offre);
        layoutScrollView = findViewById(R.id.layoutScrollView);

        monCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // vers infos du compte
            }
        });

        gestionOffres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ???
            }
        });

        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ajoutOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreationOffre.class);
                startActivity(intent);
                finish();
            }
        });

        offreQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Effacer le contenu de la ScrollView
                layoutScrollView.removeAllViews();

                // Parcourir les offres d'emploi de l'entreprise
                for (DataSnapshot offreSnapshot : dataSnapshot.getChildren()) {
                    // Récupérer les informations de l'offre d'emploi
                    String nomOffre = offreSnapshot.child("nomOffre").getValue(String.class);

                    // Créer un TextView pour afficher le nom de l'offre d'emploi
                    TextView textView = new TextView(MainConnecteEntreprise.this);
                    textView.setText(nomOffre);

                    // Ajouter le TextView à la ScrollView
                    layoutScrollView.addView(textView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur de lecture des données
                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
