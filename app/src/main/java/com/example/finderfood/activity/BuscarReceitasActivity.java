package com.example.finderfood.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.finderfood.R;
import com.example.finderfood.activity.utils.PostViewHolder;
import com.example.finderfood.exceptions.ExceptionsClass;
import com.example.finderfood.model.Receita;
import com.example.finderfood.model.User;
import com.example.finderfood.util.FirebaseUtil;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BuscarReceitasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirestorePagingAdapter<User, PostViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_receitas);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        //listar usuários
        CollectionReference collectionReference = FirebaseUtil.getInstanceFirestore().collection("users");

        Query query = collectionReference;

        //paginação da lista
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(1)
                .setPageSize(1)
                .build();

        //utilizar pra ficar carregando as litas no layout
        FirestorePagingOptions options = new FirestorePagingOptions.Builder<User>()
                .setLifecycleOwner(this)
                .setQuery(query, config, User.class)
                .build();

        adapter = new FirestorePagingAdapter<User, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.item_find_food, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull User user) {
                Receita receita = null;
                if(user.getUuid().equals("VNBaZDIEVHN5H6LfEk6iHEIaUYp1")){
                    receita = getImageRecipes(user);
                }
                holder.setItem(user,receita);
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Log.e("BuscarReceitas", e.getMessage());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state){
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case LOADED:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    case ERROR:
                        ExceptionsClass.showToastMakeText(getApplicationContext(), "Error Occurred!");
                        break;
                    case FINISHED:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        };

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // tentativa de listar receitas dos usuários
    private Receita getImageRecipes(final User user){
        Query query = FirebaseUtil.getInstanceFirestore().collection("receitas")
                .whereEqualTo("uuid", user.getUuid());

        Task<QuerySnapshot> task = query.get();
        List<Receita> receitas = new ArrayList<>();
        if(task.isSuccessful()){
            for(QueryDocumentSnapshot d : task.getResult()){
                Receita receita = d.toObject(Receita.class);
                receitas.add(receita);
            }
        }

        return receitas.get(0);
    }
}
