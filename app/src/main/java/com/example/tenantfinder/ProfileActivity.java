package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    EditText etdisplayName;
    EditText etemail;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;

    FirebaseAuth mAuth;
    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mAuth = FirebaseAuth.getInstance();
        etdisplayName = findViewById(R.id.displayName);
        etemail = findViewById(R.id.email_profile);
        imageView = findViewById(R.id.imageView3);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textViewVerified);


        loadUserInformation();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,11);
            }
        });

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        findViewById(R.id.back_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,MainActivity3.class));
            }
        });

    }




    @Override
    protected void onStart(){
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void loadUserInformation() {
       final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
        if(user.getPhotoUrl() != null){
           Picasso.get()
                    .load(user.getPhotoUrl().toString())
                    .into(imageView);
        }

        if(user.getDisplayName() != null) {
            etdisplayName.setText(user.getDisplayName());
        }

        if(user.getEmail() != null) {
            etemail.setText(user.getEmail());
        }


        if(user.isEmailVerified()){
            textView.setText("Email Verified");
        }else{
            textView.setText("Email  Not Verified (Click to Verify)");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ProfileActivity.this,"Verification Email Sent",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        }
    }




    private void saveUserInformation() {
        String displayName = etdisplayName.getText().toString();
        String email = etemail.getText().toString();

        if(displayName.isEmpty()){
            etdisplayName.setError("Name required");
            etdisplayName.requestFocus();
            return;
        }
        if(displayName.isEmpty()){
            etemail.setError("Email required");
            etemail.requestFocus();
            return;
        } 


        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(imageView.toString()))
                    .build();




            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriProfileImage = data.getData();
            imageView.setImageURI(uriProfileImage);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+ ".jpg");
        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.getFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    profileImageUrl = taskSnapshot.getTask().toString();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void logout(View view) {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }


}