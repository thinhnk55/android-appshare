package vn.vfossa.app;

import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import vn.vfossa.shareapp.R.drawable;
import vn.vfossa.shareapp.R.id;
import vn.vfossa.shareapp.R.layout;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps_activity);

		LinearLayout appList = (LinearLayout) findViewById(R.id.fileList);
		
		LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		parentParams.gravity = Gravity.LEFT;
		parentParams.topMargin = 10;

		LinearLayout row = new LinearLayout(getApplicationContext());

		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setWeightSum(30.0f);
		row.setLayoutParams(parentParams);

		// appList.setOrientation(LinearLayout.HORIZONTAL);

		DatabaseHandler db = new DatabaseHandler(this);

		List<FilesData> listApps = db.getAllFileDatas();

		int count = 0;

		LinearLayout.LayoutParams childParam = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		childParam.weight = 10.0f;

		for (FilesData file : listApps) {
			if (count == 3) {
				count = 1;
				appList.addView(row);
				row = new LinearLayout(getApplicationContext());
				row.setOrientation(LinearLayout.HORIZONTAL);
				row.setLayoutParams(parentParams);
				row.setWeightSum(30.0f);
			} else {
				count++;
			}
			TextView tv = new TextView(this);

			tv.setText(file.getName());

			ImageView image = new ImageView(this);

			image.setLayoutParams(childParam);

			if (file.getImage() != null) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;

				Bitmap bitmap = BitmapFactory.decodeByteArray(file.getImage(),
						0, file.getImage().length, options);

				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);

				image.setImageBitmap(itemImage);
				row.addView(image);
				continue;
			} else {

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.music);
				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);

				image.setImageBitmap(itemImage);

				row.addView(image);
				continue;
			}

		}
		appList.addView(row);

	}

}
