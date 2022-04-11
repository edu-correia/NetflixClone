package com.educorreia.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.educorreia.netflixclone.models.Movie;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvMoviesList = findViewById(R.id.rcv_movies_list);

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setCoverUrl(R.drawable.movie_cover);
            movies.add(movie);
        }

        MoviesListAdapter adapter = new MoviesListAdapter(movies);

        rcvMoviesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvMoviesList.setAdapter(adapter);
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imgMovieCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieCover = itemView.findViewById(R.id.img_movie_cover);
        }
    }

    private class MoviesListAdapter extends RecyclerView.Adapter<MovieHolder>{
        private final List<Movie> moviesList;

        public MoviesListAdapter(List<Movie> moviesList) {
            this.moviesList = moviesList;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = moviesList.get(position);
            holder.imgMovieCover.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }
}