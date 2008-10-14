package wheel.io;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {

	public static boolean isEmpty(File file) {
		if(file == null || !file.exists()) 
			return true;
		
		if(file.isDirectory()) {
			String[] children = file.list();
			return children == null || children.length == 0;
		}
		
		//TODO: check file contents
		return false;
	}
	
	public static File concat(File basePath, String path) {
		return concat(basePath.getAbsolutePath(), path);
	}
	
	public static File concat(String basePath, String path) {
		File file = new File(FilenameUtils.concat(basePath, path)); 
		if(!file.exists()) file.mkdirs();
		return file;
	}
	
}