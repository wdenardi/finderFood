package com.example.finderfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

                Log.i("Teste", email);
                Log.i("Teste", password);
                if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email e senha devem ser preenchidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                //metodo para logar no Firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Teste - ID", task.getResult().getUser().getUid());
                                    //Intent para o Feed
                                   // Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                                    Intent intent = new Intent(LoginActivity.this, LoginEfetuadoActivity.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Log.w("signInWithEmail:failed", task.getException());
                                    Toast.makeText(LoginActivity.this, "Ocorreu um erro durante a autenticação ,verifque se usuario ou senha estão corretos."
                                            , Toast.LENGTH_LONG).show();
                                    Log.e("Teste", task.getException().getMessage());
                                }
                            }
                        });

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
}