package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * The FileWriterService will write the result of the comparisons to a file .
 * It can create a file, write to an existing file or overwrite an already existing file.
 *
 * @author Eric
 */
public class FileWriterService {
	private String path;
	
	public FileWriterService(String path) {
		this.path = path;
	}

	/**
	 * This method will write the given string to the file with the given name at the path location.
	 * It can create a new file, overwrite an existing file or append to an already existing file.
	 *
	 * @param name the name of the file
	 * @param text the text to be written to the file
	 * @param overwrite boolean deciding if the FileWriterService should overwrite a file with the same name
	 * @param append boolean deciding if the text should be appended to a present file
	 * @throws IOException
	 */
	public void write(String name, String text, boolean overwrite, boolean append) throws IOException {
		if(overwrite) {
			File oldFile = new File(path + name);
			oldFile.delete();
		}
		File file = new File(path + name);
		if(!append) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file, append);
		writer.write(text);
		writer.write(System.lineSeparator());
		writer.close();
	}

}