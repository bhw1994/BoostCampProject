package com.example.bhw.boostcamp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView title;
        TextView year;
        TextView director;
        TextView actors;
        RatingBar ratingBar;
        ConstraintLayout layout;


        MyViewHolder(View view){
            super(view);
            posterImage=view.findViewById(R.id.movie_poster);
            title= view.findViewById(R.id.movie_title);
            year= view.findViewById(R.id.movie_year);
            director= view.findViewById(R.id.movie_director);
            actors= view.findViewById(R.id.movie_actors);
            ratingBar=view.findViewById(R.id.ratingBar);
            layout=view.findViewById(R.id.row);
        }
    }

    private ArrayList<MovieInfo> list;
    Context applicationContext;
    MyAdapter(Context context, ArrayList<MovieInfo> list)
    {
        this.list = list;
        this.applicationContext=context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(i).getLink()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);

            }
        });

        myViewHolder.title.setText(list.get(i).getTitle());
        myViewHolder.year.setText(list.get(i).getYear()+"");
        myViewHolder.actors.setText(list.get(i).getActors());
        myViewHolder.director.setText(list.get(i).getDirector());
        myViewHolder.ratingBar.setRating(list.get(i).getUserRating());

        Glide.with(applicationContext)
                .load(list.get(i).getImageUrl())
                .into(myViewHolder.posterImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
