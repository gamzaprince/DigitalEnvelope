package service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyFile {
	private static final Logger logger = Logger.getLogger(MyFile.class.getName());

	public static boolean saveFile(StringBuilder filename, byte[] content) {
		boolean success = true;
		{
			String filepath = new String(Paths.get("").toAbsolutePath().toString() + "\\src\\" + filename);
			try (FileChannel rdr = new FileOutputStream(new File(filepath)).getChannel()) {
				ByteBuffer buf = ByteBuffer.wrap(content);
				rdr.write(buf);
				buf.clear();
			} catch (FileNotFoundException e) {
				success = false;
				logger.log(Level.INFO, "[ERROR] [saveFile] [MyFile] - FileNotFoundException");
			} catch (IOException e) {
				success = false;
				logger.log(Level.INFO, "[ERROR] [saveFile] [MyFile] - IOException");
			}
		}
		return success;
	}

	public static byte[] readFile(StringBuilder filename) throws IOException {
		{
			String filepath = new String(Paths.get("").toAbsolutePath().toString() + "\\src\\" + filename);
			byte[] fileRslt = null;
			try (FileChannel rdr = new FileInputStream(new File(filepath)).getChannel()) {
				ByteBuffer buffer = ByteBuffer.allocate((int) rdr.size());
				rdr.read(buffer);
				fileRslt = ((ByteBuffer) buffer.flip()).array();
				buffer.clear();
				return fileRslt;
			} catch (FileNotFoundException e) {
				logger.log(Level.INFO, "[ERROR] [readFile] [MyFile] - FileNotFoundException");
			} catch (IOException e) {
				logger.log(Level.INFO, "[ERROR] [readFile] [MyFile] - IOException");
			}
			return fileRslt;
		}
	}

}
