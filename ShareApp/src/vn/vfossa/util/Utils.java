package vn.vfossa.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	public static void log(String Tag, String message){
		Log.d(Tag, message);
	}
	
	public static void showToast(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
