package vn.vfossa.music;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import vn.vfossa.util.Utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends ArrayAdapter<FilesData> {
	public static final String TAG = MusicAdapter.class.getSimpleName();

	private List<FilesData> checkedList = new ArrayList<FilesData>();
	private ArrayList<FilesData> song;
	private LayoutInflater mInflator;
	private Context context;
	private static final String type = "music";

	public MusicAdapter(Context context, ArrayList<FilesData> song) {
		super(context, R.layout.media_item_layout, song);
		this.context = context;
		this.song = song;
		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final SongHolder holder;
		View rowView = view;
		if (rowView == null) {

			rowView = mInflator.inflate(R.layout.media_item_layout, parent,
					false);
			holder = new SongHolder();
			holder.songTitle = (TextView) rowView.findViewById(R.id.mediaTitle);
			holder.size = (TextView) rowView.findViewById(R.id.mediaSize);
			holder.checkBox = (CheckBox) rowView
					.findViewById(R.id.checkBoxItem);
			holder.imageView = (ImageView) rowView
					.findViewById(R.id.image_media);
			rowView.setTag(holder);
		} else {
			holder = (SongHolder) rowView.getTag();
		}

		holder.songTitle.setText(getItem(position).getName());
		DecimalFormat dec = new DecimalFormat("0.00");
		holder.size.setText(dec.format(getItem(position).getSize()).concat(
				" MB"));

		if (getItem(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(getItem(position)
					.getImage(), 0, getItem(position).getImage().length,
					options);

			if (bitmap != null) {
				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);
				holder.imageView.setImageBitmap(itemImage);
			} else {
				holder.imageView.setImageResource(R.drawable.music);
			}
		} else {
			holder.imageView.setImageResource(R.drawable.music);
		}

		holder.checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
					Utils.showToast(getContext(), "remove an item "+getItem(position).getPath());
				} else {
					checkedList.add(getItem(position));
					Utils.showToast(getContext(), "add an item "+getItem(position).getPath());
				}
			}
		});

		holder.checkBox.setChecked(checkedList.contains(getItem(position)));

		return rowView;
	}

	static class SongHolder {
		CheckBox checkBox;
		TextView size;
		TextView songTitle;
		ImageView imageView;
	}

	public List<FilesData> getCheckedList() {
		return checkedList;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				DatabaseHandler db = new DatabaseHandler(context);
				ArrayList<FilesData> songs = new ArrayList<FilesData>();
				List<FilesData> listSongs = db.getAllFileWithType(type);
				for (FilesData sd : listSongs) {
					FilesData song = new FilesData();
					song.setID(sd.getID());
					song.setName(sd.getName());
					song.setPath(sd.getPath());
					song.setSize(sd.getSize());
					byte[] data = sd.getImage();
					if (data != null) {
						song.setImage(data);
					} else {
						song.setImage(null);
					}

					songs.add(song);
				}
				FilterResults results = new FilterResults();
				ArrayList<FilesData> filter = new ArrayList<FilesData>();
				constraint = constraint.toString().toLowerCase();

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0; i < songs.size(); i++) {
						String strName = songs.get(i).getName();
						if (strName.toLowerCase().contains(
								constraint.toString())) {
							filter.add(songs.get(i));
						}
					}
				}
				if (constraint == null || constraint.toString().length() == 0) {
					for (int i = 0; i < songs.size(); i++) {
						filter.add(songs.get(i));
					}
				}

				results.count = filter.size();
				results.values = filter;
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null) {
					song.clear();
					@SuppressWarnings("unchecked")
					ArrayList<FilesData> items = new ArrayList<FilesData>(
							(ArrayList<FilesData>) results.values);

					if (items.size() > 0) {
						for (FilesData item : items) {
							song.add(item);
						}
					}
					notifyDataSetChanged();
				}

			}

		};
	}

}