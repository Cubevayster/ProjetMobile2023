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

public class MonCompteEntreprise extends AppCompatActivity {
    Button modifier;
    Button retour;
    TextView titre;
    TextView nom;
    TextView service;
    TextView numero;
    TextView telephone;
    TextView email1;
    TextView email2;
    TextView adresse;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mon_compte_entreprise);

        titre = findViewById(R.id.titre);
        nom = findViewById(R.id.nom);
        service = findViewById(R.id.service);
        numero = findViewById(R.id.numero);
        telephone = findViewById(R.id.telephone);
        email1 = findViewById(R.id.email1);
        email2 = findViewById(R.id.email2);
        adresse = findViewById(R.id.adresse);

        auth = FirebaseAuth.getInstance();
        retour = findViewById(R.id.retour);
        modifier = findViewById(R.id.modifier);
        user = auth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titre.setText("Profil");
                nom.setText(dataSnapshot.child("nom").getValue(String.class));
                telephone.setText(dataSnapshot.child("telephone").getValue(String.class));
                email1.setText(dataSnapshot.child("email").getValue(String.class));
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
                    email2.setText(dataSnapshot.child("service").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur d'accès à la base de données
                Toast.makeText(MonCompteEntreprise.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonCompteEntreprise.this, MainConnecteEntreprise.class);
                startActivity(intent);
                finish();
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonCompteEntreprise.this, ModifierEntreprise.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
