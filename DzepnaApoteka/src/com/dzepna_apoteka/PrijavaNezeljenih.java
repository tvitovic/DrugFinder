package com.dzepna_apoteka;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PrijavaNezeljenih extends Fragment {
	View rootView;
	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.prijava_nezeljenih, container,
				false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		btn = (Button) rootView.findViewById(R.id.prijavi);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(),
						"Hvala Vam što ste prijavili neželjeno dejstvo", Toast.LENGTH_LONG)
						.show();

			}
		});
		return rootView;
	}
}
