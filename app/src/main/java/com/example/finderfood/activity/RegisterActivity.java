package com.example.finderfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finderfood.R;
import com.example.finderfood.dao.UserDAO;
import com.example.finderfood.exceptions.ExceptionsClass;
import com.example.finderfood.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEdtUsername;
    private EditText mEditEmail;
    private EditText mEditSenha;
    private Button mBtnEnter;
    private Button mBtnSelectedPhoto;
    private Uri mSelectedUri;
    private ImageView mImgPhoto;

    private UserDAO dao = new UserDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEdtUsername = findViewById(R.id.edit_username);
        mEditEmail = findViewById(R.id.edit_email);
        mEditSenha = findViewById(R.id.edit_password);
        mBtnEnter = findViewById(R.id.btn_enter);
        mBtnSelectedPhoto = findViewById(R.id.btn_selected_photo);
        mImgPhoto = findViewById(R.id.img_photo);

        mBtnSelectedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPhto();
            }
        });

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void selectedPhto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            mSelectedUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                //alternativo ao inves do metodo deprecated
                /*
                Drawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                mImgPhoto.setImageDrawable(drawable);
                 */
                mImgPhoto.setImageDrawable(new BitmapDrawable(bitmap));
                mBtnSelectedPhoto.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createUser() {
        final String nome = mEdtUsername.getText().toString();
        String email = mEditEmail.getText().toString();
        String senha = mEditSenha.getText().toString();

        if (verifyFileds(mSelectedUri, nome,email,senha)) {
            return;
        }

        FirebaseUtil.getInstanceAuth().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Teste - ID", task.getResult().getUser().getUid());
                            dao.createUser(mSelectedUri, nome);
                            Intent intent = new Intent(RegisterActivity.this, LoginEfetuadoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Teste - ERRO", e.getMessage());
                ExceptionsClass.showToastMakeText(RegisterActivity.this, e.getMessage());
            }
        });
    }

    private boolean verifyFileds(Object photo, String... fields) {
        if (fields[0].isEmpty() && fields[1].isEmpty() && fields[2].isEmpty() && fields[3].isEmpty()) {
            ExceptionsClass.emptyFields(this);
            return true;
        } else if (fields[0].isEmpty()) {
            ExceptionsClass.emptyFields(this, "nome");
            return true;
        } else if (fields[1].isEmpty()) {
            ExceptionsClass.emptyFields(this, "e-mail");
            return true;
        } else if(fields[2].isEmpty()){
            ExceptionsClass.emptyFields(this, "senha");
        }
        else if(photo == null){
            ExceptionsClass.emptyFields(this, "foto");
        }
        return false;
    }
}

