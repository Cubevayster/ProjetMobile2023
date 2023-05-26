package com.example.jobtempo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Candidature extends Activity {

    Button cv_button;
    Button lm_button;
    Button confirmer_button;
    Button retour_button;
    CheckBox cvCheckBox;
    CheckBox lmCheckBox;
    Uri cvUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidature);

        cv_button = findViewById(R.id.depot_cv_button);
        lm_button = findViewById(R.id.depot_lm_button);
        confirmer_button = findViewById(R.id.confirmer_button);
        retour_button = findViewById(R.id.retour_button);

        cv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la boîte de dialogue de sélection de fichier
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf"); // Filtre pour les fichiers PDF, vous pouvez modifier selon vos besoins
                startActivityForResult(intent, 1);
            }
        });

        confirmer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cvUri != null){
                    // Envoyer le fichier à Firebase Storage
                    uploadFileToStorage(cvUri);
                } else {
                    // Aucun CV sélectionné
                    Toast.makeText(getApplicationContext(), "Veuillez sélectionner un CV", Toast.LENGTH_SHORT).show();
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
            // Récupérer l'URI du fichier sélectionné
            //Uri fileUri = data.getData();
            cvUri = data.getData();

            cvCheckBox = findViewById(R.id.cvCheckBox);
            lmCheckBox = findViewById(R.id.lmCheckBox);

            // Rendre la checkbox visible et la cocher la checkbox
            cvCheckBox.setVisibility(View.VISIBLE);
            cvCheckBox.setChecked(true);
        }
    }

    private void uploadFileToStorage(Uri fileUri) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // L'utilisateur n'est pas connecté
            return;
        }
        String userId = currentUser.getUid();

        // Référence à l'emplacement dans Firebase Storage où vous souhaitez stocker les CVs
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("cvs");

        // Créer un nom pour le fichier en utiliant l'identifiant de l'utilisateur
        String fileName = userId;

        // Référence au fichier dans Firebase Storage
        StorageReference fileRef = storageRef.child(fileName);

        // Upload du fichier
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Le fichier a été téléchargé avec succès
                    // Vous pouvez maintenant obtenir l'URL du fichier et l'utiliser comme nécessaire
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
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
}
