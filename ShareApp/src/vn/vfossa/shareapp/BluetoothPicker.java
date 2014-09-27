package vn.vfossa.shareapp;

import java.util.List;

import vn.vfossa.device.Device;
import vn.vfossa.device.DeviceAdapter;
import vn.vfossa.shareapp.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BluetoothPicker extends Activity {

	private String mLaunchPackage;
	private String mLaunchClass;

	public static final String EXTRA_NEED_AUTH = "android.bluetooth.devicepicker.extra.NEED_AUTH";
	public static final String EXTRA_FILTER_TYPE = "android.bluetooth.devicepicker.extra.FILTER_TYPE";
	public static final String EXTRA_LAUNCH_PACKAGE = "android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE";
	public static final String EXTRA_LAUNCH_CLASS = "android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS";

	public static final String ACTION_DEVICE_SELECTED = "android.bluetooth.devicepicker.action.DEVICE_SELECTED";
	public static final String ACTION_LAUNCH = "android.bluetooth.devicepicker.action.LAUNCH";

	/** Ask device picker to show all kinds of BT devices */
	public static final int FILTER_TYPE_ALL = 0;
	/** Ask device picker to show BT devices that support AUDIO profiles */
	public static final int FILTER_TYPE_AUDIO = 1;
	/** Ask device picker to show BT devices that support Object Transfer */
	public static final int FILTER_TYPE_TRANSFER = 2;
	
	public static final String DEVICE_ADDRESS = "00:26:5E:DE:49:73";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popup_bluetooth);

		BluetoothDevice device = null;
		DeviceAdapter deviceAdapter = MainActivity.getDeviceAdapter();
		List<Device> devices = deviceAdapter.getCheckedList();

		if (devices.isEmpty()) {
			Log.e("PRINT", "Failed to get selected bluetooth device!");
			finish();
			return;
		} else {
			for (BluetoothDevice itDevice : BluetoothAdapter
					.getDefaultAdapter().getBondedDevices()) {
				if (itDevice.getAddress().equals(devices.get(0).getAddress())) {
					device = itDevice;
				}
			}
		}
		
		

		Intent intent = getIntent();
		mLaunchPackage = intent.getStringExtra(EXTRA_LAUNCH_PACKAGE);
		mLaunchClass = intent.getStringExtra(EXTRA_LAUNCH_CLASS);

		sendDevicePickedIntent(device);

		finish();
	}

	private void sendDevicePickedIntent(BluetoothDevice device) {
		Intent intent = new Intent(ACTION_DEVICE_SELECTED);
		intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);

		if (mLaunchPackage != null && mLaunchClass != null) {
			intent.setClassName(mLaunchPackage, mLaunchClass);
		}

		sendBroadcast(intent);
	}
}
