package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
         startActivity(new Intent(MainActivity2.this,property_details.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       recyclerView=findViewById(R.id.recyclerView);
       productList= new ArrayList<>();
       adapter=new ProductAdapter(this, productList);
       databaseReference= FirebaseDatabase.getInstance().getReference("property_details");
       recyclerView.setAdapter(adapter);

       databaseReference.addValueEventListener(new ValueEventListener() {
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

           }
       });




    }
}


