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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finderfood.activity.ChatActivity;
import com.example.finderfood.activity.FindRecipeActivity;
import com.example.finderfood.activity.LoginActivity;
import com.example.finderfood.activity.RegisterActivity;
import com.example.finderfood.model.User;
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

//Tela após o usuario se logar
public class LoginEfetuadoActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private User user;
    private User usertemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_efetuado);

        Button receitasSalvas = findViewById(R.id.btnReceitasSalvas);
        Button adicionarReceitas = findViewById(R.id.btnAdicionarReceitas);
        Button buscarReceitas = findViewById(R.id.btnBuscarReceitas);
        Button chat = findViewById(R.id.btnChat);

        verificaAutenticacao();

        fetchUsers();

        receitasSalvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(LoginEfetuadoActivity.this,"Botao Receitas Salvas clicado",Toast.LENGTH_LONG).show();
                Intent intentBuscaReceitas = new Intent(LoginEfetuadoActivity.this, ReceitasSalvasActivity.class);
                startActivity(intentBuscaReceitas);
            }
        });
        adicionarReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(LoginEfetuadoActivity.this,"Botao Adicionar Receitas clicado",Toast.LENGTH_LONG).show();

                Intent intentAdicionarReceitas = new Intent(LoginEfetuadoActivity.this, RegisterActivity.class);
                intentAdicionarReceitas.putExtra("user", usertemp);
                startActivity(intentAdicionarReceitas);

            }
        });
        buscarReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginEfetuadoActivity.this,"Botao BuscarReceitas clicado",Toast.LENGTH_LONG).show();
                Intent intentBuscaReceitas = new Intent(LoginEfetuadoActivity.this, FindRecipeActivity.class);
                startActivity(intentBuscaReceitas);

            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginEfetuadoActivity.this,"Botao Chat clicado",Toast.LENGTH_LONG).show();
                Intent intentChat = new Intent(LoginEfetuadoActivity.this, ChatActivity.class);
                startActivity(intentChat);

            }
        });

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

                            //transformando o documento em uma objeto User
                            user = doc.toObject(User.class);
                            //comparando a id dos usuarios
                            if (user.getUuid().equals(FirebaseAuth.getInstance().getUid())) {
                                //Toast.makeText(LoginEfetuadoActivity.this, "Id - Iguais " + user.getUuid().toString(), Toast.LENGTH_LONG).show();
                                //String url = user.getProfileUrl().toString();
                                usertemp = user;
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
