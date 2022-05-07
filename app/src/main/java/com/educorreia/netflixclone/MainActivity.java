package com.educorreia.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.educorreia.netflixclone.models.Category;
import com.educorreia.netflixclone.models.Movie;
import com.educorreia.netflixclone.utils.CategoryTask;
import com.educorreia.netflixclone.utils.ImageDownloaderTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private RecyclerView rcvCategoriesList;
    private CategoriesListAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvCategoriesList = findViewById(R.id.rcv_categories_list);

        categoriesAdapter = new CategoriesListAdapter(new ArrayList<Category>());
        rcvCategoriesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvCategoriesList.setAdapter(categoriesAdapter);

        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResult(List<Category> categories) {
        categoriesAdapter.setCategories(categories);
        categoriesAdapter.notifyDataSetChanged();
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
        private List<Category> categoriesList;

        public CategoriesListAdapter(List<Category> categoriesList) {
            this.categoriesList = categoriesList;
        }

        public void setCategories(List<Category> categories) {
            this.categoriesList = categories;
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

        public MovieHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            imgMovieCover = itemView.findViewById(R.id.img_movie_cover);
            itemView.setOnClickListener((view) -> {
                onItemClickListener.onClick(getAdapterPosition());
            });
        }
    }

    private class MoviesListAdapter extends RecyclerView.Adapter<MovieHolder> implements OnItemClickListener{
        private final List<Movie> moviesList;

        public MoviesListAdapter(List<Movie> moviesList) {
            this.moviesList = moviesList;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.movie_item, parent, false);
            return new MovieHolder(view, this);
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

        @Override
        public void onClick(int position) {
            final int id = moviesList.get(position).getId();
            if(id <= 3) {
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }
}