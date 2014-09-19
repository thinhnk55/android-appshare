package vn.vfossa.music;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.R.integer;
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

public class MusicAdapter extends ArrayAdapter<FilesData> {

	private Activity context;
	private ArrayList<FilesData> song;
	// private SongHolder holder;
	private static int[] checkedState;
	private boolean[] checkboxChecked;

	public MusicAdapter(Activity context, ArrayList<FilesData> song) {
		super(context, R.layout.media_item_layout, song);
		this.context = context;
		this.song = song;
		this.checkboxChecked = new boolean[song.size()];

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final SongHolder holder;
		View rowView = view;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.media_item_layout, null, true);
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

		holder.songTitle.setText(song.get(position).getName());
		DecimalFormat dec = new DecimalFormat("0.00");
		holder.size.setText(dec.format(song.get(position).getSize()).concat(
				" MB"));

		if (song.get(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(song.get(position)
					.getImage(), 0, song.get(position).getImage().length,
					options);

			Bitmap itemImage = Bitmap
					.createScaledBitmap(bitmap, 100, 100, true);
			holder.imageView.setImageBitmap(itemImage);
		} else {
			holder.imageView.setImageResource(R.drawable.music);
		}

		holder.checkBox.setId(position);
		holder.imageView.setId(position);
		holder.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (checkboxChecked[id]) {
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

	static class SongHolder {
		CheckBox checkBox;
		TextView size;
		TextView songTitle;
		ImageView imageView;
	}

	public void createCheckedState(int size) {
		checkedState = new int[size];
		for (int i = 0; i < size; i++) {
			checkedState[i] = 0;
		}
	}

	public int getCheckedState(int position) {
		return checkedState[position];
	}

	public int getNumberChecked() {
		int count = 0;
		for (int i = 0; i < song.size(); i++) {
			if (checkboxChecked[i]) {
				count++;
			}
		}
		return count;
	}
	
	public int[] getIdChecked(){
		int[] IDs = new int[getNumberChecked()];
		
		int count = 0;
		for (int i = 0; i < song.size(); i++) {
			if (checkboxChecked[i]) {
				IDs[count++] = song.get(i).getID();
			}
		}
		return IDs;
		
	}

	public void changeCheckedState(int position) {
		if (checkedState[position] == 0) {
			checkedState[position] = 1;
		} else {
			checkedState[position] = 0;
		}
	}

}