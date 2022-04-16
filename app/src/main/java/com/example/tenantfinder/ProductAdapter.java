package com.example.tenantfinder;


/*
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
*/


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mCtx;
     ArrayList<Product> productList;

    public ProductAdapter(Context mCtx,ArrayList<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_list,parent,false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.textViewTitle.setText(product.getTitle());
        holder.textViewDesc.setText(product.getShortdesc());
        holder.textViewRating.setText(String.valueOf(product.getRating()));
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));

        Picasso.get()
                .load(product.getCoverPhoto())
                .placeholder(R.drawable.image_gallery)
                .into(holder.coverPhoto);


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public  static  class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView coverPhoto;
        TextView textViewTitle,textViewDesc,textViewPrice;
         EditText textViewRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);


            coverPhoto=itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }
    }
}
