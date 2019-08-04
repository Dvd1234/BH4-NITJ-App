package com.networkcommittee.wc.bh4nitjtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;


public class ComplaintPortal extends AppCompatActivity {


    SharedPreferences mPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_portal);

        Spinner dropdown=findViewById(R.id.complaintSpinner);
        String items[]=new String[]{"Select Any One","Mess Related","Sports Related","Network Related","Maintenance Related"};

        ArrayAdapter<String > adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);

        dropdown.setAdapter(adapter);

        mPreference= PreferenceManager.getDefaultSharedPreferences(this);

    }




    public void sendComplaint(View view){



            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("loading");
            pd.show();
            pd.setCancelable(false);

            final SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);

            EditText title = findViewById(R.id.titleText);
            EditText details = findViewById(R.id.detailsText);
            Spinner spin = findViewById(R.id.complaintSpinner);
            final String type = spin.getSelectedItem().toString();

            if (type.equals("Select Any One")) {
                Toast.makeText(this, "Select An Item in Complaint Type.", Toast.LENGTH_LONG).show();
                pd.dismiss();
                return;
            } else if (details.getText().toString().equals("") || details.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Title Cannot be Empty.", Toast.LENGTH_LONG).show();
                pd.dismiss();
                return;
            }

            Random rand = new Random();
            int id = rand.nextInt(12000) + 1000;


            Complaint complaint = new Complaint();
            complaint.setComplaintId(String.valueOf(id));
            complaint.setDetails(details.getText().toString());
            Date today = new Date();
            complaint.setComments("");
            complaint.setType(type);
            //Check THis line Out
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            complaint.setDate(format.format(today));

            complaint.setTitle(title.getText().toString());
            complaint.setSender(mPreference.getString("rollNumber", ""));
            complaint.setStatus("Open");
            complaint.setSenderKey(mPreference.getString("key",""));

            final FirebaseDatabase firebase = FirebaseDatabase.getInstance();

            DatabaseReference complaintRef = firebase.getReference("complaints");

            final String tempKey = complaintRef.push().getKey();

            complaintRef.child(tempKey).setValue(complaint);

            final DatabaseReference complaintSent = firebase.getReference("student/" + mPreference.getString("key", "") + "/complaintSent");
            String tempKey2 = complaintSent.push().getKey();

            complaintSent.child(tempKey2).child("key").setValue(tempKey);


        final List<String> emailRecipients=new ArrayList<>();


            final DatabaseReference secretary = firebase.getReference("secretary");
            secretary.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot secretarySnap : dataSnapshot.getChildren()) {

                        //This would fetch instances of secretary and send particular secretary the complaint.
                        Secretary sec = secretarySnap.getValue(Secretary.class);

                        if(sec.type.equals("Hostel Related")){

                            DatabaseReference complaintReceived = firebase.getReference("student/" + sec.key + "/complaintReceived");
                            String tempKey2 = complaintReceived.push().getKey();

                            complaintReceived.child(tempKey2).child("key").setValue(tempKey);

                            final DatabaseReference hostelSecretaryEmailRetrieval=firebase.getReference("student/"+sec.key);
                            hostelSecretaryEmailRetrieval.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Student hostelSecretary=dataSnapshot.getValue(Student.class);
                                    emailRecipients.add(hostelSecretary.email);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                        if (sec.type.equals(type)) {
                            DatabaseReference complaintReceived = firebase.getReference("student/" + sec.key + "/complaintReceived");
                            String tempKey2 = complaintReceived.push().getKey();

                            complaintReceived.child(tempKey2).child("key").setValue(tempKey);
                            pd.dismiss();

                            final DatabaseReference hostelSecretaryEmailRetrieval=firebase.getReference("student/"+sec.key);
                            hostelSecretaryEmailRetrieval.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Student committeeSecretary=dataSnapshot.getValue(Student.class);
                                    emailRecipients.add(committeeSecretary.email);

                                    new SendMailTask(ComplaintPortal.this).execute("complainttrackingbh4nitj@gmail.com",
                                            "Complaintbh4@nitj", emailRecipients, "Complaint Received", "Dear Sir,<br/>You have received a new Complaint. Please have a look by going into the \'<b>Committee Login</b>\' in Complaint Portal and take necessary actions.<br/>Thank You!<br/><br/>With Regards,<br/>Complaint Tracking Portal<br/>Ravi Hostel NITJ");


                                    Toast.makeText(getApplicationContext(), "Successfully Sent.", Toast.LENGTH_LONG).show();
                                    Intent uploadMenu = new Intent(getApplicationContext(), homepageActivity.class);
                                    startActivity(uploadMenu);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Uploading Failed. Check Intenet", Toast.LENGTH_LONG).show();

                }
            });




    }




}
