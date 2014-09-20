package com.javacodegeeks.android.androidsocketclient;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Client extends Activity {

	private Socket socket;

	private static final int SERVERPORT = 9090;
	private String SERVER_IP = "192.168.56.103";
	private Button btConnect;
	private EditText etIP;
	private Button btSendText;
	private Button btSendImage;
	// private EditText msgChat;
	private Button btBrowse;
	private TextView tvPath;
	private static boolean checkConnect = false;
	private static final int SELECT_PICTURE = 1;

	private String selectedImagePath;
	private ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btConnect = (Button) findViewById(R.id.btConnect);
		btSendText = (Button) findViewById(R.id.btSendText);
		btSendImage = (Button) findViewById(R.id.btSendImage);
		etIP = (EditText) findViewById(R.id.etIP);
		// msgChat = (EditText) findViewById(R.id.EditText01);
		img = (ImageView) findViewById(R.id.image);
		btBrowse = (Button) findViewById(R.id.btBrowse);
		tvPath = (TextView) findViewById(R.id.tvPath);

		btConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SERVER_IP = etIP.getText().toString();
				new Thread(new ClientThread()).start();
			}
		});

		btSendText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkConnect) {
					try {
						EditText et = (EditText) findViewById(R.id.EditText01);
						String str = et.getText().toString();
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						out.println(str);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		btSendImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkConnect) {
					try {

						File myFile = new File(selectedImagePath);
						byte[] mybytearray = new byte[(int) myFile.length()];

						FileInputStream fis = new FileInputStream(myFile);
						BufferedInputStream bis = new BufferedInputStream(fis);

						bis.read(mybytearray, 0, mybytearray.length);
						OutputStream os = socket.getOutputStream();
						os.write(mybytearray, 0, mybytearray.length);
						os.flush();

						bis.close();

					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		btBrowse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			}
		});

	}

	class ClientThread implements Runnable {

		@Override
		public void run() {

			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

				socket = new Socket(serverAddr, SERVERPORT);

				checkConnect = true;

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				tvPath.setText("Image Path : " + selectedImagePath);
				img.setImageURI(selectedImageUri);
			}
		}
	}

	public String getPath(Uri uri) {
		String result = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		// Cursor cursor = managedQuery(uri, projection, null, null, null);
		// int column_index = cursor
		// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// result = cursor.getString(column_index);
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			result = cursor.getString(column_index);
		}

		cursor.moveToFirst();
		return result;
	}
}