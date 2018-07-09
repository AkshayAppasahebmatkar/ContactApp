package com.map.google.contactapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by levia4 on 29-05-2017.
 */

public class DeletedItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<Contact> allDeletedList;


    public DeletedItemAdapter(Context context, List<Contact> allDeletedList) {
        this.context=context;
        this.allDeletedList=allDeletedList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return allDeletedList.size();
    }

    @Override
    public Object getItem(int i) {
        return allDeletedList;
    }

    @Override
    public long getItemId(int i) {
        return allDeletedList.size();
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
        ImageView photo,recall;
        TextView name,phone_number,name_first_text;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.deleted_listview_item, null);
            holder = new ViewHolder();
            holder.recall = (ImageView) convertView.findViewById(R.id.recall);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.name_first_text = (TextView) convertView.findViewById(R.id.name_first_text);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(allDeletedList.get(position).getContact_name());
        holder.phone_number.setText(allDeletedList.get(position).getContact_number());
        String person_name = allDeletedList.get(position).getContact_name();
        String firstLetter = String.valueOf(person_name.charAt(0));
        holder.name_first_text.setText(firstLetter);
        holder.name_first_text.setBackgroundColor(Helpers.getRandomColor());

        Uri u = Helpers.getPhotoUri(context,allDeletedList.get(position).getContact_id());
        if(u != null){
            holder.photo.setVisibility(View.VISIBLE);
            holder.name_first_text.setVisibility(View.GONE);
            holder.photo.setImageURI(u);
        }else {
            holder.photo.setVisibility(View.GONE);
        }

        holder.recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            DBAdapter.runQuery("UPDATE contact SET " + DBAdapter.DELETED_ITEM + "='0' WHERE contact_id ='" +
                    allDeletedList.get(position).getContact_id() + "'");
                allDeletedList.remove(position);
                allDeletedList = DBAdapter.getAllDeletedContactList();
                if(allDeletedList.size()!=0) {
                    DeletedListActivity.mAdapter = new DeletedItemAdapter(context, allDeletedList);
                    DeletedListActivity.deleted_listview.setAdapter(DeletedListActivity.mAdapter);
                    //DeletedListActivity.mAdapter.notifyDataSetChanged();
                }/*else {
                    DeletedListActivity.mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                }*/
                DeletedListActivity.mAdapter.notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
