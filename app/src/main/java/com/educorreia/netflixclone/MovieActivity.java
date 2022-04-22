package com.educorreia.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.educorreia.netflixclone.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView rcvSimilarMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.txt_movie_title);
        txtDesc = findViewById(R.id.txt_movie_desc);
        txtCast = findViewById(R.id.txt_movie_cast);
        rcvSimilarMovies = findViewById(R.id.rcv_similar_options);

        final Toolbar tbrMovieActivity = findViewById(R.id.tbr_movie_activity);
        setSupportActionBar(tbrMovieActivity);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
            getSupportActionBar().setTitle(null);
        }

        LayerDrawable shadowsDrawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);

        if(shadowsDrawable != null){
            Drawable newMovieCover = ContextCompat.getDrawable(this, R.drawable.movie_player);
            shadowsDrawable.setDrawableByLayerId(R.id.player_drawable, newMovieCover);
            ((ImageView) findViewById(R.id.img_movie_player)).setImageDrawable(shadowsDrawable);
        }

        List<Movie> movies = new ArrayList<>();
        for (int j = 0; j < 30; j++) {
            Movie movie = new Movie();
            movie.setCoverUrl(R.drawable.movie_cover);
            movies.add(movie);
        }
        MoviesListAdapter adapter = new MoviesListAdapter(movies);
        rcvSimilarMovies.setAdapter(adapter);
        rcvSimilarMovies.setLayoutManager(new GridLayoutManager(this, 3));
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
            return new MovieHolder(getLayoutInflater().inflate(R.layout.similar_movie_item, parent, false));
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