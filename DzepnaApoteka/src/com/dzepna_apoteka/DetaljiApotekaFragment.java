package com.dzepna_apoteka;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetaljiApotekaFragment extends Fragment {
	Bundle args;
	View rootView;
	TextView name,county,address,phones,workingTime;
	String num="988";
	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.apoteka_desc, container, false);
		name=(TextView)rootView.findViewById(R.id.name);
		county=(TextView)rootView.findViewById(R.id.county);
		address=(TextView)rootView.findViewById(R.id.address);
		workingTime=(TextView)rootView.findViewById(R.id.worktime);
		phones=(TextView)rootView.findViewById(R.id.phones);
		
		btn=(Button) rootView.findViewById(R.id.pozovi_apoteku);
		
		args=getArguments();
		if(args!=null){
			name.setText(args.getString("name"));
			county.setText(args.getString("county"));
			address.setText(args.getString("address"));
			workingTime.setText(args.getString("workingTime"));
			phones.setText(args.getString("phones"));
			num=args.getString("phones");
		}
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent phoneIntent=new Intent(Intent.ACTION_CALL);
				phoneIntent.setData(Uri.parse("tel:"+num));
				startActivity(phoneIntent);
			}
		});
		return rootView;
	}

	




	

}
