package com.example.finderfood.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public final class FirebaseUtil {

    private static FirebaseAuth firebaseAuth;

    private static FirebaseStorage firebaseStorage;

    private static FirebaseFirestore firebaseFirestore;

    private static FirebaseDatabase firebaseDatabase;

    private FirebaseUtil(){
    }

    public static FirebaseAuth getInstanceAuth(){
        if(firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }

    public static FirebaseStorage getInstanceStorage(){
        if(firebaseStorage == null)
            firebaseStorage = FirebaseStorage.getInstance();
        return firebaseStorage;
    }

    public static FirebaseFirestore getInstanceFirestore(){
        if(firebaseFirestore == null)
            firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore;
    }

    public static FirebaseDatabase getInstanceDatabase(){
        if(firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();
        return firebaseDatabase;
    }
}
