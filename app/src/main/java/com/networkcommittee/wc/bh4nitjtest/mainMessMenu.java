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
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainMessMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    View headerLayout;
    Toolbar toolbar;
    private DrawerLayout drawer;
    TextView userNameTextView;
    SharedPreferences mPreference;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mess_menu);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //To Set Up The first Loaded page in accordance to the current Day of the week
        SimpleDateFormat sdf=new SimpleDateFormat("EEEE");
        Date d=new Date();
        String dayOfTheWeek=sdf.format(d);
        if( dayOfTheWeek.toLowerCase().equals("monday"))
            mViewPager.setCurrentItem(0);
        else if( dayOfTheWeek.toLowerCase().equals("tuesday"))
            mViewPager.setCurrentItem(1);
        else if( dayOfTheWeek.toLowerCase().equals("wednesday"))
            mViewPager.setCurrentItem(2);
        else if( dayOfTheWeek.toLowerCase().equals("thursday"))
            mViewPager.setCurrentItem(3);
        else if( dayOfTheWeek.toLowerCase().equals("friday"))
            mViewPager.setCurrentItem(4);
        else if( dayOfTheWeek.toLowerCase().equals("saturday"))
            mViewPager.setCurrentItem(5);
        else if( dayOfTheWeek.toLowerCase().equals("sunday"))
            mViewPager.setCurrentItem(6);

        // Set up the tabs for ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        getSupportActionBar().setTitle("BH4 NITJ");
//        getSupportActionBar().setSubtitle("Empowering Since 1988");
        Drawable dr=getResources().getDrawable(R.drawable.nitlogo);
        Bitmap bitmap=((BitmapDrawable)dr).getBitmap();
        Drawable drawable=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,100,100,true));
        getSupportActionBar().setLogo(drawable);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);//changed from setDrawerListener to addDrawerlistener
        toggle.syncState();

//        userNameTextView=(TextView)findViewById(R.id.userNameTextView);
//        userNameTextView.setText("Hello, "+mPreference.getString("name",""));




        //added by me
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mainMessMenu.this, Snacks.class);
                intent.putExtra("title", "");
                intent.putExtra("uri", "");
                startActivity(intent);
            }
        });


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);



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
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_mess_menu, menu);
        return true;
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

                Intent loginMenu=new Intent(mainMessMenu.this,LoginActivity.class);
                startActivity(loginMenu);
                break;
            case R.id.poll:
                Intent messMenu=new Intent(this,PollQuestionAnswer.class);
                startActivity(messMenu);
                break;
            case R.id.messMenu:
                Toast.makeText(this,"You are Already At Mess Menu.",Toast.LENGTH_SHORT).show();
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

            case R.id.wardenCorner:
                Intent uploadMenu=new Intent(this,WardenMessage.class);
                startActivity(uploadMenu);
                break;

            case R.id.leavePortal:
                Intent messMenu2=new Intent(this,LeaveRouter.class);
                startActivity(messMenu2);
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

    // Deleted PlaceholderFragment class from here.

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0 :
                    Monday tab1 = new Monday();
                    return tab1;
                case 1 :
                    Tuesday tab2 = new Tuesday();
                    return tab2;
                case 2 :
                    Wednesday tab3 = new Wednesday();
                    return tab3;
                case 3 :
                    Thursday tab4 = new Thursday();
                    return tab4;
                case 4 :
                    Friday tab5 = new Friday();
                    return tab5;
                case 5 :
                    Saturday tab6 = new Saturday();
                    return tab6;
                case 6 :
                    Sunday tab7 = new Sunday();
                    return tab7;
                default :
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }

        // This function was not written beforehand.
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
                case 0 :
                    return "Mon";
                case 1 :
                    return "Tue";
                case 2 :
                    return "Wed";
                case 3 :
                    return "Thu";
                case 4 :
                    return "Fri";
                case 5 :
                    return "Sat";
                case 6 :
                    return "Sun";
            }
            return null;
        }


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
}
