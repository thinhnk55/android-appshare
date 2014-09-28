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
