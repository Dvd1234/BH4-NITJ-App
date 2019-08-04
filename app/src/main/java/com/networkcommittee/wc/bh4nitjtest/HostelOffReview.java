package com.networkcommittee.wc.bh4nitjtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HostelOffReview extends AppCompatActivity {


    ListView reviewComplaintListVIew;
    ArrayAdapter<String> reviewComplaintAdapter;
    ArrayList<String > reviewComplaintArrayList;
    ArrayList<String> keyCollection;
    ArrayList<String> statusCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_off_review);

        final SharedPreferences mPreference= PreferenceManager.getDefaultSharedPreferences(this);

        reviewComplaintArrayList=new ArrayList<String>();
        reviewComplaintListVIew=findViewById(R.id.reviewComplaintList);
        reviewComplaintAdapter=new ArrayAdapter<String>(this, R.layout.bill_list_info, R.id.bill_list_info, reviewComplaintArrayList);

        statusCollection=new ArrayList<>();

        keyCollection=new ArrayList<String>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference receivedComplaint = database.getReference("student/"+mPreference.getString("key","")+"/hostelLeaveSent");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("loading");
        pd.setCancelable(false);
        pd.show();


        receivedComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot complaints:dataSnapshot.getChildren()) {

                    final ComplaintReferance complaint =complaints.getValue(ComplaintReferance.class);

                    DatabaseReference complaintRef=database.getReference("hostelLeave/"+complaint.key);




                    complaintRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            HostelLeaveClass oneComplaint=dataSnapshot.getValue(HostelLeaveClass.class);

                            if(!oneComplaint.status.toString().equals("Closed")) {
                                reviewComplaintArrayList.add(0, "Leave From: " + oneComplaint.fromdate + "\nLeave Upto: " + oneComplaint.getTodate() + "\nStatus: " + oneComplaint.status + "\nDeparture Time:" + oneComplaint.departureTime+"\nPurpose:"+oneComplaint.purpose);

                                keyCollection.add(0,complaint.key);
                                statusCollection.add(0,oneComplaint.status);

                                reviewComplaintListVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        if(!statusCollection.get(position).equals("Off")) {
                                            Intent intent = new Intent();
                                            intent.setClass(getApplicationContext(), SelectedHostelLeave.class);
                                            intent.putExtra("complaintKey", keyCollection.get(position));
                                            startActivity(intent);
                                        }
                                        else
                                            Toast.makeText(getApplicationContext(),"Sorry The Leave Is Already Off.",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                reviewComplaintListVIew.setAdapter(reviewComplaintAdapter);
                            }
                            if(pd.isShowing())
                                pd.dismiss();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                reviewComplaintArrayList.add(0,"No Hostel Leave Request Sent Till Now.");

                reviewComplaintListVIew.setAdapter(reviewComplaintAdapter);
                pd.dismiss();

            }
        });

        if(pd.isShowing())
            pd.dismiss();


    }
}
