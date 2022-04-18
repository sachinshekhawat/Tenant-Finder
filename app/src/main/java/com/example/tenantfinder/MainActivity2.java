package com.example.tenantfinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {


    //a list to store all the products
     ArrayList<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;
    ProductAdapter adapter;
    DatabaseReference databaseReference;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
         startActivity(new Intent(MainActivity2.this,MainActivity3.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


       recyclerView=findViewById(R.id.recyclerView);
       productList= new ArrayList<>();
       adapter=new ProductAdapter(this, productList);
       databaseReference= FirebaseDatabase.getInstance().getReference("Property Available");
       recyclerView.setAdapter(adapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));




       databaseReference.addValueEventListener(new ValueEventListener() {
           @SuppressLint("NotifyDataSetChanged")
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                   Product product=dataSnapshot.getValue(Product.class);
                   productList.add(product);
               }
               adapter.notifyDataSetChanged();
           }


           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.d("HHHHHH",error.getMessage());
           }
       });




    }
}


