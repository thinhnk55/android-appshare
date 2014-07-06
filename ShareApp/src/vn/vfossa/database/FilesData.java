package vn.vfossa.database;

public class FilesData {
	private int _id;
	private String _type;
	private String _name;
	private String _path;
	private byte[] _image;
	private float _size;

	public FilesData(String _type, String _name, String _path, byte[] _image, float _size) {
		this._type =_type ;
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}

	public FilesData(int _id,String _type, String _name, String _path,byte[] _image,  float _size) {
		this._id = _id;
		this._type =_type ;
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
	
	public String getType() {
		return _type;
	}

	public void setType(String _type) {
		this._type = _type;
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

	public float getSize() {
		return _size;
	}

	public void setSize(float _size) {
		this._size = _size;
	}

}
