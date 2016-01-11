package com.dzepna_apoteka;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.dzepna_apoteka.adapter.ListViewAdapter;
import com.dzepna_apoteka.logic.XMLParser;

public class InfoFragment extends Fragment {
	static final String URL = "https://feedity.com/apotekabeograd-co-rs/VVVXVVZR.rss";
	static final String KEY_ITEM = "item";
	static final String KEY_TITLE = "title";
	static final String KEY_LINK = "link";
	View rootView;
	Button btn;
	ArrayList<HashMap<String, String>> vesti;
	private OnHeadlineSelectedListener mCallback;
	public InfoFragment(){}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.info_layout, container, false);
		 new ShowNews().execute();
		 btn=(Button) rootView.findViewById(R.id.prikazi_najblizu);
		 btn.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	mCallback.onArticleSelected(3);
	            }
		 });
        return rootView;
	}
	
	 // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

	private class ShowNews extends AsyncTask<Void, Void, Void> {
		 

        @Override
        protected Void doInBackground(Void... params) {
    		XMLParser parser = new XMLParser();
    		String xml = parser.getXmlFromUrl(URL); // getting XML
    		Document doc = parser.getDomElement(xml); // getting DOM element
    		
    		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
    		
    		 vesti = new ArrayList<HashMap<String, String>>();
    		// looping through all item nodes <item>
    		for (int i = 0; i < nl.getLength(); i++) {
    			// creating new HashMap
    			HashMap<String, String> map = new HashMap<String, String>();
    			Element e = (Element) nl.item(i);
    			// adding each child node to HashMap key => value
    			map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
    			map.put(KEY_LINK, parser.getValue(e, KEY_LINK));
    			

    			// adding HashList to ArrayList
    			vesti.add(map);
    		}
    		
          return null;
           
        
    }
        @Override
        protected void onPostExecute(Void result) {
        	ListView listView=(ListView) rootView.findViewById(R.id.lista_vesti);
    		ListViewAdapter la= new ListViewAdapter(rootView.getContext(),vesti );
    		listView.setAdapter(la);
        	super.onPostExecute(result);
        }
        
}
	
    public void openMaps(){
    	MainActivity ma=new MainActivity();
    	ma.displayView(3);
    	
    }
}
