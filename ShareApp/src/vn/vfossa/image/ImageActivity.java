package vn.vfossa.image;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.additionalclass.CheckableAndFilterableActivity;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageActivity extends Activity implements CheckableAndFilterableActivity {

	private ImageAdapter adapter;
	private ArrayList<FilesData> listImage;
	private GridView gridView;
	private static final String type = "image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.images_activity);

		setList();
		adapter = new ImageAdapter(this, listImage);

		// Set custom adapter to gridview
		gridView = (GridView) findViewById(R.id.gridViewImage);
		gridView.setAdapter(adapter);

		// Implement On Item click listener
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			}
		});

	}

	public void setList() {
		listImage = new ArrayList<FilesData>();
		DatabaseHandler db = new DatabaseHandler(this);
		List<FilesData> listImages = db.getAllFileWithType(type);
		for (FilesData song : listImages) {
			listImage.add(song);
		}
	}

	@Override
	public void Filter(CharSequence strSearch) {
		if (!adapter.isEmpty()) {
			adapter.getFilter().filter(strSearch);
		}
	}

	@Override
	public List<FilesData> getCheckedList() {
		return adapter.getCheckedList();
	}

}
