package relay.book.http.Send_Recv;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class Sell_inform_thread extends AsyncTask<Void, Void, Void> {

	String filepath;
	String Subject, Title, Writer, publisher, Price, Quality, PhoneNum;

	/* multipart 전송규약을 위한 인자값 */
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";

	/* ++++ ++++ ++++ ++++ ++++ ++++ */

	public void setFilepath(String filepath) {
		this.filepath = filepath;
		System.out.println("파일이름 : " + Subject);
	}

	public void setBookSell_inform(String Subject, String Title, String Writer,
			String publisher, String Price, String Quality, String PhoneNum) {

		this.Subject = Subject;
		this.Title = Title;
		this.Writer = Writer;
		this.publisher = publisher;
		this.Price = Price;
		this.Quality = Quality;
		this.PhoneNum = PhoneNum;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		HttpFileUpload("http://192.168.25.5:8080/upload/RecvText");

		return null;
	}

	private boolean HttpFileUpload(String urlString) {
		try {
			FileInputStream mFileInputStream = new FileInputStream(new File(
					filepath));
			URL connectUrl = new URL(urlString);

			// 데이터 경계선
			String delimiter = "\r\n--" + boundary + "\r\n";
			StringBuffer postDataBuilder = new StringBuffer();

			// 추가하고 싶은 Key & Value 추가
			// key & value를 추가한 후 꼭 경계선을 삽입해줘야 데이터를 구분할 수 있다.

			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("Subject", Subject));
			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("Title", Title));
			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("Writer", Writer));
			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("publisher", publisher));
			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("Price", Price));
			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("Quality", Quality));
			postDataBuilder.append(delimiter);
			postDataBuilder.append(setValue("PhoneNum", PhoneNum));
			postDataBuilder.append(delimiter);

			// 파일 첨부

			postDataBuilder.append(setFile("upload2", filepath));
			postDataBuilder.append(lineEnd);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) connectUrl
					.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(conn.getOutputStream()));

			// 위에서 작성한 메타데이터를 먼저 전송(한글때메 UTF-8 메소드 사용)
			dos.writeUTF(postDataBuilder.toString());

			int bytesAvailable = mFileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

			// read image
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = mFileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			mFileInputStream.close();
			dos.flush(); // finish upload...

			// get response
			int ch;
			InputStream is = conn.getInputStream();
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			dos.close();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String setValue(String key, String value) {
		return "Content-Disposition: form-data; name=\"" + key + "\"r\n\r\n"
				+ value;
	}

	/**
	 * 업로드할 파일에 대한 메타 데이터를 설정한다.
	 * 
	 * @param key
	 *            : 서버에서 사용할 파일 변수명
	 * @param fileName
	 *            : 서버에서 저장될 파일명
	 * @return
	 */
	public String setFile(String key, String fileName) {
		return "Content-Disposition: form-data; name=\"" + key
				+ "\";filename=\"" + fileName + "\"\r\n";
	}

}
