package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class changePassword extends AppCompatActivity {

    String key;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        final Intent intent = getIntent();
        key=intent.getStringExtra("key");
        password=intent.getStringExtra("password");

    }

    public void changePassword(View view){
        EditText newPassword=findViewById(R.id.newPassword);
        String newPasswordValue=newPassword.getText().toString();


        if(newPasswordValue.trim().equals("")){
            Toast.makeText(this, "Your new Password cannot be Blank", Toast.LENGTH_LONG).show();
            return;
        }
        else if(Hashing.md5(newPasswordValue).equals(password)){
            Toast.makeText(this, "Your new Password cannot be same as the Old Password", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
            DatabaseReference passwordField = firebase.getReference("student/" + key+"/password" );


            passwordField.setValue(Hashing.md5(newPasswordValue));


            Toast.makeText(this, "Your Password has been Changed.", Toast.LENGTH_SHORT).show();

            Intent uploadMenu = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(uploadMenu);


        }
        return;
    }

    int backButtonCount=0;
    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            Intent uploadMenu = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(uploadMenu);
            backButtonCount=0;
        }
        else
        {
            Toast.makeText(this, "Press the back button again to go to Login Page.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


}
