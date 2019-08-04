package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class PollQuestionAnswer extends AppCompatActivity implements AdapterView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    ArrayList<String> pollList1;
    ArrayAdapter<String> pollAdapter1;
    ArrayList<String> pollsShown;
    Toolbar toolbar;
    private DrawerLayout drawer;
    TextView userNameTextView;
    SharedPreferences mPreference;
    View headerLayout;
    Secretary guest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_question_answer);
        final ListView listView = (ListView)findViewById(R.id.listView);
        pollList1 = new ArrayList<>();
        pollAdapter1 = new ArrayAdapter<String>(this, R.layout.activity_poll_question_answer_info, R.id.textView1, pollList1);
        listView.setOnItemClickListener(this);



        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        //for Navigation Drawer
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BH4 NITJ");
        Drawable dr=getResources().getDrawable(R.drawable.nitlogo);
        Bitmap bitmap=((BitmapDrawable)dr).getBitmap();
        Drawable drawable=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,100,100,true));
        getSupportActionBar().setLogo(drawable);


        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



//        userNameTextView=(TextView)findViewById(R.id.userNameTextView);
//        userNameTextView.setText("Hello, "+mPreference.getString("name",""));

        pollsShown=new ArrayList<>();
        //Its just taking all poll Ids of all polls that have been submitted by user.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference studentDatabase = database.getReference("student/"+mPreference.getString("key","")+"/pollAnswered");

        final ArrayList<String> pollList=new ArrayList<>();
        studentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot pollSnapshot: dataSnapshot.getChildren()){

                    PollId poll=pollSnapshot.getValue(PollId.class);
                    pollList.add(poll.pollId);


                }

                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Takes in all polls and checks if they have been already submitted by user
        final DatabaseReference studentDatabase1 = database.getReference("pollsquestion");

        //This is for getting id of all polls available
        studentDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot pollSnapshot: dataSnapshot.getChildren()){

                    Poll poll=pollSnapshot.getValue(Poll.class);

                    if(!pollList.contains(poll.pollId)){
                        //Poll has not been taken by user and hence can be shown.
                        //Hence Load it into the poll Activity.
                        //You can send the key to have easy reference in the main Polling activity

                        pollsShown.add(0,pollSnapshot.getKey());

                        pollList1.add(0,"Poll Id: "+poll.pollId+"\nTitle: "+poll.title);
//

                        listView.setAdapter(pollAdapter1);
//


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);

        userNameTextView=(TextView)headerLayout.findViewById(R.id.userNameTextView);
        userNameTextView.setText("Hello, "+mPreference.getString("name",""));

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadMenu=new Intent(getApplicationContext(),testStorage.class);
                startActivity(uploadMenu);
            }
        });
        final ImageView userImage=(ImageView)headerLayout.findViewById(R.id.userImage);
        String completePath = Environment.getExternalStorageDirectory().getPath() + "/BH4NITJ/"+mPreference.getString("rollNumber","")+"/"+mPreference.getString("profileFileName","")+".jpg";

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);


        Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);






    }


    @Override
    protected void onRestart() {
        super.onRestart();

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){


        getMenuInflater().inflate(R.menu.menu_main_mess_menu,menu);
        return true;


    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
//        Log.i("Hello", "You clicked Poll : " + id + " at position : " + position);
        // Then you start a new Activity via Intent.



            Intent intent = new Intent();
            intent.setClass(this, SelectedPoll.class);
            intent.putExtra("key", pollsShown.get(position));
//        intent.putExtra("id", id);
            startActivity(intent);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.settings:
                Intent uploadMenu=new Intent(getApplicationContext(),testStorage.class);
                startActivity(uploadMenu);
                break;
            case R.id.logOut:
                String dst = Environment.getExternalStorageDirectory().getPath()+"/BH4NITJ/"+mPreference.getString("rollNumber","");

                //Deleting a person's Files when he/she logs out.

                File dstfolder = new File(dst);
//                Toast.makeText(getApplicationContext(), "Will Now Create a file", Toast.LENGTH_SHORT).show();
                if (dstfolder.isDirectory()) {
                    File[] children=dstfolder.listFiles();
                    for(File child:children)
                        child.delete();
                    dstfolder.delete();
                }

                Intent loginMenu=new Intent(this,LoginActivity.class);
                startActivity(loginMenu);
                break;
            case R.id.poll:
                Toast.makeText(this,"You are Already At Polls Page.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.messMenu:
                Intent messMenu1=new Intent(this,mainMessMenu.class);
                startActivity(messMenu1);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();


        switch (item.getItemId()){

            case R.id.homeConnection:
                Intent homeMenu=new Intent(this,homepageActivity.class);
                startActivity(homeMenu);
                break;

            case R.id.leavePortal:
                Intent messMenu2=new Intent(this,LeaveRouter.class);
                startActivity(messMenu2);
                break;

            case R.id.wardenCorner:
                Intent uploadMenu=new Intent(this,WardenMessage.class);
                startActivity(uploadMenu);
                break;

            case R.id.messAccount:
                Intent messMenu1=new Intent(this,messAccount.class);
                startActivity(messMenu1);
                break;


            case R.id.hostelStaff:
                Intent hostelStaff=new Intent(this,HostelStaff.class);
                startActivity(hostelStaff);
                break;

            case R.id.developers:
                Intent developers=new Intent(this,Developers.class);
                startActivity(developers);
                break;
            case R.id.hostelCommittees:
                Intent hostelCommittees=new Intent(this,HostelCommittees.class);
                startActivity(hostelCommittees);
                break;
            case R.id.complaintPortal:
                Intent complaint=new Intent(this,ComplaintRouter.class);
                startActivity(complaint);
                break;
            default:
                Toast.makeText(this,"Button Pressed",Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    int backButtonCount=0,fetch=0;

    @Override
    public void onBackPressed() {
        backButtonCount++;
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }
        else if(backButtonCount >= 1)
        {
            backButtonCount=0;
            Intent developers=new Intent(this,homepageActivity.class);
            startActivity(developers);

        }

    }



}




