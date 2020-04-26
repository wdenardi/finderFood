package com.example.finderfood.exceptions;

import android.content.Context;
import android.widget.Toast;

public final class ExceptionsClass {

    private ExceptionsClass(){
    }

    private static void showToastMakeText(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void emptyFields(Context context, String nomeDoCampo){
        showToastMakeText(context, "O ".concat(nomeDoCampo).concat(" não pode ser vazio."));
    }
}
