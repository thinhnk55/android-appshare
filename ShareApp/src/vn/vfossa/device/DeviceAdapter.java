package vn.vfossa.device;

import java.util.ArrayList;

import vn.vfossa.shareapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends ArrayAdapter<Device> {

	private LayoutInflater mInflater;
	private static int[] checkedState;
	private boolean[] checkboxChecked;

	public DeviceAdapter(Context context, ArrayList<Device> objects) {
		super(context, R.layout.device_item_layout, objects);

		mInflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.checkboxChecked = new boolean[100];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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

		holder.nameDevice.setId(position);
		holder.imageDevice.setId(position);
		holder.checkBox.setId(position);
		holder.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (checkboxChecked[id]) {
					cb.setChecked(false);
					checkboxChecked[id] = false;
				} else {
					cb.setChecked(true);
					checkboxChecked[id] = true;
				}

			}
		});
		holder.checkBox.setChecked(checkboxChecked[position]);

		return convertView;
	}

	private static class Holder {
		public ImageView imageDevice;
		public TextView nameDevice;
		public CheckBox checkBox;
	}

	public boolean getCheckedState(int position) {
		return checkboxChecked[position];
	}

	public int getNumberChecked() {
		int count = 0;
		for (int i = 0; i < getCount(); i++) {
			if (checkboxChecked[i]) {
				count++;
			}
		}
		return count;
	}

	public String[] getDeviceChecked() {
		String[] addresses = new String[getNumberChecked()];

		int count = 0;
		for (int i = 0; i < getCount(); i++) {
			if (checkboxChecked[i]) {
				addresses[count++] = getItem(i).getAddress();
			}
		}
		return addresses;

	}

}
