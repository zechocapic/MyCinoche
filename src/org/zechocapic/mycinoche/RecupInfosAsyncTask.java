package org.zechocapic.mycinoche;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecupInfosAsyncTask extends AsyncTask<String, Void, Document> {
	private int appStyle;
	private Context context;
	private ProgressDialog progressDialog;
	
	RecupInfosAsyncTask(int appStyle, Context context) {
		this.appStyle = appStyle;
		this.context = context;
	}

    @Override
	protected void onPreExecute() {
    	this.progressDialog = new ProgressDialog(context);
    	this.progressDialog.setMessage("Infos du film en cours de chargement");
    	this.progressDialog.show();
	}
    
    // Recuperation de la page web
	@Override
    protected Document doInBackground(String... urls) {
        Document doc = null;
        try {
        	for (String url : urls) {
                doc = Jsoup.connect(url).get();
        	}
            return doc;
        } catch (IOException e) {
            return null;
        }
    }
	
    // Parsing de la page web
	@Override
    protected void onPostExecute(Document doc) {
		// Fermeture de la fenetre de chargement
		progressDialog.dismiss();
		
		// Toast pour gerer les cas ou la connexion est en carton
        if(doc == null){
        	Toast.makeText(context, "Erreur : le chargement de la page n'aboutit pas !", Toast.LENGTH_SHORT).show();;
            return;
        }
        
        // Variables de style
        int titleTextSize, simpleTextSize;
        int titleBackgroundColor, subtitleBackgroundColor, simpleBackgroundColor;
        int titleTextColor, simpleTextColor;
        
        // Definition des differents styles
        if (appStyle == 1) {
        	titleTextSize = 20;
        	simpleTextSize = 16;
        	titleBackgroundColor = Color.rgb(47, 55, 64);
        	subtitleBackgroundColor = Color.rgb(192, 192, 192);
        	simpleBackgroundColor = Color.WHITE;
        	titleTextColor = Color.WHITE;
        	simpleTextColor = Color.BLACK;
        } else {
        	titleTextSize = 32;
        	simpleTextSize = 24;
        	titleBackgroundColor = Color.BLACK;
        	subtitleBackgroundColor = Color.rgb(32, 32, 32);
        	simpleBackgroundColor = Color.rgb(64, 64, 64);
        	titleTextColor = Color.WHITE;
        	simpleTextColor = Color.WHITE;
        }
        
		// Bloc du film
        LinearLayout linearLayout = (LinearLayout) ((Activity) context).findViewById(R.id.layout_film);
        linearLayout.removeAllViews();
        
        // TextView du titre
		Element titre = doc.select("div.bloc_rub_titre").first();
        TextView twTitre = new TextView(context);
        twTitre.setBackgroundColor(titleBackgroundColor);
        twTitre.setTextSize(titleTextSize);
        twTitre.setTypeface(null, Typeface.BOLD);
        twTitre.setTextColor(titleTextColor);
        twTitre.setText(titre.text());
        linearLayout.addView(twTitre);
		
		// layout horizontal affiche et infos
        LinearLayout layoutAfficheInfos = new LinearLayout(context);            			
        layoutAfficheInfos.setOrientation(LinearLayout.HORIZONTAL);
        layoutAfficheInfos.setBackgroundColor(Color.BLACK);
		
		// ImageView affiche
		Element affiche = doc.select("div.visuel img[src]").first();
		ImageView imAffiche = new ImageView(context);
		imAffiche.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 2f));
		layoutAfficheInfos.addView(imAffiche);
        new RecupImageAsyncTask(imAffiche).execute(affiche.attr("src"));
        
        // Textview date de sortie
        Element genre = doc.select("div.infos [itemprop=genre]").first();
        Element dateSortie = doc.select("div.infos [itemprop=datePublished]").first();
        Element realisateur = doc.select("div.infos [itemprop=director] [itemprop=name]").first();
        Element acteur = doc.select("div.infos [itemprop=actors]").first();
        Element duree = doc.select("div.infos [itemprop=duration]").first();
        TextView twInfosDiverses = new TextView(context);
        twInfosDiverses.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
        twInfosDiverses.setBackgroundColor(simpleBackgroundColor);
        twInfosDiverses.setTextSize(simpleTextSize);
        twInfosDiverses.setTextColor(simpleTextColor);
        twInfosDiverses.setText("Genre : " + genre.text() + "\n" +
        		"Date de sortie : " + dateSortie.text() + "\n" +
        		"Réalisateur : " + realisateur.text() + "\n" +
        		"Acteurs : " + acteur.text().replace("Avec :", "").replace("...> Tout le casting", "") + "\n" +
        		"Durée : " + duree.text());
        layoutAfficheInfos.addView(twInfosDiverses);
        
        linearLayout.addView(layoutAfficheInfos);

        
        // Cadre Synopsys
		TextView twCadreSynopsis = new TextView(context);
		twCadreSynopsis.setTypeface(null, Typeface.BOLD_ITALIC);
		twCadreSynopsis.setBackgroundColor(subtitleBackgroundColor);
		twCadreSynopsis.setTextSize(simpleTextSize);
		twCadreSynopsis.setTextColor(simpleTextColor);
		twCadreSynopsis.setText("Synopsys");
		linearLayout.addView(twCadreSynopsis);
		
		// TextView du synopsys
		Element description = doc.select("div.description_cnt").first();
        TextView twDesc = new TextView(context);
        twDesc.setBackgroundColor(simpleBackgroundColor);
        twDesc.setTextSize(simpleTextSize);
        twDesc.setTextColor(simpleTextColor);
        twDesc.setText(description.text());
        linearLayout.addView(twDesc);
        
    }
}