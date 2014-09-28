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
import vn.vfossa.app.ApplicationActivity;
import vn.vfossa.bluetooth.BluetoothManager;
import vn.vfossa.database.FilesData;
import vn.vfossa.device.Device;
import vn.vfossa.device.DeviceAdapter;
import vn.vfossa.image.ImageActivity;
import vn.vfossa.music.MusicActivity;
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
import android.util.Log;
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
	public static final String IMAGE_TAB = "HinhAnh";
	public static final String MUSIC_TAB = "NgheNhac";
	public static final String VIDEO_TAB = "Video";
	private TabHost tabHost;

	private Button btScan;
	private Button btShare;
	private static DeviceAdapter deviceAdapter;
	private HListView listDevice;
	private TextView etSearch;
	public static boolean goodVersion = false;

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

		checkVersion();

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
				switch (tabTag) {
				case APP_TAB:
					ApplicationActivity appActivity = (ApplicationActivity) getLocalActivityManager()
							.getActivity(APP_TAB);
					appActivity.Filter(s);
					break;
				case MUSIC_TAB:
					MusicActivity musicActivity = (MusicActivity) getLocalActivityManager()
							.getActivity(MUSIC_TAB);
					musicActivity.Filter(s);
					break;
				case IMAGE_TAB:
					ImageActivity imageActivity = (ImageActivity) getLocalActivityManager()
							.getActivity(IMAGE_TAB);
					imageActivity.Filter(s);
					break;
				case VIDEO_TAB:
					VideoActivity videoActivity = (VideoActivity) getLocalActivityManager()
							.getActivity(VIDEO_TAB);
					videoActivity.Filter(s);
					break;

				default:
					break;
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

		TabSpec appspec = tabHost.newTabSpec(APP_TAB);
		appspec.setIndicator("Ứng dụng");
		Intent appsIntent = new Intent(this, ApplicationActivity.class);
		appspec.setContent(appsIntent);

		TabSpec photospec = tabHost.newTabSpec(IMAGE_TAB);
		photospec.setIndicator("Hình ảnh");
		Intent photosIntent = new Intent(this, ImageActivity.class);
		photospec.setContent(photosIntent);

		TabSpec songspec = tabHost.newTabSpec(MUSIC_TAB);
		songspec.setIndicator("Nghe nhạc");
		Intent songsIntent = new Intent(this, MusicActivity.class);
		songspec.setContent(songsIntent);

		TabSpec videospec = tabHost.newTabSpec(VIDEO_TAB);
		videospec.setIndicator("Video");
		Intent videosIntent = new Intent(this, VideoActivity.class);
		videospec.setContent(videosIntent);

		tabHost.addTab(appspec);
		tabHost.addTab(photospec);
		tabHost.addTab(songspec);
		tabHost.addTab(videospec);
	}

	private void checkVersion() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			goodVersion = true;
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
			List<FilesData> musicList, videoList;
			List<Bitmap> imageList;
			List<ApplicationInfo> appList;

			MusicActivity musicActivity = (MusicActivity) getLocalActivityManager()
					.getActivity(MUSIC_TAB);
			ApplicationActivity appActivity = (ApplicationActivity) getLocalActivityManager()
					.getActivity(APP_TAB);
			ImageActivity imageActivity = (ImageActivity) getLocalActivityManager()
					.getActivity(IMAGE_TAB);
			VideoActivity videoActivity = (VideoActivity) getLocalActivityManager()
					.getActivity(VIDEO_TAB);

			if (musicActivity != null) {
				musicList = musicActivity.getCheckedList();
			} else {
				musicList = new ArrayList<FilesData>();
			}

			if (imageActivity != null) {
				imageList = imageActivity.getCheckedList();
			} else {
				imageList = new ArrayList<Bitmap>();
			}

			if (videoActivity != null) {
				videoList = videoActivity.getCheckedList();
			} else {
				videoList = new ArrayList<FilesData>();
			}

			if (appActivity != null) {
				appList = appActivity.getCheckedList();
			} else {
				appList = new ArrayList<ApplicationInfo>();
			}

			List<Device> deviceList = deviceAdapter.getCheckedList();

			for (Device device : deviceList) {
				Log.e("device", device.getName() + " : " + device.getAddress());

				if (!BluetoothAdapter.getDefaultAdapter().getBondedDevices()
						.contains(device)) {

					BluetoothDevice bd = BluetoothAdapter.getDefaultAdapter()
							.getRemoteDevice(device.getAddress());
					pairDevice(bd);
				}
			}

			for (Device device : deviceList) {
				for (FilesData music : musicList) {
					sendFile(new File(music.getPath()), device.getAddress());
				}
				for (FilesData video : videoList) {
					sendFile(new File(video.getPath()), device.getAddress());
				}
				for (ApplicationInfo app : appList) {
					sendFile(new File(app.publicSourceDir), device.getAddress());
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
			Log.e(TAG, e.getMessage());
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
		if (goodVersion) {
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
