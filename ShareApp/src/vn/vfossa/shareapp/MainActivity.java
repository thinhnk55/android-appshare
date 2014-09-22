package vn.vfossa.shareapp;

import it.sephiroth.android.library.widget.HListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import vn.vfossa.additionalclass.BluetoothShare;
import vn.vfossa.app.ApplicationActivity;
import vn.vfossa.bluetooth.BluetoothSender;
import vn.vfossa.database.DatabaseHandler;
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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends TabActivity implements ChannelListener {

	public static final String TAG = "shareApp";
	private TabHost tabHost;
	private static final String MEDIA_PATH = "/sdcard/";
	private Button btScan;
	private Button btShare;
	private Button btProgress;
	private DeviceAdapter deviceAdapter;
	private ArrayList<Device> arrayListDevice = new ArrayList<Device>();
	private HListView listDevice;

	private WifiP2pManager manager;
	private boolean isWifiP2pEnabled = false;
	private boolean retryChannel = false;

	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private BroadcastReceiver receiver = null;

	private BluetoothSender bluetoothSender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		btScan = (Button) findViewById(R.id.btScan);
		btShare = (Button) findViewById(R.id.btShare);
		btProgress = (Button) findViewById(R.id.btProgress);
		listDevice = (HListView) findViewById(R.id.listDevice);
		
		File home = new File(MEDIA_PATH);

		scanDirectory(home);

		tabHost = getTabHost();

		TabSpec appspec = tabHost.newTabSpec("UngDung");
		appspec.setIndicator("Ứng dụng");
		Intent appsIntent = new Intent(this, ApplicationActivity.class);
		appspec.setContent(appsIntent);

		TabSpec photospec = tabHost.newTabSpec("HinhAnh");
		photospec.setIndicator("Hình ảnh");
		Intent photosIntent = new Intent(this, ImageActivity.class);
		photospec.setContent(photosIntent);

		TabSpec songspec = tabHost.newTabSpec("NgheNhac");
		songspec.setIndicator("Nghe nhạc");
		Intent songsIntent = new Intent(this, MusicActivity.class);
		songspec.setContent(songsIntent);

		TabSpec videospec = tabHost.newTabSpec("video");
		videospec.setIndicator("Video");
		Intent videosIntent = new Intent(this, VideoActivity.class);
		videospec.setContent(videosIntent);

		tabHost.addTab(appspec);
		tabHost.addTab(photospec);
		tabHost.addTab(songspec);
		tabHost.addTab(videospec);

		bluetoothSender = new BluetoothSender(getApplicationContext());

		getBluetoothState();

		bluetoothSender.getPairedDevices();
		this.arrayListDevice = bluetoothSender.getDevices();
		deviceAdapter = new DeviceAdapter(MainActivity.this, arrayListDevice);
		listDevice.setAdapter(deviceAdapter);
		
		Toast.makeText(getApplicationContext(), "number devices:" + arrayListDevice.size(),
				Toast.LENGTH_SHORT).show();
		deviceAdapter.notifyDataSetChanged();

		btScan.setOnClickListener(btScanDeviceOnClickListener);
		btShare.setOnClickListener(btShareOnClickListener);
		btProgress.setOnClickListener(btProgressOnClickListener);

		registerReceiver(ActionFoundReceiver, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));

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

		if (file.getName().endsWith(".apk")) {
			if (db.checkPath(file.getPath())) {
				String appName = file.getName().substring(0,
						(file.getName().length() - 4));
				String appPath = file.getPath();
				float size = (float) (file.length()) / (1024 * 1024);
				db.addFileData(new FilesData("app", appName, appPath, null,
						size));
				db.close();
			}
		}

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

		// if (file.getName().endsWith(".jpg") ||
		// file.getName().endsWith(".jpeg")
		// || file.getName().endsWith(".png")) {
		// if (db.checkPath(file.getPath())) {
		// String imageName;
		// if (file.getName().endsWith(".jpeg")) {
		// imageName = file.getName().substring(0,
		// (file.getName().length() - 5));
		// } else {
		// imageName = file.getName().substring(0,
		// (file.getName().length() - 4));
		// }
		// String imagePath = file.getPath();
		// File imgFile = new File(imagePath);
		//
		// Bitmap itemImage = null;
		//
		// if (imgFile.exists()) {
		//
		// Bitmap bitmap = BitmapFactory.decodeFile(imgFile
		// .getAbsolutePath());
		// if (bitmap == null) {
		// db.close();
		// return;
		// }
		// itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
		// true);
		// }
		//
		// ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// itemImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
		// byte[] byteArray = stream.toByteArray();
		//
		// float size = (float) (file.length()) / (1024 * 1024);
		// db.addFileData(new FilesData("image", imageName, imagePath,
		// byteArray, size));
		// db.close();
		// }
		// }

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

	private Button.OnClickListener btScanDeviceOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			arrayListDevice.clear();
			bluetoothSender = new BluetoothSender(getApplicationContext());
			bluetoothSender.getPairedDevices();
			arrayListDevice = bluetoothSender.getDevices();
			deviceAdapter.notifyDataSetChanged();
			bluetoothSender.discovery();
			Toast.makeText(getApplicationContext(), "number devices:" + arrayListDevice.size(),
					Toast.LENGTH_SHORT).show();
		}
	};

	private Button.OnClickListener btShareOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			MusicActivity music = (MusicActivity) getLocalActivityManager()
					.getActivity("NgheNhac");
			// music.refreshContent();
			int[] IDs = music.getMusicFilesChecked();
			String[] paths = new String[IDs.length];

			DatabaseHandler db = new DatabaseHandler(MainActivity.this);

			for (int i = 0; i < IDs.length; i++) {
				paths[i] = db.getFileData(IDs[i]).getPath();
				// Toast.makeText(MainActivity.this, "file "+ i +": " +
				// paths[i],
				// Toast.LENGTH_SHORT).show();
			}

			String address[] = deviceAdapter.getDeviceChecked();
			// getListFilesChecked();
			// getListDeviceChecked();

			// String address = null;
			// String filePath = Environment.getExternalStorageDirectory()
			// .toString() + "/img0.jpg";
			for (int i = 0; i < address.length; i++) {
				for (int j = 0; j < IDs.length; j++) {
					Log.e("device ", address[i]);
					Log.e("file ", paths[j]);
					ContentValues values = new ContentValues();
					values.put(BluetoothShare.URI,
							Uri.fromFile(new File(paths[j])).toString());
					values.put(BluetoothShare.DESTINATION, address[i]);
					values.put(BluetoothShare.DIRECTION,
							BluetoothShare.DIRECTION_OUTBOUND);
					Long ts = System.currentTimeMillis();
					values.put(BluetoothShare.TIMESTAMP, ts);
					Uri contentUri = getContentResolver().insert(
							BluetoothShare.CONTENT_URI, values);
				}
			}
			db.close();

		}

	};

	private Button.OnClickListener btProgressOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			MusicActivity music = (MusicActivity) getLocalActivityManager()
					.getActivity("NgheNhac");
			int[] IDs = music.getMusicFilesChecked();
			String[] paths = new String[IDs.length];

			DatabaseHandler db = new DatabaseHandler(MainActivity.this);

			for (int i = 0; i < IDs.length; i++) {
				paths[i] = db.getFileData(IDs[i]).getPath();
				Toast.makeText(MainActivity.this,
						"file " + i + ": " + paths[i], Toast.LENGTH_SHORT)
						.show();
			}

			String address[] = deviceAdapter.getDeviceChecked();
			for (int i = 0; i < address.length; i++) {
				Toast.makeText(MainActivity.this,
						"device " + i + ": " + address[i], Toast.LENGTH_SHORT)
						.show();
			}
			db.close();

		}

	};

	public void getBluetoothState() {
		int state = bluetoothSender.CheckBlueToothState();
		switch (state) {
		case BluetoothSender.BLUETOOTH_ENABLED:
			btScan.setEnabled(true);
			break;

		case BluetoothSender.BLUETOOTH_NOTENABLED:

			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent,
					BluetoothSender.REQUEST_ENABLE_BT);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BluetoothSender.REQUEST_ENABLE_BT) {
			getBluetoothState();
		}
	}

	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Device newDevice = new Device();
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Set<BluetoothDevice> pairedDevices = bluetoothSender
						.getBoundedDevices();
				if (!pairedDevices.contains(device)) {
					newDevice.setName(device.getName());

					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), R.drawable.device);
					Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100,
							100, true);
					newDevice.setImage(itemImage);
					arrayListDevice.add(newDevice);
					deviceAdapter.notifyDataSetChanged();
				}
			}
		}
	};

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

}
