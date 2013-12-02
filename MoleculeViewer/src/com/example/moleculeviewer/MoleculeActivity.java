package com.example.moleculeviewer;

//import java.util.Arrays;
//import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class MoleculeActivity extends Activity {
	private Chemical molecule;
	private TextView data_text;
	private TwoDModelView moleculeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_molecule);
		
		// molecule name from parent activity
		Intent intent = getIntent();
		molecule = (Chemical)intent.getSerializableExtra("SELECTED_MOLECULE");
		moleculeView= (TwoDModelView) findViewById(R.id.two_d_view);
		data_text = (TextView)findViewById(R.id.data_text);
		
		// construct data
		StringBuilder data = new StringBuilder();
		data.append("Molecular Formula:\n" + molecule.formula + "\n\n");
		data.append("Molecular Weight:\n" + molecule.molecular_weight + "u\n\n");
		data.append("SMILES Bond Notation:\n" + molecule.bond_string);
		
		data_text.setText(data.toString());
		
		// set up action bar
		setupActionBar();
		
		//moleculeView.initData();
		moleculeView.drawMolecule(molecule);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setTitle(molecule.name);
			ab.setSubtitle("2D Model");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.molecule, menu);
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
		return 