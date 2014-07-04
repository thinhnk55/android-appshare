package vn.vfossa.database;

public class FilesData {
	private int _id;
	private String _name;
	private String _path;
	private byte[] _image;
	private float _size;

	public FilesData(String _name, String _path, byte[] _image, float _size) {
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}

	public FilesData(int _id, String _name, String _path,byte[] _image,  float _size) {
		this._id = _id;
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}

	public FilesData() {

	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getPath() {
		return this._path;
	}

	public void setPath(String path) {
		this._path = path;
	}

	public byte[] getImage() {
		return _image;
	}

	public void setImage(byte[] _image) {
		this._image = _image;
	}

	public float get_size() {
		return _size;
	}

	public void set_size(float _size) {
		this._size = _size;
	}

}
