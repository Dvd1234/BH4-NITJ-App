package com.networkcommittee.wc.bh4nitjtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class testStorage extends AppCompatActivity implements View.OnClickListener /*  implementing click listener */ {
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;


    private boolean imageBackout=false;
    //ImageView
    private ImageView imageView;

    SharedPreferences mPreference;
    private StorageReference storageReference;
    //a Uri object to store file path
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_storage);

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.imageView);



        mPreference= PreferenceManager.getDefaultSharedPreferences(this);


        storageReference = FirebaseStorage.getInstance().getReference();



        //New To load image from the storage SD CARD.
        String completePath = Environment.getExternalStorageDirectory().getPath() + "/BH4NITJ/"+mPreference.getString("rollNumber","")+"/"+mPreference.getString("profileFileName","")+".jpg";

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);

        Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);


        TextView textView=(TextView)findViewById(R.id.name);
        textView.setText(mPreference.getString("name",""));
        final int[] evaluate = {0};

        textView=(TextView)findViewById(R.id.rollNumber);
        textView.setText(mPreference.getString("rollNumber",""));

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);



    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Glide.with(getApplicationContext()).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
//                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }

        //if the clicked button is upload
        else if (view == buttonUpload) {

            uploadFile();
        }
    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {

            final ScrollView constraintLayout=findViewById(R.id.activity_main);
            constraintLayout.setVisibility(View.INVISIBLE);
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference riversRef = storageReference.child("personalClick/"+mPreference.getString("rollNumber","")+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();

                            SharedPreferences.Editor mEditor = mPreference.edit();
                            mEditor.putString("imageChanged","yes");

                            mEditor.commit();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            imageBackout=true;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            ;
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }


    public void checkPassword(View view){

        final EditText oldPassword=(EditText)findViewById(R.id.oldPassword);
        final EditText newPassword=(EditText)findViewById(R.id.newPassword);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference studentDatabase = database.getReference("student/"+mPreference.getString("key",""));


        studentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Student student=dataSnapshot.getValue(Student.class);
                    if(student.password.equals(Hashing.md5(oldPassword.getText().toString()))){

                        if(Hashing.md5(newPassword.getText().toString()).equals(student.password))
                        {
                            Toast.makeText(testStorage.this, "New and Old Password can't be same", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(newPassword.getText().toString().trim().equals("")){

                            Toast.makeText(testStorage.this, "The New Password field Cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DatabaseReference studentDatabase2 = database.getReference("student/"+mPreference.getString("key",""));
                        studentDatabase2.child("password").setValue(Hashing.md5(newPassword.getText().toString()));
                        SharedPreferences.Editor editor=mPreference.edit();
                        editor.putString("password",Hashing.md5(newPassword.getText().toString()));
                        editor.commit();
                        Intent loginMenu=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(loginMenu);
                        return;
                    }

                Toast.makeText(testStorage.this, "The Old Password did Not Match", Toast.LENGTH_SHORT).show();

                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void onBackPressed() {
        if(imageBackout){
            Intent intent = new Intent(this,homepageActivity.class);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }

    }



}
