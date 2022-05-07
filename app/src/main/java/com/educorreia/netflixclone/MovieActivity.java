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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.educorreia.netflixclone.models.Movie;
import com.educorreia.netflixclone.models.MovieDetail;
import com.educorreia.netflixclone.utils.ImageDownloaderTask;
import com.educorreia.netflixclone.utils.MovieDetailTask;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private ImageView imgCover;
    private RecyclerView rcvSimilarMovies;
    MoviesListAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.txt_movie_title);
        txtDesc = findViewById(R.id.txt_movie_desc);
        txtCast = findViewById(R.id.txt_movie_cast);
        rcvSimilarMovies = findViewById(R.id.rcv_similar_options);
        imgCover = findViewById(R.id.img_movie_player);

        final Toolbar tbrMovieActivity = findViewById(R.id.tbr_movie_activity);
        setSupportActionBar(tbrMovieActivity);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
            getSupportActionBar().setTitle(null);
        }

        List<Movie> movies = new ArrayList<>();
        movieAdapter = new MoviesListAdapter(movies);
        rcvSimilarMovies.setAdapter(movieAdapter);
        rcvSimilarMovies.setLayoutManager(new GridLayoutManager(this, 3));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");

            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this);
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        txtTitle.setText(movieDetail.getMovie().getTitle());
        txtDesc.setText(movieDetail.getMovie().getDescription());
        txtCast.setText(movieDetail.getMovie().getCast());

        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(imgCover);
        imageDownloaderTask.setShadowsEnabled(true);
        imageDownloaderTask.execute(movieDetail.getMovie().getCoverUrl());

        movieAdapter.setMovies(movieDetail.getSimilarMovies());
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imgMovieCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieCover = itemView.findViewById(R.id.img_movie_cover);
        }
    }

    private class MoviesListAdapter extends RecyclerView.Adapter<MovieHolder>{
        private List<Movie> moviesList;

        public MoviesListAdapter(List<Movie> moviesList) {
            this.moviesList = moviesList;
        }

        public void setMovies(List<Movie> movies) {
            this.moviesList = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.similar_movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = moviesList.get(position);
            new ImageDownloaderTask(holder.imgMovieCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }
}