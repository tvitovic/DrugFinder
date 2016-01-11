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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.dzepna_apoteka.logic.*;

public class LekoviFragment extends Fragment {
	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	View rootView;
	ArrayList<String> names;
	String proba = "";
	ListView listView;
	EditText input;
	private ArrayList<String> array_sort= new ArrayList<String>();
	int textlength=0;

	private DrugDetails mCallback;

	private static int DETAILS_INT = 99;

	public LekoviFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.lekovi_layout, container, false);
		new DownloadJSON().execute();
		return rootView;
	}

	public interface DrugDetails {
		public void openDetails(HashMap<String, String> map, int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (DrugDetails) activity;
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
			names = new ArrayList<String>();
			// Retrieve JSON Objects from the given URL address
			jsonarray = com.dzepna_apoteka.logic.JSONfunctions
					.getJSONfromURL("http://boiling-ravine-2299.herokuapp.com/drugs");
			Log.e("Darko", "jsonarray pre for petlje: " + jsonarray.toString());
			Log.e("Darko", "" + jsonarray.length() + " duzina");

			try {
				Log.e("Darko", "pre for petlje");
				for (int i = 0; i < jsonarray.length(); i++) {
					Log.e("Darko", "" + i + " prolaz");
					HashMap<String, String> map = new HashMap<String, String>();
					jsonobject = jsonarray.getJSONObject(i);
					// Retrive JSON Objects
					map.put("name", jsonobject.getString("name"));
					names.add(jsonobject.getString("name"));
					array_sort.add(jsonobject.getString("name"));//promenjeno
					proba += jsonobject.getString("name");
					map.put("ingredients", jsonobject.getString("ingredients"));
					// map.put("directions",
					// jsonobject.getString("directions"));
					map.put("priceRange", jsonobject.getString("priceRange"));
					// Set the JSON Objects into the array

					arraylist.add(map);
				}

				Log.e("Darko", "probni String:" + proba);
				

				Collections.sort(array_sort);//promenjeno
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

			listView = (ListView) rootView.findViewById(R.id.listview1);
			listView.setAdapter(new ArrayAdapter<String>(rootView.getContext(),
					android.R.layout.simple_list_item_1, array_sort));//promenjeno
			
			input = (EditText) rootView.findViewById(R.id.inputSearch);
			input.addTextChangedListener(new TextWatcher() {

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// Abstract Method of TextWatcher Interface.
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					textlength = input.getText().length();
					array_sort.clear();
					for (int i = 0; i < names.size(); i++) {
						if (textlength <= names.get(i).length()) {
							if (input.getText()
									.toString()
									.equalsIgnoreCase(
											(String) names.get(i)
													.subSequence(0, textlength))) {
								array_sort.add(names.get(i));
							}
						}
					}
					listView.setAdapter(new ArrayAdapter<String>(
							rootView.getContext(),
							android.R.layout.simple_list_item_1, array_sort));
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long arg) {
					// Object listItem = listView.getItemAtPosition(position);
					HashMap<String, String> details=null;
					for (HashMap<String, String> pomocna:arraylist) {
						if(pomocna.get("name").equals(array_sort.get(position))){
							details=pomocna;
							break;
						}
					}
					mCallback.openDetails(details, DETAILS_INT);
					//mCallback.openDetails(arraylist.get(position), DETAILS_INT);
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
