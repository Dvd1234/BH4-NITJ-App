package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }


    public void submitFeedback(View view){


        EditText feedback=findViewById(R.id.feedbackText);
        final FirebaseDatabase firebase=FirebaseDatabase.getInstance();
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseReference complaintRef=firebase.getReference("feedback");

        Feedback feedBack=new Feedback();
        feedBack.feedback=feedback.getText().toString();
        feedBack.sender=mPreference.getString("rollNumber","");

        final String tempKey = complaintRef.push().getKey();



        complaintRef.child(tempKey).child("feedback").setValue(feedBack);

        Intent secretLogin=new Intent(this,homepageActivity.class);
        startActivity(secretLogin);


    }



}
