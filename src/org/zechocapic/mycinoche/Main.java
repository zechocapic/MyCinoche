package org.zechocapic.mycinoche;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.SharedPreferences;

public class Main extends Activity {
	public final static String URL_MESSAGE = "org.zechocapic.mycinoche.URL_MESSAGE";
	public static final String PREFS_NAME = "MyCinochePrefsFile";
	private final static int UGC_DEFENSE = 32619;
	private final static int UGC_LES_HALLES = 158;
	private final static int UGC_LYON_BASTILLE = 145;
	
	private int appStyle;
	private int numeroCine;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // chargement des preferences perso
        SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
        numeroCine = settings.getInt("numeroCine", UGC_DEFENSE);
        appStyle = settings.getInt("appStyle", 1);
        
        // mise en place du layout
        setContentView(R.layout.activity_main);
        
        // recuperation de la liste des seances
        new RecupSeanceAsyncTask(appStyle, this).execute("http://www.premiere.fr/horaire/cine/" + numeroCine);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.ugc_defense:
			numeroCine = UGC_DEFENSE;
	        new RecupSeanceAsyncTask(appStyle, this).execute("http://www.premiere.fr/horaire/cine/" + numeroCine);
	        return true;
		case R.id.ugc_cine_cite_les_halles:
			numeroCine = UGC_LES_HALLES;
	        new RecupSeanceAsyncTask(appStyle, this).execute("http://www.premiere.fr/horaire/cine/" + numeroCine);
	        return true;
		case R.id.ugc_lyon_bastille:
			numeroCine = UGC_LYON_BASTILLE;
	        new RecupSeanceAsyncTask(appStyle, this).execute("http://www.premiere.fr/horaire/cine/" + numeroCine);
	        return true;
		case R.id.style:
			appStyle = - appStyle;
	        new RecupSeanceAsyncTask(appStyle, this).execute("http://www.premiere.fr/horaire/cine/" + numeroCine);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		// enregistrement des preferences perso
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putInt("numeroCine", numeroCine);
		editor.putInt("appStyle", appStyle);
		
		editor.commit();
	}
	
}
