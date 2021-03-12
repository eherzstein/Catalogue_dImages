package com.example.cataloguedimages.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cataloguedimages.Activities.DetailsActivity;
import com.example.cataloguedimages.Model.Image;
import com.example.cataloguedimages.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder>
    {
        private Context context;
        private List<Image> imageList;
    public ImageRecyclerViewAdapter(Context context, List<Image> images) {
        this.context=context;
        imageList=images;
    }

        @NonNull
        @Override
        public ImageRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image,parent,false);
        return new ViewHolder(view,context);
    }

        @Override
        public void onBindViewHolder(@NonNull ImageRecyclerViewAdapter.ViewHolder holder, int position) {
        Image image = imageList.get(position);
        String pictureLink =  image.getPreviewUrl();

        Picasso.get()
                .load(pictureLink)
                .fit()
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.picture);
    }

        @Override
        public int getItemCount() {
        return imageList.size();
    }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            ImageView picture;

            public ViewHolder(@NonNull View itemView, final Context ctx)
            {
                super(itemView);
                context=ctx;


                picture = itemView.findViewById(R.id.imageID);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Image image = imageList.get(getAdapterPosition());

                        Intent intent = new Intent(context, DetailsActivity.class);

                        intent.putExtra("image", image);
                        ctx.startActivity(intent);



                    }
                });
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
