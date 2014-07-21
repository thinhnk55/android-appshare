package vn.vfossa.video;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.music.MusicAdapter;
import vn.vfossa.shareapp.R;
import vn.vfossa.shareapp.R.layout;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class VideoActivity extends Activity {

	private VideoAdapter adapter;
	private ArrayList<Bitmap> listImage;
	private GridView gridView;
	private int[] checkedState;
	private static final String type = "video";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videos_activity);

		setList();

		checkedState = new int[listImage.size()];
		for (int i = 0; i < listImage.size(); i++) {
			checkedState[i] = 0;
		}

		adapter = new VideoAdapter(this, listImage, checkedState);

		// Set custom adapter to gridview
		gridView = (GridView) findViewById(R.id.gridViewVideo);
		if (listImage.size()>0){
			gridView.setAdapter(adapter);
		}

		// Implement On Item click listener
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
//				Toast.makeText(MusicActivity.this, "check",
//				Toast.LENGTH_SHORT).show();
				//notifyDataSetChanged();
			}
		});

	}

	public void setList() {
		listImage = new ArrayList<Bitmap>();

		DatabaseHandler db = new DatabaseHandler(this);
		List<FilesData> listApps = db.getAllFileWithType(type);

		for (FilesData file : listApps) {
			if (file.getImage() != null) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;

				Bitmap bitmap = BitmapFactory.decodeByteArray(file.getImage(),
						0, file.getImage().length, options);

				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);

				listImage.add(itemImage);
			} else {

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.music);
				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);

				listImage.add(itemImage);
			}

		}
	}
}
