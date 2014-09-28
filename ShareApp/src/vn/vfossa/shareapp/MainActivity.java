package vn.vfossa.shareapp;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.AdapterView.OnItemClickListener;
import it.sephiroth.android.library.widget.HListView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import vn.vfossa.additionalclass.BluetoothShare;
import vn.vfossa.additionalclass.CheckableAndFilterableActivity;
import vn.vfossa.app.ApplicationActivity;
import vn.vfossa.bluetooth.BluetoothManager;
import vn.vfossa.database.FilesData;
import vn.vfossa.device.Device;
import vn.vfossa.device.DeviceAdapter;
import vn.vfossa.image.ImageActivity;
import vn.vfossa.music.MusicActivity;
import vn.vfossa.util.Utils;
import vn.vfossa.video.VideoActivity;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements ChannelListener {

	public static final String TAG = "shareApp";
	public static final String APP_TAB = "UngDung";
	public static final String APP_INDICATOR = "Ứng dụng";
	public static final String IMAGE_TAB = "HinhAnh";
	public static final String IMAGE_INDICATOR = "Hình ảnh";
	public static final String MUSIC_TAB = "NgheNhac";
	public static final String MUSIC_INDICATOR = "Nghe Nhạc";
	public static final String VIDEO_TAB = "Video";
	public static final String VIDEO_INDICATOR = "Video";

	public static final String[] TABS = new String[] { APP_TAB, IMAGE_TAB,
			MUSIC_TAB, VIDEO_TAB };
	public static final Class<?>[] TABS_CLASS = new Class[] {
			ApplicationActivity.class, ImageActivity.class,
			MusicActivity.class, VideoActivity.class };
	public static final String[] TABS_INDICATOR = new String[] { APP_INDICATOR,
			IMAGE_INDICATOR, MUSIC_INDICATOR, VIDEO_INDICATOR };

	private TabHost tabHost;

	private Button btScan;
	private Button btShare;
	private static DeviceAdapter deviceAdapter;
	private HListView listDevice;
	private TextView etSearch;

	// private static BluetoothManager bluetoothSender;
	private Context context;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private ArrayList<Device> devices = new ArrayList<Device>();
	public static final int REQUEST_ENABLE_BT = 1;

	public static final int BLUETOOTH_NOTSUPPORTED = -1;
	public static final int BLUETOOTH_NOTENABLED = 0;
	public static final int BLUETOOTH_ENABLED = 1;
	public static final int BLUETOOTH_ISDISCOVERING = 2;
	public static final String UUID0 = "c2915cd0-5c3c-11e3-949a-0800200c9a66";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		btScan = (Button) findViewById(R.id.btScan);
		btShare = (Button) findViewById(R.id.btShare);
		listDevice = (HListView) findViewById(R.id.listDevice);

		setUpTabs();

		// bluetoothSender = new BluetoothManager(getApplicationContext());
		deviceAdapter = new DeviceAdapter(MainActivity.this, devices);
		listDevice.setAdapter(deviceAdapter);

		listDevice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		btScan.setOnClickListener(btnScanDeviceOnClickListener);
		btShare.setOnClickListener(btnShareOnClickListener);

		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String tabTag = tabHost.getCurrentTabTag();
				for (int tabId = 0; tabId < TABS.length; ++tabId) {
					if (tabTag.equals(TABS[tabId])) {
						CheckableAndFilterableActivity activity = (CheckableAndFilterableActivity) getLocalActivityManager()
								.getActivity(TABS[tabId]);
						activity.filter(s);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void setUpTabs() {
		tabHost = getTabHost();

		for (int tabId = 0; tabId < TABS.length; ++tabId) {
			TabSpec tabSpec = tabHost.newTabSpec(TABS[tabId]);
			tabSpec.setIndicator(TABS_INDICATOR[tabId]);
			Intent appsIntent = new Intent(this, TABS_CLASS[tabId]);
			tabSpec.setContent(appsIntent);

			tabHost.addTab(tabSpec);
		}
	}

	private Button.OnClickListener btnScanDeviceOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			updatePairedDevices();
			deviceAdapter.notifyDataSetChanged();
			bluetoothAdapter.startDiscovery();

		}
	};

	private Button.OnClickListener btnShareOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			List<?>[] lists = new List<?>[TABS.length];
			for (int listId = 0; listId < TABS.length; ++listId) {
				CheckableAndFilterableActivity activity = (CheckableAndFilterableActivity) getLocalActivityManager()
						.getActivity(TABS[listId]);
				if (activity != null) {
					lists[listId] = activity.getCheckedList();
				} else {
					lists[listId] = new ArrayList<Object>();
				}
			}
			List<Device> deviceList = deviceAdapter.getCheckedList();
			for (Device device : deviceList) {
				Utils.log("device",
						device.getName() + " : " + device.getAddress());

				if (!BluetoothAdapter.getDefaultAdapter().getBondedDevices()
						.contains(device)) {

					BluetoothDevice bd = BluetoothAdapter.getDefaultAdapter()
							.getRemoteDevice(device.getAddress());
					pairDevice(bd);
				}
			}

			for (Device device : deviceList) {
				for (int listId = 0; listId < TABS.length; ++listId) {
					for (Object object : lists[listId]) {
						if (object instanceof FilesData) {
							FilesData file = (FilesData) object;
							sendFile(new File(file.getPath()),
									device.getAddress());
						} else if (object instanceof ApplicationInfo) {
							ApplicationInfo app = (ApplicationInfo) object;
							sendFile(new File(app.publicSourceDir),
									device.getAddress());
						}
					}
				}
			}
		}

	};

	private void pairDevice(BluetoothDevice device) {
		try {
			Method m = device.getClass()
					.getMethod("createBond", (Class[]) null);
			m.invoke(device, (Object[]) null);
		} catch (Exception e) {
			Utils.log(TAG, e.getMessage());
		}
	}

	public void prepareBluetooth() {
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "You cannot use this app without bluetooth",
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					Toast.makeText(
							context,
							"Bluetooth is currently in device discovery process.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Bluetooth is Enabled.",
							Toast.LENGTH_SHORT).show();
					btScan.setEnabled(true);
				}
			} else {
				Toast.makeText(context, "Bluetooth is NOT Enabled!",
						Toast.LENGTH_SHORT).show();
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent,
						BluetoothManager.REQUEST_ENABLE_BT);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BluetoothManager.REQUEST_ENABLE_BT) {
			prepareBluetooth();
		}
	}

	/*
	 * Function to get all files that are chosen to send
	 */
	public void getListFilesChecked() {

	}

	/*
	 * Function to get all device that the user want to send files
	 */
	public void getListDeviceChecked() {

	}

	@Override
	public void onChannelDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();

		prepareBluetooth();
		updatePairedDevices();
		deviceAdapter.notifyDataSetChanged();
		bluetoothAdapter.startDiscovery();

		registerReceiver(ActionFoundReceiver, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
	}

	@Override
	public void onPause() {
		unregisterReceiver(ActionFoundReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		deviceAdapter.clear();
		deviceAdapter.notifyDataSetChanged();
	}

	public static DeviceAdapter getDeviceAdapter() {
		return deviceAdapter;
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

	public void sendFile(File file, String address) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("audio/*");
			sharingIntent.setComponent(new ComponentName(
					"com.android.bluetooth",
					"com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
			sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			startActivity(sharingIntent);
		} else {
			ContentValues values = new ContentValues();
			File addfile = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/img0.jpg");
			values.put(BluetoothShare.URI, Uri.fromFile(addfile).toString());
			values.put(BluetoothShare.URI, Uri.fromFile(file).toString());
			values.put(BluetoothShare.DESTINATION, address);
			values.put(BluetoothShare.DIRECTION,
					BluetoothShare.DIRECTION_OUTBOUND);
			Long ts = System.currentTimeMillis();
			values.put(BluetoothShare.TIMESTAMP, ts);
			context.getContentResolver().insert(BluetoothShare.CONTENT_URI,
					values);
		}
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
					newDevice.setAddress(device.getAddress());
					Bitmap itemImage = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.device);
					newDevice.setImage(itemImage);
					devices.add(newDevice);
					deviceAdapter.notifyDataSetChanged();
				}
			}
		}
	};

}
