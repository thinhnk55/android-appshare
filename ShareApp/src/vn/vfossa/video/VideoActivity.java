package vn.vfossa.video;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.additionalclass.CheckableAndFilterableActivity;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class VideoActivity extends ListActivity implements CheckableAndFilterableActivity {

	private VideoAdapter adapter;
	private ArrayList<FilesData> videos = new ArrayList<FilesData>();
	private ListView listView;
	private Context context;
	private static final String type = "video";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		context = this;

		DatabaseHandler db = new DatabaseHandler(context);

		List<FilesData> listVideos = db.getAllFileWithType(type);
		db.close();
		setList(listVideos);
		listView.setItemsCanFocus(false);
	}

	public void setList(List<FilesData> listVideos) {

		for (FilesData song : listVideos) {
			videos.add(song);
		}
		adapter = new VideoAdapter(VideoActivity.this, videos);

		listView = getListView();
		listView.setAdapter(adapter);
	}
	
	@Override
	public List<FilesData> getCheckedList(){
		return adapter.getCheckedList();
	}
	
	@Override
	public void Filter(CharSequence strSearch){
		if (!adapter.isEmpty()){
			adapter.getFilter().filter(strSearch);
		}
	}
}
