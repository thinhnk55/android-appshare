package vn.vfossa.device;

import android.graphics.Bitmap;

public class Device {
	private String name;
	private String address;
	private Bitmap image;
	
	public Device(){
		
	}
	
	public Device(String name, String address, Bitmap image) {
		this.name = name;
		this.address = address;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	

}
