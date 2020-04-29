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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Group;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

//Classe para carregar o feed de receitas
public class FeedActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        RecyclerView rv = findViewById(R.id.recycler_feed);
        adapter = new GroupAdapter();

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        verifyAuthentication();

        fetchUsers();
    }

    private void fetchUsers() {
        //FirebaseFirestore.getInstance().collection("/users")

        FirebaseFirestore.getInstance().collection("/receitas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Teste", e.getMessage(), e);
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            UserItens useritens = doc.toObject(UserItens.class);
                            adapter.add(new Itemfeed(useritens));
                        }
                    }
                });

    }

    public class Itemfeed extends Item<ViewHolder> {

        private final UserItens userItens;

        public Itemfeed(UserItens userItens) {
            this.userItens = userItens;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            // TextView titulo = viewHolder.itemView.findViewById(R.id.txtTitulo);
            ImageView imagem = viewHolder.itemView.findViewById(R.id.imgFeed);

            Picasso.get()
                    .load(userItens.getProfileUrl())
                    .into(imagem);


        }

        @Override
        public int getLayout() {
            return R.layout.activity_feed;
        }
    }


    //m[etodo para certificar se o usuario esta logado ou nao
    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addReceita:
                // Intent intentAddRceita =new Intent(FeedActivity.this,RegisterReceitasActivity.class);
                // startActivity(intentAddRceita);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
