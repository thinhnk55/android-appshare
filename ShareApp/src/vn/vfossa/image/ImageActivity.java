package vn.vfossa.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	private ArrayList<FilesData> allImages;
	private ArrayList<FilesData> showImages;
	private GridView gridView;
	private static final String type = "image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.images_activity);

		initShowList();
		adapter = new ImageAdapter(this, showImages);

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

	public void initShowList() {
		showImages = new ArrayList<FilesData>();
		DatabaseHandler db = new DatabaseHandler(this);
		allImages = db.getAllFileWithType(type);
		for (FilesData song : allImages) {
			showImages.add(song);
		}
	}

	@Override
	public void filter(CharSequence constraint) {
		if (! allImages.isEmpty()){
			showImages.clear();
			if (constraint != null && constraint.length() > 0){
				constraint = constraint.toString().toLowerCase(Locale.getDefault());
				for (FilesData image : allImages){
					if (image.getName().toLowerCase(Locale.getDefault()).contains(constraint)){
						showImages.add(image);
					}
				}
			} else {
				for (FilesData song : allImages){
					showImages.add(song);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public List<FilesData> getCheckedList() {
		return adapter.getCheckedList();
	}

}
