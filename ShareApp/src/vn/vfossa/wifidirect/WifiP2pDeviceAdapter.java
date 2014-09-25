package vn.vfossa.wifidirect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vn.vfossa.shareapp.R;
import vn.vfossa.util.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class WifiP2pDeviceAdapter extends BaseAdapter{
	
	private List<WifiP2pDevice> mDeviceList;
    private WifiP2pDevice device;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<WifiP2pDevice> checkedList = new ArrayList<WifiP2pDevice>();

    public WifiP2pDeviceAdapter(Context context, List<WifiP2pDevice> devicelist) {
        this.mDeviceList = devicelist;
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void clear() {
        mDeviceList.clear();
    }

    public void addAll(Collection<? extends WifiP2pDevice> collection) {
        mDeviceList.addAll(collection);
    }

    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mDeviceList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	Holder holder;
    	if (convertView == null){
    		convertView = mLayoutInflater.inflate(R.layout.device_item_layout,
					parent, false);
    		holder = new Holder();
			holder.nameDevice = (TextView) convertView
					.findViewById(R.id.deviceName);
			holder.imageDevice = (ImageView) convertView
					.findViewById(R.id.deviceImage);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxDevice);

			convertView.setTag(holder);
    	}
    	else{
    		holder = (Holder) convertView.getTag();
    	}
    	
    	device = mDeviceList.get(position);
    	holder.nameDevice.setText(device.deviceName);
    	Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.device);
		holder.imageDevice.setImageBitmap(bitmap);
		
		holder.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(mDeviceList.get(position));
					Utils.showToast(context, "remove an item");
				} else {
					checkedList.add(mDeviceList.get(position));
					Utils.showToast(context, "add an item");
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
    
    public List<WifiP2pDevice> getCheckedList() {
		return checkedList;
	}

}
