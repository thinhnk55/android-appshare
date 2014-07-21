package vn.vfossa.database;

import android.graphics.Bitmap;

public class AppData {
	private int _id;
	private String _name;
	private String _path;
	private Bitmap _image;
	private float _size;
	
	
	public AppData(int _id, String _name, String _path, Bitmap _image,
			float _size) {
		super();
		this._id = _id;
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}


	public AppData(String _name, String _path, Bitmap _image, float _size) {
		super();
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}


	public AppData() {
		
	}


	public int get_id() {
		return _id;
	}


	public void set_id(int _id) {
		this._id = _id;
	}


	public String get_name() {
		return _name;
	}


	public void set_name(String _name) {
		this._name = _name;
	}


	public String get_path() {
		return _path;
	}


	public void set_path(String _path) {
		this._path = _path;
	}


	public Bitmap get_image() {
		return _image;
	}


	public void set_image(Bitmap _image) {
		this._image = _image;
	}


	public float get_size() {
		return _size;
	}


	public void set_size(float _size) {
		this._size = _size;
	}

	
	
}
