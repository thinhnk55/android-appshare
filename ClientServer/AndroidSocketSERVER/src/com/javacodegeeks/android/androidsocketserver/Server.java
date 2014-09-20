package com.javacodegeeks.android.androidsocketserver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class Server extends Activity {

	private ServerSocket serverSocket;

	Handler updateConversationHandler;

	Thread serverThread = null;

	private TextView text;
	int filesize = 6022386;

	public static final int SERVERPORT = 9090;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text = (TextView) findViewById(R.id.text2);

		updateConversationHandler = new Handler();

		this.serverThread = new Thread(new ServerThread());
		this.serverThread.start();

	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ServerThread implements Runnable {

		public void run() {
			Socket socket = null;
			try {
				serverSocket = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {

				try {
					// if (!serverSocket.isClosed()) {

					socket = serverSocket.accept();

					String read = "connected";
					updateConversationHandler.post(new updateUIThread(read));

					CommunicationThread commThread = new CommunicationThread(
							socket);
					new Thread(commThread).start();
					// }

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// while (true) {
			//
			// try {
			//
			// Socket sock = null;
			// sock = serverSocket.accept();
			//
			// // receive file
			// byte[] mybytearray = new byte[filesize];
			// Log.e("size", "" + mybytearray.length);
			// InputStream is = sock.getInputStream();
			// FileOutputStream fos = new FileOutputStream(
			// "/sdcard/DCIM/WebOffice.jpg");
			// BufferedOutputStream bos = new BufferedOutputStream(fos);
			// bytesRead = is.read(mybytearray, 0, mybytearray.length);
			// current = bytesRead;
			//
			// do {
			// bytesRead = is.read(mybytearray, current,
			// (mybytearray.length - current));
			// if (bytesRead >= 0)
			// current += bytesRead;
			// } while (bytesRead > -1);
			//
			// bos.write(mybytearray, 0, current);
			// bos.flush();
			// bos.close();
			//
			// // sock.close();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	class CommunicationThread implements Runnable {

		private Socket clientSocket;

		private InputStream input;
		//private BufferedReader inputText;
		private FileOutputStream output;
		private BufferedOutputStream bos;
		private int byteRead = 0;
		private int current = 0;
		private byte[] mybytearray = new byte[filesize];

		public CommunicationThread(Socket clientSocket) {

			this.clientSocket = clientSocket;

			try {
				// this.inputText = new BufferedReader(new InputStreamReader(
				// this.clientSocket.getInputStream()));
				File sdCard = Environment.getExternalStorageDirectory();
				File directory = new File(sdCard.getAbsolutePath() + "/Myfiles");
				// if (!directory.exists()) {
				directory.mkdirs();
				// }
				File file = new File(directory, "image.jpg");
				this.input = this.clientSocket.getInputStream();
				this.output = new FileOutputStream(file);
				this.bos = new BufferedOutputStream(this.output);
				this.byteRead = this.input.read(mybytearray, 0,
						mybytearray.length);
				this.current = this.byteRead;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			int count = 0;

			// while (!Thread.currentThread().isInterrupted()) {

			try {
				// String read = inputText.readLine();
				// updateConversationHandler.post(new updateUIThread(read));

				Log.e("debug", "running " + count++);

				String read = "sending image";
				updateConversationHandler.post(new updateUIThread(read));

				do {
					this.byteRead = this.input.read(this.mybytearray, current,
							(mybytearray.length - current));
					if (this.byteRead >= 0)
						current += this.byteRead;
				} while (this.byteRead > -1);

				this.bos.write(this.mybytearray, 0, current);
				this.bos.flush();

				read = "send image";
				updateConversationHandler.post(new updateUIThread(read));

				Thread.currentThread().interrupt();
				this.bos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			// }
		}

	}

	class updateUIThread implements Runnable {
		private String msg;

		public updateUIThread(String str) {
			this.msg = str;
		}

		@Override
		public void run() {
			text.setText(text.getText().toString() + "Client Says: " + msg
					+ "\n");
		}

	}

}