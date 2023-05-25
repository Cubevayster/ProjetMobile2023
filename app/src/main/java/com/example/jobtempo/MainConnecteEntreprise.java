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
                    // Récupérer l'identifiant de l'offre d'emploi
                    String offreId = offreSnapshot.getKey();
                    System.out.println("ID OFFRE : " + offreId);

                    // Récupérer les informations de l'offre d'emploi
                    String nomOffre = offreSnapshot.child("nomOffre").getValue(String.class);

                    // Créer un bouton pour afficher le nom de l'offre d'emploi
                    Button offreButton = new Button(MainConnecteEntreprise.this);
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
                            // Récupérer l'identifiant de l'offre à partir du tag du bouton
                            String offreId = (String) view.getTag();

                            // Rediriger vers l'activité RecapOffre en passant l'identifiant de l'offre
                            Intent intent = new Intent(getApplicationContext(), RecapOffre.class);
                            intent.putExtra("offreId", offreId);
                            startActivity(intent);
                        }
                    });

                    // Ajouter le bouton à la ScrollView
                    layoutScrollView.addView(offreButton);
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
