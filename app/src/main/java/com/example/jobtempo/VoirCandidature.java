package com.example.jobtempo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class VoirCandidature extends Activity {

    TextView nomOffre;
    TextView nomUtilisateur;
    TextView prenomUtilisateur;
    TextView emailUtilisateur;
    TextView adresseUtilisateur;
    TextView dobUtilisateur;
    TextView numeroUtilisateur;

    Button dlCvButton;
    Button dlLmButton;
    Button retourButton;

    String offreId;
    CandidatureInfo candidatureInfo;

    String nom;
    String prenom;
    String email;
    String adresse;
    String dob;
    String numero;
    String cvFileId;
    String lmFileId;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voir_candidature);

        nomOffre = findViewById(R.id.nomOffre);
        nomUtilisateur = findViewById(R.id.nomUtilisateurView);
        prenomUtilisateur = findViewById(R.id.prenomUtilisateurView);
        emailUtilisateur = findViewById(R.id.emailUtilisateurView);
        adresseUtilisateur = findViewById(R.id.adresseUtilisateurView);
        dobUtilisateur = findViewById(R.id.dobUtilisateurView);
        numeroUtilisateur = findViewById(R.id.numeroUtilisateurView);
        dlCvButton = findViewById(R.id.voir_cv_button);
        dlLmButton = findViewById(R.id.voir_lm_button);
        retourButton = findViewById(R.id.retour_button);

        // Récupérer l'id de l'offre + l'objet CandidatureInfo depuis l'intent
        Intent intent = getIntent();
        offreId = intent.getStringExtra("offreId");
        candidatureInfo = (CandidatureInfo) intent.getSerializableExtra("candidatureInfo");

        // Récupérer les infos du candidat dans l'objet CandidatureInfo
        nom = candidatureInfo.getNom();
        prenom = candidatureInfo.getPrenom();
        email = candidatureInfo.getEmail();
        adresse = candidatureInfo.getAdresse();
        dob = candidatureInfo.getDateNaissance();
        numero = candidatureInfo.getTelephone();
        cvFileId = candidatureInfo.getCvFileId();
        lmFileId = candidatureInfo.getLmFileId();

        // Afficher les infos récupérées dans les TextView
        nomUtilisateur.setText(nom);
        prenomUtilisateur.setText(prenom);
        emailUtilisateur.setText(email);
        adresseUtilisateur.setText(adresse);
        dobUtilisateur.setText(dob);
        numeroUtilisateur.setText(numero);

        dlCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Créer une référence au fichier CV dans le stockage Firebase
                StorageReference cvRef = FirebaseStorage.getInstance().getReference().child("cvs").child(cvFileId);

                // Télécharger le fichier CV vers un emplacement local
                try {
                    File localFile = File.createTempFile(cvFileId, "pdf");
                    cvRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Le téléchargement est réussi, vous pouvez ouvrir le fichier ici
                                    Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.jobtempo.fileprovider", localFile);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(fileUri, "application/pdf");
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        // Gérer l'erreur si aucune application capable de lire les fichiers PDF n'est disponible
                                        Toast.makeText(getApplicationContext(), "Aucune application disponible pour ouvrir le CV", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Gérer l'erreur de téléchargement du fichier CV
                                    Toast.makeText(getApplicationContext(), "Erreur lors du téléchargement du CV", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer l'erreur de création du fichier local
                    Toast.makeText(getApplicationContext(), "Erreur lors de la création du fichier local", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dlLmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Créer une référence au fichier CV dans le stockage Firebase
                StorageReference cvRef = FirebaseStorage.getInstance().getReference().child("lms").child(lmFileId);

                // Télécharger le fichier CV vers un emplacement local
                try {
                    File localFile = File.createTempFile(lmFileId, "pdf");
                    cvRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Le téléchargement est réussi, vous pouvez ouvrir le fichier ici
                                    Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.jobtempo.fileprovider", localFile);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(fileUri, "application/pdf");
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        // Gérer l'erreur si aucune application capable de lire les fichiers PDF n'est disponible
                                        Toast.makeText(getApplicationContext(), "Aucune application disponible pour ouvrir la LM", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Gérer l'erreur de téléchargement du fichier CV
                                    Toast.makeText(getApplicationContext(), "Erreur lors du téléchargement de la LM", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer l'erreur de création du fichier local
                    Toast.makeText(getApplicationContext(), "Erreur lors de la création du fichier local", Toast.LENGTH_SHORT).show();
                }
            }
        });

        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CandidatureEntreprise.class);
                intent.putExtra("offreId", offreId);
                startActivity(intent);
            }
        });



    }
}
