package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class PropertyActivity extends AppCompatActivity {



    EditText etpricep,etprop,etratep,etaddressp;
    Button submitp,previewp;
    ImageView imageviewp;


    DatabaseReference databaseUsers;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    Uri uri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        etpricep=findViewById(R.id.etpricep);
        etprop=findViewById(R.id.etprop);
        etratep=findViewById(R.id.etratep);
        etaddressp=findViewById(R.id.etaddressp);
        progressBar=findViewById(R.id.progressBar2);
        submitp=findViewById(R.id.submitp);
        previewp=findViewById(R.id.previewp);
        imageviewp=findViewById(R.id.imageViewp);


        databaseUsers=FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        databaseUsers.child("Property Available").child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                Product product = snapshot.getValue(Product.class);
                    assert product != null;
                    Picasso.get()
                            .load(product.getCoverPhoto())
                            .placeholder(R.drawable.image_gallery)
                            .into(imageviewp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        previewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PropertyActivity.this,MainActivity2.class));
                finish();
            }
        });

        imageviewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,11);
            }
        });


        submitp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addprop();
            }
        });

        findViewById(R.id.back_property).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PropertyActivity.this,MainActivity3.class));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            uri = data.getData();
            imageviewp.setImageURI(uri);

            final StorageReference reference = storage.getReference().child("cover_photo")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PropertyActivity.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void addprop() {
        String  name= etprop.getText().toString();
        String  adrs= etaddressp.getText().toString();
        String  rating= etratep.getText().toString();
        String  price= etpricep.getText().toString();
        String id =databaseUsers.push().getKey();

        if(name.isEmpty()){
            etprop.setError("Property Details required");
            etprop.requestFocus();
            return;}

        if(adrs.isEmpty()){
            etaddressp.setError("Address required");
            etaddressp.requestFocus();
            return;}

        if(rating.isEmpty()){
            etratep.setError("Rating necessary");
            etratep.requestFocus();
            return;}

        if(price.isEmpty()){
            etpricep.setError(" Price required");
            etpricep.requestFocus();
            return;}


        Product product=new Product(name,adrs,rating,price,uri.toString());

        databaseUsers.child("Property Available").child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PropertyActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


}