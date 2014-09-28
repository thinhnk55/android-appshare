package vn.vfossa.shareapp;

import java.io.ByteArrayOutputStream;
import java.io.File;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

public class StartUpActivity extends Activity {
	private static final String MEDIA_PATH = Environment
			.getExternalStorageDirectory().getPath();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
		
		new ScanFileTask().execute();
	}

	private class ScanFileTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			scanDirectory(new File(MEDIA_PATH));			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		private void scanDirectory(File directory) {
			DatabaseHandler db = new DatabaseHandler(StartUpActivity.this);
			if (directory != null) {
				File[] listFiles = directory.listFiles();
				if (listFiles != null && listFiles.length > 0) {
					for (File file : listFiles) {
						if (!file.getName().equals(".thumbnails")
								&& !file.getName().equals("cache")
								&& !file.getName().startsWith(".")
								&& !file.getName().startsWith("com.")) {
							if (file.isDirectory() && db.checkPath(file.getPath())) {
								scanDirectory(file);
							} else {
								addFileToList(file);
							}
						}

					}
				}
			}
			db.close();
		}

		private void addFileToList(File file) {
			DatabaseHandler db = new DatabaseHandler(StartUpActivity.this);

			if (file.getName().endsWith(".mp3")) {
				if (db.checkPath(file.getPath())) {
					String songName = file.getName().substring(0,
							(file.getName().length() - 4));
					String songPath = file.getPath();
					MediaMetadataRetriever media = new MediaMetadataRetriever();
					media.setDataSource(songPath);
					byte[] data = media.getEmbeddedPicture();
					media.release();
					float size = (float) (file.length()) / (1024 * 1024);
					db.addFileData(new FilesData("music", songName, songPath, data,
							size));
					db.close();
				}
			}

			if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")
					|| file.getName().endsWith(".png")) {
				if (db.checkPath(file.getPath())) {
					String imageName;
					if (file.getName().endsWith(".jpeg")) {
						imageName = file.getName().substring(0,
								(file.getName().length() - 5));
					} else {
						imageName = file.getName().substring(0,
								(file.getName().length() - 4));
					}
					String imagePath = file.getPath();
					File imgFile = new File(imagePath);

					Bitmap itemImage = null;

					if (imgFile.exists()) {

						Bitmap bitmap = BitmapFactory.decodeFile(imgFile
								.getAbsolutePath());
						if (bitmap == null) {
							db.close();
							return;
						}
						itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
								true);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						itemImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] byteArray = stream.toByteArray();

						float size = (float) (file.length()) / (1024 * 1024);
						db.addFileData(new FilesData("image", imageName, imagePath,
								byteArray, size));
					}
					
					db.close();
				}
			}
			if (file.getName().endsWith(".flv") || file.getName().endsWith(".mp4")
					|| file.getName().endsWith(".mkv")
					|| file.getName().endsWith(".3gp")) {
				if (db.checkPath(file.getPath())) {

					String videoName = file.getName().substring(0,
							(file.getName().length() - 4));
					String videoPath = file.getPath();
					MediaMetadataRetriever media = new MediaMetadataRetriever();
					media.setDataSource(videoPath);
					byte[] data = media.getEmbeddedPicture();

					media.release();
					float size = (float) (file.length()) / (1024 * 1024);
					db.addFileData(new FilesData("video", videoName, videoPath,
							data, size));
					db.close();
				}
			}
		}
	}
}
