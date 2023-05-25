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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Vérifier si l'utilisateur est un chercheur
                    boolean estChercheur = dataSnapshot.child("isChercheur").getValue(Boolean.class);

                    if (estChercheur) {
                        // Rediriger vers l'activité pour les chercheurs
                        Intent intentChercheur = new Intent(getApplicationContext(), MainConnecte.class);
                        startActivity(intentChercheur);
                    } else {
                        // Rediriger vers l'activité pour les non-chercheurs
                        Intent intentNonChercheur = new Intent(getApplicationContext(), MainConnecteEntreprise.class);
                        startActivity(intentNonChercheur);
                    }

                    // Terminer l'activité de connexion
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gérer l'erreur d'accès à la base de données
                    Toast.makeText(Connexion.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            //Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
            //startActivity(intent);
            //finish();
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

                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userId);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Vérifier si l'utilisateur est un chercheur
                                    boolean estChercheur = dataSnapshot.child("isChercheur").getValue(Boolean.class);

                                    if (estChercheur) {
                                        // Rediriger vers l'activité pour les chercheurs
                                        Intent intentChercheur = new Intent(getApplicationContext(), MainConnecte.class);
                                        startActivity(intentChercheur);
                                    } else {
                                        // Rediriger vers l'activité pour les non-chercheurs
                                        Intent intentNonChercheur = new Intent(getApplicationContext(), MainConnecteEntreprise.class);
                                        startActivity(intentNonChercheur);
                                    }

                                    // Terminer l'activité de connexion
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Gérer l'erreur d'accès à la base de données
                                    Toast.makeText(Connexion.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            //Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
                            //startActivity(intent);
                            //finish();
                        } else {
                            Toast.makeText(Connexion.this, "Connexion échouée, veuillez vérifier votre email et mot de passe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
