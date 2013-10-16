package relay.book.Multipart;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class GeoPictureUploader
{
	String TAG = "RelayBook";
//    static String serviceDomain = "http://staging.abaqus.net";
    static String postUrl = "http://14.63.212.134/MyRelayServer/RecvBookInform.jsp";
//    static String postUrl = "http://121.156.253.58:8090/MyRelayServer/RecvBookInform.jsp";
    static String CRLF = "\r\n"; 
    static String twoHyphens = "--"; 
    static String boundary = "*****Relaybook*****"; 

    private String pictureFileName = null;
    private String Subject, Title, Writer, Publisher, Price,Quality,PhoneNum;
    private DataOutputStream dataStream = null;

    enum ReturnCode { noPicture, unknown, http201, http400, http401, http403, http404, http500};

    public GeoPictureUploader(String Subject, String Title, String Writer,String Publisher, String Price, String Quality, String PhoneNum ) 
    {
        this.Subject = Subject;
        this.Title = Title;
        this.Writer = Writer;
        this.Publisher = Publisher;
        this.Price = Price;
        this.Quality = Quality;
        this.PhoneNum = PhoneNum;
    }

//    public static void setServiceDomain(String domainName)
//    {
//        serviceDomain = domainName;
//    }
//
//    public static String getServiceDomain()
//    {
//        return serviceDomain;
//    }

    public ReturnCode uploadPicture(String pictureFileName1,String pictureFileName2,String pictureFileName3)
    {
        this.pictureFileName = pictureFileName;
        File uploadFile1 = new File(pictureFileName1); 
        File uploadFile2 = new File(pictureFileName2);
        File uploadFile3 = new File(pictureFileName3); 

        if (uploadFile1.exists())
            try 
            { 
                FileInputStream fileInputStream1 = new FileInputStream(uploadFile1); 
                FileInputStream fileInputStream2 = new FileInputStream(uploadFile2); 
                FileInputStream fileInputStream3 = new FileInputStream(uploadFile3);
                
                StringBuffer postDataBuilder = new StringBuffer(); // 한글처리를 
               
                URL connectURL = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection(); 

                
                conn.setDoInput(true); 
                conn.setDoOutput(true); 
                conn.setUseCaches(false); 
                conn.setRequestMethod("POST"); 

                conn.setRequestProperty("User-Agent", "myGeodiary-V1");
                conn.setRequestProperty("Connection","Keep-Alive"); 
                conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary); 

                conn.connect();

                
                dataStream = new DataOutputStream(conn.getOutputStream()); 

                writeFormField("Subject", Subject);
                writeFormField("Title", Title);
                writeFormField("Writer", Writer);
                writeFormField("publisher", Publisher);
                writeFormField("Price", Price);
                writeFormField("Quality", Quality);
                writeFormField("PhoneNum", PhoneNum);
                
                // 파일전송
                writeFileField("photo1", pictureFileName1, "image/jpg", fileInputStream1);
                writeFileField("photo2", pictureFileName2, "image/jpg", fileInputStream2);
                writeFileField("photo3", pictureFileName3, "image/jpg", fileInputStream3);
               
                
                // final closing boundary line
                dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF); 
                
                fileInputStream1.close(); 
                fileInputStream2.close(); 
                fileInputStream3.close(); 
                dataStream.flush(); 
                dataStream.close();
                dataStream = null;

                
                String response = getResponse(conn);
                
                if (response.contains("RecvOK"))
                    return ReturnCode.http201;
                else 
                    // for now assume bad name/password
                    return ReturnCode.http401; 
            } 
            catch (MalformedURLException mue) { 
                // Log.e(Tag, "error: " + mue.getMessage(), mue); 
                System.out.println("GeoPictureUploader.uploadPicture: Malformed URL: " + mue.getMessage());
                return ReturnCode.http400;
            } 
            catch (IOException ioe) { 
                // Log.e(Tag, "error: " + ioe.getMessage(), ioe); 
                System.out.println("GeoPictureUploader.uploadPicture: IOE: " + ioe.getMessage());
                return ReturnCode.http500;
            } 
            catch (Exception e) { 
                // Log.e(Tag, "error: " + ioe.getMessage(), ioe); 
                System.out.println("GeoPictureUploader.uploadPicture: unknown: " + e.getMessage());
                return ReturnCode.unknown;
            } 
        else 
        {
            return ReturnCode.noPicture;  
        }
    }

    /**
     * @param conn
     * @return
     */
    private String getResponse(HttpURLConnection conn)
    {
        try 
        {
            DataInputStream dis = new DataInputStream(conn.getInputStream()); 
            byte []        data = new byte[1024];
            int             len = dis.read(data, 0, 1024);

            dis.close();

            if (len > 0)
                return new String(data, 0, len);
            else
                return "";
        }
        catch(Exception e)
        {
            Log.e(TAG, "GeoPictureUploader: biffed it getting HTTPResponse");
            return "";
        }
    }


    /**
     * write one form field to dataSream
     * @param fieldName
     * @param fieldValue
     */
    private void writeFormField(String fieldName, String fieldValue)
    {
        try
        {
            dataStream.writeBytes(twoHyphens + boundary + CRLF);    
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            dataStream.writeUTF(fieldValue); //파라미터의 값은 한글이 올수도 있으므로 UTF-8로 변환하여 저장해주면 서버에서 받을때 한글이 깨지지 않고 출력된다.
            dataStream.writeBytes(CRLF);
            
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            //Log.e(TAG, "GeoPictureUploader.writeFormField: got: " + e.getMessage());
        }
    }

    /**
     * write one file field to dataSream
     * @param fieldName - name of file field
     * @param fieldValue - file name
     * @param type - mime type
     * @param fileInputStream - stream of bytes that get sent up
     */
    private void writeFileField(
        String fieldName,
        String fieldValue,
        String type,
        FileInputStream fis)
    {
        try
        {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);    
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                                  + fieldName
                                  + "\";filename=\"" 
                                  + fieldValue
                                  + "\"" 
                                  + CRLF);
            dataStream.writeBytes("Content-Type: " + type +  CRLF);
            dataStream.writeBytes(CRLF); 

            // create a buffer of maximum size 
            int bytesAvailable = fis.available(); 
            int maxBufferSize = 1024; 
            int bufferSize = Math.min(bytesAvailable, maxBufferSize); 
            byte[] buffer = new byte[bufferSize]; 
            // read file and write it into form... 
            int bytesRead = fis.read(buffer, 0, bufferSize); 
            while (bytesRead > 0) 
            { 
                dataStream.write(buffer, 0, bufferSize); 
                bytesAvailable = fis.available(); 
                bufferSize = Math.min(bytesAvailable, maxBufferSize); 
                bytesRead = fis.read(buffer, 0, bufferSize); 
            } 

            // closing CRLF
            dataStream.writeBytes(CRLF);
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            //Log.e(TAG, "GeoPictureUploader.writeFormField: got: " + e.getMessage());
        }
    }



}