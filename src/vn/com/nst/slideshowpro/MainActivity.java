package vn.com.nst.slideshowpro;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import vn.com.nst.slideshowpro.R.id;

public class MainActivity extends Activity {
	CheckBox chkAuto;
	ImageView imgHinhAnh;
	ImageButton btnPrevious, btnNext;
	ArrayList<String> arrURL;
	ProgressDialog progressDialog;
	Timer timer = null;
	TimerTask timerTask;
	int position = 0;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setControls();
		setEvents();
	}

	private void setEvents() {
		chkAuto.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked == true) {
					xuLyAuto();
				} else {
					xuLyKhongAuto();
				}
			}
		});

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				xuLyNext();
			}

		});
		btnPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				xuLyPrevious();
			}

		});

	}

	private void setControls() {
		chkAuto = (CheckBox) findViewById(R.id.chkAuto);
		imgHinhAnh = (ImageView) findViewById(R.id.imgHinh);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		progressDialog = new ProgressDialog(MainActivity.this);
		handler = new Handler();
		arrURL = new ArrayList<String>();
		arrURL.add("http://www.freeiconspng.com/uploads/animals-dog-icon-15.png");
		arrURL.add("https://cdn2.iconfinder.com/data/icons/animals/256/Panda.png");
		arrURL.add("http://icons.iconarchive.com/icons/martin-berube/animal/256/ant-icon.png");
		arrURL.add("https://cdn2.iconfinder.com/data/icons/animals/256/Butterfly.png");
		arrURL.add("https://cdn2.iconfinder.com/data/icons/animals/256/Dolphin.png");
		/*
		 * ImageTask startTask = new ImageTask();
		 * startTask.execute(arrURL.get(position));
		 */
		xuLyAuto();
	}

	private void xuLyNext() {
		position++;
		if (position == arrURL.size()) {
			position = 0;
		}
		ImageTask nextTask = new ImageTask();
		nextTask.execute(arrURL.get(position));
	}

	private void xuLyPrevious() {
		position--;
		if (position < 0) {
			position = arrURL.size() - 1;
		}
		ImageTask previousTask = new ImageTask();
		previousTask.execute(arrURL.get(position));
	}

	private void xuLyKhongAuto() {
		if (timer != null) {
			timer.cancel();
		}
		btnNext.setEnabled(true);
		btnPrevious.setEnabled(true);
	}

	private void xuLyAuto() {
		btnNext.setEnabled(false);
		btnPrevious.setEnabled(false);
		timerTask = new TimerTask() {

			@Override
			public void run() {
				/*
				 * handler.post(new Runnable() {
				 * 
				 * @Override public void run() { xuLyNext(); } });
				 */
				runOnUiThread(new Runnable() {
					public void run() {
						xuLyNext();
					}
				});
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 0, 5000);
	}

	class ImageTask extends AsyncTask<String, Boolean, Bitmap> {

		@Override
		protected void onPreExecute() {
			// should use show method as a static method
			progressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Please wait a minute");
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String imageUrl = params[0];
			boolean finish = false;
			Bitmap bitmap = null;
			try {
				// ImageView i = (ImageView)findViewById(R.id.image);
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
				// imgHinhAnh.setImageBitmap(bitmap);
				finish = true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			publishProgress(finish);
			return bitmap;
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			/*
			 * if(values[0]==true){ progressDialog.dismiss(); }
			 */
			progressDialog.dismiss();
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// progressDialog.dismiss();
			imgHinhAnh.setImageBitmap(result);
			super.onPostExecute(result);
		}
	}
}
