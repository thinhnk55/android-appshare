package vn.vfossa.video;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class VideoActivity extends ListActivity {

	private VideoAdapter adapter;
	private ArrayList<FilesData> videos = new ArrayList<FilesData>();
	private ListView listView;
	//private int[] checkedState;
	private Context context;
	private static final String type = "video";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		context = this;

		DatabaseHandler db = new DatabaseHandler(context);

		List<FilesData> listVideos = db.getAllFileWithType(type);
//		checkedState = new int[listVideos.size()];
//		for (int i = 0; i < listVideos.size(); i++) {
//			checkedState[i] = 0;
//		}
		db.close();
		setList(listVideos);
		listView.setItemsCanFocus(false);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (checkedState[position] == 0)
//					checkedState[position] = 1;
//				else
//					checkedState[position] = 0;
//			}
//		});

	}

	public void setList(List<FilesData> listVideos) {

		for (FilesData song : listVideos) {
			videos.add(song);
		}
		adapter = new VideoAdapter(VideoActivity.this, videos);

		listView = getListView();
		listView.setAdapter(adapter);
	}
	
	public List<FilesData> getCheckedList(){
		return adapter.getCheckedList();
	}
}
