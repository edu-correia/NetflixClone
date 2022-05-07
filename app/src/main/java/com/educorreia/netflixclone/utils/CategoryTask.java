package com.educorreia.netflixclone.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.educorreia.netflixclone.models.Category;
import com.educorreia.netflixclone.models.Movie;

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

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {
    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setCategoryLoader(CategoryLoader loader){
        this.categoryLoader = loader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Context context = this.context.get();
        if(context != null)
            dialog = ProgressDialog.show(context, "Carregando...", "", true);
    }

    @Override
    protected List<Category> doInBackground(String... strings) {
        String url = strings[0];
        List<Category> categories = null;

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

            categories = getCategories(new JSONObject(jsonAsString));

            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);

        dialog.dismiss();

        if(categoryLoader != null)
            categoryLoader.onResult(categories);
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

    public interface CategoryLoader{
        void onResult(List<Category> categories);
    }
}
