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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifierEntreprise extends AppCompatActivity {
    Button modifier;
    Button confirmer;
    TextView titre;
    EditText nom;
    EditText service;
    EditText email;
    EditText adresse;
    EditText email2;
    EditText telephone;
    EditText numero;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_entreprise);

        titre = findViewById(R.id.titre);
        nom = findViewById(R.id.nom);
        service = findViewById(R.id.service);
        email = findViewById(R.id.email);
        adresse = findViewById(R.id.adresse);
        email2 = findViewById(R.id.email2);
        telephone = findViewById(R.id.telephone);
        numero = findViewById(R.id.numero);

        confirmer = findViewById(R.id.confirmer);
        modifier = findViewById(R.id.modifier);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titre.setText("Modifier Profil");
                nom.setText(dataSnapshot.child("nom").getValue(String.class));
                telephone.setText(dataSnapshot.child("telephone").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                adresse.setText(dataSnapshot.child("adresse").getValue(String.class));
                numero.setText(dataSnapshot.child("numéro").getValue(String.class));
                if(dataSnapshot.child("service").getValue(String.class) == null){
                    service.setText("non spécifiée");
                }else{
                    service.setText(dataSnapshot.child("service").getValue(String.class));
                }
                if(dataSnapshot.child("email2").getValue(String.class) == null){
                    email2.setText("non spécifiée");
                }else{
                    email2.setText(dataSnapshot.child("email2").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(ModifierEntreprise.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Nom = String.valueOf(nom.getText());
                String Service = String.valueOf(service.getText());
                String Email = String.valueOf(email.getText());
                String Adresse = String.valueOf(adresse.getText());
                String Email2 = String.valueOf(email2.getText());
                String Telephone = String.valueOf(telephone.getText());
                String Numero = String.valueOf(numero.getText());

                boolean notAllConditions = TextUtils.isEmpty(Nom) || TextUtils.isEmpty(Email)
                        || TextUtils.isEmpty(Adresse) || TextUtils.isEmpty(Telephone) || TextUtils.isEmpty(Numero);

                if(notAllConditions){
                    Toast.makeText(ModifierEntreprise.this, "Veuillez remplir toutes les informations nécessaires", Toast.LENGTH_SHORT).show();
                    return;
                }

                userRef.child("nom").setValue(Nom);
                userRef.child("service").setValue(Service);
                userRef.child("email").setValue(Email);
                userRef.child("email2").setValue(Email2);
                userRef.child("telephone").setValue(Telephone);
                userRef.child("adresse").setValue(Adresse);
                userRef.child("numéro").setValue(Numero);

                Intent intent = new Intent(ModifierEntreprise.this, MonCompteEntreprise.class);
                startActivity(intent);
                finish();
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifierEntreprise.this, ModifierMDP.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
