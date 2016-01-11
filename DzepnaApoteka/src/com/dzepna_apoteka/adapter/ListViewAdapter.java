package com.dzepna_apoteka.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzepna_apoteka.R;

public class ListViewAdapter extends BaseAdapter{
	Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    View itemView;
 
    public ListViewAdapter(Context context,
            ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }
 
    @Override
    public int getCount() {
        return data.size();
    }
 
    @Override
    public Object getItem(int position) {
        return null;
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView title;

 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        itemView = inflater.inflate(R.layout.info_item, parent, false);
        // Get the position
        resultp = data.get(position);
 
        // Locate the TextViews in listview_item.xml
        title = (TextView) itemView.findViewById(R.id.title);

        // Capture position and set results to the TextViews
        title.setText(resultp.get("title"));
 
        itemView.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultp.get("link")));
                itemView.getContext().startActivity(browserIntent);
 
            }
        });
        return itemView;
    }
    
}
