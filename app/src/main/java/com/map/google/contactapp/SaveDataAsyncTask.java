package com.map.google.contactapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class SaveDataAsyncTask extends AsyncTask<Void, Void, List> {
    private final Context context;
    public static List<Contact> contactList = new ArrayList<Contact>();
    // ProgressBar bar;
    private ProgressDialog progressDialog;
    private OnContactFetchListener listener;

    public SaveDataAsyncTask(final Context context, OnContactFetchListener listener) {
        this.context = context;
        this.listener = listener;
        progressDialog = new ProgressDialog(context);
        //dialog = new ProgressDialog(context);
        // setProgressBar(bar);
        //this.execute();
    }

    @Override
    protected void onPreExecute() {
        // display a progress dialog for good user experiance
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        /*dialog.setMessage("Doing something, please wait.");
        dialog.show();*/
    }

    @Override
    protected List<Contact> doInBackground(Void... voids) {
     try{
        AddContactInDatabase();
    } catch (Exception e) {
        e.printStackTrace();
    }

        return contactList;
    }

    private void AddContactInDatabase() {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int Id = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String Name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String photo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                        // Insert Data In SQLite Database
                        Contact contact_data = new Contact();
                        contact_data.setContact_id(Id);
                        contact_data.setContact_name(Name);
                        contact_data.setContact_number(phoneNo);
                        contact_data.setImages("0");
                        contact_data.setFavourite_item("0");
                        contact_data.setDeleted_item("0");
                        DBAdapter.addContactData(contact_data);
                    }
                    pCur.close();
                }
            }
        }
        if(cur != null){
            cur.close();
        }
    }

    protected void onPostExecute(List list) {
     super.onPostExecute(list);
     if(listener!=null){
         listener.onContactFetch(list);
     }
         progressDialog.dismiss();
        /*contactList = DBAdapter.getAllContactData();
        ContactListActivity.mAdapter = new ContactItemAdapter(context, contactList);
        ContactListActivity.contacts_listview.setAdapter(ContactListActivity.mAdapter);*/

    }
    public interface OnContactFetchListener{
        void onContactFetch(List list);
    }
}
