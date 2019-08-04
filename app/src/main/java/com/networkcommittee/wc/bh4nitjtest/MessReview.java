package com.networkcommittee.wc.bh4nitjtest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessReview extends AppCompatActivity {


    ListView reviewComplaintListVIew;
    ArrayAdapter<String> reviewComplaintAdapter;
    ArrayList<String > reviewComplaintArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_review);



        final SharedPreferences mPreference= PreferenceManager.getDefaultSharedPreferences(this);

        reviewComplaintArrayList=new ArrayList<String>();
        reviewComplaintListVIew=findViewById(R.id.reviewComplaintList);
        reviewComplaintAdapter=new ArrayAdapter<String>(this, R.layout.bill_list_info, R.id.bill_list_info, reviewComplaintArrayList);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sentComplaint = database.getReference("student/"+mPreference.getString("key","")+"/messLeaveSent");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("loading");
        pd.setCancelable(false);
        pd.show();


        sentComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot complaints:dataSnapshot.getChildren()) {

                    ComplaintReferance complaint =complaints.getValue(ComplaintReferance.class);

                    DatabaseReference complaintRef=database.getReference("messLeave/"+complaint.key);

                    complaintRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            MessLeaveClass oneComplaint=dataSnapshot.getValue(MessLeaveClass.class);
                            reviewComplaintArrayList.add(0,"Mess Off From: "+oneComplaint.fromdate+"\nOff Diet: "+oneComplaint.getFromdiet()+"\nOff To Date: "+oneComplaint.todate+"\nOff To Diet: "+oneComplaint.todiet+"\nOff Done On: "+oneComplaint.requestSendDate);

                            reviewComplaintListVIew.setAdapter(reviewComplaintAdapter);
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

                reviewComplaintArrayList.add(0,"No Mess Leave Request Sent Till Now.");

                reviewComplaintListVIew.setAdapter(reviewComplaintAdapter);
                pd.dismiss();

            }
        });

        if(pd.isShowing())
            pd.dismiss();

    }

}
