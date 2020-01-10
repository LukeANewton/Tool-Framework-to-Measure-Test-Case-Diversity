package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterService {
	private String path;
	
	public FileWriterService(String path) {
		this.path = path;
	}
	
	public void write(String text, boolean overwrite, boolean append) throws IOException {
		
		if(overwrite) {
			File oldFile = new File(path + "/" + "SimilarityResult.txt");
			oldFile.delete();
		} 
			
		File file = new File(path + "/" + "SimilarityResult.txt");
		file.createNewFile();
		FileWriter writer;
		if (append) {
			writer = new FileWriter(file, true);
		} else {
			writer = new FileWriter(file, false);
		}
		writer.write(text);
		writer.write(System.lineSeparator());
		writer.close();
	}

}
