package vn.vfossa.wifidirect;

import vn.vfossa.shareapp.MainActivity;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Message;
import android.util.Log;

public class WifiDirectActivity extends MainActivity {

	private final int WIFIP2PDEVICE = 0;

	public void handleMessage(Message msg) {
		if (msg.what == WIFIP2PDEVICE) {
			WifiP2pDevice device = (WifiP2pDevice) msg.obj;
			WifiP2pConfig config = new WifiP2pConfig();
			config.deviceAddress = device.deviceAddress;
			config.wps.setup = WpsInfo.PBC;
			mWifiP2pManager.connect(mChannel, config, new ActionListener() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onFailure(int reason) {
				}
			});

		}
	};

	public void scanDevice() {
		mWifiP2pManager.discoverPeers(mChannel, new ActionListener() {
			@Override
			public void onSuccess() {
			}

			@Override
			public void onFailure(int reason) {
			}
		});
	}
	
	

}
