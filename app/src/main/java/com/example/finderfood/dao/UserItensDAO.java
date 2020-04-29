package com.example.finderfood.dao;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finderfood.model.User;
import com.example.finderfood.util.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;

public class UserItensDAO {

    public void createUser(Uri uri, final String... args){
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseUtil.getInstanceStorage().getReference("/images/" + filename);
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urlFoto = uri.toString();
                                Log.i("Teste - LINK DA FOTO", urlFoto);

                                String uid = FirebaseAuth.getInstance().getUid();
                                String username = args[0];
                                String profileUrl = uri.toString();

                                User user = new User(uid, username, profileUrl);

                                FirebaseUtil.getInstanceFirestore().collection("users")
                                        .document(uid)
                                        .set(user)
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

    public List<User> pagenatedListUser(){
        return null;
    }

    public List<User> listAll(){
        return null;
    }

    public User findUser(){
        return null;
    }

    public User updateUser(){
        return null;
    }

}
