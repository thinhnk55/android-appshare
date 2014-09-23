
package vn.vfossa.wifidirect;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class FileTransferManager implements Runnable {
    private final String TAG = FileTransferManager.class.getSimpleName();

    public enum Type {
        CLIENT,
        SERVER,
    }

    public enum Operation {
        SEND_FILE,
        RECEIVE_FILE,
        SEND_STRING,
        RECEIVE_STRING,
    }

    private Type mType = null;
    private Operation mOperation = null;
    private String mFilepath = null;
    private String mString = null;

    private final int mBufferLength = 102400;
    private byte[] mBuffer = new byte[mBufferLength];
    private int mRead = 0;

    private final int mTimeout = 10 * 1000; // 10 seconds.
    private Socket mSocket = null;
    private SocketAddress mSocketAddress = null;
    private ServerSocket mServerSocket = null;

    private ConnectionListener mConnectionListener = null;
    private SendStringListener mSendStringListener = null;
    private ReceiveStringListener mReceiveStringListener = null;
    private SendFileListener mSendFileListener = null;
    private ReceiveFileListener mReceiveFileListener = null;

    public FileTransferManager(Type type, int port, ConnectionListener listener) {
        if (type != Type.SERVER) {
            throw new IllegalArgumentException(
                    "FileTransferManager type error. type should be Type.SERVER .");
        }
        mType = type;
        mSocketAddress = new InetSocketAddress(port);
        mConnectionListener = listener;
    }

    public FileTransferManager(Type type, String ip, int port, ConnectionListener listener) {
        if (type != Type.CLIENT) {
            throw new IllegalArgumentException(
                    "FileTransferManager type error. type should be Type.CLIENT .");
        }
        mType = type;
        mSocketAddress = new InetSocketAddress(ip, port);
        mConnectionListener = listener;
    }

    public void sendString(String str, SendStringListener listener) {
        mOperation = Operation.SEND_STRING;
        mSendStringListener = listener;
        mString = str;
        new Thread(this).start();
    }

    public void receiveString(ReceiveStringListener listener) {
        mOperation = Operation.RECEIVE_STRING;
        mReceiveStringListener = listener;
        new Thread(this).start();
    }

    public void sendFile(String filepath, SendFileListener listener) {
        mOperation = Operation.SEND_FILE;
        mSendFileListener = listener;
        mFilepath = filepath;
        new Thread(this).start();
    }

    public void receiveFile(String filepath, ReceiveFileListener listener) {
        mOperation = Operation.RECEIVE_FILE;
        mReceiveFileListener = listener;
        mFilepath = filepath;
        new Thread(this).start();
    }

    private void connect() {
        if (mType == Type.SERVER) {
            try {
                mServerSocket = new ServerSocket();
                mServerSocket.bind(mSocketAddress);
                mServerSocket.setSoTimeout(mTimeout);
                mSocket = mServerSocket.accept();
                mConnectionListener.onConnectionSuccess();

                Log.d(TAG, "accept socket.");

            } catch (IOException e) {
                mConnectionListener.onConnectionFailure();

                Log.e(TAG, "server socket exception!");
                e.printStackTrace();
            }

        } else {
            try {
                mSocket = new Socket();
                mSocket.connect(mSocketAddress, mTimeout);
                mConnectionListener.onConnectionSuccess();

                Log.d(TAG, "socket connect successful.");

            } catch (IllegalArgumentException e) {
                mConnectionListener.onConnectionFailure();

                Log.e(TAG, "address or timeout exception.");
                e.printStackTrace();

            } catch (IOException e) {
                mConnectionListener.onConnectionFailure();

                Log.e(TAG, "client socket exception!");
                e.printStackTrace();
            }
        }
    }

    private void disconnect() {
        try {
            if (mSocket != null) {
                mSocket.close();
            }
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendString() {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(mSocket.getOutputStream());
            bos.write((mString + "\n").getBytes());
            bos.flush();

            mSendStringListener.onStringSendSuccess(mString);

        } catch (IOException e) {
            mSendStringListener.onStringSendFailure(mString);

            Log.e(TAG, "send string exception.");
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveString() {
        BufferedInputStream bis = null;
        StringBuilder sb = new StringBuilder();
        try {
            bis = new BufferedInputStream(mSocket.getInputStream());

            while ((mRead = bis.read(mBuffer, 0, mBufferLength)) != -1) {
                sb.append(new String(mBuffer, 0, mRead));
            }

            mReceiveStringListener.onStringReceiveSuccess(sb.toString());

        } catch (IOException e) {
            mReceiveStringListener.onStringReceiveFailure();

            Log.e(TAG, "receive string exception.");
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendFile() {
        BufferedOutputStream bos = null;
        FileInputStream fis = null;
        try {
            bos = new BufferedOutputStream(mSocket.getOutputStream());
            fis = new FileInputStream(mFilepath);

            while ((mRead = fis.read(mBuffer, 0, mBufferLength)) != -1) {
                bos.write(mBuffer, 0, mRead);
            }

            mSendFileListener.onFileSendSuccess();

        } catch (IOException e) {
            mSendFileListener.onFileSendFailure();

            Log.e(TAG, "send file exception.");
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveFile() {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            bis = new BufferedInputStream(mSocket.getInputStream());
            fos = new FileOutputStream(mFilepath);

            while ((mRead = bis.read(mBuffer, 0, mBufferLength)) != -1) {
                fos.write(mBuffer, 0, mRead);
            }

            mReceiveFileListener.onFileReceiveSuccess();

        } catch (IOException e) {
            mReceiveFileListener.onFileReceiveFailure();

            Log.e(TAG, "receive file exception.");
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        connect();

        switch (mOperation) {
            case SEND_STRING:
                sendString();
                break;
            case RECEIVE_STRING:
                receiveString();
                break;
            case SEND_FILE:
                sendFile();
                break;
            case RECEIVE_FILE:
                receiveFile();
            default:
                Log.e(TAG, "error operation!");
        }

        disconnect();
    }

    public static interface ConnectionListener {
        public void onConnectionSuccess();

        public void onConnectionFailure();
    }

    public static interface SendStringListener {
        public void onStringSendSuccess(String str);

        public void onStringSendFailure(String str);
    }

    public static interface ReceiveStringListener {
        public void onStringReceiveSuccess(String str);

        public void onStringReceiveFailure();
    }

    public static interface SendFileListener {
        public void onFileSendSuccess();

        public void onFileSendFailure();
    }

    public static interface ReceiveFileListener {
        public void onFileReceiveSuccess();

        public void onFileReceiveFailure();
    }

}
