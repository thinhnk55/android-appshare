package vn.vfossa.shareapp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	private TabHost tabHost;
	private ViewGroup appView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
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
    
}
