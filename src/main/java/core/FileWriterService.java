package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The FileWriterService will write the result of the comparisons to a file .
 * It can create a file, write to an existing file or overwrite an already existing file.
 *
 * @author Eric
 */
public class FileWriterService {
	private String path;

	/**
	 * FileWriterService will write to the given path
	 *
	 * @param path used to decide where to write or find the file
	 */
	public FileWriterService(String path) { this.path = path; }

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
		File file = new File(path + name);
		if(overwrite && !append) {
			file.delete();
		} else if(!append && file.exists()) {
			throw new IOException(name + " already exists and overwrite is not enabled");
		}
		if(!append) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file, append);
		writer.write(text + System.lineSeparator());
		writer.close();
	}
}