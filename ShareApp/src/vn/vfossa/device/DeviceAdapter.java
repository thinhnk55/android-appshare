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

package vn.vfossa.device;

import java.util.ArrayList;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends CheckableAdapter<Device> {

	private LayoutInflater mInflater;

	public DeviceAdapter(Context context, ArrayList<Device> objects) {
		super(context, R.layout.device_item_layout, objects);

		mInflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			// Inflate the view since it does not exist
			convertView = mInflater.inflate(R.layout.device_item_layout,
					parent, false);

			// Create and save off the holder in the tag so we get quick access
			// to inner fields
			// This must be done for performance reasons
			holder = new Holder();
			holder.nameDevice = (TextView) convertView
					.findViewById(R.id.deviceName);
			holder.imageDevice = (ImageView) convertView
					.findViewById(R.id.deviceImage);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxDevice);

			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}

		// Populate the text
		holder.nameDevice.setText(getItem(position).getName());
		holder.imageDevice.setImageBitmap(getItem(position).getImage());

		holder.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
				} else {
					checkedList.clear(); //checkedList keeps only one item.
					checkedList.add(getItem(position));
				}
			}
		});

		holder.checkBox.setChecked(checkedList.contains(getItem(position)));

		return convertView;
	}

	private static class Holder {
		public ImageView imageDevice;
		public TextView nameDevice;
		public CheckBox checkBox;
	}
}
