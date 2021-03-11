package com.example.cataloguedimages.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cataloguedimages.Model.Image;
import com.example.cataloguedimages.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {
    private Image image;
    private TextView tags;
    private TextView views;
    private TextView downloads;
    private TextView likes;
    private TextView comments;
    private TextView type;
    private TextView favourites;
    private TextView user;
    private String imageId;
    private ImageView imageIdDets;
    private RequestQueue queue;
    private TextView credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        queue = Volley.newRequestQueue(this);

        image = (Image) getIntent().getSerializableExtra("image"); // récupérer tous les éléments
        imageId = image.getImageId();

        setUpUI();
        getImageDetails(imageId);

    }
    private void setUpUI() {

        likes = findViewById(R.id.likesDets);
        favourites =findViewById(R.id.favouritesDets);
        comments = findViewById(R.id.commentsDets);
        tags = findViewById(R.id.tags);
        downloads = findViewById(R.id.downloadsDets);
       // user = findViewById(R.id.userDets);
       // type = findViewById(R.id.typeDets);
        views = findViewById(R.id.viewsDets);
        imageIdDets = findViewById(R.id.imageIDDets);
        credit = findViewById(R.id.credit);
    }

    private void getImageDetails(String id) {
        Log.d("id", "the id is "+ id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://pixabay.com/api/?key=20527961-daa10618999b3fc3f337f468d&id=" + id,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                       JSONArray hits = response.getJSONArray("hits");
                       JSONObject object = hits.getJSONObject(0);

                        likes.setText(object.getString("likes"));
                        favourites.setText(object.getString("favorites"));
                        comments.setText(object.getString("comments"));
                        tags.setText(object.getString("tags"));
                      //  downloads.setText("Downloads: " + object.getString("downloads"));
                      // user.setText("Creator: " + object.getString("user"));
                      // type.setText("Type: " + object.getString("type"));
                        views.setText(object.getString("views"));
                        credit.setText(object.getString("type")+" by "+ object.getString("user"));

                        Picasso.get()
                                .load(object.getString("previewURL"))
                                .fit()
                                .into(imageIdDets);
                }catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error:", error.getMessage());

            }
        });
        queue.add(jsonObjectRequest);

    }

}