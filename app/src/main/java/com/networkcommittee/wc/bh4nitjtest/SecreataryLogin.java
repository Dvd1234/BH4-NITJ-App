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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SecreataryLogin extends AppCompatActivity {


    ListView reviewComplaintListVIew;
    ArrayAdapter<String> reviewComplaintAdapter;
    ArrayList<String > reviewComplaintArrayList;
    ArrayList<String> keyCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secreatary_login);


        final SharedPreferences mPreference= PreferenceManager.getDefaultSharedPreferences(this);

        reviewComplaintArrayList=new ArrayList<String>();
        reviewComplaintListVIew=findViewById(R.id.secretaryList);
        reviewComplaintAdapter=new ArrayAdapter<String>(this, R.layout.bill_list_info, R.id.bill_list_info, reviewComplaintArrayList);

        keyCollection=new ArrayList<String>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference receivedComplaint = database.getReference("student/"+mPreference.getString("key","")+"/complaintReceived");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("loading");
        pd.setCancelable(false);
        pd.show();


        receivedComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot complaints:dataSnapshot.getChildren()) {

                    final ComplaintReferance complaint =complaints.getValue(ComplaintReferance.class);

                    DatabaseReference complaintRef=database.getReference("complaints/"+complaint.key);




                    complaintRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Complaint oneComplaint=dataSnapshot.getValue(Complaint.class);

                            if(!oneComplaint.status.toString().equals("Closed")) {
                                reviewComplaintArrayList.add(0, "Complaint ID: " + oneComplaint.complaintId + "\nPresent Status: " + oneComplaint.getStatus() + "\nTitle: " + oneComplaint.title + "\nType:" + oneComplaint.type);

                                keyCollection.add(0,complaint.key);


                                reviewComplaintListVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent intent = new Intent();
                                        intent.setClass(getApplicationContext(), SelectedComplaint.class);
                                        intent.putExtra("complaintKey", keyCollection.get(position));
                                        startActivity(intent);

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

                reviewComplaintArrayList.add(0,"No Complaint Received till Now.");

                reviewComplaintListVIew.setAdapter(reviewComplaintAdapter);
                pd.dismiss();

            }
        });

        if(pd.isShowing())
            pd.dismiss();



    }


}
