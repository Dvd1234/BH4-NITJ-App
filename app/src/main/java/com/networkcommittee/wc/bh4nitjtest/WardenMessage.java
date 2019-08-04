package com.networkcommittee.wc.bh4nitjtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class WardenMessage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    View headerLayout;
    Toolbar toolbar;
    private DrawerLayout drawer;
    TextView userNameTextView;
    SharedPreferences mPreference;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden_message);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the ViewPager with the sections adapter.

        // Set up the tabs for ViewPager


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BH4 NITJ");
        Drawable dr=getResources().getDrawable(R.drawable.nitlogo);
        Bitmap bitmap=((BitmapDrawable)dr).getBitmap();
        Drawable drawable=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,100,100,true));
        getSupportActionBar().setLogo(drawable);

        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//
//        userNameTextView=(TextView)findViewById(R.id.userNameTextView);
//        userNameTextView.setText("Hello, "+mPreference.getString("name",""));



        navigationView.setCheckedItem(R.id.messAccount);


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


        navigationView.setCheckedItem(R.id.wardenCorner);
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
                Intent messMenu=new Intent(this,PollQuestionAnswer.class);
                startActivity(messMenu);
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
    protected void onRestart() {
        super.onRestart();

        navigationView.setCheckedItem(R.id.wardenCorner);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){


        getMenuInflater().inflate(R.menu.menu_main_mess_menu,menu);
        return true;


    }

    int backButtonCount=0,fetch=0;
    public void onBackPressed() {
        backButtonCount++;
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }
        else if(backButtonCount >= 1)
        {
            backButtonCount=0;
            super.onBackPressed();

        }

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
                Toast.makeText(this,"You are already at Warden\'s Corner.",Toast.LENGTH_SHORT).show();

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
}
