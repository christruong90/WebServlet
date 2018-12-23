import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.*;

public class FileClient {

	private Socket s;

	public FileClient(String host, int port, String file) {
		try {
			s = new Socket(host, port);
			sendFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendFile(String file) throws IOException {
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];

		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}

		fis.close();
		dos.close();
	}

	public static void main(String[] args) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String fileName = "/Users/Denny/Desktop/tomcat/webapps/Asn02/WEB-INF/classes/caption_timestamp.txt";
		String timeStamp = dtf.format(now);
		String caption = "picture of leaves";

		try {
			PrintWriter outputStream = new PrintWriter(fileName);
			outputStream.println("Caption: " + caption);
			outputStream.println("Timestamp: " + timeStamp);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FileClient fc = new FileClient("localhost", 1988, "leaves.jpg");
	}

}
