package vn.vfossa.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	private ArrayList<ApplicationInfo> allApps = null;
	private ArrayList<ApplicationInfo> showApps = null;
	private ApplicationAdapter adapter = null;
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
		return adapter.getCheckedList();
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
			allApps = checkForLaunchIntent(packageManager
					.getInstalledApplications(PackageManager.GET_META_DATA));
			
			showApps = new ArrayList<ApplicationInfo>();
			for (ApplicationInfo app : allApps){
				showApps.add(app);
			}
			adapter = new ApplicationAdapter(ApplicationActivity.this,
					showApps);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			gridView.setAdapter(adapter);
			progress.dismiss();
			super.onPostExecute(result);
		}
	}
	
	@Override
	public void filter(CharSequence constraint){
		if (! allApps.isEmpty()){
			showApps.clear();
			if (constraint != null && constraint.length() > 0){
				constraint = constraint.toString().toLowerCase(Locale.getDefault());
				for (ApplicationInfo app : allApps){
					String appName = (String) app.loadLabel(getPackageManager());
					if (appName.toLowerCase(Locale.getDefault()).contains(constraint)){
						showApps.add(app);
					}
				}
			} else {
				for (ApplicationInfo song : allApps){
					showApps.add(song);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
}