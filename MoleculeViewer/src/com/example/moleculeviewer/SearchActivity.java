package com.example.moleculeviewer;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SearchActivity extends Activity {
	private ArrayList<String> listItems = new ArrayList<String>();
	private ListView mListView;
	private DataXmlParser Parser = new DataXmlParser();
	private ArrayList<Chemical> Chemicals = new ArrayList<Chemical>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// set up the ListView
		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems));
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent searchIntent = new Intent(getApplicationContext(), MoleculeMenuActivity.class);
		    	searchIntent.putExtra("MOLECULE_NAME", listItems.get(position));
		    	startActivity(searchIntent);
			}
		});
		
		// deal with incoming searches from MainActivity
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			String search = extras.getString("SEARCH_TERM");
			setupActionBar(search);
			if(search.equals("")) {
				displayAllMolecules();
			}
			else {
				searchMolecules(search);
			}
		}
		else {
			setupActionBar("");
			displayAllMolecules();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar(String search) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			if(!search.equals("")) {
				setTitle(search);
			}
			else {
				setTitle("All Molecules");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * called if there is an incoming search term
	 */
	private void searchMolecules(String search){
		// TODO search XML and populate list with matches
		
		// placeholder for now
		for(int i = 0; i < 20; ++i) {
			listItems.add("Search Result " + Integer.toString(i));
		}
		((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
	}
	
	private void displayAllMolecules() {
		// TODO search XML and populate list with all molecules
		InputStream is = getResources().openRawResource(R.xml.molecules);
		try{
			Chemicals = Parser.parse(is);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		// placeholder for now
		for(int i = 0; i < Chemicals.size(); ++i) {
			listItems.add(Chemicals.get(i).bond_string);
		}
		((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
	}
}
