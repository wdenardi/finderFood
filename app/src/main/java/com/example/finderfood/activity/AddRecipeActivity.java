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
import android.widget.Toast;

import com.example.finderfood.R;
import com.example.finderfood.dao.ItensDAO;
import com.example.finderfood.dao.UserDAO;
import com.example.finderfood.exceptions.ExceptionsClass;
import com.example.finderfood.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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

        ItensDAO dao = new ItensDAO();
        //dao.saveItensRecip(tituloReceita,tipoReceita,descReceita,addReceita);

    }

    private void selectedPhto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void AddReceita() {
        String eNtituloReceita = tituloReceita.getText().toString();
        String eNtipoReceita =   tipoReceita.getText().toString();
        String eNdescReceita =   descReceita.getText().toString();

        if (eNtituloReceita == null || eNtituloReceita.isEmpty() || eNtipoReceita == null || eNtipoReceita.isEmpty() || eNdescReceita == null || eNdescReceita.isEmpty()) {
            Toast.makeText(this, "Email e senha devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }







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
                ImgPhoto.setImageDrawable(new BitmapDrawable(bitmap));
                btn_selected_foto.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}






