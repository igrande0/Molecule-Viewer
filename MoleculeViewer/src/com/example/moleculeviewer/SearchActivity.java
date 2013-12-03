package com.example.moleculeviewer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SearchActivity extends Activity {
	private ListView mListView;
	private DataXmlParser Parser = new DataXmlParser();
	private ArrayList<Chemical> Chemicals = new ArrayList<Chemical>();
	private ArrayList<Chemical> searchResults = new ArrayList<Chemical>();
	private ChemicalAdapter mAdapter;
	private SpinnerAdapter mSpinnerAdapter;
	private ActionBar ab;
	private boolean isSearch = false;
	
	@SuppressLint("NewApi")
	private OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
		
		@Override
		public boolean onNavigationItemSelected(int position, long itemId) {
			switch(position) {
				// name ascending
				case 0:
					mAdapter.setFormulaSubtitle();
					if(isSearch){
						Collections.sort(searchResults, Chemical.NameAscending);
						mAdapter.updateAdapter(searchResults);
					}
					else {
						Collections.sort(Chemicals, Chemical.NameAscending);
						mAdapter.updateAdapter(Chemicals);
					}
					break;
				// name descending
				case 1:
					mAdapter.setFormulaSubtitle();
					if(isSearch){
						Collections.sort(searchResults, Chemical.NameDescending);
						mAdapter.updateAdapter(searchResults);
					}
					else {
						Collections.sort(Chemicals, Chemical.NameDescending);
						mAdapter.updateAdapter(Chemicals);
					}
					break;
				// formula ascending
				case 2:
					mAdapter.setFormulaSubtitle();
					if(isSearch){
						Collections.sort(searchResults, Chemical.FormulaAscending);
						mAdapter.updateAdapter(searchResults);
					}
					else {
						Collections.sort(Chemicals, Chemical.FormulaAscending);
						mAdapter.updateAdapter(Chemicals);
					}
					break;
				// formula descending
				case 3:
					mAdapter.setFormulaSubtitle();
					if(isSearch){
						Collections.sort(searchResults, Chemical.FormulaDescending);
						mAdapter.updateAdapter(searchResults);
					}
					else {
						Collections.sort(Chemicals, Chemical.FormulaDescending);
						mAdapter.updateAdapter(Chemicals);
					}
					break;
				// weight ascending
				case 4:
					mAdapter.setWeightSubtitle();
					if(isSearch){
						Collections.sort(searchResults, Chemical.MolecularWeightAscending);
						mAdapter.updateAdapter(searchResults);
					}
					else {
						Collections.sort(Chemicals, Chemical.MolecularWeightAscending);
						mAdapter.updateAdapter(Chemicals);
					}
					break;
				// weight descending
				case 5:
					mAdapter.setWeightSubtitle();
					if(isSearch){
						Collections.sort(searchResults, Chemical.MolecularWeightDescending);
						mAdapter.updateAdapter(searchResults);
					}
					else {
						Collections.sort(Chemicals, Chemical.MolecularWeightDescending);
						mAdapter.updateAdapter(Chemicals);
					}
					break;
				default:
					break;
			}
			//ab.setSubtitle("Sorted by: " + strings[position]);
			return true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		mAdapter = new ChemicalAdapter(this, Chemicals);
		mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_list, android.R.layout.simple_spinner_dropdown_item);
		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent searchIntent = new Intent(getApplicationContext(), MoleculeActivity.class);
				if(isSearch) {
					searchIntent.putExtra("SELECTED_MOLECULE", searchResults.get(position));
				}
				else {
					searchIntent.putExtra("SELECTED_MOLECULE", Chemicals.get(position));
				}
			   	startActivity(searchIntent);
			}
		});
		
		// get all chemicals from XML file
		try {
			InputStream is = getResources().getAssets().open("molecules.xml");
			Chemicals = Parser.parse(is);
			Collections.sort(Chemicals, Chemical.NameAscending);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// deal with incoming searches from MainActivity
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			String search = extras.getString("SEARCH_TERM");
			setupActionBar(search);
			if(search.equals("")) {
				displayAllMolecules();
			}
			else {
				isSearch = true;
				searchMolecules(search);
			}
		}
		else {
			setupActionBar("");
			isSearch = false;
			displayAllMolecules();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar(String search) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			if(!search.equals("")) {
				ab.setTitle("Search: " + search);
			}
			else {
				ab.setTitle("All Molecules");	
			}
			//ab.setSubtitle("Sorted by: Name \u2194");
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			ab.setListNavigationCallbacks(mSpinnerAdapter,mOnNavigationListener);
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
	
	private void searchMolecules(String search){
		for(int i = 0; i < Chemicals.size(); ++i){
			Chemical current = Chemicals.get(i);
			if(current.formula.toLowerCase(Locale.ENGLISH).contains(search.toLowerCase(Locale.ENGLISH))
					||current.molecular_weight.toLowerCase(Locale.ENGLISH).contains(search.toLowerCase(Locale.ENGLISH))
					||current.bond_string.toLowerCase(Locale.ENGLISH).contains(search.toLowerCase(Locale.ENGLISH))
					||current.name.toLowerCase(Locale.ENGLISH).contains(search.toLowerCase(Locale.ENGLISH))) {
				searchResults.add(current);
			}
		}
		mAdapter.updateAdapter(searchResults);
	}
	
	private void displayAllMolecules() {
		mAdapter.updateAdapter(Chemicals);
	}
}
