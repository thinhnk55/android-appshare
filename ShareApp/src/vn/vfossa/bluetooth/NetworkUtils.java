package vn.vfossa.bluetooth;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class NetworkUtils {
	
	public static final String DEVICE_ADDRESS = "00:26:5E:DE:49:73";
	public static BluetoothDevice getSelectedDevice() {
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!btAdapter.isEnabled()) {
			Log.e("PRINT", "Bluetooth adapter is not enabled!");
			return null;
		}

		Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
		Log.i("Bluetooth", "Automatic printer selection");
		

		// Take the first printer paired
		for (BluetoothDevice itDevice : devices) {
			if (itDevice.getAddress().equals(DEVICE_ADDRESS)){
				return itDevice;
			}
		}

		Log.e("PRINT", "No usable printer!");
		return null;
	}
}
