package vn.vfossa.additionalclass;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String filename) {
		return (filename.endsWith(".mp3") || filename.endsWith(".MP3"));
	}

}
