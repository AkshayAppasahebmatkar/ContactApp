package com.map.google.contactapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavouritesListActivity extends AppCompatActivity {
    public static List<Contact> allFavouritesList = new ArrayList<Contact>();
    public static ListView favourites_listview;
    Context context;
    public static FavouritesItemAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);
        context = this.getApplicationContext();
        overridePendingTransition(R.animator.enter_from_right,R.animator.exit_to_left);
        allFavouritesList = DBAdapter.getAllFavouriteContactList();
        mAdapter = new FavouritesItemAdapter(getApplicationContext(), allFavouritesList);
        favourites_listview = (ListView) findViewById(R.id.favourites_listview);
        favourites_listview.setAdapter(mAdapter);
        @SuppressLint("ResourceType")
        Animation animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.move);
        favourites_listview.startAnimation(animMoveToTop);
        favourites_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
    public void onBackPressed() {

        finish();
        return;
    }
}
