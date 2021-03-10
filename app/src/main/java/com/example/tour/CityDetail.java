package com.example.tour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import  android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CityDetail extends AppCompatActivity {
    TextView mTitleTv,mDetailTv,mCity;
    ImageView mImageIv;
    Bitmap bitmap;
    Button mSaveBtn, mShareBtn, mWallBtn;


    private static  final int WRITE_EXTERNAL_STORAGE_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("Mekan Detayları");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTitleTv= findViewById(R.id.titleTv);
        mDetailTv = findViewById(R.id.description);
        mImageIv= findViewById(R.id.imageView);
        mCity= findViewById(R.id.city);
        mSaveBtn= findViewById(R.id.saveBtn);
        mShareBtn= findViewById(R.id.shareBtn);
        mWallBtn= findViewById(R.id.wallBtn);


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();

            }
        });

        byte[] bytes= getIntent().getByteArrayExtra("image");
        String title= getIntent().getStringExtra("title");
        String desc= getIntent().getStringExtra("description");
        String city= getIntent().getStringExtra("city");

        Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        mTitleTv.setText(title);
        mDetailTv.setText(desc);
        mImageIv.setImageBitmap(bmp);
        mCity.setText(city);
        bitmap = ((BitmapDrawable)mImageIv.getDrawable()).getBitmap();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permisson={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permisson,WRITE_EXTERNAL_STORAGE_CODE);
                    }
                    else{
                        saveImage();
                    }
                }
                else{
                    saveImage();
                }
            }


        });
        mWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImgWallpaper();
            }
        });

    }

    private void setImgWallpaper() {
        WallpaperManager wallpaperManager= WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this,"duvarkağıdı değiştiriliyor...",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void shareImage() {
        try {
            String s = mTitleTv.getText().toString()+"\n"+mDetailTv.getText().toString();
            File file= new File(getExternalCacheDir(),"sample.png");
            FileOutputStream fout= new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.flush();
            file.setReadable(true,false);
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, s);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent,"ile paylaş"));


        }catch (Exception e){
Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImage() {

String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        Locale.getDefault()).format(System.currentTimeMillis());
File path= Environment.getDownloadCacheDirectory();
File dir= new File(path+"Firebase/");
dir.mkdirs();
String imageName= timeStamp+".PNG";
File file=  new File(dir, imageName);
        OutputStream out;
        try {
            out= new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();
            Toast.makeText(this,imageName+"kaydedildi"+dir,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode){
        case WRITE_EXTERNAL_STORAGE_CODE:{
            if (grantResults.length > 0 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
            else{
                Toast.makeText(this, "İzin verildi",Toast.LENGTH_SHORT).show();
            }

        }
    }

    }
}