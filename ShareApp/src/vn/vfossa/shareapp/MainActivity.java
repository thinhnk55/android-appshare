package vn.vfossa.shareapp;

import java.io.ByteArrayOutputStream;
import java.io.File;

import vn.vfossa.app.AppActivity;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.image.ImageActivity;
import vn.vfossa.music.MusicActivity;
import vn.vfossa.video.VideoActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	private TabHost tabHost;
	private static final String MEDIA_PATH = "/sdcard/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		File home = new File(MEDIA_PATH);

		scanDirectory(home);

		tabHost = getTabHost();

		TabSpec appspec = tabHost.newTabSpec("UngDung");
		appspec.setIndicator("Ứng dụng");
		Intent appsIntent = new Intent(this, AppActivity.class);
		appspec.setContent(appsIntent);

		TabSpec photospec = tabHost.newTabSpec("HinhAnh");
		photospec.setIndicator("Hình ảnh");
		Intent photosIntent = new Intent(this, ImageActivity.class);
		photospec.setContent(photosIntent);

		TabSpec songspec = tabHost.newTabSpec("NgheNhac");
		songspec.setIndicator("Nghe nhạc");
		Intent songsIntent = new Intent(this, MusicActivity.class);
		songspec.setContent(songsIntent);

		TabSpec videospec = tabHost.newTabSpec("XemPhim");
		videospec.setIndicator("Xem phim");
		Intent videosIntent = new Intent(this, VideoActivity.class);
		videospec.setContent(videosIntent);

		tabHost.addTab(appspec);
		tabHost.addTab(photospec);
		tabHost.addTab(songspec);
		tabHost.addTab(videospec);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void scanDirectory(File directory) {
		DatabaseHandler db = new DatabaseHandler(MainActivity.this);
		if (directory != null) {
			File[] listFiles = directory.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File file : listFiles) {
					if (file.isDirectory() && db.checkPath(file.getPath())) {
						scanDirectory(file);
					} else {
						addFileToList(file);
					}

				}
			}
		}
		db.close();
	}

	private void addFileToList(File file) {
		DatabaseHandler db = new DatabaseHandler(MainActivity.this);
		
		if (file.getName().endsWith(".apk")) {
			if (db.checkPath(file.getPath())) {
				String appName = file.getName().substring(0,
						(file.getName().length() - 4));
				String appPath = file.getPath();
				
				db.addFileData(new FilesData("app", appName, appPath, null,
						0));
				db.close();
			}
		}

		if (file.getName().endsWith(".mp3")) {
			if (db.checkPath(file.getPath())) {
				String songName = file.getName().substring(0,
						(file.getName().length() - 4));
				String songPath = file.getPath();
				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(songPath);
				byte[] data = media.getEmbeddedPicture();
				media.release();
				float size =(float) (file.length())/(1024*1024);
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

					itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
							true);
				}

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				itemImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				db.addFileData(new FilesData("image", imageName, imagePath,
						byteArray, 0));
				db.close();
			}
		}

		if (file.getName().endsWith(".flv") || file.getName().endsWith(".mp4")
				|| file.getName().endsWith(".mkv")
				|| file.getName().endsWith(".3gp")) {
			Log.e("video", file.getName());
			if (db.checkPath(file.getPath())) {
				
				String videoName = file.getName().substring(0,
						(file.getName().length() - 4));
				String videoPath = file.getPath();
				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(videoPath);
				byte[] data = media.getEmbeddedPicture();

				media.release();
				db.addFileData(new FilesData("video", videoName, videoPath,
						data, 0));
				db.close();
			}
		}
	}

}
