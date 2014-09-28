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

package vn.vfossa.video;

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

public class VideoActivity extends ListActivity implements CheckableAndFilterableActivity {

	private VideoAdapter adapter;
	private ArrayList<FilesData> allVideos;
	private ArrayList<FilesData> showVideos;
	private ListView listView;
	private Context context;
	private static final String type = "video";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		context = this;

		DatabaseHandler db = new DatabaseHandler(context);
		allVideos = db.getAllFileWithType(type);
		db.close();
		
		initShowList(allVideos);
		listView.setItemsCanFocus(false);
	}

	public void initShowList(List<FilesData> listVideos) {
		showVideos = new ArrayList<FilesData>();
		for (FilesData song : listVideos) {
			showVideos.add(song);
		}
		adapter = new VideoAdapter(VideoActivity.this, showVideos);

		listView = getListView();
		listView.setAdapter(adapter);
	}
	
	@Override
	public List<FilesData> getCheckedList(){
		return adapter.getCheckedList();
	}
	
	@Override
	public void filter(CharSequence constraint){
		if (! allVideos.isEmpty()){
			showVideos.clear();
			if (constraint != null && constraint.length() > 0){
				constraint = constraint.toString().toLowerCase(Locale.getDefault());
				for (FilesData video : allVideos){
					if (video.getName().toLowerCase(Locale.getDefault()).contains(constraint)){
						showVideos.add(video);
					}
				}
			} else {
				for (FilesData song : allVideos){
					showVideos.add(song);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
}
