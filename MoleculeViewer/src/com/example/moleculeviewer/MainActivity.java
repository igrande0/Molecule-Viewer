package com.example.moleculeviewer;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /* Called when user clicks the search button */
    public void search(View view){
    	Intent searchIntent = new Intent(this, SearchActivity.class);
    	EditText searchText = (EditText) findViewById(R.id.search_message);
    	String search = searchText.getText().toString();
    	searchIntent.putExtra(SearchManager.QUERY, search);
    	startActivity(searchIntent);
    }
    
}
