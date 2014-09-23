package vn.vfossa.bluetooth;

import it.sephiroth.android.library.widget.HListView;

import java.util.ArrayList;
import java.util.Set;

import vn.vfossa.device.Device;
import vn.vfossa.shareapp.R;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class BluetoothSender {

	private Context context;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private ArrayList<Device> devices;
	public static final int REQUEST_ENABLE_BT = 1;

	public static final int BLUETOOTH_NOTSUPPORTED = -1;
	public static final int BLUETOOTH_NOTENABLED = 0;
	public static final int BLUETOOTH_ENABLED = 1;
	public static final int BLUETOOTH_ISDISCOVERING = 2;

	/*
	 * 
	 */
	public BluetoothSender(Context context) {
		this.context = context;
		this.devices = new ArrayList<Device>();
	}

	/*
	 * 
	 */
	public ArrayList<Device> getDevices() {
		return devices;
	}

	/*
	 * 
	 */
	public void discovery() {
		bluetoothAdapter.startDiscovery();
	}

	/*
	 * 
	 */
	public Set<BluetoothDevice> getBoundedDevices() {
		return bluetoothAdapter.getBondedDevices();
	}

	/*
	 * 
	 */
	public void getPairedDevices() {
		Set<BluetoothDevice> pairedDevices = getBoundedDevices();

		if (pairedDevices.size() > 0) {
			
			for (BluetoothDevice device : pairedDevices) {
				Device newDevice = new Device();
				newDevice.setName(device.getName());
				newDevice.setAddress(device.getAddress());

				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.device);
				// Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100,
				// 100,
				// true);
				newDevice.setImage(bitmap);
				this.devices.add(newDevice);
				// deviceAdapter.notifyDataSetChanged();
			}
		}
	}

	/*
	 * 
	 */
	public int CheckBlueToothState() {
		if (bluetoothAdapter == null) {
			Toast.makeText(context, "This device do not support Bluetooth",
					Toast.LENGTH_SHORT).show();
			return BLUETOOTH_NOTSUPPORTED;
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					Toast.makeText(
							context,
							"Bluetooth is currently in device discovery process.",
							Toast.LENGTH_SHORT).show();
					return BLUETOOTH_ISDISCOVERING;
				} else {
					Toast.makeText(context, "Bluetooth is Enabled.",
							Toast.LENGTH_SHORT).show();
					// btScan.setEnabled(true);
					return BLUETOOTH_ENABLED;
				}
			} else {
				Toast.makeText(context, "Bluetooth is NOT Enabled!",
						Toast.LENGTH_SHORT).show();
				// startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				return BLUETOOTH_NOTENABLED;
			}
		}
	}

}
