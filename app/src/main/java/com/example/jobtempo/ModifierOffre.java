package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifierOffre extends Activity {

    EditText nom_offre;
    EditText employeur;
    EditText metier_cible;
    EditText localisation;
    EditText periode;
    EditText remuneration;
    EditText description;

    Button valider;
    Button retour;
    Button supprimer;

    FirebaseAuth mAuth;
    FirebaseDatabase linkData = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference mDatabase = linkData.getReference("Offers");

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_offre);

        mAuth = FirebaseAuth.getInstance();

        nom_offre = findViewById(R.id.nom_offre);
        employeur = findViewById(R.id.employeur);
        metier_cible = findViewById(R.id.metier_cible);
        localisation = findViewById(R.id.localisation);
        periode = findViewById(R.id.periode);
        remuneration = findViewById(R.id.remuneration);
        description = findViewById(R.id.description);

        valider = findViewById(R.id.bouton_valider);
        retour = findViewById(R.id.bouton_retour);
        supprimer = findViewById(R.id.bouton_suppr);

        String offreId = getIntent().getStringExtra("offreId");

        DatabaseReference offreRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Offers");

        // Effectuer une requête pour récupérer les informations de l'offre spécifiée
        offreRef.child(offreId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

                nom_offre.setText(nomOffreString);
                employeur.setText(employeurString);
                metier_cible.setText(metierCible);
                localisation.setText(localisationString);
                periode.setText(periodeString);
                remuneration.setText(remunerationString);
                description.setText(descriptionString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur de lecture des données
                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NomOffre = String.valueOf(nom_offre.getText());
                String Employeur = String.valueOf(employeur.getText());
                String Metier = String.valueOf(metier_cible.getText());
                String Localisation = String.valueOf(localisation.getText());
                String Periode = String.valueOf(periode.getText());
                String Remuneration = String.valueOf(remuneration.getText());
                String Description = String.valueOf(description.getText());

                offreRef.child(offreId).child("nomOffre").setValue(NomOffre);
                offreRef.child(offreId).child("descriptionString").setValue(Description);
                offreRef.child(offreId).child("employeurString").setValue(Employeur);
                offreRef.child(offreId).child("localisationString").setValue(Localisation);
                offreRef.child(offreId).child("metierCible").setValue(Metier);
                offreRef.child(offreId).child("periodeString").setValue(Periode);
                offreRef.child(offreId).child("remunerationString").setValue(Remuneration);


                Intent intent = new Intent(getApplicationContext(), RecapOffre.class);
                intent.putExtra("offreId", offreId);
                startActivity(intent);
                finish();
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecapOffre.class);
                intent.putExtra("offreId", offreId);
                startActivity(intent);
                finish();
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offreRef.child(offreId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Gérer l'erreur de lecture des données
                        Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(getApplicationContext(), MainConnecteEntreprise.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

