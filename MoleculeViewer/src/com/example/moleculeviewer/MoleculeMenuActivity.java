package com.example.moleculeviewer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class MoleculeMenuActivity extends Activity {
	private String moleculeName;
	private ArrayList<String> listItems = new ArrayList<String>();
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_molecule_menu);
		
		// set up the ListView
		mListView = (ListView) findViewById(R.id.list_view);		
		
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems));
		listItems.add("2D Model");
		listItems.add("3D Model");
		listItems.add("Data");
		((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
		
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch(position) {
					case 0: {
						Intent searchIntent = new Intent(getApplicationContext(), TwoDModelActivity.class);
						searchIntent.putExtra("MOLECULE_NAME", moleculeName);
				    	startActivity(searchIntent);
						break;
					}
					case 1: {
						Intent searchIntent = new Intent(getApplicationContext(), ThreeDModelActivity.class);
						searchIntent.putExtra("MOLECULE_NAME", moleculeName);
				    	startActivity(searchIntent);
						break;
					}
					case 2: {
						Intent searchIntent = new Intent(getApplicationContext(), DataActivity.class);
						searchIntent.putExtra("MOLECULE_NAME", moleculeName);
				    	startActivity(searchIntent);
						break;
					}
					default:
						break;
				}

		    	
			}
		});
		
		// deal with the incoming molecule name from SearchActivity
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			moleculeName = extras.getString("MOLECULE_NAME");
		}
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setTitle(moleculeName);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.molecule_menu, menu);
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

}
