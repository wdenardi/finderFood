package com.example.finderfood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.finderfood.R;
import com.example.finderfood.dao.ItensDAO;
import com.example.finderfood.model.UserItens;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText tituloReceita;
    private EditText tipoReceita;
    private EditText descReceita;
    private Button addReceita;
    private Button btn_selected_foto;
    private Uri mSelectedUri;
    private ImageView ImgPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receita);

        tituloReceita = findViewById(R.id.titleRecipe);
        tipoReceita = findViewById(R.id.typeRecipe);
        descReceita = findViewById(R.id.descRecip);
        addReceita = findViewById(R.id.addRecip);
        btn_selected_foto = findViewById(R.id.btn_selected_foto);
        ImgPhoto = findViewById(R.id.img_foto);

        btn_selected_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPhto();
            }
        });

        addReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReceita();
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
            //caminho da foto
            mSelectedUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                ImgPhoto.setImageDrawable(new BitmapDrawable(bitmap));
                //escondendo o botao após a foto ser setada
                btn_selected_foto.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void AddReceita() {

        String eNtituloReceita = tituloReceita.getText().toString();
        String eNtipoReceita = tipoReceita.getText().toString();
        String eNdescReceita = descReceita.getText().toString();

        String uid = FirebaseAuth.getInstance().getUid();
        String url = urlFoto();

        ItensDAO dao = new ItensDAO();
        UserItens userItens = new UserItens();
        userItens.setUuid(uid);
        userItens.setTituloReceita(eNtituloReceita);
        userItens.setTipoReceita(eNtipoReceita);
        userItens.setDescricaoReceita(eNdescReceita);
        userItens.setProfileUrl(url);

        dao.saveItensRecip(userItens);

    }

    private String urlFoto() {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String fotoUrl = uri.toString();
                Log.d("Teste URL Foto", fotoUrl.toString());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage());
                    }
                });

        return urlFoto();
    }

}