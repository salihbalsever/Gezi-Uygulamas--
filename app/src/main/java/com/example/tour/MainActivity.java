package com.example.tour;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;

import static com.example.tour.R.id.action_search;
import static com.example.tour.R.id.imageView;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Nere Gitmeli");
        mRecyclerView = findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager( this));
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://tour-f31eb-default-rtdb.firebaseio.com/");
        mRef = mFirebaseDatabase.getReference("Data");
    }
    private  void  firebaseSearch(String searchText){
        String query= searchText.toLowerCase();
        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(query).endAt(query +"\uf8ff");
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(), model.getImage(),model.getCity());
                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder= super.onCreateViewHolder(parent,viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mDescTv= view.findViewById(R.id.rDescription);
                                ImageView mimageView = view.findViewById(R.id.rImageView);
                                TextView mCityTv = view.findViewById(R.id.rCity);
                                String mTitle = mTitleTv.getText().toString();
                                String mDesc = mDescTv.getText().toString();
                                Drawable mDrawable= mimageView.getDrawable();
                                String mCity= mCityTv.getText().toString();
                                Bitmap mBitmap=((BitmapDrawable)mDrawable).getBitmap();
                                Intent intent;
                                intent = new Intent(view.getContext(),CityDetail.class);
                                ByteArrayOutputStream stream= new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte [] bytes= stream.toByteArray();
                                intent.putExtra("image",bytes);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description", mDesc);
                                intent.putExtra("city", mCity);
                                startActivity(intent);

                            }
                            @Override
                            public void onItemLongClick(View mView, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });
                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(), model.getImage(),model.getCity());
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder= super.onCreateViewHolder(parent,viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mDescTv= view.findViewById(R.id.rDescription);
                                ImageView mimageView = view.findViewById(R.id.rImageView);
                                TextView mCityTv = view.findViewById(R.id.rCity);
                                String mTitle = mTitleTv.getText().toString();
                                String mDesc = mDescTv.getText().toString();
                                Drawable mDrawable= mimageView.getDrawable();
                                String mCity= mCityTv.getText().toString();
                                Bitmap mBitmap=((BitmapDrawable)mDrawable).getBitmap();
                                Intent intent;
                                intent = new Intent(view.getContext(),CityDetail.class);
                                ByteArrayOutputStream stream= new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte [] bytes= stream.toByteArray();
                                intent.putExtra("image",bytes);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description", mDesc);
                                intent.putExtra("city", mCity);
                                startActivity(intent);

                            }
                            @Override
                            public void onItemLongClick(View mView, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });
                        return viewHolder;
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item= menu.findItem(action_search);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.action_settings){
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }
}