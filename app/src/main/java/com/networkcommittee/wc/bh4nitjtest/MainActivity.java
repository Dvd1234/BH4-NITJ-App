package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    boolean insertionComplete=false;
    private final int SPLASH_DISPLAY_LENGTH = 500;
    SharedPreferences mPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference studentDatabase = database.getReference("student");

         mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mPreference.edit();

        final Intent[] mainIntent = new Intent[1];

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
//                    if(!mPreference.getString("rollNumber","").equals("")&&!mPreference.getString("password","").equals("")) {

            //Get getBaseContext in getUsernameValue
            if (mPreference.getString("name", "").length() != 0) {
                studentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {

                            Student student = studentSnapshot.getValue(Student.class);
                            if (student.rollNumber.equals(mPreference.getString("rollNumber", "")) && student.password.equals(mPreference.getString("password", ""))) {


                                Toast.makeText(MainActivity.this, "Successfully Logged In as " + mPreference.getString("name", ""), Toast.LENGTH_SHORT).show();
                                mainIntent[0] = new Intent(MainActivity.this, homepageActivity.class);

                                MainActivity.this.startActivity(mainIntent[0]);
                                MainActivity.this.finish();

                                return;
                            }
                        }
                        mainIntent[0] = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(mainIntent[0]);
                        MainActivity.this.finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
                    else {


                        mainIntent[0] = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(mainIntent[0]);
                        MainActivity.this.finish();

                    }
                }
            }, SPLASH_DISPLAY_LENGTH);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

    }
}
