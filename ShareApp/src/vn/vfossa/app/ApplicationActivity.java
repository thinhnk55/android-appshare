package vn.vfossa.app;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.additionalclass.CheckableAndFilterableActivity;
import vn.vfossa.shareapp.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;

public class ApplicationActivity extends Activity implements CheckableAndFilterableActivity {
	private PackageManager packageManager = null;
	private ArrayList<ApplicationInfo> appList = null;
	private ApplicationAdapter listAdapter = null;
	private GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps_activity);
		gridView = (GridView) findViewById(R.id.gridViewApp);

		packageManager = getPackageManager();

		new LoadApplications().execute();
	}
	
	@Override
	public List<ApplicationInfo> getCheckedList() {
		return listAdapter.getCheckedList();
	}

	private ArrayList<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> list) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : list) {
			try {
				if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
					applist.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return applist;
	}

	private class LoadApplications extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress = null;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(ApplicationActivity.this, null,
					"Loading application info...");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			appList = checkForLaunchIntent(packageManager
					.getInstalledApplications(PackageManager.GET_META_DATA));
			listAdapter = new ApplicationAdapter(ApplicationActivity.this,
					appList);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			gridView.setAdapter(listAdapter);
			progress.dismiss();
			super.onPostExecute(result);
		}
		

	}
	
	@Override
	public void Filter(CharSequence strSearch){
		if (!listAdapter.isEmpty()){
			listAdapter.getFilter().filter(strSearch);
		}
	}
}