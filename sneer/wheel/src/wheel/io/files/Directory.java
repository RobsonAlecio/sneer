package wheel.io.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface Directory {

	OutputStream createFile(String fileName) throws IOException;

	void createFile(String fileName, String contents) throws IOException;
	
	InputStream openFile(String fileName) throws IOException;
	String contentsAsString(String fileName) throws IOException;

	String[] fileNames();
	boolean fileExists(String fileName);
	File file(String fileName) throws FileNotFoundException;

	void renameFile(String oldName, String newName) throws IOException;
	void deleteFile(String fileName) throws IOException;
		
	void deleteAllContents() throws IOException;

	/** Closes all open Streams and does not allow opening of new Streams.*/
	void close();

}