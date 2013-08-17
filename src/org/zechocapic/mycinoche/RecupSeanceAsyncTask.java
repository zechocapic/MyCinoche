package org.zechocapic.mycinoche;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecupSeanceAsyncTask extends AsyncTask<String, Void, Document> {
	private int appStyle;
	private Context context;
	private TextView.OnClickListener onClickListener = new TextView.OnClickListener() {
		public void onClick(View v) {
			String url = (String) v.getTag();
			Intent intent = new Intent(context, Film.class);
			intent.putExtra(Main.URL_MESSAGE, url);
			context.startActivity(intent);
		}
	};

	public RecupSeanceAsyncTask(int appStyle, Context context) {
		this.appStyle = appStyle;
		this.context = context;
	}

    // Recuperation de la page web
	@Override
    protected Document doInBackground(String... urls) {
        Document doc = null;
        try {
    		String url = urls[0];
            doc = Jsoup.connect(url).get();
            return doc;
        } catch (IOException e) {
            return null;
        }
    }

    // Parsing de la page web
	@Override
    protected void onPostExecute(Document doc) {
		// Toast pour gerer les cas ou la connexion est en carton
        if(doc == null){
        	Toast.makeText(context, "Erreur : le chargement de la page n'aboutit pas !", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Variables de style
        int titleTextSize, versionTextSize, horaireTextSize, noteTextSize;
        int titleBackgroundColor, subtitleBackgroundColor, simpleBackgroundColor;
        int titleTextColor, otherTextColor;
        
        // Definition des differents styles
        if (appStyle == 1) {
        	titleTextSize = 20;
        	versionTextSize = 16;
        	horaireTextSize = 16;
        	noteTextSize = 16;
        	titleBackgroundColor = Color.rgb(47, 55, 64);
        	subtitleBackgroundColor = Color.rgb(192, 192, 192);
        	simpleBackgroundColor = Color.WHITE;
        	titleTextColor = Color.WHITE;
        	otherTextColor = Color.BLACK;
        } else {
        	titleTextSize = 40;
        	versionTextSize = 32;
        	horaireTextSize = 32;
        	noteTextSize = 32;
        	titleBackgroundColor = Color.BLACK;
        	subtitleBackgroundColor = Color.rgb(32, 32, 32);
        	simpleBackgroundColor = Color.rgb(64, 64, 64);
        	titleTextColor = Color.WHITE;
        	otherTextColor = Color.WHITE;
        }

		// Layout de la page
        LinearLayout layoutListeFilms = (LinearLayout) ((Activity) context).findViewById(R.id.layout_liste_films);
        layoutListeFilms.removeAllViews();
		
        Elements blocs = doc.select("div.bloc_salles_bloc");
        
		for (Element bloc : blocs) {
			// Layout pour chaque film
			LinearLayout layoutBloc = new LinearLayout(context);
			layoutBloc.setOrientation(LinearLayout.VERTICAL);
			layoutBloc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			// Titre du film
			Element titre = bloc.select("div.titre").first(); 
            Element titreURL = titre.select("a[href]").first();
            TextView twTitre = new TextView(context);
            twTitre.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            twTitre.setTextSize(titleTextSize);
            twTitre.setTypeface(null, Typeface.BOLD);
            twTitre.setBackgroundColor(titleBackgroundColor);
            twTitre.setTextColor(titleTextColor);
            twTitre.setOnClickListener(onClickListener);
            twTitre.setTag(titreURL.attr("href"));
            twTitre.setText(titre.text());
            layoutBloc.addView(twTitre);
			
			// Seances du film
			Elements seances = bloc.select("ul.cine-seances li");
			for (Element seance : seances) {
    			LinearLayout layoutSeances = new LinearLayout(context);            			
    			layoutSeances.setOrientation(LinearLayout.HORIZONTAL);
    			layoutSeances.setBackgroundColor(Color.BLACK);
				
				// Version du film
    			Element version = seance.select("span.cine-version").first();
				TextView twVersion = new TextView(context);
				twVersion.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				twVersion.setTextSize(versionTextSize);
				twVersion.setWidth(100);
				twVersion.setTypeface(null, Typeface.BOLD);
				twVersion.setBackgroundColor(subtitleBackgroundColor);
				twVersion.setTextColor(otherTextColor);
				twVersion.setText(version.text() + " : ");
                layoutSeances.addView(twVersion);
                
                // Horaire du film
				Element horaire = seance.select("span.cine-horaires").first();
                TextView twHoraire = new TextView(context);
				twHoraire.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				twHoraire.setTextSize(horaireTextSize);
                twHoraire.setTextColor(otherTextColor);
                twHoraire.setBackgroundColor(simpleBackgroundColor);
                /*twHoraire.setSingleLine(true);
                twHoraire.setEllipsize(TruncateAt.MARQUEE);
                twHoraire.setFocusable(true);
                twHoraire.setFocusableInTouchMode(true);
                twHoraire.setHorizontallyScrolling(true);
                twHoraire.setText(horaire.text());
				HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getApplicationContext());
				horizontalScrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				horizontalScrollView.setBackgroundColor(Color.rgb(200, 200, 200));
                horizontalScrollView.addView(twHoraire);*/
                twHoraire.setText(horaire.text());
                layoutSeances.addView(twHoraire);
                layoutBloc.addView(layoutSeances);
			}

			
			// Notes du film
			Elements notes = bloc.select("li.infos_notes span"); 
			LinearLayout layoutNotes = new LinearLayout(context);            			
			layoutNotes.setOrientation(LinearLayout.HORIZONTAL);
			//layoutNotes.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			layoutNotes.setBackgroundColor(Color.BLACK);
			
			// TextView notePresse
            TextView twNotePresse = new TextView(context);
            twNotePresse.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
            twNotePresse.setTypeface(null, Typeface.BOLD);
            twNotePresse.setBackgroundColor(subtitleBackgroundColor);
            twNotePresse.setTextSize(noteTextSize);
            twNotePresse.setTextColor(otherTextColor);
            twNotePresse.setText("Presse ");
            
            // ImageView notePresse
            ImageView imNotePresse = new ImageView(context);
            imNotePresse.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
            imNotePresse.setBackgroundColor(simpleBackgroundColor);
            
            // TextView noteSpectateurs
            TextView twNoteSpectateurs = new TextView(context);
            twNoteSpectateurs.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
            twNoteSpectateurs.setTypeface(null, Typeface.BOLD);
            twNoteSpectateurs.setBackgroundColor(subtitleBackgroundColor);
            twNoteSpectateurs.setTextSize(noteTextSize);
            twNoteSpectateurs.setTextColor(otherTextColor);
            twNoteSpectateurs.setText("Spectateurs ");
            
            // ImageView noteSectateurs
            ImageView imNoteSpectateurs = new ImageView(context);
            imNoteSpectateurs.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
            imNoteSpectateurs.setBackgroundColor(simpleBackgroundColor);
            
			for (Element note : notes) {
                
				// Note Presse
				if (note.text().contains("Presse 1")) {
                    imNotePresse.setImageResource(R.drawable.rating_m1);
				} 
				else if (note.text().contains("Presse 2")) {
                    imNotePresse.setImageResource(R.drawable.rating_m2);
				}
				else if (note.text().contains("Presse 3")) {
                    imNotePresse.setImageResource(R.drawable.rating_m3);
				}
				else if (note.text().contains("Presse 4")) {
                    imNotePresse.setImageResource(R.drawable.rating_m4);
				}
    			// Note spectateurs
				else if (note.text().contains("Spectateurs 1")) {
                    imNoteSpectateurs.setImageResource(R.drawable.rating_m1);
				}
				else if (note.text().contains("Spectateurs 2")) {
                    imNoteSpectateurs.setImageResource(R.drawable.rating_m2);
				}
				else if (note.text().contains("Spectateurs 3")) {
                    imNoteSpectateurs.setImageResource(R.drawable.rating_m3);
				}
				else if (note.text().contains("Spectateurs 4")) {
                    imNoteSpectateurs.setImageResource(R.drawable.rating_m4);
				}
			}
            layoutNotes.addView(twNotePresse);
            layoutNotes.addView(imNotePresse);
            
            layoutNotes.addView(twNoteSpectateurs);
            layoutNotes.addView(imNoteSpectateurs);
            
            layoutBloc.addView(layoutNotes);
            layoutListeFilms.addView(layoutBloc);
		}

    }
}