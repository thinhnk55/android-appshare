package vn.vfossa.bluetooth;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import vn.vfossa.additionalclass.BluetoothShare;
import vn.vfossa.device.Device;
import vn.vfossa.shareapp.R;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class BluetoothManager {

	private Context context;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private static ArrayList<Device> devices; //Why static???
	public static final int REQUEST_ENABLE_BT = 1;

	public static final int BLUETOOTH_NOTSUPPORTED = -1;
	public static final int BLUETOOTH_NOTENABLED = 0;
	public static final int BLUETOOTH_ENABLED = 1;
	public static final int BLUETOOTH_ISDISCOVERING = 2;
	public static final String UUID0 = "c2915cd0-5c3c-11e3-949a-0800200c9a66";

	public BluetoothManager(Context context) {
		this.context = context;
		devices = new ArrayList<Device>();
	}

	public ArrayList<Device> getDevices() {
		return devices;
	}

	public void discovery() {
		bluetoothAdapter.startDiscovery();
	}

	public void updatePairedDevices() {
		devices.clear();

		Set<BluetoothDevice> pairedDevices = bluetoothAdapter
				.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				Device newDevice = new Device();
				newDevice.setName(device.getName());
				newDevice.setAddress(device.getAddress());

				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.device);
				newDevice.setImage(bitmap);
				devices.add(newDevice);
			}
		}
	}

	public int checkBlueToothState() {
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

	public void sendFileForBadVersion(Context context, File file, String address) {
		ContentValues values = new ContentValues();
		File addfile = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/img0.jpg");
		values.put(BluetoothShare.URI, Uri.fromFile(addfile).toString());
		values.put(BluetoothShare.URI, Uri.fromFile(file).toString());
		values.put(BluetoothShare.DESTINATION, address);
		values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
		Long ts = System.currentTimeMillis();
		values.put(BluetoothShare.TIMESTAMP, ts);
		context.getContentResolver().insert(BluetoothShare.CONTENT_URI, values);
	}

	public final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Device newDevice = new Device();
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Set<BluetoothDevice> pairedDevices = bluetoothAdapter
						.getBondedDevices();
				if (!pairedDevices.contains(device)) {
					newDevice.setName(device.getName());

					Bitmap itemImage = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.device);
					newDevice.setImage(itemImage);
					Toast.makeText(context, device.getName(),
							Toast.LENGTH_SHORT).show();
					devices.add(newDevice);
				}
			}
		}
	};

}
