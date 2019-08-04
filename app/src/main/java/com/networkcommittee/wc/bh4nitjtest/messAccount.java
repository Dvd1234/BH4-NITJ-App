package com.networkcommittee.wc.bh4nitjtest;

import android.app.ProgressDialog;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class messAccount extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    Toolbar toolbar;
    private DrawerLayout drawer;

    TextView textView1;
    TextView balanceTextView;
    TextView textView3;
    ListView listView;
    ListView listView2;
    ArrayList<String> billList;
    ArrayAdapter<String> billAdapter;
    SharedPreferences mPreference;
    TextView userNameTextView;
    View headerLayout;
    NavigationView navigationView;


    ArrayList<String> notificationList;
    ArrayList<Circulars>circularData;
    ArrayAdapter<String> notificationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_account);

        textView1 = (TextView)findViewById(R.id.textView1);
        balanceTextView = (TextView)findViewById(R.id.balanceTextView);
        textView3 = (TextView)findViewById(R.id.textView3);
        listView2 = (ListView)findViewById(R.id.listView2);
        listView = (ListView)findViewById(R.id.listView);

        billList = new ArrayList<>();
        billAdapter = new ArrayAdapter<String>(this, R.layout.bill_list_info, R.id.bill_list_info, billList);



        circularData=new ArrayList<>();
        notificationList=new ArrayList<>();
        notificationAdapter = new ArrayAdapter<String>(this, R.layout.notification_item, R.id.textView, notificationList);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference studentDatabase = database.getReference("messAccount");

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mPreference.edit();

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


        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setTitle("Loading Your Wallet...");
        pd.setCancelable(false);
        pd.show();



        navigationView.setCheckedItem(R.id.messAccount);

        studentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot studentSnapshot: dataSnapshot.getChildren()){

                    MessAccountComputation studentMessAccount=studentSnapshot.getValue(MessAccountComputation.class);
                    if(studentMessAccount.rollNumber.equals(mPreference.getString("rollNumber",""))){


                        int balance=0;

                        if(!studentMessAccount.oddSemesterFee.equals("")){
                            balance=balance+Integer.parseInt(studentMessAccount.oddSemesterFee);
                            billList.add("5th Semester Fee   + \u20B9 "+studentMessAccount.oddSemesterFee);
                        }

                        if(!studentMessAccount.august.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.august);
                            billList.add("July And August   - \u20B9 "+studentMessAccount.august);
                        }
                        if(!studentMessAccount.september.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.september);
                            billList.add("September   - \u20B9 "+studentMessAccount.getSeptember());
                        }
                        if(!studentMessAccount.october.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.october);
                            billList.add("October   - \u20B9 "+studentMessAccount.getOctober());
                        }
                        if(!studentMessAccount.november.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.november);
                            billList.add("November -\u20B9 "+studentMessAccount.getNovember());
                        }
                        if(!studentMessAccount.december.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.december);
                            billList.add("December - \u20B9 "+studentMessAccount.getDecember());
                        }

                        if(!studentMessAccount.evenSemesterFee.equals("")){
                            balance=balance+Integer.parseInt(studentMessAccount.evenSemesterFee);
                            billList.add("6th Semester Fee   + \u20B9 "+studentMessAccount.getEvenSemesterFee());
                        }
                        if(!studentMessAccount.january.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.january);
                            billList.add("January   - \u20B9 "+studentMessAccount.getJanuary());
                        }
                        if(!studentMessAccount.february.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.february);
                            billList.add("February   - \u20B9 "+studentMessAccount.getFebruary());
                        }
                        if(!studentMessAccount.march.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.march);
                            billList.add("March   - \u20B9 "+studentMessAccount.getMarch());
                        }
                        if(!studentMessAccount.april.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.april);
                            billList.add("April   - \u20B9 "+studentMessAccount.getApril());
                        }
                        if(!studentMessAccount.may.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.may);
                            billList.add("May   - \u20B9 "+studentMessAccount.getMay());
                        }
                        if(!studentMessAccount.june.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.june);
                            billList.add("June   - \u20B9 "+studentMessAccount.getJune());
                        }
                        if(!studentMessAccount.july.equals("")){
                            balance -= Integer.parseInt(studentMessAccount.july);
                            billList.add("July   - \u20B9 "+studentMessAccount.getJuly());
                        }


                        balanceTextView.setText("\u20B9 "+balance);
                        listView.setAdapter(billAdapter);

                        pd.dismiss();

                        return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
            }
        });



        final DatabaseReference circularDatabase = database.getReference("messCirculars");

        circularDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot circularSnap:dataSnapshot.getChildren()){
                    Circulars circulars=circularSnap.getValue(Circulars.class);
                    circularData.add(0,circulars);
                    notificationList.add(0,circulars.getTitle());//This shall show recent to last
                    listView2.setAdapter(notificationAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent();
//        intent.setClass(this, SelectedPoll.class);
                intent.setClass(getApplicationContext(), Snacks.class);
                intent.putExtra("uri", circularData.get(position).link);
                intent.putExtra("title", circularData.get(position).title);
//        intent.putExtra("id", id);
                startActivity(intent);


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


        navigationView.setCheckedItem(R.id.messAccount);

        Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        navigationView.setCheckedItem(R.id.messAccount);

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

            case R.id.messAccount:
                Toast.makeText(this,"You are already at mess Account Page.",Toast.LENGTH_SHORT).show();
                break;


            case R.id.hostelStaff:
                Intent hostelStaff=new Intent(this,HostelStaff.class);
                startActivity(hostelStaff);
                break;
            case R.id.leavePortal:
                Intent messMenu2=new Intent(this,LeaveRouter.class);
                startActivity(messMenu2);
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
