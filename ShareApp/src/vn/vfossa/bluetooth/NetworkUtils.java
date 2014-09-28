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
