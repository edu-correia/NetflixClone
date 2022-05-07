package com.educorreia.netflixclone.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.educorreia.netflixclone.models.Category;
import com.educorreia.netflixclone.models.Movie;
import com.educorreia.netflixclone.models.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {
    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private MovieDetailLoader movieDetailLoader;

    public MovieDetailTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setMovieDetailLoader(MovieDetailLoader loader){
        this.movieDetailLoader = loader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Context context = this.context.get();
        if(context != null)
            dialog = ProgressDialog.show(context, "Carregando...", "", true);
    }

    @Override
    protected MovieDetail doInBackground(String... strings) {
        String url = strings[0];
        MovieDetail movieDetail = null;

        try {
            URL requestUrl = new URL(url);

            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();

            if(responseCode >= 400){
                throw new IOException("Erro na comunicação do servidor!");
            }

            InputStream inputStream = urlConnection.getInputStream();

            BufferedInputStream buffer = new BufferedInputStream(inputStream);

            String jsonAsString = jsonToString(buffer);

            movieDetail = getMovieDetail(new JSONObject(jsonAsString));

            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return movieDetail;
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);

        dialog.dismiss();

        if(movieDetailLoader != null)
            movieDetailLoader.onResult(movieDetail);
    }

    private MovieDetail getMovieDetail(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String title = json.getString("title");
        String description = json.getString("desc");
        String cast = json.getString("cast");
        String coverUrl = json.getString("cover_url");
        Movie movie = new Movie(id, title, coverUrl, description, cast);

        List<Movie> similarMoviesList = new ArrayList<>();
        JSONArray similarMoviesArray = json.getJSONArray("movie");
        for(int  i = 0; i < similarMoviesArray.length(); i++){
            JSONObject movieJsonObj = similarMoviesArray.getJSONObject(i);
            String similarCoverUrl = movieJsonObj.getString("cover_url");
            int similarId = movieJsonObj.getInt("id");

            Movie similarMovie = new Movie();
            similarMovie.setId(similarId);
            similarMovie.setCoverUrl(similarCoverUrl);

            similarMoviesList.add(similarMovie);
        }

        return new MovieDetail(movie, similarMoviesList);
    }

    private String jsonToString(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int readBytes;
        while((readBytes = inputStream.read(bytes)) > 0){
            outputStream.write(bytes, 0, readBytes);
        }

        return outputStream.toString();
    }

    private List<Category> getCategories(JSONObject json) throws JSONException {
        List<Category> categories = new ArrayList<>();

        JSONArray categoryArray = json.getJSONArray("category");

        for (int i  = 0; i < categoryArray.length(); i++) {
            JSONObject category = categoryArray.getJSONObject(i);
            String title = category.getString("title");

            List<Movie> movies = new ArrayList<>();
            JSONArray movieArray = category.getJSONArray("movie");
            for (int j = 0; j < movieArray.length(); j++) {
                JSONObject movie = movieArray.getJSONObject(j);

                int id = movie.getInt("id");
                String coverUrl = movie.getString("cover_url");

                Movie movieObj = new Movie();
                movieObj.setId(id);
                movieObj.setCoverUrl(coverUrl);

                movies.add(movieObj);
            }

            Category categoryObj = new Category();
            categoryObj.setName(title);
            categoryObj.setMovies(movies);

            categories.add(categoryObj);
        }

        return categories;
    }

    public interface MovieDetailLoader{
        void onResult(MovieDetail movieDetail);
    }
}
