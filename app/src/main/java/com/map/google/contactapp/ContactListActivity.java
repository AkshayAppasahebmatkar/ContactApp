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

public class ContactListActivity extends AppCompatActivity {
//long contact_id = 532;
public static ListView contacts_listview;
    Context context;
    public static ContactItemAdapter mAdapter;
    public static List<Contact> allContactsList = new ArrayList<Contact>();
    Animation animMoveToTop;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        context = this.getApplicationContext();
        overridePendingTransition(R.animator.enter_from_right,R.animator.exit_to_left);
        allContactsList = DBAdapter.getAllContactData();
        contacts_listview = (ListView) findViewById(R.id.contacts_listview);

        if(allContactsList.size()!=0) {
            mAdapter = new ContactItemAdapter(this, allContactsList);
            contacts_listview.setAdapter(mAdapter);
        }
        animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.move);
        contacts_listview.startAnimation(animMoveToTop);
        contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
