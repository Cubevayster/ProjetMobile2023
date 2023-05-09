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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Connexion extends AppCompatActivity {
    Button inscription;
    Button retour;
    Button connexion;

    FirebaseAuth mAuth;
    EditText AdresseEmail;
    EditText MDP;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);

        inscription = findViewById(R.id.bouton_inscription);
        retour = findViewById(R.id.bouton_retour);
        connexion = findViewById(R.id.bouton_connexion);

        mAuth = FirebaseAuth.getInstance();

        AdresseEmail = findViewById(R.id.email);
        MDP = findViewById(R.id.mdp);

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connexion.this, InscriptionChoix.class);
                startActivity(intent);
                finish();
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connexion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(AdresseEmail.getText());
                String mdp = String.valueOf(MDP.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Connexion.this, "Veuillez mettre votre email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mdp)){
                    Toast.makeText(Connexion.this, "Veuillez mettre votre mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Connexion.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Connexion.this, "Connexion échouée, veuillez vérifier votre email et mot de passe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
