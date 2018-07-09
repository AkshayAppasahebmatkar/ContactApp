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

public class DeletedListActivity extends AppCompatActivity {
    public static List<Contact> allDeletedList = new ArrayList<Contact>();
    public static ListView deleted_listview;
    Context context;
    public static DeletedItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_list);
        context = this.getApplicationContext();
        allDeletedList = DBAdapter.getAllDeletedContactList();
        overridePendingTransition(R.animator.enter_from_right,R.animator.exit_to_left);
        deleted_listview = (ListView) findViewById(R.id.deleted_listview);
        mAdapter = new DeletedItemAdapter(getApplicationContext(), allDeletedList);
        deleted_listview.setAdapter(mAdapter);
        @SuppressLint("ResourceType")
        Animation animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.move);
        deleted_listview.startAnimation(animMoveToTop);
        deleted_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
    public void onBackPressed() {
        /*Intent intent = new Intent(DeletedListActivity.this, HomeActivity.class);
        startActivity(intent);*/
        finish();
        return;
    }
}
