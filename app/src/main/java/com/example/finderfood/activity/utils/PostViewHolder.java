package com.example.finderfood.activity.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finderfood.R;
import com.example.finderfood.model.User;

public class PostViewHolder extends RecyclerView.ViewHolder  {
    private TextView textViewTitle;
    private TextView textViewBody;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewBody = itemView.findViewById(R.id.textViewBody);
    }

    public void setItem(User user){
        textViewTitle.setText(user.getUsername());
        textViewBody.setText("Descrição");
    }
}
