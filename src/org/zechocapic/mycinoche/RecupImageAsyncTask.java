package org.zechocapic.mycinoche;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class RecupImageAsyncTask extends AsyncTask<String, Void, Bitmap>{
	private ImageView imageView;
	
	public RecupImageAsyncTask(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		String url = urls[0];
		Bitmap image = null;
		try {
			InputStream in = new URL(url).openStream();
			image = BitmapFactory.decodeStream(in);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}

}
