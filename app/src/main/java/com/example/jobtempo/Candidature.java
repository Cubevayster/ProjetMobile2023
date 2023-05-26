package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class Candidature extends Activity {

    Button cv_button;
    Button lm_button;
    Button confirmer_button;
    Button retour_button;
    CheckBox cvCheckBox;
    CheckBox lmCheckBox;
    Uri cvUri;
    Uri lmUri;
    String offreId;
    String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidature);

        cv_button = findViewById(R.id.depot_cv_button);
        lm_button = findViewById(R.id.depot_lm_button);
        confirmer_button = findViewById(R.id.confirmer_button);
        retour_button = findViewById(R.id.retour_button);

        // Obtenir l'ID de l'offre à partir de l'intent
        Intent intent = getIntent();
        offreId = intent.getStringExtra("offreId");

        // Obtenir l'ID de l'utilisateur
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        cv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la boîte de dialogue de sélection de fichier
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf"); // Filtre pour les fichiers PDF
                startActivityForResult(intent, 1);
            }
        });

        lm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la boîte de dialogue de sélection de fichier
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 2); // Utilisez un code différent pour la lettre de motivation (ici, 2)
            }
        });

        confirmer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cvUri != null && lmUri != null){
                    // Envoyer le fichier à Firebase Storage

                    cvUploadFileToStorage(cvUri);
                    lmUploadFileToStorage(lmUri);

                    // Envoyer les données personnelles du candidat
                    // LES IDS DU CV ET DE LA LM SONT L'ID DE L'USER EN ATTENDANT
                    createCandidatureEntry(userId, offreId, userId, userId);
                } else {
                    // Aucun CV ou LM sélectionné
                    Toast.makeText(getApplicationContext(), "Veuillez sélectionner un CV et une LM", Toast.LENGTH_SHORT).show();
                }
            }
        });

        retour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainConnecte.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Récupérer l'URI du fichier sélectionné (CV)
            cvUri = data.getData();

            cvCheckBox = findViewById(R.id.cvCheckBox);

            // Rendre la checkbox visible et la cocher
            cvCheckBox.setVisibility(View.VISIBLE);
            cvCheckBox.setChecked(true);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Récupérer l'URI du fichier sélectionné (LM)
            lmUri = data.getData();

            lmCheckBox = findViewById(R.id.lmCheckBox);

            // Rendre la checkbox visible et la cocher
            lmCheckBox.setVisibility(View.VISIBLE);
            lmCheckBox.setChecked(true);
        }
    }

    private void cvUploadFileToStorage(Uri fileUri) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // L'utilisateur n'est pas connecté
            return;
        }
        String userId = currentUser.getUid();

        // Référence à l'emplacement dans Firebase Storage où vous souhaitez stocker les CVs
        StorageReference cvStorageRef = FirebaseStorage.getInstance().getReference().child("cvs");

        // Créer un nom pour le fichier en utiliant l'identifiant de l'utilisateur
        // EN ATTENDANT : le fichier du CV et celui de la LM ont le même nom -> l'identifiant de l'utilisateur
        String fileName = userId;
        //String fileName = UUID.randomUUID().toString();

        // Référence au fichier dans Firebase Storage
        StorageReference cvFileRef = cvStorageRef.child(fileName);

        // Upload du fichier CV
        cvFileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Le fichier a été téléchargé avec succès
                    // Vous pouvez maintenant obtenir l'URL du fichier et l'utiliser comme nécessaire
                    cvFileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String fileUrl = uri.toString();
                        // Ici, vous pouvez enregistrer l'URL du fichier dans votre base de données ou effectuer d'autres opérations
                        Toast.makeText(getApplicationContext(), "Succès du dépot", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Une erreur s'est produite lors de l'upload du fichier
                    // Gérer l'erreur de manière appropriée
                    Toast.makeText(getApplicationContext(), "Erreur lors de l'upload", Toast.LENGTH_SHORT).show();
                });
    }

    private void lmUploadFileToStorage(Uri fileUri){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // L'utilisateur n'est pas connecté
            return;
        }
        String userId = currentUser.getUid();

        // Référence à l'emplacement dans Firebase Storage où vous souhaitez stocker les LMs
        StorageReference lmStorageRef = FirebaseStorage.getInstance().getReference().child("lms");

        // Créer un nom pour le fichier en utiliant l'identifiant de l'utilisateur
        // EN ATTENDANT : le fichier du CV et celui de la LM ont le même nom -> l'identifiant de l'utilisateur
        String fileName = userId;
        //String fileName = UUID.randomUUID().toString();

        // Référence au fichier dans Firebase Storage
        StorageReference lmFileRef = lmStorageRef.child(fileName);

        // Upload du fichier LM
        lmFileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Le fichier a été téléchargé avec succès
                    // Vous pouvez maintenant obtenir l'URL du fichier et l'utiliser comme nécessaire
                    lmFileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String fileUrl = uri.toString();
                        // Ici, vous pouvez enregistrer l'URL du fichier dans votre base de données ou effectuer d'autres opérations
                        Toast.makeText(getApplicationContext(), "Succès du dépot", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Une erreur s'est produite lors de l'upload du fichier
                    // Gérer l'erreur de manière appropriée
                    Toast.makeText(getApplicationContext(), "Erreur lors de l'upload", Toast.LENGTH_SHORT).show();
                });
    }

    private void createCandidatureEntry(String userId, String offreId, String cvFileId, String lmFileId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userId);
        DatabaseReference candidaturesRef = FirebaseDatabase.getInstance("https://jobtempo-2934d-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Candidatures");

        // Générez une nouvelle clé unique pour l'entrée de candidature
        String candidatureId = candidaturesRef.push().getKey();

        // Récupérez les informations de l'utilisateur à partir du nœud "Users"
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // L'utilisateur existe dans la base de données
                    String nomFamille = dataSnapshot.child("nomFamille").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String adresse = dataSnapshot.child("adresse").getValue(String.class);
                    String dateNaissance = dataSnapshot.child("dateNaissance").getValue(String.class);
                    String telephone = dataSnapshot.child("telephone").getValue(String.class);

                    // Créez une instance de la classe CandidatureInfo avec les données nécessaires
                    CandidatureInfo candidatureInfo = new CandidatureInfo(offreId, nomFamille, prenom, email, adresse, dateNaissance, telephone, cvFileId, lmFileId);

                    // Enregistrez les données dans la base de données
                    candidaturesRef.child(candidatureId).setValue(candidatureInfo)
                            .addOnSuccessListener(aVoid -> {
                                // L'entrée de candidature a été créée avec succès
                                Toast.makeText(getApplicationContext(), "Candidature créée avec succès", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Une erreur s'est produite lors de la création de l'entrée de candidature
                                // Gérer l'erreur de manière appropriée
                                Toast.makeText(getApplicationContext(), "Erreur lors de la création de la candidature", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // L'utilisateur n'existe pas dans la base de données
                    Toast.makeText(getApplicationContext(), "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Une erreur s'est produite lors de la récupération des informations de l'utilisateur
                // Gérer l'erreur de manière appropriée
                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des informations de l'utilisateur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
