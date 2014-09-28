package com.example.sample;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class APK_list extends ListActivity
{
	String name;
	String in_path;
	String out_path="sdcard/APP_SHARING/";
	Bundle bundle;
	ArrayList<String> AppName=new ArrayList<String>();
	ArrayList<String> new_AppName=new ArrayList<String>();
	static ArrayList<Bitmap> AppIcon=new ArrayList<Bitmap>();
	PackageManager pm;
	Intent mainIntent;
	List<?> pkgAppsList;
	File file,my_file;
	ProgressDialog dialoge;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//		setContentView(R.layout.activity_main);

		pm = getPackageManager();

		mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);

		dialoge = new ProgressDialog(APK_list.this);
		dialoge.setMessage("Please wait");
		dialoge.setCancelable(false);
		dialoge.show();
		new RetreiveFeedTask().execute("");



	}


	public ArrayList<String> do_background()
	{


		for (Object object : pkgAppsList)
		{
			ResolveInfo info = (ResolveInfo) object;

			file =new File( info.activityInfo.applicationInfo.publicSourceDir);
			// copy the .apk file to wherever
			in_path=file.getPath();
			name=file.getName();

			my_file=new File(out_path+name);

			if(my_file.exists()==true)
			{

				bundle=appdetailz_from_APK(out_path+name);
				AppName.add((String) bundle.get("AppNAme"));
				AppIcon.add( (Bitmap) bundle.getParcelable("AppIcon"));


			}
			else{
				copyFile(in_path, name, out_path);
				bundle=appdetailz_from_APK(out_path+name);
				AppName.add((String) bundle.get("AppNAme"));
				AppIcon.add( (Bitmap) bundle.getParcelable("AppIcon"));
			}


		}

		return AppName;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	private void copyFile(String inputPath, String inputFile, String outputPath)
	{

		InputStream in = null;
		OutputStream out = null;
		try {

			//create output directory if it doesn't exist
			File dir = new File (outputPath); 
			if (!dir.exists())
			{
				dir.mkdirs();
			}


			in = new FileInputStream(inputPath);        
			out = new FileOutputStream(outputPath + inputFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file (You have now copied the file)
			out.flush();
			out.close();
			out = null;        

		}  catch (FileNotFoundException fnfe1) {
			Log.e("tag", fnfe1.getMessage());
		}
		catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}
	public Bundle appdetailz_from_APK(String path)
	{
		//		String APKFilePath = path;

		PackageInfo    pi = pm.getPackageArchiveInfo(path, 0);

		// the secret are these two lines....
		pi.applicationInfo.sourceDir       = path;
		pi.applicationInfo.publicSourceDir = path;
		//

		Drawable icon = pi.applicationInfo.loadIcon(pm);
		Bitmap APKicon=((BitmapDrawable)icon).getBitmap();
		String   AppName = (String)pi.applicationInfo.loadLabel(pm);
		Bundle params=new Bundle();
		params.putString("AppNAme", AppName);
		params.putParcelable("AppIcon", APKicon);
		return params;
	}


	class RetreiveFeedTask extends AsyncTask<String, Void, ArrayList<String>> {

		protected ArrayList<String> doInBackground(String... urls) 
		{

			for (Object object : pkgAppsList)
			{
				ResolveInfo info = (ResolveInfo) object;

				file =new File( info.activityInfo.applicationInfo.publicSourceDir);
				// copy the .apk file to wherever
				in_path=file.getPath();
				name=file.getName();

				my_file=new File(out_path+name);

				if(my_file.exists()==true)
				{

					bundle=appdetailz_from_APK(out_path+name);
					AppName.add((String) bundle.get("AppNAme"));
					AppIcon.add( (Bitmap) bundle.getParcelable("AppIcon"));


				}
				else{
					copyFile(in_path, name, out_path);
					bundle=appdetailz_from_APK(out_path+name);
					AppName.add((String) bundle.get("AppNAme"));
					AppIcon.add( (Bitmap) bundle.getParcelable("AppIcon"));
				}


			}

			return AppName;
		}

		protected void onPostExecute(ArrayList<String> list)
		{
			
			dialoge.cancel();
			if(list.isEmpty()==true)
				Toast.makeText(getApplicationContext(), "EMPTY", Toast.LENGTH_SHORT).show();
			else{

				Toast.makeText(getApplicationContext(), "NOT EMPTY", Toast.LENGTH_SHORT).show();
				getListView().setDividerHeight(0);

				setListAdapter(new APK_LIST_Custom(getApplicationContext(), list));
			}

		}


	}
}
