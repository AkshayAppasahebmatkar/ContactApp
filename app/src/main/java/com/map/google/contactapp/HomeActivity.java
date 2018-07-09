package com.map.google.contactapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    File mainFolder,dBFolder;
    boolean success,successDBFolder;
    Button contacts,favourite,deleted;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;
    Bitmap bmp;
    ImageView image;
    public static List<Contact> contactList = new ArrayList<Contact>();
    public static List<Contact> favouritesList = new ArrayList<Contact>();
    public static List<Contact> deletedList = new ArrayList<Contact>();
    byte[] data;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int READEXTERNAL_STORAGE_PERMISSION_CONSTANT = 101;
    private static final int READ_CONTACTS_PERMISSION_CONSTANT = 102;
    private static final int WRITE_CONTACTS_PERMISSION_CONSTANT = 103;
    private static final int SEND_SMS_PERMISSION_CONSTANT = 104;
    private static final int CALL_PHONE_PERMISSION_CONSTANT = 104;
    Animation animMoveToTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Method for M Permision access
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AccessPermision();
        }else {
            proceedAfterPermission();
        }
    }

    private void AccessPermision() {

        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READEXTERNAL_STORAGE_PERMISSION_CONSTANT);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_CONSTANT);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS}, WRITE_CONTACTS_PERMISSION_CONSTANT);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_CONSTANT);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_CONSTANT);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            }
        }
    }

    @SuppressLint("ResourceType")
    private void proceedAfterPermission() {
        //Create Contact folder
        mainFolder = new File(
                Environment.getExternalStorageDirectory() + File.separator + "/CONTACT");
        success = true;
        if (!mainFolder.exists()) {
            success = mainFolder.mkdir();
        }
        //Create DB folder
        dBFolder = new File(
                Environment.getExternalStorageDirectory() + File.separator + "/CONTACT/DB");
        successDBFolder = true;
        if (!dBFolder.exists()) {
            successDBFolder = dBFolder.mkdir();
            DBAdapter.init(this);
        } else {
            DBAdapter.init(this);
        }

        contacts = (Button) findViewById(R.id.contact);
        animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.enter_from_left);
        contacts.startAnimation(animMoveToTop);
        contacts.setOnClickListener(this);
        favourite = (Button) findViewById(R.id.favourite);
        animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.enter_from_right);
        favourite.startAnimation(animMoveToTop);
        favourite.setOnClickListener(this);
        deleted = (Button) findViewById(R.id.deleted);
        animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.enter_from_left);
        deleted.startAnimation(animMoveToTop);
        deleted.setOnClickListener(this);
        //Method for Add contacts in sqlite database
        contactList = DBAdapter.getAllContactData();
        /*Cursor cursor =  managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        int count = cursor.getCount();*/
        if(contactList.size()==0) {
            new SaveDataAsyncTask(HomeActivity.this,new SaveDataAsyncTask.OnContactFetchListener(){
                public void onContactFetch(List contacts){
                }
            }).execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.contact:

                Intent i = new Intent(HomeActivity.this, ContactListActivity.class);
                startActivity(i);

                break;

            case R.id.favourite:
                favouritesList = DBAdapter.getAllFavouriteContactList();
                if(favouritesList.size()!=0) {
                Intent j = new Intent(HomeActivity.this, FavouritesListActivity.class);
                startActivity(j);
                }else {
                    Toast.makeText(getApplicationContext(),"Favourites list is Empty!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.deleted:
                deletedList = DBAdapter.getAllDeletedContactList();
                if(deletedList.size()!=0) {
                    Intent k = new Intent(HomeActivity.this, DeletedListActivity.class);
                    startActivity(k);
                }else {
                    Toast.makeText(getApplicationContext(),"Deleted list is Empty", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


}
