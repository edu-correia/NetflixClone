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

import com.educorreia.netflixclone.models.Category;
import com.educorreia.netflixclone.models.Movie;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvCategoriesList = findViewById(R.id.rcv_categories_list);

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = new Category();

            List<Movie> movies = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                Movie movie = new Movie();
                movie.setCoverUrl(R.drawable.movie_cover);
                movies.add(movie);
            }

            category.setName("Categoria: " + i);
            category.setMovies(movies);

            categories.add(category);
        }

        CategoriesListAdapter adapter = new CategoriesListAdapter(categories);
        rcvCategoriesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvCategoriesList.setAdapter(adapter);
    }

    private static class CategoryHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;
        RecyclerView rcvMoviesList;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txt_category_name);
            rcvMoviesList = itemView.findViewById(R.id.rcv_movies_list);
        }
    }

    private class CategoriesListAdapter extends RecyclerView.Adapter<CategoryHolder>{
        private final List<Category> categoriesList;

        public CategoriesListAdapter(List<Category> categoriesList) {
            this.categoriesList = categoriesList;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categoriesList.get(position);
            holder.txtCategoryName.setText(category.getName());

            MoviesListAdapter adapter = new MoviesListAdapter(category.getMovies());
            holder.rcvMoviesList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.rcvMoviesList.setAdapter(adapter);
        }

        @Override
        public int getItemCount() {
            return categoriesList.size();
        }
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