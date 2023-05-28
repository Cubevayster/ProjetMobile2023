package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifierMDP extends AppCompatActivity {
    Button retour;
    Button confirmer;
    EditText nouveauMDP;
    EditText confirmMDP;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_mdp);

        nouveauMDP = findViewById(R.id.nouveauMdp);
        confirmMDP = findViewById(R.id.confirmationMdp);

        confirmer = findViewById(R.id.confirmer);
        retour = findViewById(R.id.retour);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());
        final boolean[] estChercheur = new boolean[1];

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estChercheur[0] = dataSnapshot.child("isChercheur").getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(ModifierMDP.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nouveauMdp = String.valueOf(nouveauMDP.getText());
                String confirmMdp = String.valueOf(confirmMDP.getText());
                if(!nouveauMdp.equals(confirmMdp)){
                    Toast.makeText(ModifierMDP.this, "Veuillez confirmer correctement votre nouveau mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.updatePassword(confirmMdp)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ModifierMDP.this, "Confirmation du nouveau mot de passe", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ModifierMDP.this, "Erreur lors de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Afficher le message d'erreur ou effectuer des actions supplémentaires pour le débogage
                                Log.e("FirebaseAuth", "Erreur lors de la mise à jour du mot de passe", e);
                            }
                        });

                if(estChercheur[0]) {
                    Intent intent = new Intent(ModifierMDP.this, MonCompteChercheur.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ModifierMDP.this, MonCompteEntreprise.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estChercheur[0]) {
                    Intent intent = new Intent(ModifierMDP.this, MonCompteChercheur.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ModifierMDP.this, MonCompteEntreprise.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
