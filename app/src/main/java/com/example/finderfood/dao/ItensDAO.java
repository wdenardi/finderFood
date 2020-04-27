package com.example.finderfood.dao;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finderfood.model.UserItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ItensDAO {


    public void saveItensRecip(Uri uri, final String... args) {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urlFoto = uri.toString();

                                FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();
                                String uidU = String.valueOf(uid);
                                String uiIdItem = args[0];
                                String titleRecip = args[1];
                                String typeRecip = args[2];
                                String descRecip = args[3];

                                String profileUrl = uri.toString();

                                UserItem userItens = new UserItem(uidU, uiIdItem, titleRecip, typeRecip, descRecip, profileUrl);

                                FirebaseFirestore.getInstance().collection("usrItem")
                                        .document(uiIdItem)
                                        .set(userItens)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("Teste", e.getMessage());
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage(), e);
                    }
                });

    }


}
