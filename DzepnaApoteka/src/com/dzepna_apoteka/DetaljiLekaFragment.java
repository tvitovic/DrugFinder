package com.dzepna_apoteka;



import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class DetaljiLekaFragment extends Fragment {
	private TabHost mTabHost;
	Bundle args;
	View rootView;
	ViewGroup cont;
	TextView name,priceRange,ingredients;
	PrijavaNRL mCallback;
	String drugName="";
	Button btn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 rootView = inflater.inflate(R.layout.opis_leka_layout, container, false);
		 cont=container;
		
		/*mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
       mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

	        mTabHost.addTab(mTabHost.newTabSpec("sastav").setIndicator("Sastav leka"),
	                Sastav.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("uputstvo").setIndicator("Uputstvo"),
	                Uputstvo.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("paralele").setIndicator("Paralele"),
	                Paralele.class, null);*/

		mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
        args=getArguments();
        
        TabHost.TabSpec spec = mTabHost.newTabSpec("sastav");
        spec.setIndicator("Sastav leka");
        spec.setContent(new TabHost.TabContentFactory() {

            @Override
            public View createTabContent(String tag) {
                // TODO Auto-generated method stub
               // return (new AnalogClock(getActivity()));
            	LayoutInflater inf=(LayoutInflater) rootView.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	View v =inf.inflate(R.layout.drug_desc,(ViewGroup) getView());
            	name=(TextView) v.findViewById(R.id.name);
            	ingredients=(TextView) v.findViewById(R.id.ingredients);
            	priceRange=(TextView) v.findViewById(R.id.price);
            	if(args!=null){
            		name.setText(args.getString("name"));
            		ingredients.setText(args.getString("ingredients"));
            		priceRange.setText(args.getString("priceRange"));
            		drugName=args.getString("name");
            	}
            	return v;
            }
        });
        mTabHost.addTab(spec);
        spec = mTabHost.newTabSpec("uputstvo");
        spec.setIndicator("Uputstvo");
        spec.setContent(new TabHost.TabContentFactory() {

            @Override
            public View createTabContent(String tag) {
                // TODO Auto-generated method stub
                return (new AnalogClock(getActivity()));
            }
        });
        mTabHost.addTab(spec);
        spec = mTabHost.newTabSpec("paralele");
        spec.setIndicator("Paralele");
        spec.setContent(new TabHost.TabContentFactory() {

            @Override
            public View createTabContent(String tag) {
                // TODO Auto-generated method stub
                return (new AnalogClock(getActivity()));
            }
        });
        mTabHost.addTab(spec);
        btn=(Button) rootView.findViewById(R.id.prijavi_nezeljena);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCallback.openNRLform(drugName, 101);
			}
		});
		return rootView;
	}
	
	public interface PrijavaNRL {
		public void openNRLform(String name, int position);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (PrijavaNRL) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}
}
