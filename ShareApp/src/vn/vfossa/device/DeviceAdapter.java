package vn.vfossa.device;

import java.util.ArrayList;

import vn.vfossa.shareapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends ArrayAdapter<Device> {

	private LayoutInflater layoutInflater;

	public DeviceAdapter(Context context, ArrayList<Device> objects) {
		super(context, R.layout.device_item_layout, objects);

		layoutInflater = (LayoutInflater) getContext().getSystemService(
				context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = layoutInflater.inflate(R.layout.device_item_layout, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.nameDevice = (TextView) convertView.findViewById(R.id.deviceName);
            holder.imageDevice = (ImageView) convertView.findViewById(R.id.deviceImage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        holder.nameDevice.setText(getItem(position).getName());
        holder.imageDevice.setImageBitmap(getItem(position).getImage());

        return convertView;
	}
	
	private static class Holder {
		public ImageView imageDevice;
        public TextView nameDevice;
    }

}
