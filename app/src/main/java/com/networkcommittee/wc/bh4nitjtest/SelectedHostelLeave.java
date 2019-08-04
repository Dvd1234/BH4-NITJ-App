package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectedHostelLeave extends AppCompatActivity {

    String key;
    HostelLeaveClass hostelLeave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_hostel_leave);

        final Intent intent = getIntent();
        key=intent.getStringExtra("complaintKey");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sentComplaint = database.getReference("hostelLeave/"+key);

        sentComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                hostelLeave= dataSnapshot.getValue(HostelLeaveClass.class);

                TextView complaintText=findViewById(R.id.complaintText);
                complaintText.setText("Leave From: " + hostelLeave.fromdate + "\nLeave Upto: " + hostelLeave.getTodate() + "\nStatus: " + hostelLeave.status + "\nDeparture Time:" + hostelLeave.departureTime+"\nPurpose:"+hostelLeave.purpose);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void changeToOffLeave(View view){


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sentComplaint = database.getReference("hostelLeave/"+key);

        Date c= Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate= dateFormat.format(c);

        sentComplaint.child("status").setValue("Off");
        sentComplaint.child("statusOffRequestDate").setValue(formattedDate);

        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseReference studentOnLeave = database.getReference("student/" + mPreference.getString("key", "") + "/studentOnLeave");
        studentOnLeave.setValue("NO");

        Toast.makeText(getApplicationContext(),"Leave Off Done..",Toast.LENGTH_SHORT).show();

        Intent secretLogin=new Intent(this,LeaveRouter.class);
        startActivity(secretLogin);

    }


}
