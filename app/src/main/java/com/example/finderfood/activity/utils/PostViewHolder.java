package com.example.finderfood.activity.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finderfood.R;
import com.example.finderfood.model.Receita;
import com.example.finderfood.model.User;
import com.squareup.picasso.Picasso;

public class PostViewHolder extends RecyclerView.ViewHolder  {
    private TextView textViewTitle;
    private ImageView imageView;
    private TextView textViewBody;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewBody = itemView.findViewById(R.id.textViewBody);
        imageView = itemView.findViewById(R.id.img_receita);
    }

    public void setItem(User user, Receita receita){
        textViewTitle.setText(user.getUsername());
        if(receita != null) {
            Picasso.get()
                    .load(receita.getProfileUrl())
                    .into(imageView);
            textViewBody.setText(receita.getDescricaoReceita());
        }
    }
}
