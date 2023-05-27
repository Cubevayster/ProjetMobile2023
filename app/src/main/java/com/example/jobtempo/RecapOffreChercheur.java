package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecapOffreChercheur extends Activity {


    TextView nomOffre;
    TextView employeurView;
    TextView metierCibleView;
    TextView localisationView;
    TextView periodeView;
    TextView remunerationView;
    TextView descriptionView;
    TextView name;

    Button candidature_button;

    Button retour_button;
    String offreId;

    FirebaseUser user;
    FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recap_offre_chercheur);

        nomOffre = findViewById(R.id.nomOffre);
        employeurView = findViewById(R.id.employeurView);
        metierCibleView = findViewById(R.id.metierCibleView);
        localisationView = findViewById(R.id.localisationView);
        periodeView = findViewById(R.id.periodeView);
        remunerationView = findViewById(R.id.remunerationView);
        descriptionView = findViewById(R.id.descriptionView);
        retour_button = findViewById(R.id.retour_button);
        candidature_button = findViewById(R.id.candidature_button);
        name = findViewById(R.id.name);

        offreId = getIntent().getStringExtra("offreId");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());
        DatabaseReference offreRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Offers");

        // Effectuer une requête pour récupérer les informations de l'offre spécifiée
        offreRef.child(offreId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // L'offre existe dans la base de données
                    OffreInfo offreInfo = dataSnapshot.getValue(OffreInfo.class);

                    // Utiliser les informations de l'offre pour afficher le récapitulatif
                    String nomOffreString = offreInfo.nomOffre;
                    String employeurString = offreInfo.employeurString;
                    String metierCible = offreInfo.metierCible;
                    String localisationString = offreInfo.localisationString;
                    String periodeString = offreInfo.periodeString;
                    String remunerationString = offreInfo.remunerationString;
                    String descriptionString = offreInfo.descriptionString;

                    nomOffre.setText(nomOffreString);
                    employeurView.setText(employeurString);
                    metierCibleView.setText(metierCible);
                    localisationView.setText(localisationString);
                    periodeView.setText(periodeString);
                    remunerationView.setText(remunerationString);
                    descriptionView.setText(descriptionString);

                } else {
                    // L'offre n'existe pas dans la base de données
                    Toast.makeText(getApplicationContext(), "Aucune offre correspondante trouvée", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur de lecture des données
                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                String nom = dataSnapshot.child("nomFamille").getValue(String.class);
                name.setText(prenom + " " + nom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(RecapOffreChercheur.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        candidature_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Candidature.class);
                intent.putExtra("offreId", offreId);
                startActivity(intent);
            }
        });

        retour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
