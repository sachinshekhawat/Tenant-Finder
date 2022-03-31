package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {


    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    EditText etdisplayName , etphoneNo , etAddress;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        etdisplayName =  (EditText) findViewById(R.id.displayName);
        etphoneNo =  (EditText) findViewById(R.id.phoneNo);
        imageView = (ImageView) findViewById(R.id.imageView3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textViewVerified);
//        etAddress = (EditText)findViewById(R.id.editTextAddress);

        loadUserInformation();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        findViewById(R.id.buttonsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
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
            Glide.with(this)
                    .load(user.getPhotoUrl().toString())
                    .into(imageView);
        }

        if(user.getDisplayName() != null) {
            etdisplayName.setText(user.getDisplayName());
        }

        if(user.getPhoneNumber() != null) {
            etphoneNo.setText(user.getPhoneNumber());
        }
//        if(user.getProviderData() != null) {
//            etAddress.setText((CharSequence) user.getProviderData());
//        }


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
        String phoneNumber = etphoneNo.getText().toString();
//        String address = etAddress.getText().toString();

        if(displayName.isEmpty()){
            etdisplayName.setError("Name required");
            etdisplayName.requestFocus();
            return;
        }
        if(phoneNumber.isEmpty()){
            etphoneNo.setError("Phone number required");
            etphoneNo.requestFocus();
            return;
        } 
//        if(address.isEmpty()){
//            etAddress.setError("Phone number required");
//            etAddress.requestFocus();
//            return;
//        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
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
                            Toast.makeText(ProfileActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case  R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return true;
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"),CHOOSE_IMAGE);
    }
}