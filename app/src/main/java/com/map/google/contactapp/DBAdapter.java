package com.map.google.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {

    /******************** if debug is set true then it will show all Logcat message ************/
    public static final boolean DEBUG = true;

    /******************** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "POS_DATABASE.db";

    // Table Names
    public static final String TABLE_CONTACT = "contact";

    // column names OF LOGIN TABLE
    public static final String CONTACT_ID = "contact_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String IMAGES = "images";
    public static final String FAVOURITE_ITEM = "favourite_item";
    public static final String DELETED_ITEM = "deleted_item";

    // ********************** End *******************
    // Settle table create statement
    public static final String CREATE_TABLE_CONTACT = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACT + "(" + CONTACT_ID + " INTEGER NOT NULL PRIMARY KEY,"+ CONTACT_NAME + " TEXT,"
            + CONTACT_NUMBER + " TEXT," + IMAGES + " TEXT," + FAVOURITE_ITEM + " TEXT DEFAULT 0," + DELETED_ITEM + " TEXT DEFAULT 0);";

    /******************** Set all table with comma seperated like USER_TABLE,ABC_TABLE ************/
    private static final String[] ALL_TABLES = {CREATE_TABLE_CONTACT};

    /******************** Used to open database in syncronized way ************/
    private static DataBaseHelper DBHelper = null;
    private static String[] whereArgs=null;

    public DBAdapter(HomeActivity homeActivity) {
    }

    /******************* Initialize database *************/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }

    /********************** Main Database creation INNER class ********************/
    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {

            super(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/CONTACT/DB/" + DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {
                db.execSQL(CREATE_TABLE_CONTACT);

            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed

    /********************** Open database for insert,update,delete in synchronized manner ********************/
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }

    /************************ General functions**************************/
    /*********************** Escape string for single quotes (Insert,Update)************/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace("'", "''");
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }

    /*********************** UnEscape string for single quotes (show data)************/
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("''", "'");
        }

        return aReturn;
    }


    /********************************************************************/
    public static void addContactData(Contact contact) {
        final SQLiteDatabase db = open();
        String contact_id = sqlEscapeString(contact.getContact_id()+"");
        String contact_name = sqlEscapeString(contact.getContact_name());
        String contact_number = sqlEscapeString(contact.getContact_number());
        String images = sqlEscapeString(contact.getImages());
        String favourite_item = sqlEscapeString(contact.getFavourite_item());
        String deleted_item = sqlEscapeString(contact.getDeleted_item());


        ContentValues cVal = new ContentValues();
        cVal.put(CONTACT_ID, contact_id);
        cVal.put(CONTACT_NAME, contact_name);
        cVal.put(CONTACT_NUMBER, contact_number);
        cVal.put(IMAGES, images);
        cVal.put(FAVOURITE_ITEM, favourite_item);
        cVal.put(DELETED_ITEM, deleted_item);

        db.insert(TABLE_CONTACT, null, cVal);
        db.close();                                              // Closing database connection
    }


    public static void updateContact(Contact contact) {
        final SQLiteDatabase db = open();

        String contact_id = sqlEscapeString(contact.getContact_id()+"");
        String contact_name = sqlEscapeString(contact.getContact_name());
        String contact_number = sqlEscapeString(contact.getContact_number());
        String images = sqlEscapeString(contact.getImages());
        String favourite_item = sqlEscapeString(contact.getFavourite_item());
        String deleted_item = sqlEscapeString(contact.getDeleted_item());


        ContentValues cVal = new ContentValues();
        cVal.put(CONTACT_ID, contact_id);
        cVal.put(CONTACT_NAME, contact_name);
        cVal.put(CONTACT_NUMBER, contact_number);
        cVal.put(IMAGES, images);
        cVal.put(FAVOURITE_ITEM, favourite_item);
        cVal.put(DELETED_ITEM, deleted_item);

        // updating row
        db.update(TABLE_CONTACT, cVal, CONTACT_ID + " = ?",
                new String[] { String.valueOf(contact.getContact_id()) });
        db.close();
    }

    public static List<Contact> getAllContactData() {
        List<Contact> dataList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTACT + " WHERE " + DELETED_ITEM + "='0' ORDER BY contact_name ASC";

        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact data = new Contact();
                data.setContact_id(cursor.getInt(0));
                data.setContact_name(cursor.getString(1));
                data.setContact_number(cursor.getString(2));
                data.setImages(cursor.getString(3));
                data.setFavourite_item(cursor.getString(4));
                data.setDeleted_item(cursor.getString(5));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dataList;
    }
    /*
     * This method give all Product;
     */
    public static List<Contact> getAllFavouriteContactList() {
        List<Contact> dataList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE "+ FAVOURITE_ITEM + "='1'";

        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact data = new Contact();
                data.setContact_id(cursor.getInt(0));
                data.setContact_name(cursor.getString(1));
                data.setContact_number(cursor.getString(2));
                data.setImages(cursor.getString(3));
                data.setFavourite_item(cursor.getString(4));
                data.setDeleted_item(cursor.getString(5));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dataList;
    }
    // Selected Images Path
    public static List<Contact> getAllDeletedContactList() {
        List<Contact> dataList = new ArrayList<Contact>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE "+ MENU_ID + "=" + productMenuId + " AND " + PRODUCT_TYPE + "=" + productType;
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE "+ DELETED_ITEM + "='1'";
        // select * from product where menu_id=1 and product_type=1

        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Contact data = new Contact();
                data.setContact_id(cursor.getInt(0));
                data.setContact_name(cursor.getString(1));
                data.setContact_number(cursor.getString(2));
                data.setImages(cursor.getString(3));
                data.setFavourite_item(cursor.getString(4));
                data.setDeleted_item(cursor.getString(5));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dataList;
    }

    public static String runQuery(String query) {
        // TODO Auto-generated method stub
        final SQLiteDatabase db = open();
        db.execSQL(query);
        return query;
    }
}
