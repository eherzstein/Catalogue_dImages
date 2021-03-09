package com.example.cataloguedimages.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cataloguedimages.Data.ImageRecyclerViewAdapter;
import com.example.cataloguedimages.Model.Image;
import com.example.cataloguedimages.R;
import com.example.cataloguedimages.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Image> imageList;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageList = new ArrayList<>();

        //Chercher les préferences pour le dernière recherche...

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();

        imageList = getImages(search);
        movieRecyclerViewAdapter = new ImageRecyclerViewAdapter(this, imageList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();


    }

    // Recupère les différentes images
    public List<Image> getImages(String searchTerm) {
        imageList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://pixabay.com/api/?key=20527961-daa10618999b3fc3f337f468d&q=" + searchTerm,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray imagesArray = response.getJSONArray("hits");

                    for (int i = 0; i < imagesArray.length(); i++) {

                        JSONObject imageObj = imagesArray.getJSONObject(i);

                        Image image = new Image();
                        image.setPreviewUrl(imageObj.getString("previewURL"));
                        ///Log.d("Movies =: ", movie.getTitle());
                        imageList.add(image);

                    }

                    // pour mettre à jour les résultats de la recherche
                    movieRecyclerViewAdapter.notifyDataSetChanged();


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return imageList;

    }

    //Recherche
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem=menu.findItem(R.id.new_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Écrire le nom à rechercher");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Prefs prefs = new Prefs(MainActivity.this);
                if(!query.isEmpty())
                {
                    prefs.setSearch(query);
                    imageList.clear();
                    getImages(query);

                    //gestion du clavier

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null)
                    {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

}