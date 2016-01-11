package com.dzepna_apoteka;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class KontaktFragment extends Fragment {
	Button btn;
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.kontakt_layout, container,
				false);
		btn=(Button) rootView.findViewById(R.id.posalji);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "Vaša poruka je uspešno poslata", Toast.LENGTH_LONG).show();
				
			}
		});
		return rootView;
	}
}
