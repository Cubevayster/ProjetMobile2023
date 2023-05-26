package com.example.jobtempo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ModifierChercheur extends AppCompatActivity {
    Button modifier;
    Button confirmer;
    TextView titre;
    EditText nom;
    EditText prenom;
    EditText email;
    EditText adresse;
    EditText date;
    EditText telephone;
    EditText nationalite;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_chercheur);

        titre = findViewById(R.id.titre);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        email = findViewById(R.id.email);
        adresse = findViewById(R.id.adresse);
        date = findViewById(R.id.date);
        telephone = findViewById(R.id.telephone);
        nationalite = findViewById(R.id.nationalite);

        confirmer = findViewById(R.id.confirmer);
        modifier = findViewById(R.id.modifier);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titre.setText("Modifier Profil");
                nom.setText(dataSnapshot.child("nomFamille").getValue(String.class));
                prenom.setText(dataSnapshot.child("prenom").getValue(String.class));
                telephone.setText(dataSnapshot.child("telephone").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                date.setText(dataSnapshot.child("dateNaissance").getValue(String.class));
                adresse.setText(dataSnapshot.child("adresse").getValue(String.class));
                if(dataSnapshot.child("nationalite").getValue(String.class) == null){
                    nationalite.setText("non spécifiée");
                }else{
                    nationalite.setText(dataSnapshot.child("nationalite").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(ModifierChercheur.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NomDeFamille = String.valueOf(nom.getText());
                String Prenom = String.valueOf(prenom.getText());
                String Email = String.valueOf(email.getText());
                String Adresse = String.valueOf(adresse.getText());
                String DateNaissance = String.valueOf(date.getText());
                String Telephone = String.valueOf(telephone.getText());
                String Nationalite = String.valueOf(nationalite.getText());

                boolean notAllConditions = TextUtils.isEmpty(NomDeFamille) || TextUtils.isEmpty(Prenom) || TextUtils.isEmpty(Email)
                        || TextUtils.isEmpty(Adresse) || TextUtils.isEmpty(DateNaissance) || TextUtils.isEmpty(Telephone) || TextUtils.isEmpty(Nationalite);

                if(notAllConditions){
                    Toast.makeText(ModifierChercheur.this, "Veuillez remplir toutes les informations nécessaires", Toast.LENGTH_SHORT).show();
                    return;
                }

                userRef.child("nomFamille").setValue(NomDeFamille);
                userRef.child("prenom").setValue(Prenom);
                userRef.child("email").setValue(Email);
                userRef.child("dateNaissance").setValue(DateNaissance);
                userRef.child("telephone").setValue(Telephone);
                userRef.child("adresse").setValue(Adresse);
                userRef.child("nationalite").setValue(Nationalite);

                Intent intent = new Intent(ModifierChercheur.this, MonCompteChercheur.class);
                startActivity(intent);
                finish();
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //modifier
            }
        });
    }
}
