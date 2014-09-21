package vn.vfossa.music;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MusicActivity extends ListActivity {

	private MusicAdapter adapter;
	private ArrayList<FilesData> listMusics = new ArrayList<FilesData>();
	private ListView listView;
	private Context context;
	private static final String type = "music";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		context = this;

		DatabaseHandler db = new DatabaseHandler(context);

		List<FilesData> listSongs = db.getAllFileWithType(type);

		db.close();
		setList(listSongs);
		listView.setItemsCanFocus(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.changeCheckedState(position);
			}
		});

	}

	public void setList(List<FilesData> listSongs) {

		for (FilesData song : listSongs) {
			listMusics.add(song);
		}
		if (listSongs.size() > 0) {
			adapter = new MusicAdapter(MusicActivity.this, listMusics);
			adapter.createCheckedState(listSongs.size());
		}
		listView = getListView();
		listView.setAdapter(adapter);
	}

	public int[] getMusicFilesChecked() {
//		SparseBooleanArray checked = listView.getCheckedItemPositions();
//		
//		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
//			if (checked.get(i)) {
//				
//			}
//		}
		return adapter.getIdChecked();
	}
}
