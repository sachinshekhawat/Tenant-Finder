package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class property_details extends AppCompatActivity {


    EditText etemailp,etnamep,etratep,etaddressp;
    Button submitp,previewp;
    DatabaseReference databaseUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        etemailp=findViewById(R.id.etpricep);
        etnamep=findViewById(R.id.etnamep);
        etratep=findViewById(R.id.etratep);
        etaddressp=findViewById(R.id.etaddressp);
        submitp=findViewById(R.id.submitp);
        previewp=findViewById(R.id.previewp);
        databaseUsers=FirebaseDatabase.getInstance().getReference();

        previewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(property_details.this,MainActivity2.class));
                finish();
            }
        });



        submitp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addprop();
            }


        });


    }
    private void addprop() {
        String  name= etnamep.getText().toString();
        String  adrs= etaddressp.getText().toString();
        String  rating= etratep.getText().toString();
        String  email= etemailp.getText().toString();
        String id =databaseUsers.push().getKey();

        Product product=new Product(name,adrs,rating,email);
        databaseUsers.child("Property Available").child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(property_details.this, "successfully added", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}