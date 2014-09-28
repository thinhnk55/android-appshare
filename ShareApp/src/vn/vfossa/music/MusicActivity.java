/****************************************************************************** * 
* Copyright (C) 2013   Nguyen Khanh Thinh, Nguyen Van Dai and Contributors
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details.  
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
* Any further request, feel freely to mhst1024-10@googlegroups.com 
*************************************************************************************************/

package vn.vfossa.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.vfossa.additionalclass.CheckableAndFilterableActivity;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class MusicActivity extends ListActivity implements CheckableAndFilterableActivity {
	public static final String TAG = MusicActivity.class.getName();

	private MusicAdapter adapter;
	private ArrayList<FilesData> allSongs;
	private ArrayList<FilesData> showSongs;
	private ListView listView;
	private Context context;
	private static final String type = "music";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		context = this;

		DatabaseHandler db = new DatabaseHandler(context);
		allSongs = db.getAllFileWithType(type);
		db.close();
		
		initShowList(allSongs);
		
		listView.setItemsCanFocus(false);
	}

	public void initShowList(List<FilesData> listSongs) {
		showSongs = new ArrayList<FilesData>();
		for (FilesData song : listSongs) {
			showSongs.add(song);
		}
		adapter = new MusicAdapter(MusicActivity.this, showSongs);
		listView = getListView();
		listView.setAdapter(adapter);
	}

	@Override
	public List<FilesData> getCheckedList(){
		return adapter.getCheckedList();
	}
	
	@Override
	public void filter(CharSequence constraint){
		if (! allSongs.isEmpty()){
			showSongs.clear();
			if (constraint != null && constraint.length() > 0){
				constraint = constraint.toString().toLowerCase(Locale.getDefault());
				for (FilesData song : allSongs){
					if (song.getName().toLowerCase(Locale.getDefault()).contains(constraint)){
						showSongs.add(song);
					}
				}
			} else {
				for (FilesData song : allSongs){
					showSongs.add(song);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
}
