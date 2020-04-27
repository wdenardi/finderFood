package com.example.finderfood.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finderfood.model.UserItens;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItensDAO {


    public void saveItensRecip(UserItens usr) {

            //salvando no firebase o usuario
            FirebaseFirestore.getInstance().collection("/receitas")
                    .document(usr.getUuid())
                    .collection(usr.getTipoReceita())
                    .add(usr)

                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste",documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste",e.getMessage());
                        }
                    });
        }
    }

