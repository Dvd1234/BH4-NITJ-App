package com.networkcommittee.wc.bh4nitjtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class passwordRecovery extends AppCompatActivity {

    String randomNumber="";
    EditText rollNumber;
    String pass;
    String name;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        rollNumber=findViewById(R.id.rollNumber);
        final EditText emailId=findViewById(R.id.emailId);



        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setTitle("Fetching Details...");
        pd.setCancelable(false);

        TextWatcher mTextFieldWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final TextInputLayout securityCodeBox=(findViewById(R.id.securityCodeBox));
                final TextInputLayout emailBox=(findViewById(R.id.emailBox));

                final Button changePassword=findViewById(R.id.changePassword);
                final Button sendSecurityCode=findViewById(R.id.sendSecurityCode);

                if (s.length() == 8) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference studentDatabase = database.getReference("student");

                    randomNumber="";

                    closeKeyBoard();


                    pd.show();

                    studentDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                Student student = studentSnapshot.getValue(Student.class);

                                String ourKey = studentSnapshot.getKey();
                                if (student.rollNumber.equals(rollNumber.getText().toString())) {

                                    key=ourKey;
                                    name=student.name;
                                    pass=student.password;

                                    emailBox.setVisibility(View.VISIBLE);
                                    sendSecurityCode.setVisibility(View.VISIBLE);

                                    if (pd.isShowing())
                                        pd.dismiss();

                                    emailId.setText(student.email);

                                    emailId.setEnabled(false);



//            MainActivity.this.finish();
                                    return;
                                }
                            }
                            if (pd.isShowing())
                                pd.dismiss();
                            Toast.makeText(passwordRecovery.this, "Wrong Roll Number. Please Check again!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else{
                    if(emailBox.getVisibility()==View.VISIBLE)
                        emailBox.setVisibility(View.INVISIBLE);
                    if(sendSecurityCode.getVisibility()==View.VISIBLE)
                        sendSecurityCode.setVisibility(View.INVISIBLE);
                    if(securityCodeBox.getVisibility()==View.VISIBLE)
                        securityCodeBox.setVisibility(View.INVISIBLE);
                    if(changePassword.getVisibility()==View.VISIBLE)
                        changePassword.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        rollNumber.addTextChangedListener(mTextFieldWatcher);

    }

    public void sendSecurityCode(View view){

        final TextInputLayout securityCodeBox=(findViewById(R.id.securityCodeBox));

        Random random = new Random();
        for (int i = 0; i < 6; i++)
            randomNumber += Integer.toString(random.nextInt(9) + 1);


        final List<String> emailRecipients=new ArrayList<>();

        final EditText emailId=findViewById(R.id.emailId);

        emailRecipients.add(emailId.getText().toString());
        new SendMailTask(passwordRecovery.this).execute("complainttrackingbh4nitj@gmail.com",
                "Complaintbh4@nitj", emailRecipients, "Password Change Security Code Hostel App", "Dear "+name+",<br/>You have requested to change your Password. Your Security Code for Password change is:<br/><center><b>"+randomNumber+"</b></center> <br>Please enter it in the App to change your password.<br/><br/>If not requested by you,please contact the admin.<br/>Thank You!<br/><br/>With Regards,<br/>App Admin<br/>Ravi Hostel NITJ");
        Toast.makeText(passwordRecovery.this, "Security Code Mail has been sent to your registered Mail-Id!", Toast.LENGTH_LONG).show();


        final Button changePassword=findViewById(R.id.changePassword);
        final Button sendSecurityCode=findViewById(R.id.sendSecurityCode);

        if(sendSecurityCode.getVisibility()==View.VISIBLE)
            sendSecurityCode.setVisibility(View.INVISIBLE);
        if(changePassword.getVisibility()==View.INVISIBLE)
            changePassword.setVisibility(View.VISIBLE);
        if(securityCodeBox.getVisibility()==View.INVISIBLE)
            securityCodeBox.setVisibility(View.VISIBLE);


    }

    public void changePassword(View view){

        EditText securityCode=findViewById(R.id.securityCode);
        if(securityCode.getText().toString().equals(randomNumber)){

            Intent uploadMenu = new Intent(getApplicationContext(), changePassword.class);
            uploadMenu.putExtra("password", pass);
            uploadMenu.putExtra("key", key);
            startActivity(uploadMenu);

        }
        else {

            Toast.makeText(passwordRecovery.this, "Wrong Security Code! Please Check Security Code again!", Toast.LENGTH_SHORT).show();

        }

    }

    public void closeKeyBoard(){
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }



}
