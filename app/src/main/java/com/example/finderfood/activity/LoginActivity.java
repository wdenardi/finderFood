package com.example.finderfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finderfood.R;
import com.example.finderfood.exceptions.ExceptionsClass;
import com.example.finderfood.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEditEmail;
    private EditText mEditSenha;
    private Button mBtnEnter;
    private TextView mTxtAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditEmail = findViewById(R.id.edit_email);
        mEditSenha = findViewById(R.id.edit_password);
        mBtnEnter = findViewById(R.id.btn_enter);
        mTxtAccount = findViewById(R.id.txt_account);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditEmail.getText().toString();
                String password = mEditSenha.getText().toString();

                login(email, password);

            }
        });

        mTxtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String email, String password) {
        if (verifyFileds(email, password)) {
            return;
        }

        FirebaseUtil.getInstanceAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Intent intent = new Intent(LoginActivity.this, MessagesActivity.class);

                            Intent intent = new Intent(LoginActivity.this, LoginEfetuadoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Verifique seu e-mail e senha.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste - ERRO", e.getMessage());
                    }
                });
    }

    private boolean verifyFileds(String... fields) {
        if (fields[0].isEmpty() && fields[1].isEmpty()) {
            ExceptionsClass.emptyFields(this);
            return true;
        } else if (fields[0].isEmpty()) {
            ExceptionsClass.emptyFields(this, "e-mail");
            return true;
        } else if (fields[1].isEmpty()) {
            ExceptionsClass.emptyFields(this, "senha");
            return true;
        }
        return false;
    }
}