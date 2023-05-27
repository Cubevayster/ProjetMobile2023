package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CandidatureEntreprise extends Activity {

    Button retour_button;
    LinearLayout layoutScrollView;
    String offreId;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidature_entreprise);

        retour_button = findViewById(R.id.retour_button);
        layoutScrollView = findViewById(R.id.layoutScrollView);

        // Récupérer l'id de l'offre depuis l'intent
        Intent intent = getIntent();
        offreId = intent.getStringExtra("offreId");

        // Récupérer les candidatures correspondant à l'offre depuis Firebase
        Query candidatureQuery = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Candidatures")
                .orderByChild("offreId")
                .equalTo(offreId);

        candidatureQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Effacer le contenu existant de la ScrollView
                layoutScrollView.removeAllViews();

                // S'il existe des candidatures
                if (dataSnapshot.exists()){
                    // Parcourir les candidatures
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Récupérer les informations de la candidature depuis le DataSnapshot
                        CandidatureInfo candidatureInfo = snapshot.getValue(CandidatureInfo.class);

                        // Créer un LinearLayout horizontal pour chaque candidature
                        LinearLayout candidatureLayout = new LinearLayout(getApplicationContext());
                        candidatureLayout.setOrientation(LinearLayout.HORIZONTAL);

                        // Créer une TextView avec le nom et le prénom du candidat
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText(candidatureInfo.getNom() + " " + candidatureInfo.getPrenom());
                        float textSizeInSp = 20;
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
                        textView.setTextColor(getResources().getColor(R.color.white));

                        // Créer un bouton "Voir" pour la candidature
                        Button voirButton = new Button(getApplicationContext());
                        voirButton.setText("Voir");
                        voirButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Rediriger vers une autre activité en transmettant l'offreId et l'instance de CandidatureInfo
                                Intent intent = new Intent(CandidatureEntreprise.this, VoirCandidature.class);
                                intent.putExtra("offreId", offreId);
                                intent.putExtra("candidatureInfo", candidatureInfo);
                                startActivity(intent);
                            }
                        });

                        // Ajouter la TextView au LinearLayout horizontal
                        candidatureLayout.addView(textView);
                        // Ajouter le bouton au LinearLayout horizontal
                        candidatureLayout.addView(voirButton);

                        // Ajouter le LinearLayout horizontal au LinearLayout de la ScrollView
                        layoutScrollView.addView(candidatureLayout);
                    }
                } else {
                    // S'il n'y aucune candidature
                    // Créer une TextView avec le nom et le prénom du candidat
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText("Aucune candidature à cette offre pour l'instant");
                    float textSizeInSp = 20;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
                    textView.setTextColor(getResources().getColor(R.color.white));

                    layoutScrollView.addView(textView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur de lecture des données
                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });

        retour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rediriger vers l'activité RecapOffre en passant l'identifiant de l'offre
                Intent intent = new Intent(getApplicationContext(), RecapOffre.class);
                intent.putExtra("offreId", offreId);
                startActivity(intent);
            }
        });
    }
}
