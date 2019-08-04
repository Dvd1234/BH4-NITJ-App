package com.networkcommittee.wc.bh4nitjtest;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class messLeave extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] items;
    SharedPreferences mPreference;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_leave);

        Spinner dropdown = findViewById(R.id.toDietSpinner);
        dropdown.setOnItemSelectedListener(this);
        Spinner dropdown2 = findViewById(R.id.fromDietSpinner);
        dropdown2.setOnItemSelectedListener(this);
        //get the spinner from the xml.
        //create a list of items for the spinner.
        items= new String[]{"Select","Breakfast", "Lunch", "Dinner"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown2.setAdapter(adapter);

        showDietAlertDialog();

    }


    public void showDietAlertDialog(){

        Date c= Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
        String formattedTime=timeFormat.format(c);


        int hourOfRequestSent=Integer.parseInt(formattedTime.split(":")[0]);

        final int trialVariable;

        if(hourOfRequestSent>=15)
            trialVariable=3;
        else if(hourOfRequestSent>=10)
            trialVariable=2;
        else
            trialVariable=1;



        int daysToAdd=1;

        int minOffDietPos=trialVariable;


        if(minOffDietPos==3){
            daysToAdd++;
        }

        for(int i=0;i<4;i++){
            minOffDietPos++;
            if((minOffDietPos%4)==0){
                minOffDietPos++;
            }
        }

        minOffDietPos=minOffDietPos%4;


        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        final String formattedDate= dateFormat.format(c);


        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,daysToAdd);

        String minOffInfoDate=dateFormat.format(calendar.getTime());


        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            builder=new AlertDialog.Builder(com.networkcommittee.wc.bh4nitjtest.messLeave.this,android.R.style.Theme_Material_Dialog_Alert);
        }else {
            builder=new AlertDialog.Builder(com.networkcommittee.wc.bh4nitjtest.messLeave.this);
        }
        builder.setTitle("Confirmation of Off Date And Diet")
                .setMessage("As of now. Your Diet Can Be Closed from:-\n"+minOffInfoDate+" "+items[minOffDietPos]+"\n\nNote: 'Mess Off' is NOT available from 10PM to 4AM everyday.")
                .setPositiveButton("Proceed Further?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeDialog();

                    }
                })
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void submit(View view) {

        final MessLeaveClass messLeave=new MessLeaveClass();

        TextView fromText=(TextView)findViewById(R.id.fromText);
        //From Date
        messLeave.setFromdate(fromText.getText().toString());


        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        Spinner spinner=(Spinner)findViewById(R.id.fromDietSpinner);
        final int fromDietPos=spinner.getSelectedItemPosition();
        Spinner toDietSpinner=(Spinner)findViewById(R.id.toDietSpinner);
        final int toDietPos=toDietSpinner.getSelectedItemPosition();

        if(toDietPos==0||fromDietPos==0){

            Toast.makeText(this,"Fill Your Fields Correctly..",Toast.LENGTH_SHORT).show();
            return;
        }


        final TextView toText=(TextView)findViewById(R.id.toText);
        if(fromText.getText().toString().equals("")||toText.getText().toString().equals("")||fromText.getText().toString().split("/")[1].equals("")||toText.getText().toString().split("/")[1].equals("")){

            Toast.makeText(this,"Check Date Correctly.",Toast.LENGTH_SHORT).show();
            return;

        }

        Date c= Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
        String formattedTime=timeFormat.format(c);


        int hourOfRequestSent=Integer.parseInt(formattedTime.split(":")[0]);

        final int trialVariable;

        if(hourOfRequestSent<4||hourOfRequestSent>=22){
            Toast.makeText(this,"Sorry This Service Is Unavailable from:\n10PM To 4 AM EveryDay.",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(hourOfRequestSent>=15)
            trialVariable=3;
        else if(hourOfRequestSent>=10)
            trialVariable=2;
        else
            trialVariable=1;


        //insert date roll number roomnumbr in here
        messLeave.roomNumber=mPreference.getString("roomNumber","");
        messLeave.name=mPreference.getString("name","");



        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        final String formattedDate= dateFormat.format(c);


        messLeave.requestSendDiet=items[trialVariable];
        messLeave.setTodate(toText.getText().toString());
        messLeave.setFromdiet(items[fromDietPos]);
        messLeave.setTodiet(items[toDietPos]);
        messLeave.setRequestSendDate(formattedDate);
        messLeave.setRollNumber(mPreference.getString("rollNumber",""));

        //Insert in mess Leave DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messLeave");
        final String tempkey=myRef.push().getKey();
        myRef.child(tempkey).setValue(messLeave);

        //Insert in student Db
        final DatabaseReference complaintSent = database.getReference("student/" + mPreference.getString("key", "") + "/messLeaveSent");
        String tempKey2 = complaintSent.push().getKey();

        complaintSent.child(tempKey2).child("key").setValue(tempkey);

        //You need to redirect here
        Toast.makeText(getApplicationContext(),"Leave Sent Successfully.",Toast.LENGTH_SHORT).show();
        Intent feed=new Intent(getApplicationContext(),LeaveRouter.class);
        startActivity(feed);




    }


    public  void closeDialog(){
        dialog.dismiss();
    }

}
