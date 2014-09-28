package vn.vfossa.video;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends CheckableAdapter<FilesData> {

	private LayoutInflater mInflator;
	private Context context;
	private ArrayList<FilesData> videos;
	private static final String type = "video";

	public VideoAdapter(Context context, ArrayList<FilesData> videos) {
		super(context, R.layout.media_item_layout, videos);
		this.context = context;
		this.videos = videos;
		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		VideoHolder holder = null;

		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.media_item_layout, parent,
					false);

			holder = new VideoHolder();
			holder.videoTitle = (TextView) convertView
					.findViewById(R.id.mediaTitle);
			holder.size = (TextView) convertView.findViewById(R.id.mediaSize);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.image_media);
			convertView.setTag(holder);
		} else {
			holder = (VideoHolder) convertView.getTag();
		}

		holder.videoTitle.setText(getItem(position).getName());
		DecimalFormat dec = new DecimalFormat("0.00");
		holder.size.setText(dec.format(getItem(position).getSize()).concat(
				" MB"));

		if (getItem(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(getItem(position)
					.getImage(), 0, getItem(position).getImage().length,
					options);

			Bitmap itemImage = Bitmap
					.createScaledBitmap(bitmap, 100, 100, true);
			holder.imageView.setImageBitmap(itemImage);
		} else {
			holder.imageView.setImageResource(R.drawable.video);
		}

		holder.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
				} else {
					checkedList.add(getItem(position));
				}
			}
		});

		holder.checkBox.setChecked(checkedList.contains(getItem(position)));

		return convertView;
	}

	class VideoHolder {
		CheckBox checkBox;
		TextView size;
		TextView videoTitle;
		ImageView imageView;
	}

	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				DatabaseHandler db = new DatabaseHandler(context);
				ArrayList<FilesData> listVideos = new ArrayList<FilesData>();
				List<FilesData> allSongs = db.getAllFileWithType(type);
				for (FilesData sd : allSongs) {
					FilesData video = new FilesData();
					video.setID(sd.getID());
					video.setName(sd.getName());
					video.setPath(sd.getPath());
					video.setSize(sd.getSize());
					byte[] data = sd.getImage();
					if (data != null) {
						video.setImage(data);
					} else {
						video.setImage(null);
					}

					listVideos.add(video);
				}
				FilterResults results = new FilterResults();
				ArrayList<FilesData> filter = new ArrayList<FilesData>();
				constraint = constraint.toString().toLowerCase(
						Locale.getDefault());

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0; i < listVideos.size(); i++) {
						String strName = listVideos.get(i).getName();
						if (strName.toLowerCase(Locale.getDefault()).contains(
								constraint.toString())) {
							filter.add(listVideos.get(i));
						}
					}
				}
				if (constraint == null || constraint.toString().length() == 0) {
					for (int i = 0; i < listVideos.size(); i++) {
						filter.add(listVideos.get(i));
					}
				}

				results.count = filter.size();
				results.values = filter;
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results.count != 0) {
					videos.clear();
					@SuppressWarnings("unchecked")
					ArrayList<FilesData> items = new ArrayList<FilesData>(
							(ArrayList<FilesData>) results.values);

					if (items.size() > 0) {
						for (FilesData item : items) {
							videos.add(item);
						}
					}
					notifyDataSetChanged();
				}

			}

		};
	}

}