package com.dzepna_apoteka.adapter;


import com.dzepna_apoteka.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {
    
    private Context context;
    private String[] navDrawerItems;
     
    public NavDrawerListAdapter(Context context, String[] navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.length;
    }
 
    @Override
    public Object getItem(int position) {       
        return navDrawerItems[position];
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_item, null);
        }
          
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            
        txtTitle.setText(navDrawerItems[position]);
         
  
        return convertView;
    }
}