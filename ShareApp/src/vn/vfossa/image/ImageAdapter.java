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

package vn.vfossa.image;

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

public class ImageAdapter extends CheckableAdapter<FilesData> {

	private LayoutInflater mInflator;

	public ImageAdapter(Context context, ArrayList<FilesData> listImage) {
		super(context, R.layout.item_layout, listImage);

		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder view;

		if (convertView == null) {
			view = new ViewHolder();
			convertView = mInflator
					.inflate(R.layout.item_layout, parent, false);

			view.imgViewItem = (ImageView) convertView
					.findViewById(R.id.imageItem);
			view.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		if (getItem(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(getItem(position)
					.getImage(), 0, getItem(position).getImage().length,
					options);

			if (bitmap != null) {
				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);
				view.imgViewItem.setImageBitmap(itemImage);
			} else {
				view.imgViewItem.setImageResource(R.drawable.image);
			}
		} else {
			view.imgViewItem.setImageResource(R.drawable.image);
		}
		view.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
				} else {
					checkedList.add(getItem(position));
				}
			}
		});

		view.checkBox.setChecked(checkedList.contains(getItem(position)));

		// notifyDataSetChanged();
		return convertView;
	}
}
