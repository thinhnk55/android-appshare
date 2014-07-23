package vn.vfossa.video;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ListActivity;
import android.content.Context;

public class VideoActivity extends ListActivity {

	private VideoAdapter adapter;
	private ArrayList<FilesData> videos = new ArrayList<FilesData>();
	private ListView listView;
	private int[] checkedState;
	private Context context;
	private static final String type = "video";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		context = this;

		DatabaseHandler db = new DatabaseHandler(context);

		List<FilesData> listVideos = db.getAllFileWithType(type);
		checkedState = new int[listVideos.size()];
		for (int i = 0; i < listVideos.size(); i++) {
			checkedState[i] = 0;
		}
		db.close();
		setList(listVideos);
		listView.setItemsCanFocus(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (checkedState[position] == 0)
					checkedState[position] = 1;
				else
					checkedState[position] = 0;
			}
		});

	}

	public void setList(List<FilesData> listSongs) {

		for (FilesData song : listSongs) {
			videos.add(song);
		}
		if (listSongs.size() > 0) {
			adapter = new VideoAdapter(VideoActivity.this, videos, checkedState);
		}

		listView = getListView();
		listView.setAdapter(adapter);
	}
}
