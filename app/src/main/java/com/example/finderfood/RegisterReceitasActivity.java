package com.example.finderfood;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

//Classe utilizada para registrar a receita do usuario
public class RegisterReceitasActivity extends AppCompatActivity {

    private EditText mEdtNomeReceita;
    private EditText mEditTipoReceita;
    private EditText mEditDescricaoReceita;
    private Button mBtnCadastrarReceita;
    private Button mBtnSelectedPhoto;
    private Uri mSelectedUri;
    private ImageView mImgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_receitas);

        mEdtNomeReceita = findViewById(R.id.edit_nome_receita);
        mEditTipoReceita = findViewById(R.id.edit_tipo_receita);
        mEditDescricaoReceita = findViewById(R.id.edit_descricao_receita);
        mBtnCadastrarReceita = findViewById(R.id.btn_cadastrar_receita);
        mBtnSelectedPhoto = findViewById(R.id.btn_selected_photo);
        mImgPhoto = findViewById(R.id.img_foto_receita);

        //capturando o usuario vindo da tela inicial
        final User user = getIntent().getExtras().getParcelable("user");
        String uuid = user.getUuid();
        String username = user.getUsername();
        String url = user.getProfileUrl();

        Log.i("Teste UUID", uuid);
        Log.i("Teste username", username);
        Log.i("Teste urlPhoto", url);

        mBtnSelectedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPhto();
            }
        });

        mBtnCadastrarReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReceita(user);
            }
        });
    }

    //metodo para selecionar a foto
    private void selectedPhto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            //caminho da foto
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
                //escondendo o botao após a foto ser setada
                mBtnSelectedPhoto.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void createReceita(User user) {

        Toast.makeText(this,"Botao adicionar receita clicado",Toast.LENGTH_LONG).show();

        String tituloReceita = mEdtNomeReceita.getText().toString();
        String tipoReceita = mEditTipoReceita.getText().toString();
        String descricaoReceita = mEditDescricaoReceita.getText().toString();

        //validando se usuario ou senha nao estáo vazios
        if (tituloReceita == null || tituloReceita.isEmpty() || tipoReceita == null || tipoReceita.isEmpty() || descricaoReceita == null || descricaoReceita.isEmpty()) {
            Toast.makeText(this, "Dados precisam ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        UserItens userItens = new UserItens();
        userItens.setUuid(user.getUuid());
        userItens.setTituloReceita(tituloReceita);
        userItens.setTipoReceita(tipoReceita);
        userItens.setDescricaoReceita(descricaoReceita);

        //salvando no firebase a receita
        FirebaseFirestore.getInstance().collection("/receitas")
                .document(user.getUuid())
                .collection(tipoReceita)
                .add(userItens)
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

