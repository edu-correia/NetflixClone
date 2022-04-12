package com.educorreia.netflixclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.txt_movie_title);
        txtDesc = findViewById(R.id.txt_movie_desc);
        txtCast = findViewById(R.id.txt_movie_cast);

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
    }
}