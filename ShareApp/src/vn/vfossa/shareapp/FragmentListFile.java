package vn.vfossa.shareapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentListFile extends Fragment {
	private View view;
	private TextView IfAlbum;
	private TextView IfArtist;
	private TextView IfLength;
	private String Album;
	private String Artist;
	private String Length;
	private LinearLayout appView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.apps_activity, container, false);

		appView = (LinearLayout) view
				.findViewById(R.id.appContainer);
		// Bundle bundle = this.getArguments();
		// Album = bundle.getString("album");
		// Artist = bundle.getString("artist");
		// Length = bundle.getString("length");
		//
		// IfAlbum.setText(Album);
		// IfArtist.setText(Artist);
		// IfLength.setText(Length);

		return view;
	}

	public void upDateTextView(String artist, String album, String length) {
		IfAlbum = (TextView) view.findViewById(R.id.appContainer);
		IfAlbum.setText(album);
	}
	
	public void addData(){
		
	}

}