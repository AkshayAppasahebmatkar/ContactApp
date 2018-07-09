package com.map.google.contactapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by levia4 on 29-05-2017.
 */

public class ContactItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<Contact> allContactsList;
    private Object objItem;
    int ID;
    public static List<Contact> contactsList = new ArrayList<Contact>();
    Animation animMoveToTop;

    public ContactItemAdapter(Context context, List<Contact> allContactsList) {
        this.context=context;
        this.allContactsList=allContactsList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return allContactsList.size();
    }

    @Override
    public Object getItem(int i) {
        return allContactsList;
    }

    @Override
    public long getItemId(int i) {
        return allContactsList.size();
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    private class ViewHolder {
        ImageView photo,enable_favourites,disable_favourites,delete;
        TextView name,phone_number,name_first_text;
        LinearLayout goto_Call_massege;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contact_listview_item, null);
            holder = new ViewHolder();
            holder.goto_Call_massege = (LinearLayout) convertView.findViewById(R.id.goto_Call_massege);
            holder.enable_favourites = (ImageView) convertView.findViewById(R.id.enable_favourites);
            holder.disable_favourites = (ImageView) convertView.findViewById(R.id.disable_favourites);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete_contact);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.name_first_text = (TextView) convertView.findViewById(R.id.name_first_text);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ID = allContactsList.get(position).getContact_id();
        holder.name.setText(allContactsList.get(position).getContact_name());
        holder.phone_number.setText(allContactsList.get(position).getContact_number());
        String person_name = allContactsList.get(position).getContact_name();
        String firstLetter = String.valueOf(person_name.charAt(0));
        holder.name_first_text.setText(firstLetter);
        holder.name_first_text.setBackgroundColor(Helpers.getRandomColor());

        Uri u = Helpers.getPhotoUri(context,ID);
        if(u != null){
            holder.photo.setVisibility(View.VISIBLE);
            holder.name_first_text.setVisibility(View.GONE);
            holder.photo.setImageURI(u);
        }else {
            holder.photo.setVisibility(View.GONE);
        }

        if(allContactsList.get(position).getFavourite_item().equals("1")){
            holder.enable_favourites.setVisibility(View.VISIBLE);
            holder.disable_favourites.setVisibility(View.GONE);
        }

        holder.goto_Call_massege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = allContactsList.get(position).getContact_number();
                Intent intent = new Intent(context, CallMassegeConfirmationActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("number",phone_number);
                context.getApplicationContext().startActivity(intent);
            }
        });

        holder.enable_favourites.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                animMoveToTop = AnimationUtils.loadAnimation(context, R.animator.bounce);
                holder.enable_favourites.startAnimation(animMoveToTop);
                if(allContactsList.get(position).getFavourite_item().equals("1")) {
                    DBAdapter.runQuery("UPDATE contact SET " + DBAdapter.FAVOURITE_ITEM + "='0' WHERE contact_id ='" +
                            allContactsList.get(position).getContact_id() + "'");
                    holder.enable_favourites.setVisibility(View.GONE);
                    holder.disable_favourites.setVisibility(View.VISIBLE);
                    allContactsList = DBAdapter.getAllContactData();
                }
            }
        });
        holder.disable_favourites.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                animMoveToTop = AnimationUtils.loadAnimation(context, R.animator.bounce);
                holder.enable_favourites.startAnimation(animMoveToTop);
                if(allContactsList.get(position).getFavourite_item().equals("0")) {
                    DBAdapter.runQuery("UPDATE contact SET " + DBAdapter.FAVOURITE_ITEM + "='1' WHERE contact_id ='" +
                            allContactsList.get(position).getContact_id() + "'");
                    holder.enable_favourites.setVisibility(View.VISIBLE);
                    holder.disable_favourites.setVisibility(View.GONE);
                    allContactsList = DBAdapter.getAllContactData();
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                // Setting Dialog Title
                alertDialog.setTitle("Delete Contact.");
                // Setting Dialog Message
                alertDialog.setMessage("Do you want to delete this Contact?");
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to invoke YES event
                        //finish();
                        //System.exit(0);
                    }
                });
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        DBAdapter.runQuery("UPDATE contact SET " + DBAdapter.DELETED_ITEM + "='1', "+  DBAdapter.FAVOURITE_ITEM + "='0' WHERE contact_id ='" +
                                allContactsList.get(position).getContact_id() + "'");
                        holder.enable_favourites.setVisibility(View.GONE);
                        holder.disable_favourites.setVisibility(View.VISIBLE);
                        allContactsList.remove(position);
                        contactsList = DBAdapter.getAllContactData();
                        ContactListActivity.mAdapter = new ContactItemAdapter(context, contactsList);
                        ContactListActivity.contacts_listview.setAdapter(ContactListActivity.mAdapter);
                        ContactListActivity.mAdapter.notifyDataSetChanged();
                        //FavouritesListActivity.mAdapter.notifyDataSetChanged();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        });
        return convertView;
    }

}
