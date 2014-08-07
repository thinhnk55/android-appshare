package vn.vfossa.video;

import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends ArrayAdapter<FilesData> {

	private Activity context;
	private ArrayList<FilesData> videos;
	private VideoHolder holder;
	private int[] checkedState;
	private boolean[] checkboxChecked;

	public VideoAdapter(Activity context, ArrayList<FilesData> videos,int[] checkedState) {
		super(context, R.layout.media_item_layout, videos);
		this.context = context;
		this.videos = videos;
		this.checkedState = checkedState;
		this.checkboxChecked = new boolean[videos.size()];

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View rowView = view;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.media_item_layout, null, true);
			holder = new VideoHolder();
			holder.videoTitle = (TextView) rowView.findViewById(R.id.mediaTitle);
			holder.size = (TextView) rowView.findViewById(R.id.mediaSize);
			holder.checkBox = (CheckBox) rowView
					.findViewById(R.id.checkBoxItem);
			holder.imageView = (ImageView) rowView
					.findViewById(R.id.image_media);
			rowView.setTag(holder);
		} else {
			holder = (VideoHolder) rowView.getTag();
		}

		holder.videoTitle.setText(videos.get(position).getName());
		DecimalFormat dec = new DecimalFormat("0.00");
		holder.size.setText(dec.format(videos.get(position).getSize()).concat(" MB"));

		if (videos.get(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(videos.get(position).getImage(),
					0, videos.get(position).getImage().length, options);

			Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
					true);
			holder.imageView.setImageBitmap(itemImage);
		} else {
			holder.imageView.setImageResource(R.drawable.music);
		}
		
		holder.checkBox.setId(position);
		holder.imageView.setId(position);
		holder.checkBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (checkboxChecked[id]){
					cb.setChecked(false);
					checkboxChecked[id] = false;
				} else {
					cb.setChecked(true);
					checkboxChecked[id] = true;
				}
			}
		});
		
		holder.checkBox.setChecked(checkboxChecked[position]);
		
		return rowView;
	}

	static class VideoHolder {
		CheckBox checkBox;
		TextView size;
		TextView videoTitle;
		ImageView imageView;
	}

}