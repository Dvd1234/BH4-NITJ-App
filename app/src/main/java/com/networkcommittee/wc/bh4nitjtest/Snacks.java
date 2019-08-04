package com.networkcommittee.wc.bh4nitjtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Snacks extends AppCompatActivity {

    String pdfUrl;
    TextView titleText;
    String uri;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_snacks);

        titleText=findViewById(R.id.titleText);
        final Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        String title=intent.getStringExtra("title");
        titleText.setText(title);

        pd = new ProgressDialog(this);
        pd.setTitle("loading");
        pd.setCancelable(false);
        pd.show();




        final ImageView img=(ImageView)findViewById(R.id.imageView);
        if(uri.equals("")) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            storageReference.getRoot().child("mess").child("extras_list.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for the file



                    Glide.with(getApplicationContext()).load(uri).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Loading Failed.Please Check Your Internet.",Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pd.dismiss();
                            return false;
                        }
                    }).into(img);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                }
            });







        }
        else if((uri.split("\\.")[1]).equals("pdf")){

            String dst = Environment.getExternalStorageDirectory().getPath()+"/BH4NITJ/"+"PDFs/"+uri;

            File dstfile = new File(dst);



            if (dstfile.exists()) {
                Uri path;
                pd.dismiss();
                if(Build.VERSION.SDK_INT>=24) {
                    path=FileProvider.getUriForFile(this,
                            getString(R.string.file_provider_authority),
                            dstfile);
                }
                else
                    path=Uri.fromFile(dstfile);
                Intent intent2=new Intent(Intent.ACTION_VIEW);
                intent2.setDataAndType(path,"application/pdf");
                intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent2.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);

            }
            else {

                //PDF is non existent

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                storageReference.getRoot().child("circulars").child(uri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for the file



                        pdfUrl = uri.toString();
                        ImageSaveTask imageSaveTask = new ImageSaveTask(getApplicationContext());
                        imageSaveTask.execute();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors

                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }
        else{

            //Its an image.
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            storageReference.getRoot().child("circulars").child(uri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for the file

                    Glide.with(getApplicationContext()).load(uri).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pd.dismiss();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pd.dismiss();
                            return false;
                        }
                    }).into(img);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),exception.toString(),Toast.LENGTH_SHORT).show();
                }
            });
//            Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/bh4-nitj.appspot.com/o/uploads%2Fteacher-clipart-transparent-background-2.png?alt=media&token=7b126512-4f8b-47df-be41-6f56efe9e185").into(img);
        }

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(img);
        pAttacher.update();
        //        img.setBackgroundResource(R.drawable.common_full_open_on_phone);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent home=new Intent(getApplicationContext(),homepageActivity.class);
        startActivity(home);
    }

    public class ImageSaveTask extends AsyncTask<String, Void, Void> {
        private Context context;

        public ImageSaveTask(Context context) {
            this.context = context;
        }




        @Override
        protected Void doInBackground(String... params) {

            String dst = Environment.getExternalStorageDirectory().getPath()+"/BH4NITJ/"+"PDFs";

            try {

                File file = Glide.with(context)
                        .load(pdfUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();

//                URL u = new URL(pdfUrl);
//                HttpURLConnection c = (HttpURLConnection) u.openConnection();
//                c.setRequestMethod("GET");
//                c.setDoOutput(true);
//                c.connect();


                File dstfolder = new File(dst);

//                Toast.makeText(getApplicationContext(), "Will Now Create a file", Toast.LENGTH_SHORT).show();
                if (!dstfolder.exists()) {
                    if (dstfolder.mkdirs()) {


                        dst=dst+"/"+uri;
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
                    dst=dst+"/"+uri;
                    File dstFile=new File(dst);

                    dstFile.createNewFile();
                }

                InputStream in = null;
                OutputStream out = null;

                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    out = new BufferedOutputStream(new FileOutputStream(dst));
//                    in = c.getInputStream();

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

                    File dstfile = new File(dst);
                    if (dstfile.exists()) {
                        pd.dismiss();

                        Uri path=Uri.fromFile(dstfile);
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path,"application/pdf");
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
            } catch ( IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public class GenericFileProvider extends FileProvider {}




}
