package com.networkcommittee.wc.bh4nitjtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.http.Url;

public class homepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //implements , AdapterView.OnItemClickListener

//    ViewFlipper viewFlipper;
    ListView listView;
    ListView achievementListView;
    ArrayList<Circulars>circularData;
    ArrayList<Circulars>achievementData;
    ArrayList<String> notificationList;
    ArrayList<String> achievementList;
    ArrayAdapter<String> notificationAdapter;
    ArrayAdapter<String> achievementAdapter;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.slideshow1,R.drawable.slideshow2,R.drawable.slideshow3,R.drawable.slideshow4,R.drawable.slideshow5};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    String imageUrl;
    Secretary guest;

    Toolbar toolbar;
    private DrawerLayout drawer;
    SharedPreferences mPreference;
    TextView userNameTextView;
    View headerLayout;
    Handler handler;
    boolean execute;
    TextView quoteText;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

//        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
//        int images[] = {R.drawable.slideshow1, R.drawable.slideshow2, R.drawable.slideshow3,R.drawable.slideshow4};

        final TextView marquee = (TextView)findViewById(R.id.marquee_scrolling_text);
        marquee.setSelected(true);

        execute=true;

        achievementListView=(ListView)findViewById(R.id.achievementListView);
        achievementList=new ArrayList<>();
        achievementAdapter=new ArrayAdapter<String>(this,R.layout.notification_item,R.id.textView,achievementList);


        listView = (ListView)findViewById(R.id.listView);
        notificationList = new ArrayList<>();
        notificationAdapter = new ArrayAdapter<String>(this, R.layout.notification_item, R.id.textView, notificationList);
