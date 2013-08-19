package org.zechocapic.mycinoche;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class Film extends Activity {
	
	private int appStyle;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // chargement des preferences perso
        SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
        appStyle = settings.getInt("appStyle", 1);
        
		// get the message from the intent
		Intent intent = getIntent();
		url = intent.getStringExtra(Main.URL_MESSAGE);
		
		// mise en place du layout
		setContentView(R.layout.activity_film);
		
		// recuperation des infos du film choisi
        new RecupInfosAsyncTask(appStyle, this).execute(url);
        
		// show the Up button in the action bar.
		setupActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.film, menu);
		return true;
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.style:
			appStyle = - appStyle;
	        new RecupInfosAsyncTask(appStyle, this).execute(url);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		// enregistrement des preferences perso
		SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putInt("appStyle", appStyle);
		
		editor.commit();
	}
	
}
