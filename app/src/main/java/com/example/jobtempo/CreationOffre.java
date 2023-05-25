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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreationOffre extends Activity {

    EditText nom_offre;
    EditText employeur;
    EditText metier_cible;
    EditText localisation;
    EditText periode;
    EditText remuneration;
    EditText description;

    Button valider;
    Button retour;

    FirebaseAuth mAuth;
    FirebaseDatabase linkData = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference mDatabase = linkData.getReference("Offers");

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_offre);

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

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomOffre = String.valueOf(nom_offre.getText());
                String employeurString = String.valueOf(employeur.getText());
                String metierCible = String.valueOf(metier_cible.getText());
                String localisationString = String.valueOf(localisation.getText());
                String periodeString = String.valueOf((periode.getText()));
                String remunerationString = String.valueOf(remuneration.getText());
                String descriptionString = String.valueOf(description.getText());

                boolean notAllConditions = TextUtils.isEmpty(nomOffre) || TextUtils.isEmpty(employeurString) || TextUtils.isEmpty(metierCible)
                        || TextUtils.isEmpty(localisationString) || TextUtils.isEmpty(remunerationString) || TextUtils.isEmpty(descriptionString);

                if(notAllConditions){
                    Toast.makeText(getApplicationContext(), "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = mAuth.getCurrentUser().getUid();
                OffreInfo newOffer = new OffreInfo(userId, nomOffre, employeurString, metierCible, localisationString, periodeString, remunerationString, descriptionString);

                mDatabase.push().setValue(newOffer, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            // L'ajout des données a échoué
                            String errorMessage = error.getMessage();
                            Toast.makeText(getApplicationContext(), "Erreur : " + errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            // L'ajout des données a réussi
                            Toast.makeText(getApplicationContext(), "Offre crée avec succès", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MainConnecteEntreprise.class);
                            //intent.putExtra("nomOffre", nomOffre);
                            //setResult(RESULT_OK, intent);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainConnecteEntreprise.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
