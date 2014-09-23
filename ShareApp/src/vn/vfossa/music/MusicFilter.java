//package vn.vfossa.music;
//
//import java.io.File;
//
//import vn.vfossa.database.DatabaseHandler;
//import vn.vfossa.database.FilesData;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.media.MediaMetadataRetriever;
//
//public class MusicFilter {
//	
//	private Context context;
//	private String type = "music";
//	
//	public MusicFilter(Context context){
//		this.context = context;
//	}
//	
//	public void loadMemory(){
//		
//	}
//	
//	private void scanDirectory(File directory, String type) {
//		if (directory != null) {
//			File[] listFiles = directory.listFiles();
//			if (listFiles != null && listFiles.length > 0) {
//				for (File file : listFiles) {
//					if (file.isDirectory()) {
//						scanDirectory(file, type);
//					} else {
////						if (type == RESET_TYPE) {
//							addSongToList(file);
////						} else {
////							DatabaseHandler db = new DatabaseHandler(
////									Setting.this);
////							if (file.getName().endsWith(".mp3")){
////								if (db.checkSongPath(file.getPath())) {
////									Log.e("name update", file.getName());
////									addSongToList(file);
////								}
////							}
////						}
//					}
//
//				}
//			}
//		}
//	}
//	
//	private void addSongToList(File file) {
//		if (file.getName().endsWith(".mp3")) {
//			DatabaseHandler db = new DatabaseHandler(context);
//			String songName = file.getName().substring(0,
//					(file.getName().length() - 4));
//			String songPath = file.getPath();
//			MediaMetadataRetriever media = new MediaMetadataRetriever();
//			media.setDataSource(songPath);
//			byte[] data = media.getEmbeddedPicture();
//			float size = (float)file.length()/(1024*1024);
//			media.release();
//			db.addFileData(new FilesData(type, songName, songPath, data,size));
//			db.close();
//		}
//	}
//}
