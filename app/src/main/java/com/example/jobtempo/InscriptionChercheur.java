package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobtempo.ChercheurInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InscriptionChercheur extends AppCompatActivity {
    Button valider;
    Button retour;
    boolean estChercheur;

    EditText NomFamille;
    EditText Prenom;
    EditText AdresseEmail;
    EditText AdressePostale;
    EditText DateNaissance;
    EditText Telephone;
    EditText MDP;
    FirebaseAuth mAuth;
    FirebaseDatabase linkData = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference mDatabase = linkData.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_chercheur);

        mAuth = FirebaseAuth.getInstance();

        valider = findViewById(R.id.bouton_valider);
        retour = findViewById(R.id.bouton_retour);
        estChercheur = getIntent().getExtras().getBoolean("IsChercheur");

        NomFamille = findViewById(R.id.nom);
        Prenom = findViewById(R.id.prenom);
        AdresseEmail = findViewById(R.id.email);
        AdressePostale = findViewById(R.id.adresse);
        DateNaissance = findViewById(R.id.birthday);
        Telephone = findViewById(R.id.telephone);
        MDP = findViewById(R.id.mdp);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionChercheur.this, InscriptionChoix.class);
                startActivity(intent);
                finish();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomDeFamille = String.valueOf(NomFamille.getText());
                String prenom = String.valueOf(Prenom.getText());
                String email = String.valueOf(AdresseEmail.getText());
                String adresse = String.valueOf(AdressePostale.getText());
                String dateNaissance = String.valueOf(DateNaissance.getText());
                String telephone = String.valueOf(Telephone.getText());
                String mdp = String.valueOf(MDP.getText());

                boolean notAllConditions = TextUtils.isEmpty(nomDeFamille) || TextUtils.isEmpty(prenom) || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(adresse) || TextUtils.isEmpty(dateNaissance) || TextUtils.isEmpty(telephone) || TextUtils.isEmpty(mdp);

                if(notAllConditions){
                    Toast.makeText(InscriptionChercheur.this, "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChercheurInfo newUser = new ChercheurInfo(estChercheur, nomDeFamille, prenom, email, adresse, dateNaissance, telephone);

                mAuth.createUserWithEmailAndPassword(email, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(InscriptionChercheur.this, "Création de compte réussie", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            mDatabase.child(user.getUid()).setValue(newUser);

                            Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(InscriptionChercheur.this, "Création de compte échouée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
