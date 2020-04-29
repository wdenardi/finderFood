package com.example.finderfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finderfood.R;
import com.example.finderfood.model.User;
import com.example.finderfood.model.UserItens;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private String caminhoFoto;
    private String descricaoReceita;
    private String tituloReceita;
    private String tipoReceita;

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

        //Log.i("Teste UUID", uuid);
        //Log.i("Teste username", username);
        //Log.i("Teste urlPhoto", url);

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


    private void createReceita(final User user) {
        
        tituloReceita = mEdtNomeReceita.getText().toString();
        tipoReceita = mEditTipoReceita.getText().toString();
        descricaoReceita = mEditDescricaoReceita.getText().toString();

        //validando se usuario ou senha nao estáo vazios
        if (tituloReceita == null || tituloReceita.isEmpty() || tipoReceita == null || tipoReceita.isEmpty() || descricaoReceita == null || descricaoReceita.isEmpty()) {
            Toast.makeText(this, "Dados precisam ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        String filename = UUID.randomUUID().toString();
        //salvando a foto no firestore
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(mSelectedUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                caminhoFoto = uri.toString();

                                Log.i("Teste - ID", FirebaseAuth.getInstance().getUid());
                                Log.i("Teste - Username", user.getUsername().toString());
                                Log.i("Teste - LINK DA FOTO", caminhoFoto);


                                //Criando o objeto
                                UserItens userItens = new UserItens();
                                userItens.setUuid(user.getUuid());
                                userItens.setTituloReceita(tituloReceita);
                                userItens.setTipoReceita(tipoReceita);
                                userItens.setDescricaoReceita(descricaoReceita);
                                userItens.setProfileUrl(caminhoFoto);

                                salvarReceita(userItens);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                });



        Toast toast = Toast.makeText(RegisterReceitasActivity.this, "Receita cadastrada com sucesso!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        //Direcionado para a tela inicial
        Intent intentVaiParaTelaInicial = new Intent(RegisterReceitasActivity.this, LoginEfetuadoActivity.class);
        intentVaiParaTelaInicial.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentVaiParaTelaInicial);

    }

    private void salvarReceita(UserItens user) {
        //salvando no firebase a receita
        FirebaseFirestore.getInstance().collection("/receitas")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Teste", documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage());
                    }
                });
    }

    private void verificaAutenticacao() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(RegisterReceitasActivity.this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                //Deslogando o usuario
                FirebaseAuth.getInstance().signOut();
                verificaAutenticacao();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}