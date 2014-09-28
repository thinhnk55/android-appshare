package vn.vfossa.music;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.additionalclass.CheckableAndFilterableActivity;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import vn.vfossa.util.Utils;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class MusicActivity extends ListActivity implements CheckableAndFilterableActivity {
	public static final String TAG = MusicActivity.class.getName();

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
		Utils.log(TAG, "List song: " + listSongs.size());

		db.close();
		setList(listSongs);
		listView.setItemsCanFocus(false);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				adapter.changeCheckedState(position);
//			}
//		});

	}

	public void setList(List<FilesData> listSongs) {

		for (FilesData song : listSongs) {
			listMusics.add(song);
		}
		adapter = new MusicAdapter(MusicActivity.this, listMusics);
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
