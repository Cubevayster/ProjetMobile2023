package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainConnecteEntreprise extends Activity {

    Button monCompte;
    Button gestionOffres;
    Button deconnexion;
    Button ajoutOffre;

    LinearLayout layoutScrollView;

    private static final int REQUEST_CODE_AJOUT_OFFRE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_connecte_entreprise);

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
                //startActivityForResult(intent, REQUEST_CODE_AJOUT_OFFRE);
                finish();
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AJOUT_OFFRE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("nomOffre")) {
                String nomOffre = data.getStringExtra("nomOffre");
                // Ajouter un élément (TextView + bouton) à votre ScrollView (LinearLayout) avec le nom de l'offre créée
                LinearLayout linearLayout = findViewById(R.id.layoutScrollView);
                TextView textView = new TextView(this);
                textView.setText(nomOffre);
                // Ajouter le TextView au LinearLayout
                linearLayout.addView(textView);
            }
        }
    }*/
}
