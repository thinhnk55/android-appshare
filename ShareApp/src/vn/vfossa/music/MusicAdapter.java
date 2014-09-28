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

import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends CheckableAdapter<FilesData> {
	public static final String TAG = MusicAdapter.class.getSimpleName();

	private LayoutInflater mInflator;

	public MusicAdapter(Context context, ArrayList<FilesData> song) {
		super(context, R.layout.media_item_layout, song);
		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final SongHolder holder;
		View rowView = view;
		if (rowView == null) {

			rowView = mInflator.inflate(R.layout.media_item_layout, parent,
					false);
			holder = new SongHolder();
			holder.songTitle = (TextView) rowView.findViewById(R.id.mediaTitle);
			holder.size = (TextView) rowView.findViewById(R.id.mediaSize);
			holder.checkBox = (CheckBox) rowView
					.findViewById(R.id.checkBoxItem);
			holder.imageView = (ImageView) rowView
					.findViewById(R.id.image_media);
			rowView.setTag(holder);
		} else {
			holder = (SongHolder) rowView.getTag();
		}

		holder.songTitle.setText(getItem(position).getName());
		DecimalFormat dec = new DecimalFormat("0.00");
		holder.size.setText(dec.format(getItem(position).getSize()).concat(
				" MB"));

		if (getItem(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(getItem(position)
					.getImage(), 0, getItem(position).getImage().length,
					options);

			if (bitmap != null) {
				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);
				holder.imageView.setImageBitmap(itemImage);
			} else {
				holder.imageView.setImageResource(R.drawable.music);
			}
		} else {
			holder.imageView.setImageResource(R.drawable.music);
		}

		holder.checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
				} else {
					checkedList.add(getItem(position));
				}
			}
		});

		holder.checkBox.setChecked(checkedList.contains(getItem(position)));

		return rowView;
	}

	static class SongHolder {
		CheckBox checkBox;
		TextView size;
		TextView songTitle;
		ImageView imageView;
	}

}