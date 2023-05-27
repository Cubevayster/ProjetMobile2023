package com.example.jobtempo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    Button recherche;
    TextView utilisateurNom;

    EditText lieu;
    EditText metier;

    LinearLayout layoutScrollViewChercheur;
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
        layoutScrollViewChercheur = findViewById(R.id.layoutScrollViewChercheur);
        lieu = findViewById(R.id.lieu_travail);
        metier = findViewById(R.id.metier_cible);
        recherche = findViewById(R.id.recherche);
        user = auth.getCurrentUser();

        final String[] Lieu = new String[1];
        final String[] Metier = new String[1];

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

        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lieu[0] = String.valueOf(lieu.getText());
                Metier[0] = String.valueOf(metier.getText());

                DatabaseReference offresRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Offers");
                offresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Effacer le contenu de la ScrollView
                        layoutScrollViewChercheur.removeAllViews();

                        // Parcourir les offres d'emploi
                        for(DataSnapshot offreSnapshot : snapshot.getChildren()) {
                            // Récupérer les informations de l'offre d'emploi
                            String nomOffre = offreSnapshot.child("nomOffre").getValue(String.class);
                            String lieuOffre = offreSnapshot.child("localisationString").getValue(String.class);
                            String offreId = offreSnapshot.getKey();
                            if(nomOffre.contains(Metier[0]) && lieuOffre.contains(Lieu[0])) {
                                // Créer un bouton pour afficher le nom de l'offre d'emploi
                                Button offreButton = new Button(getApplicationContext());
                                offreButton.setText(nomOffre);
                                offreButton.setTag(offreId);

                                offreButton.setBackgroundResource(R.drawable.btn_bg);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                int margin = getResources().getDimensionPixelSize(R.dimen.button_margin); // Récupérer la valeur de la marge à partir des ressources
                                layoutParams.setMargins(margin, margin, margin, margin);
                                offreButton.setLayoutParams(layoutParams);

                                offreButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Récupérer l'identifiant de l'offre d'emploi
                                        String clickedOffreId = (String) view.getTag();

                                        // Passer l'identifiant de l'offre d'emploi à l'activité RecapOffre
                                        Intent intent = new Intent(getApplicationContext(), RecapOffreChercheur.class);
                                        intent.putExtra("offreId", clickedOffreId);
                                        startActivity(intent);
                                    }
                                });

                                layoutScrollViewChercheur.addView(offreButton);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainConnecte.this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        DatabaseReference offresRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Offers");
        offresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Effacer le contenu de la ScrollView
                layoutScrollViewChercheur.removeAllViews();

                // Parcourir les offres d'emploi
                for(DataSnapshot offreSnapshot : snapshot.getChildren()) {
                    // Récupérer les informations de l'offre d'emploi
                    String nomOffre = offreSnapshot.child("nomOffre").getValue(String.class);

                    String offreId = offreSnapshot.getKey();
                    // Créer un bouton pour afficher le nom de l'offre d'emploi
                    Button offreButton = new Button(getApplicationContext());
                    offreButton.setText(nomOffre);
                    offreButton.setTag(offreId);

                    offreButton.setBackgroundResource(R.drawable.btn_bg);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    int margin = getResources().getDimensionPixelSize(R.dimen.button_margin); // Récupérer la valeur de la marge à partir des ressources
                    layoutParams.setMargins(margin, margin, margin, margin);
                    offreButton.setLayoutParams(layoutParams);

                    offreButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Récupérer l'identifiant de l'offre d'emploi
                            String clickedOffreId = (String) view.getTag();

                            // Passer l'identifiant de l'offre d'emploi à l'activité RecapOffre
                            Intent intent = new Intent(getApplicationContext(), RecapOffreChercheur.class);
                            intent.putExtra("offreId", clickedOffreId);
                            startActivity(intent);
                        }
                    });

                    layoutScrollViewChercheur.addView(offreButton);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainConnecte.this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}