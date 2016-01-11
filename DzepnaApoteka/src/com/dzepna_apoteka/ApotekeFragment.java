package com.dzepna_apoteka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.dzepna_apoteka.logic.MapComparator;

public class ApotekeFragment extends Fragment {
	Spinner spinner;
	ArrayAdapter<CharSequence> adapter;
	Context context;
	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	View rootView;
	ArrayList<String> names;
	ListView listView;
	Button btn;

	String choice = null;

	private static int DETAILS_INT = 100;

	private PharmacyDetails mCallback;

	public ApotekeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.apoteke_layout, container, false);

		spinner = (Spinner) rootView.findViewById(R.id.opstine_spinner);
		adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.opstine_array, R.layout.spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				choice = spinner.getItemAtPosition(position).toString();
				new DownloadJSON().execute();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		btn = (Button) rootView.findViewById(R.id.prikazi_najblizu);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.nearestPharmacy(3);
			}
		});

		//new DownloadJSON().execute();
		return rootView;
	}

	public interface PharmacyDetails {
		public void openPharmacy(HashMap<String, String> map, int position);

		public void nearestPharmacy(int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (PharmacyDetails) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(rootView.getContext());
			// Set progressdialog title
			mProgressDialog.setTitle("Učitavanje");
			// Set progressdialog message
			mProgressDialog.setMessage("Sadržaj se učitava");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create an array
			arraylist = new ArrayList<HashMap<String, String>>();
			arraylist.clear();
			names = new ArrayList<String>();
			names.clear();
			// Retrieve JSON Objects from the given URL address
			jsonarray = com.dzepna_apoteka.logic.JSONfunctions
					.getJSONfromURL("http://boiling-ravine-2299.herokuapp.com/pharmacies");
			// Log.e("Darko", "jsonarray pre for petlje: " +
			// jsonarray.toString());
			Log.e("Darko", "" + jsonarray.length() + " duzina");

			try {
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					if (choice != null) {
						if (jsonobject.getString("county").equals(choice)) {
							HashMap<String, String> map = new HashMap<String, String>();
							// Retrive JSON Objects
							map.put("name", jsonobject.getString("name"));
							names.add(jsonobject.getString("name"));

							map.put("address", jsonobject.getString("address"));
							map.put("county", jsonobject.getString("county"));
							map.put("phones", jsonobject.getString("phones"));
							// map.put("county",
							// jsonobject.getString("county"));
							map.put("workingTime",
									jsonobject.getString("workingTime"));
							// map.put("takesPerscriptions",
							// String.valueOf(jsonobject.getBoolean("takesPerscriptions")));

							arraylist.add(map);
						}
					}else{
						HashMap<String, String> map = new HashMap<String, String>();
						// Retrive JSON Objects
						map.put("name", jsonobject.getString("name"));
						names.add(jsonobject.getString("name"));

						map.put("address", jsonobject.getString("address"));
						map.put("county", jsonobject.getString("county"));
						map.put("phones", jsonobject.getString("phones"));
						// map.put("county",
						// jsonobject.getString("county"));
						map.put("workingTime",
								jsonobject.getString("workingTime"));
						// map.put("takesPerscriptions",
						// String.valueOf(jsonobject.getBoolean("takesPerscriptions")));

						arraylist.add(map);
					}
				}

				Collections.sort(names);
				Collections.sort(arraylist, new MapComparator("name"));

			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			listView = (ListView) rootView.findViewById(R.id.lista_apoteka);
			listView.setAdapter(new ArrayAdapter<String>(rootView.getContext(),
					android.R.layout.simple_list_item_1, names));
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long arg) {
					// Object listItem = listView.getItemAtPosition(position);
					mCallback.openPharmacy(arraylist.get(position), DETAILS_INT);
				}
			});
			/*
			 * SideSelector sideSelector = (SideSelector)
			 * rootView.findViewById(R.id.side_selector);
			 * sideSelector.setListView(listView);
			 */
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}

}
