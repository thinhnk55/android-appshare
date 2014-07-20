package vn.vfossa.shareapp;

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
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
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
		if (directory != null) {
			File[] listFiles = directory.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File file : listFiles) {
					if (file.isDirectory()) {
						scanDirectory(file);
					} else {
						addFileToList(file);
					}

				}
			}
		}
	}

	private void addFileToList(File file) {
		DatabaseHandler db = new DatabaseHandler(MainActivity.this);
		
		if (file.getName().endsWith(".mp3")) {
			if (db.checkPath(file.getPath())) {
				String songName = file.getName().substring(0,
						(file.getName().length() - 4));
				String songPath = file.getPath();
				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(songPath);
				byte[] data = media.getEmbeddedPicture();
				media.release();
				db.addFileData(new FilesData("music", songName, songPath, data,
						0));
				db.close();
			}
		}
		
		if (file.getName().endsWith(".apk")) {
			if (db.checkPath(file.getPath())) {
				String songName = file.getName().substring(0,
						(file.getName().length() - 4));
				String songPath = file.getPath();
				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(songPath);
				byte[] data = media.getEmbeddedPicture();
				media.release();
				db.addFileData(new FilesData("app", songName, songPath, data,
						0));
				db.close();
			}
		}
	}
    
}
