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

package vn.vfossa.app;

import java.util.ArrayList;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ApplicationAdapter extends CheckableAdapter<ApplicationInfo> {
	private LayoutInflater mInflator;

	// private static final String type = "app";

	public ApplicationAdapter(Context context,
			ArrayList<ApplicationInfo> appsList) {
		super(context, R.layout.item_layout, appsList);
		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public static class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflator
					.inflate(R.layout.item_layout, parent, false);

			holder.imgViewItem = (ImageView) convertView
					.findViewById(R.id.imageItem);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ApplicationInfo appInfo = getItem(position);
		if (appInfo != null) {
			holder.imgViewItem.setImageDrawable(appInfo.loadIcon(getContext()
					.getPackageManager()));
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

		holder.checkBox.setChecked(checkedList.contains(appInfo));

		return convertView;
	}
};