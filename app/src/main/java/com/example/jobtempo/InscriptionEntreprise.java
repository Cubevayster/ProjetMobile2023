package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jobtempo.EntrepriseInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InscriptionEntreprise extends AppCompatActivity {
    Button valider;
    Button retour;
    boolean estChercheur;

    EditText Nom;
    EditText Numero;
    EditText AdresseEmail;
    EditText AdressePostale;
    EditText Telephone;
    EditText MDP;
    FirebaseAuth mAuth;
    FirebaseDatabase linkData = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference mDatabase = linkData.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_entreprise);

        mAuth = FirebaseAuth.getInstance();

        valider = findViewById(R.id.bouton_valider);
        retour = findViewById(R.id.bouton_retour);
        estChercheur = getIntent().getExtras().getBoolean("IsChercheur");

        Nom = findViewById(R.id.nom_entreprise);
        Numero = findViewById(R.id.numero);
        AdresseEmail = findViewById(R.id.email);
        AdressePostale = findViewById(R.id.adresse);
        Telephone = findViewById(R.id.telephone);
        MDP = findViewById(R.id.mdp);

        //retour = findViewById(R.id.bouton_retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionEntreprise.this, InscriptionChoix.class);
                startActivity(intent);
                finish();
            }
        });

        valider = findViewById(R.id.bouton_valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomEntreprise = String.valueOf(Nom.getText());
                String numero = String.valueOf(Numero.getText());
                String email = String.valueOf(AdresseEmail.getText());
                String adresse = String.valueOf(AdressePostale.getText());
                String telephone = String.valueOf(Telephone.getText());
                String mdp = String.valueOf(MDP.getText());

                boolean notAllConditions = TextUtils.isEmpty(nomEntreprise) || TextUtils.isEmpty(numero) || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(adresse) || TextUtils.isEmpty(telephone) || TextUtils.isEmpty(mdp);

                if(notAllConditions){
                    Toast.makeText(InscriptionEntreprise.this, "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
                    return;
                }

                EntrepriseInfo newUser = new EntrepriseInfo(estChercheur, nomEntreprise, numero, email, adresse, telephone);

                mAuth.createUserWithEmailAndPassword(email, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(InscriptionEntreprise.this, "Création de compte réussie", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            mDatabase.child(user.getUid()).setValue(newUser);

                            Intent intent = new Intent(getApplicationContext(), MainConnecteEntreprise.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(InscriptionEntreprise.this, "Création de compte échouée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