//        listView.setOnItemClickListener(this);



        if(Build.VERSION.SDK_INT>=23) {

            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

//            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.SEND_SMS}, 2);
//            }
//
//            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.SEND_SMS}, 2);
//            }
//
//            if (ActivityCompat.checkSelfPermission(homepageActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(homepageActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 3);
//            }


        }



        circularData=new ArrayList<>();
        achievementData=new ArrayList<>();

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BH4 NITJ");
//        getSupportActionBar().setSubtitle("Empowering Since 1988");
        Drawable dr=getResources().getDrawable(R.drawable.nitlogo);
        Bitmap bitmap=((BitmapDrawable)dr).getBitmap();
        Drawable drawable=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,100,100,true));
        getSupportActionBar().setLogo(drawable);

        quoteText=(TextView)findViewById(R.id.quote);




        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference circularDatabase = database.getReference("circulars");

        circularDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot circularSnap:dataSnapshot.getChildren()){
                    Circulars circulars=circularSnap.getValue(Circulars.class);
                    circularData.add(0,circulars);
                    notificationList.add(0,circulars.getTitle());//This shall show recent to last
                    listView.setAdapter(notificationAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    execute = false;
                    Intent intent = new Intent();
//        intent.setClass(this, SelectedPoll.class);
                    intent.setClass(homepageActivity.this, Snacks.class);
                    intent.putExtra("uri", circularData.get(position).link);
                    intent.putExtra("title", circularData.get(position).title);
//        intent.putExtra("id", id);
                    startActivity(intent);


            }
        });






        final DatabaseReference achievementDb = database.getReference("achievements");

        achievementDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot circularSnap:dataSnapshot.getChildren()){
                    Circulars circulars=circularSnap.getValue(Circulars.class);
                    achievementData.add(0,circulars);
                    achievementList.add(0,circulars.getTitle());//This shall show recent to last
                    achievementListView.setAdapter(achievementAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        achievementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    execute = false;
                    Intent intent = new Intent();
//        intent.setClass(this, SelectedPoll.class);
                    intent.setClass(homepageActivity.this, Snacks.class);
                    intent.putExtra("uri", achievementData.get(position).link);
                    intent.putExtra("title", achievementData.get(position).title);

//        intent.putExtra("id", id);
                    startActivity(intent);




            }
        });



        final DatabaseReference quoteDb = database.getReference("quote");

        quoteDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Quote quote=dataSnapshot.getValue(Quote.class);
                quoteText.setText(quote.quote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference marqueeAssign = database.getReference("instantNotification");

        marqueeAssign.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Quote quote=dataSnapshot.getValue(Quote.class);
                marquee.setText(quote.quote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

        String completePath = Environment.getExternalStorageDirectory().getPath() + "/BH4NITJ/"+mPreference.getString("rollNumber","")+"/"+mPreference.getString("profileFileName","")+".jpg";

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);
        final ImageView userImage = (ImageView) headerLayout.findViewById(R.id.userImage);

        if((!file.exists())||mPreference.getString("imageChanged","").equals("yes")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            storageReference.getRoot().child("personalClick/" + mPreference.getString("rollNumber", "") + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for the file

                    Glide.with(getApplicationContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(userImage);

                    imageUrl = uri.toString();



//                MyAsyncTask asyncTask = new MyAsyncTask();
//                asyncTask.execute();

                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.putString("firstTime", "no");
                        editor.commit();
                        ImageSaveTask imageSaveTask = new ImageSaveTask(getApplicationContext());
                        imageSaveTask.execute();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    Toast.makeText(getApplicationContext(), "Loading Profile Pic Failed.", Toast.LENGTH_SHORT).show();
                    userImage.setImageResource(R.drawable.ic_supervisor_account_black_24dp);
                }
            });
        }
        else {
            Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage);

        }
        /*for(int image : images) {
            flipperImages(image);
        }*/

        navigationView.setCheckedItem(R.id.homeConnection);

        init();


    }


    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(homepageActivity.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }

        }, 7000, 7000);



    }





    @Override
    protected void onRestart(){
        super.onRestart();



        navigationView.setCheckedItem(R.id.homeConnection);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();

        execute=true;
        if(mPreference.getString("imageChanged","").equals("yes")) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);


            final ImageView userImage = (ImageView) headerLayout.findViewById(R.id.userImage);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.getRoot().child("personalClick/" + mPreference.getString("rollNumber", "") + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for the file
                    Glide.with(getApplicationContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(userImage);
                    //To Store in the SD CARDS if image is changed.
                    imageUrl=uri.toString();


                    ImageSaveTask imageSaveTask=new ImageSaveTask(getApplicationContext());
                    imageSaveTask.execute();
                    SharedPreferences.Editor mEditor = mPreference.edit();
                    mEditor.putString("imageChanged","no");
                    mEditor.commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    Toast.makeText(getApplicationContext(), "Failed to load New image. Please change your Profile Image.", Toast.LENGTH_LONG).show();
                    userImage.setImageResource(R.drawable.ic_supervisor_account_black_24dp);
                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.homepage_menu,menu);

        return true;


    }

    //FOr Handling Menu Items
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

                Intent loginMenu=new Intent(homepageActivity.this,LoginActivity.class);
                startActivity(loginMenu);
                break;
            case R.id.feedback:
                Intent feed=new Intent(this,FeedbackActivity.class);
                startActivity(feed);
                break;
            case R.id.poll:
                Intent messMenu=new Intent(this,PollQuestionAnswer.class);
                startActivity(messMenu);
                break;
            case R.id.rulesAndRegulations:
                Intent rules=new Intent(this,RulesAndRegulations.class);
                startActivity(rules);
                break;
            case R.id.messMenu:
                Intent messMenu1=new Intent(this,mainMessMenu.class);
                startActivity(messMenu1);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        execute=false;
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
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();

        execute=false;
        switch (item.getItemId()){

            case R.id.homeConnection:
                Toast.makeText(this,"You are Already At Home.",Toast.LENGTH_SHORT).show();
                break;

            case R.id.wardenCorner:
                Intent uploadMenu=new Intent(this,WardenMessage.class);
                startActivity(uploadMenu);
                break;

            case R.id.messAccount:
                Intent messMenu1=new Intent(homepageActivity.this,messAccount.class);
                startActivity(messMenu1);
                break;

            case R.id.hostelCommittees:
                Intent hostelMenu1=new Intent(homepageActivity.this,HostelCommittees.class);
                startActivity(hostelMenu1);
                break;


            case R.id.hostelStaff:
                Intent hostelStaff=new Intent(homepageActivity.this,HostelStaff.class);
                startActivity(hostelStaff);
                break;
            case R.id.developers:
                Intent developers=new Intent(homepageActivity.this,Developers.class);
                startActivity(developers);
                break;
            case R.id.complaintPortal:
                Intent complaint=new Intent(homepageActivity.this,ComplaintRouter.class);
                startActivity(complaint);
                break;
            case R.id.leavePortal:
                Intent messMenu2=new Intent(this,LeaveRouter.class);
                startActivity(messMenu2);
                break;


            default:
                Toast.makeText(homepageActivity.this,"Button Pressed",Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public class ImageSaveTask extends AsyncTask<String, Void, Void> {
        private Context context;

        public ImageSaveTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
//
            SharedPreferences.Editor editor=mPreference.edit();
            editor.putString("imageCount",String.valueOf(Integer.parseInt(mPreference.getString("imageCount",""))+1));
            editor.commit();

            String dst = Environment.getExternalStorageDirectory().getPath()+"/BH4NITJ/"+mPreference.getString("rollNumber","");

            try {
                File file = Glide.with(context)
                        .load(imageUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();

                File dstfolder = new File(dst);

                String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                Toast.makeText(getApplicationContext(), "Will Now Create a file", Toast.LENGTH_SHORT).show();
                if (!dstfolder.exists()) {
                    if (dstfolder.mkdirs()) {


                        dst=dst+"/"+timeStamp+".jpg";
                        File dstFile=new File(dst);
                        boolean success = dstFile.createNewFile();
                        if (!success) {
//                        Toast.makeText(getApplicationContext(), "Reached dst Failure", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    }

                }
                else{
                    //If FIle Exists
                    dst=dst+"/"+timeStamp+".jpg";
                    File dstFile=new File(dst);

                    dstFile.createNewFile();
                }

                SharedPreferences.Editor mEditor = mPreference.edit();
                mEditor.putString("profileFileName",timeStamp);
                mEditor.putString("imageChanged","no");
                mEditor.commit();

                InputStream in = null;
                OutputStream out = null;

                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    out = new BufferedOutputStream(new FileOutputStream(dst));

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



}



