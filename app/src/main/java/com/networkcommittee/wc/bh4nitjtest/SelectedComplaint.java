package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectedComplaint extends AppCompatActivity {


    String key;
    Complaint complaint;
    List<String> emailRecipients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_complaint);

        emailRecipients=new ArrayList<>();

        final Intent intent = getIntent();
        key=intent.getStringExtra("complaintKey");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sentComplaint = database.getReference("complaints/"+key);

        sentComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                complaint= dataSnapshot.getValue(Complaint.class);

                TextView complaintText=findViewById(R.id.complaintText);
                complaintText.setText("Complaint ID: "+complaint.complaintId+"\nTitle: "+complaint.title+"\nPresent  Status: "+complaint.status+"\nDetails: "+complaint.details+"\nSender: "+complaint.sender+"\nPrior Comments: "+complaint.comments);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void changeToProcessing(View view){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sentComplaint = database.getReference("complaints/"+key);
        EditText comments=findViewById(R.id.comments);

        if(comments.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "The Comments Can't Be Blank.", Toast.LENGTH_SHORT).show();
            return;
        }



        sentComplaint.child("status").setValue("Processing");

        sentComplaint.child("comments").setValue(comments.getText().toString());

        sendEmail();

    }

    public void changeToClosed(View view){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sentComplaint = database.getReference("complaints/"+key);
        EditText comments=findViewById(R.id.comments);

        if(comments.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "The Comments Can't Be Blank.", Toast.LENGTH_SHORT).show();
            return;
        }




        sentComplaint.child("status").setValue("Closed");

        sentComplaint.child("comments").setValue(comments.getText().toString());

        sendEmail();


    }

    public void sendEmail(){


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sendEmailToSender=database.getReference("student/"+complaint.senderKey);

        sendEmailToSender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student senderOfComplaint=dataSnapshot.getValue(Student.class);

                emailRecipients.add(senderOfComplaint.email);

                new SendMailTask(SelectedComplaint.this).execute("complainttrackingbh4nitj@gmail.com",
                        "Complaintbh4@nitj", emailRecipients, "Complaint Status Changed!", "Dear Sir,<br/>The status of complaint, dated "+complaint.date+" with title: <b>"+complaint.title+"</b>, has been changed. Please have a look by going into the \'<b>Complaint Review</b>\' in Complaint Portal.<br/>Thank You!<br/><br/>With Regards,<br/>Complaint Tracking Portal<br/>Ravi Hostel NITJ");

                Intent secretLogin=new Intent(getApplicationContext(),ComplaintRouter.class);
                startActivity(secretLogin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
