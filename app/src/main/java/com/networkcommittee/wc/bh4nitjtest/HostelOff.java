package com.networkcommittee.wc.bh4nitjtest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HostelOff extends AppCompatActivity {


    SharedPreferences mPreference;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_off);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Checking If tHe Guy is already is in leave.
        final DatabaseReference checkStudentOnLeave = database.getReference("student/" + mPreference.getString("key", "") + "/studentOnLeave");

        checkStudentOnLeave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Quote studentOnLeaveValue=dataSnapshot.getValue(Quote.class);
                if(studentOnLeaveValue.quote.toUpperCase().trim().equals("YES")){

                    AlertDialog.Builder builder;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        builder=new AlertDialog.Builder(getApplicationContext(),android.R.style.Theme_Material_Dialog_Alert);
                    }else {
                        builder=new AlertDialog.Builder(getApplicationContext());
                    }
                    builder.setTitle("You are already on Leave")
                            .setMessage("Please OFF your leave from Hostel Leave Review Section.\nNew Leave Cannot be done till earlier is Off-ed")
                            .setNegativeButton("Go Back?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    closeDialog();
                                    Intent feed=new Intent(getApplicationContext(),LeaveRouter.class);
                                    startActivity(feed);

                                }
                            })
                            .setIcon(R.drawable.ic_error_black_24dp);

                    dialog=builder.create();

                    dialog.setCancelable(false);

                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void submit(View view){


        EditText fromDateText=findViewById(R.id.fromDateText);
        EditText toDateText=findViewById(R.id.toDateText);
        EditText address=findViewById(R.id.address);
        EditText purposeOfGoing=findViewById(R.id.purposeOfGoing);
        EditText phoneNumber=findViewById(R.id.phoneNumber);
        EditText departureTime=findViewById(R.id.departureTime);

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        Date c= Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:MM:SS");
        String formattedDate= dateFormat.format(c);
        String formattedTime=timeFormat.format(c);

        String month=formattedDate.split("/")[1];


        if(departureTime.getText().toString().trim().equals("")||purposeOfGoing.getText().toString().trim().equals("")||fromDateText.getText().toString().trim().equals("")||address.getText().toString().trim().equals("")||phoneNumber.getText().toString().trim().equals("")||toDateText.getText().toString().trim().equals("")){

            Toast.makeText(this,"Please Fill All Fields.",Toast.LENGTH_SHORT).show();
            return;
        }



        HostelLeaveClass hostelLeave=new HostelLeaveClass();
        hostelLeave.setDepartureTime(departureTime.getText().toString());
        hostelLeave.requestSendDate=formattedDate;
        hostelLeave.purpose=purposeOfGoing.getText().toString();
        hostelLeave.month=month;
        hostelLeave.status="On";
        hostelLeave.fromdate=fromDateText.getText().toString();
        hostelLeave.address=address.getText().toString();
        hostelLeave.arivalTime="Not Arrived Yet.";
        hostelLeave.name=mPreference.getString("name","");
        hostelLeave.rollNumber=mPreference.getString("rollNumber","");
        hostelLeave.roomNumber=mPreference.getString("roomNumber","");
        hostelLeave.phoneNumber=phoneNumber.getText().toString();
        hostelLeave.statusOffRequestDate="Not Arrived Yet.";
        hostelLeave.todate=toDateText.getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hostelLeave");
        final String tempkey=myRef.push().getKey();
        myRef.child(tempkey).setValue(hostelLeave);


        //Insert in student Db
        final DatabaseReference complaintSent = database.getReference("student/" + mPreference.getString("key", "") + "/hostelLeaveSent");
        String tempKey2 = complaintSent.push().getKey();

        complaintSent.child(tempKey2).child("key").setValue(tempkey);

        final DatabaseReference studentOnLeave = database.getReference("student/" + mPreference.getString("key", "") + "/studentOnLeave");

        studentOnLeave.setValue("YES");


        Toast.makeText(this,"Leave Sent Successfully.",Toast.LENGTH_SHORT).show();
        Intent feed=new Intent(this,LeaveRouter.class);
        startActivity(feed);


    }

    public  void closeDialog(){
        dialog.dismiss();
    }

}
