package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SelectedPoll extends AppCompatActivity {

    private RecyclerView optionsRecyclerView;
    private TextView textView1;
    private TextView textView2;
    String pollId;
    PollRecyclerViewAdapter recyclerViewAdapter;
    SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_poll);


        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);

        // Coming from the 'PollQuestionAnswer' class when we select a poll.
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference studentDatabase = database.getReference("pollsquestion/"+key);

        optionsRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);


        final Poll[] poll = new Poll[1];
        studentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    poll[0] =dataSnapshot.getValue(Poll.class);
                    pollId=poll[0].pollId;
                    textView1.setText(poll[0].title);
                    textView2.setText("Q. "+poll[0].question);


                    LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getApplicationContext());
                    optionsRecyclerView.setLayoutManager(recyclerLayoutManager);

                    DividerItemDecoration dividerItemDecoration =
                            new DividerItemDecoration(optionsRecyclerView.getContext(),
                                    recyclerLayoutManager.getOrientation());
                    optionsRecyclerView.addItemDecoration(dividerItemDecoration);


                    recyclerViewAdapter= new
                            PollRecyclerViewAdapter(getOptions(poll[0]),getApplication());
                    optionsRecyclerView.setAdapter(recyclerViewAdapter);

                    return;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    private List<String> getOptions(Poll poll){
        List<String> optionsList = new ArrayList<>();
        //Have Various Options in here:
        if(!poll.getOption1().equals("")) {
            optionsList.add(poll.getOption1());
        }
        if(!poll.getOption2().equals("")) {
            optionsList.add(poll.getOption2());
        }
        if(!poll.getOption3().equals("")) {
            optionsList.add(poll.getOption3());
        }
        if(!poll.getOption4().equals("")) {
            optionsList.add(poll.getOption4());
        }
        return optionsList;
    }


    public void submit(View view){
    //Submit Mechanism




        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        final int value=recyclerViewAdapter.selectedOption;
        int priorValue=recyclerViewAdapter.lastSelectedPosition;
        if(priorValue==-1){

            Toast.makeText(this,"Please Select An Option",Toast.LENGTH_SHORT).show();
            return;

        }


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference studentDatabase = database.getReference("pollResult");

        final boolean[] studentInsert = {false};
        final boolean[] pollResultInsert = {false};

        studentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot poll:dataSnapshot.getChildren()) {

                    String outKey=poll.getKey();
                    Poll pollItem=poll.getValue(Poll.class);
                    if(pollItem.pollId.equals(pollId)) {

                        int result=0;
                        if(value==0)
                        result=Integer.parseInt(pollItem.getOption1());
                        else if(value ==1)
                        result=Integer.parseInt(pollItem.getOption2());
                        else if(value==2)
                        result=Integer.parseInt(pollItem.getOption3());
                        else
                        result=Integer.parseInt(pollItem.getOption4());

                        if(!pollResultInsert[0]) {
                            final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                            final DatabaseReference studentDatabase1 = database1.getReference("pollResult/" + outKey);
                            studentDatabase1.child("option" + String.valueOf(value + 1)).setValue(String.valueOf(result + 1));
                            pollResultInsert[0] =true;
                        }


                        //Setting inside Student the poll he has answered.
                        if (!studentInsert[0]) {

                            final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                            final DatabaseReference studentDatabase2 = database2.getReference("student/" + mPreference.getString("key", "") + "/pollAnswered");

                            String tempKey = studentDatabase2.push().getKey();
                            studentDatabase2.child(tempKey).child("pollId").setValue(pollId);
                            studentInsert[0] =true;
                        }
                        Toast.makeText(getApplicationContext(),"Your Response is Recorded.Thanks You.",Toast.LENGTH_LONG).show();

                        Intent pollIntent=new Intent(SelectedPoll.this,PollQuestionAnswer.class);
                        startActivity(pollIntent);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });

    }


    public void cancel(View view){
        //Cancel Mechanism
        Intent pollIntent=new Intent(SelectedPoll.this,PollQuestionAnswer.class);
        startActivity(pollIntent);
    }


    
}
