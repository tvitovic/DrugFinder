package com.dzepna_apoteka;

import java.util.HashMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dzepna_apoteka.adapter.NavDrawerListAdapter;

public class MainActivity extends ActionBarActivity implements
		InfoFragment.OnHeadlineSelectedListener, LekoviFragment.DrugDetails,
		ApotekeFragment.PharmacyDetails, DetaljiLekaFragment.PrijavaNRL {
	private String[] navMenuTitles;
	// ArrayAdapter<String> itemsAdapter;
	private NavDrawerListAdapter adapter;
	ListView listView;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		/*
		 * itemsAdapter = new ArrayAdapter<String>(this, R.layout.menu_item,
		 * R.id.title, navMenuTitles);
		 */

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navMenuTitles);
		listView = (ListView) findViewById(R.id.list_slidermenu);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new SlideMenuClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(listView);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new InfoFragment();
			break;
		case 1:
			fragment = new LekoviFragment();
			break;
		case 2:
			fragment = new ApotekeFragment();
			break;
		case 3:
			fragment = new MapaFragment();
			break;
		case 4:
			fragment = new KontaktFragment();
			break;
		case 99:
			fragment = new DetaljiLekaFragment();
			break;
		case 100:
			fragment = new DetaljiApotekaFragment();
			break;
		case 101:
			fragment = new PrijavaNezeljenih();
			break;
		default:
			break;
		}

		if (fragment != null) {
			if (((fragment instanceof DetaljiLekaFragment) && bundle != null)
					|| ((fragment instanceof DetaljiApotekaFragment) && bundle != null)
					|| ((fragment instanceof PrijavaNezeljenih) && bundle != null)) {
				fragment.setArguments(bundle);
			}
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack("" + position).commit();

			// update selected item and title, then close the drawer
			if (!(fragment instanceof DetaljiLekaFragment)
					&& !(fragment instanceof DetaljiApotekaFragment)
					&& !(fragment instanceof PrijavaNezeljenih)) {
				listView.setItemChecked(position, true);
				listView.setSelection(position);
				setTitle(navMenuTitles[position]);
			}
			mDrawerLayout.closeDrawer(listView);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onArticleSelected(int position) {
		displayView(position);

	}

	@Override
	public void openDetails(HashMap<String, String> map, int position) {
		bundle = new Bundle();
		bundle.putString("name", map.get("name"));
		bundle.putString("ingredients", map.get("ingredients"));
		bundle.putString("priceRange", map.get("priceRange"));
		displayView(position);

	}

	@Override
	public void openPharmacy(HashMap<String, String> map, int position) {
		// TODO Auto-generated method stub
		bundle = new Bundle();
		bundle.putString("name", map.get("name"));
		bundle.putString("county", map.get("county"));
		bundle.putString("address", map.get("address"));
		bundle.putString("workingTime", map.get("workingTime"));
		bundle.putString("phones", map.get("phones"));
		displayView(position);
	}

	@Override
	public void nearestPharmacy(int position) {
		displayView(position);

	}

	@Override
	public void onBackPressed() {

		// initialize variables
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		// check to see if stack is empty
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			ft.commit();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void openNRLform(String name, int position) {
		bundle = new Bundle();
		bundle.putString("name", name);
		displayView(101);
	}

}
