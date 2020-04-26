package com.example.finderfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class LoginEfetuadoActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_efetuado);

        RecyclerView rv = findViewById(R.id.recycler);

        verificaAutenticacao();

        fetchUsers();

    }

    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Teste", e.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {

                            User user = doc.toObject(User.class);
                            //comparando a id dos usuarios
                            if (user.getUuid().equals(FirebaseAuth.getInstance().getUid())) {
                                //Toast.makeText(LoginEfetuadoActivity.this, "Id - Iguais " + user.getUuid().toString(), Toast.LENGTH_LONG).show();
                                String url = user.getProfileUrl().toString();
                                preencheImageView(user);
                            }
                        }
                    }
                });
    }

    private void preencheImageView(User user) {

        ImageView imgPhoto = findViewById(R.id.imgf);
        Picasso.get()
                .load(user.getProfileUrl())
                .into(imgPhoto);


    }

    private void verificaAutenticacao() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(LoginEfetuadoActivity.this, LoginActivity.class);

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
