package vn.vfossa.shareapp;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.AdapterView.OnItemClickListener;
import it.sephiroth.android.library.widget.HListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vn.vfossa.app.ApplicationActivity;
import vn.vfossa.bluetooth.BluetoothManager;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.device.Device;
import vn.vfossa.device.DeviceAdapter;
import vn.vfossa.image.ImageActivity;
import vn.vfossa.music.MusicActivity;
import vn.vfossa.video.VideoActivity;
import vn.vfossa.wifidirect.WifiDirectBroadcastReceiver;
import vn.vfossa.wifidirect.WifiP2pDeviceAdapter;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

public class MainActivity extends TabActivity implements ChannelListener,
		PeerListListener, ConnectionInfoListener {

	public static final String TAG = "shareApp";
	public static final String APP_TAB = "UngDung";
	public static final String IMAGE_TAB = "HinhAnh";
	public static final String MUSIC_TAB = "NgheNhac";
	public static final String VIDEO_TAB = "Video";
	private TabHost tabHost;
	private static final String MEDIA_PATH = Environment
			.getExternalStorageDirectory().getPath();
	private Button btScan;
	private Button btShare;
	private Button btProgress;
	private DeviceAdapter deviceAdapter;
	private HListView listDevice;
	private TextView etSearch;
	public static final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	public static boolean goodVersion = false;

	protected WifiP2pManager mWifiP2pManager = null;
	protected WifiP2pManager.Channel mChannel = null;
	protected BroadcastReceiver mBroadcastReceiver = null;
	protected IntentFilter mIntentFilter = null;

	private final int WIFIP2PDEVICE = 0;

	private WifiP2pDeviceAdapter mWifiP2pDeviceAdapter = null;
	private List<WifiP2pDevice> mWifiP2pDeviceList = new ArrayList<WifiP2pDevice>();
	private WifiP2pDevice mWifiP2pDevice = null;

	private BluetoothManager bluetoothSender;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == WIFIP2PDEVICE) {
				WifiP2pDevice device = (WifiP2pDevice) msg.obj;
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = device.deviceAddress;
				config.wps.setup = WpsInfo.PBC;
				mWifiP2pManager.connect(mChannel, config, new ActionListener() {
					@Override
					public void onSuccess() {
						Log.d(TAG, "connect callback success.");
					}

					@Override
					public void onFailure(int reason) {
						Log.e(TAG, "connect callback failed.");
					}
				});

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mWifiP2pManager.initialize(this, getMainLooper(), null);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		btScan = (Button) findViewById(R.id.btScan);
		btShare = (Button) findViewById(R.id.btShare);
		btProgress = (Button) findViewById(R.id.btProgress);
		listDevice = (HListView) findViewById(R.id.listDevice);

		mWifiP2pDeviceAdapter = new WifiP2pDeviceAdapter(this,
				mWifiP2pDeviceList);

		File home = new File(MEDIA_PATH);

		scanDirectory(home);

		setUpTabs();

		checkVersion();

		if (goodVersion) {
			listDevice.setAdapter(mWifiP2pDeviceAdapter);
		} else {
			bluetoothSender = new BluetoothManager(getApplicationContext());
			deviceAdapter = new DeviceAdapter(MainActivity.this,
					bluetoothSender.getDevices());
			listDevice.setAdapter(deviceAdapter);
			deviceAdapter.notifyDataSetChanged();
		}

		listDevice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		btScan.setOnClickListener(btnScanDeviceOnClickListener);
		btShare.setOnClickListener(btnShareOnClickListener);
		btProgress.setOnClickListener(btProgressOnClickListener);

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
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			goodVersion = true;
		}
	}

	private void scanDirectory(File directory) {
		DatabaseHandler db = new DatabaseHandler(MainActivity.this);
		if (directory != null) {
			File[] listFiles = directory.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File file : listFiles) {
					if (!file.getName().equals(".thumbnails")
							&& !file.getName().equals("cache")
							&& !file.getName().startsWith(".")
							&& !file.getName().startsWith("com.")) {
						if (file.isDirectory() && db.checkPath(file.getPath())) {
							scanDirectory(file);
						} else {
							addFileToList(file);
						}
					}

				}
			}
		}
		db.close();
	}

	private void addFileToList(File file) {
		DatabaseHandler db = new DatabaseHandler(MainActivity.this);

		if (file.getName().endsWith(".mp3")) {
			if (db.checkPath(file.getPath())) {
				String songName = file.getName().substring(0,
						(file.getName().length() - 4));
				String songPath = file.getPath();
				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(songPath);
				byte[] data = media.getEmbeddedPicture();
				media.release();
				float size = (float) (file.length()) / (1024 * 1024);
				db.addFileData(new FilesData("music", songName, songPath, data,
						size));
				db.close();
			}
		}

		if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")
				|| file.getName().endsWith(".png")) {
			if (db.checkPath(file.getPath())) {
				String imageName;
				if (file.getName().endsWith(".jpeg")) {
					imageName = file.getName().substring(0,
							(file.getName().length() - 5));
				} else {
					imageName = file.getName().substring(0,
							(file.getName().length() - 4));
				}
				String imagePath = file.getPath();
				File imgFile = new File(imagePath);

				Bitmap itemImage = null;

				if (imgFile.exists()) {

					Bitmap bitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					if (bitmap == null) {
						db.close();
						return;
					}
					itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
							true);
				}

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				itemImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				float size = (float) (file.length()) / (1024 * 1024);
				db.addFileData(new FilesData("image", imageName, imagePath,
						byteArray, size));
				db.close();
			}
		}

		if (file.getName().endsWith(".flv") || file.getName().endsWith(".mp4")
				|| file.getName().endsWith(".mkv")
				|| file.getName().endsWith(".3gp")) {
			if (db.checkPath(file.getPath())) {

				String videoName = file.getName().substring(0,
						(file.getName().length() - 4));
				String videoPath = file.getPath();
				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(videoPath);
				byte[] data = media.getEmbeddedPicture();

				media.release();
				float size = (float) (file.length()) / (1024 * 1024);
				db.addFileData(new FilesData("video", videoName, videoPath,
						data, size));
				db.close();
			}
		}
	}

	private Button.OnClickListener btnScanDeviceOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (goodVersion) {
				Toast.makeText(MainActivity.this, "discover peers.",
						Toast.LENGTH_SHORT).show();
				mWifiP2pManager.discoverPeers(mChannel, new ActionListener() {
					@Override
					public void onSuccess() {
						Log.d(TAG, "discover initiated!");
					}

					@Override
					public void onFailure(int reason) {
						Log.e(TAG, "discover failed!");
					}
				});
			} else {
				bluetoothSender.updatePairedDevices();
				deviceAdapter.notifyDataSetChanged();
				bluetoothSender.discovery();
			}

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

			if (goodVersion) {
				
			} else {
				List<Device> deviceList = deviceAdapter.getCheckedList();
				for (Device device : deviceList) {
					for (FilesData music : musicList) {
						bluetoothSender.sendFile(MainActivity.this, new File(
								music.getPath()), device.getAddress());
					}
					for (FilesData video : videoList) {
						bluetoothSender.sendFile(MainActivity.this, new File(
								video.getPath()), device.getAddress());
					}
					for (ApplicationInfo app : appList) {
						bluetoothSender.sendFile(MainActivity.this, new File(
								app.publicSourceDir), device.getAddress());
					}
				}
			}
		}

	};

	private Button.OnClickListener btProgressOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// MusicActivity music = (MusicActivity) getLocalActivityManager()
			// .getActivity("NgheNhac");
			// int[] IDs = music.getMusicFilesChecked();
			// String[] paths = new String[IDs.length];
			//
			// DatabaseHandler db = new DatabaseHandler(MainActivity.this);
			//
			// for (int i = 0; i < IDs.length; i++) {
			// paths[i] = db.getFileData(IDs[i]).getPath();
			// Toast.makeText(MainActivity.this,
			// "file " + i + ": " + paths[i], Toast.LENGTH_SHORT)
			// .show();
			// }
			//
			// String address[] = deviceAdapter.getDeviceChecked();
			// for (int i = 0; i < address.length; i++) {
			// Toast.makeText(MainActivity.this,
			// "device " + i + ": " + address[i], Toast.LENGTH_SHORT)
			// .show();
			// }
			// db.close();

		}

	};

	public void prepareBluetooth() {
		int state = bluetoothSender.checkBlueToothState();
		switch (state) {
		case BluetoothManager.BLUETOOTH_ENABLED:
			btScan.setEnabled(true);
			break;

		case BluetoothManager.BLUETOOTH_NOTENABLED:
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// startActivityForResult(enableBtIntent,
			// BluetoothManager.REQUEST_ENABLE_BT);
			break;

		case BluetoothManager.BLUETOOTH_NOTSUPPORTED:
			Toast.makeText(this, "You cannot use this app without bluetooth",
					Toast.LENGTH_SHORT).show();
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BluetoothManager.REQUEST_ENABLE_BT) {
			if (goodVersion) {

			} else {
				prepareBluetooth();
			}
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
		mBroadcastReceiver = new WifiDirectBroadcastReceiver(this);
		registerReceiver(mBroadcastReceiver, mIntentFilter);

		if (goodVersion) {

		} else {
			prepareBluetooth();
			bluetoothSender.updatePairedDevices();
			deviceAdapter.notifyDataSetChanged();
			bluetoothSender.discovery();

			registerReceiver(bluetoothSender.ActionFoundReceiver,
					new IntentFilter(BluetoothDevice.ACTION_FOUND));
		}

	}

	@Override
	public void onPause() {
		unregisterReceiver(bluetoothSender.ActionFoundReceiver);
		unregisterReceiver(mBroadcastReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		deviceAdapter.clear();
		deviceAdapter.notifyDataSetChanged();
		mWifiP2pDeviceAdapter.clear();
		mWifiP2pDeviceAdapter.notifyDataSetChanged();
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		mWifiP2pDeviceAdapter.clear();
		mWifiP2pDeviceAdapter.addAll(peers.getDeviceList());
		mWifiP2pDeviceAdapter.notifyDataSetChanged();
	}

	public void onConnectionChanged(NetworkInfo info) {
		if (info.isConnected()) {
			mWifiP2pManager.requestConnectionInfo(mChannel, this);
			Log.d(TAG, "request connection info.");
			Toast.makeText(this, "request connection info.", Toast.LENGTH_SHORT)
					.show();
		} else {
			Log.e(TAG, "wifi p2p not connected!");
			Toast.makeText(this, "connect failed!", Toast.LENGTH_SHORT).show();
		}
	}

	public void onPeersChanged() {
		Log.d(TAG, "request peers.");
		mWifiP2pManager.requestPeers(mChannel, this);
		Toast.makeText(this, "request peers.", Toast.LENGTH_SHORT).show();
	}

}
