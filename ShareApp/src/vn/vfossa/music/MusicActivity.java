package vn.vfossa.music;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import vn.vfossa.shareapp.R.drawable;
import vn.vfossa.shareapp.R.id;
import vn.vfossa.shareapp.R.layout;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MusicActivity extends Activity {

	private MusicAdapter adapter;
	private ArrayList<Bitmap> listImage;
	private GridView gridView;
	private int[] checkedState;
	private static final String type = "music";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);

		setList();

		checkedState = new int[listImage.size()];
		for (int i = 0; i < listImage.size(); i++) {
			checkedState[i] = 0;
		}

		adapter = new MusicAdapter(this, listImage, checkedState);

		// Set custom adapter to gridview
		gridView = (GridView) findViewById(R.id.gridViewMusic);
		gridView.setAdapter(adapter);

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