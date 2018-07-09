package com.map.google.contactapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by levia4 on 29-05-2017.
 */

public class FavouritesItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<Contact> allFavouritesList;


    public FavouritesItemAdapter(Context context, List<Contact> allFavouritesList) {
        this.context=context;
        this.allFavouritesList=allFavouritesList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return allFavouritesList.size();
    }

    @Override
    public Object getItem(int i) {
        return allFavouritesList;
    }

    @Override
    public long getItemId(int i) {
        return allFavouritesList.size();
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
        ImageView photo,favourites;
        TextView name,phone_number,name_first_text;
        LinearLayout goto_Call_massege;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.favourites_listview_item, null);
            holder = new ViewHolder();
            holder.goto_Call_massege = (LinearLayout) convertView.findViewById(R.id.goto_Call_massege);
            holder.favourites = (ImageView) convertView.findViewById(R.id.favourites);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.name_first_text = (TextView) convertView.findViewById(R.id.name_first_text);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(allFavouritesList.get(position).getContact_name());
        holder.phone_number.setText(allFavouritesList.get(position).getContact_number());
        String person_name = allFavouritesList.get(position).getContact_name();
        String firstLetter = String.valueOf(person_name.charAt(0));
        holder.name_first_text.setText(firstLetter);
        holder.name_first_text.setBackgroundColor(Helpers.getRandomColor());

        Uri u = Helpers.getPhotoUri(context,allFavouritesList.get(position).getContact_id());
        if(u != null){
            holder.photo.setVisibility(View.VISIBLE);
            holder.name_first_text.setVisibility(View.GONE);
            holder.photo.setImageURI(u);
        }else {
            holder.photo.setVisibility(View.GONE);
        }


        holder.goto_Call_massege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = allFavouritesList.get(position).getContact_number();
                Intent intent = new Intent(context, CallMassegeConfirmationActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("number",phone_number);
                context.getApplicationContext().startActivity(intent);
            }
        });
        holder.favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAdapter.runQuery("UPDATE contact SET " + DBAdapter.FAVOURITE_ITEM + "='0' WHERE contact_id ='" +
                        allFavouritesList.get(position).getContact_id() + "'");
                allFavouritesList.remove(position);
                allFavouritesList = DBAdapter.getAllFavouriteContactList();
                if(allFavouritesList.size()!=0) {
                    FavouritesListActivity.mAdapter = new FavouritesItemAdapter(context, allFavouritesList);
                    FavouritesListActivity.favourites_listview.setAdapter(FavouritesListActivity.mAdapter);
                }/*else {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                }*/
                FavouritesListActivity.mAdapter.notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
