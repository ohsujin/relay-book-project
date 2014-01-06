<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="javax.xml.crypto.Data"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%> 
<%@ page import="java.sql.*"%>
<%@ page import="org.json.*"%>
<%@ page import="org.json.simple.*"%>
<%@ page import="com.oreilly.servlet.*, com.oreilly.servlet.multipart.*" %>
<%@ page import="java.io.*, java.util.*" %>
<%
	/* Mysql 관련 부분 */
	String driver = "com.mysql.jdbc.Driver";
  	String url = "jdbc:mysql://172.27.144.195:3306/relaybook";
        String user_id = "relay";
        String password = "book";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = null;
	
	String Subject,Price,Title,publisher,Writer,PhoneNum,Quality,Memo;
	String file1,file2,file3;
	String extension = ""; //파일 확장자 저장변수

	String filename = null;
	/* */
	//현재 시간 출력
	Date dt = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a"); 
	System.out.println("\n"+sdf.format(dt).toString()+" 글등록"); 

	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	
	int postMaxSize = 10 * 1024 * 1024;
	String folderPath = "/var/lib/tomcat6/webapps/MyRelayServer/Image/"; // 파일이 저장될 경로
	String encoding = "UTF-8";

	try {
			
		
		MultipartRequest mRequest = new MultipartRequest(request, folderPath,
		        postMaxSize, encoding, new DefaultFileRenamePolicy());
		
		// 받은 데이터 출력
		String key, value;
		Enumeration<String> enumer = mRequest.getParameterNames();
		
		Map<String,String> bookInform = new HashMap<String,String>(); //Key-Value 값을 저장한다.
		Map<String,String> fileInform = new HashMap<String,String>(); //파일의 Key-Value 값을 저장한다.		
		
		while (enumer.hasMoreElements()) {
		    key = enumer.nextElement();
		    value = mRequest.getParameter(key).trim();  //공백을 제거 해줄려고 .trim()을 사용 
		    
		    bookInform.put(key, value); // Map구성
			
		    System.out.println(key + " : " + value);
		}
		
		/* mysql에 입력하기 위해 저장  */
		Subject = bookInform.get("Subject"); 
		Price = bookInform.get("Price");
		Title = bookInform.get("Title");
		publisher = bookInform.get("publisher");
		Writer = bookInform.get("Writer");
		PhoneNum = bookInform.get("PhoneNum");
		Quality = bookInform.get("Quality");		
		Memo = bookInform.get("Memo").trim();
	
		System.out.println("제목 : "+Title);
			
		if(Title.equals("")){
        		System.out.println("등록실패");
       	}else{		
			
		// 파일 이름 출력
			File file;
			File rename = new File("picture1.jpg");
			Enumeration<String> enumer1 = mRequest.getFileNames();
			
			
			//클라이언트에서 보낸 파일 이름 저장
			file1 = fileInform.get("photo1");
			file2 = fileInform.get("photo2");
			file3 = fileInform.get("photo3");
			
			
			/* 10자리 임의의 문자열 생성 */
			String pwChar = "abcdefghijklmnopqrstuvwxyz1234567890";
			char[] Random_ID = new char[10];
			Random random = new Random();
			for ( int i=0; i<10; i++ )
				Random_ID[i] = pwChar.charAt(random.nextInt(36));
			
			String R_ID = new String(Random_ID); //글과 파일의 고유값 생성		
			/* */
		
			///* 파일 이름 변경 */
			Enumeration files = mRequest.getFileNames();
			
			int i = 1; // 3개의 이미지의 파일명의 이름을 _1,_2,_3으로 바꾸기 위해 선언해준다.
			while(files.hasMoreElements()){
	
			String image_name = (String)files.nextElement(); //클라이언트에서 보내주는 이미지를 가르키는 파라미터를 찾아줌
			String Orginal_file_name = mRequest.getFilesystemName(image_name); //파라미터를 이용해서 클라이언트가 보낸 파일의 이름을 찾는다.
			
			System.out.println("파일명 : "+Orginal_file_name);
			
			File Rename_file = null;  //원본파일의 이름을 바꾸기 위해 
		
			
			extension = Orginal_file_name.substring(Orginal_file_name.length()-3,Orginal_file_name.length());//확장자 가져오기
			
			
				if(Orginal_file_name != null){
						 File Save_file = new File(folderPath+Orginal_file_name); //원본 파일 저장
				    //원본파일의 이름을 변경해주는 부분
			
				      Rename_file = new File(folderPath+"/"+R_ID+"_"+i+"."+extension); //MyPhone 부분에 변수 대입가능 , Rename_file이라는 file변수에 새로운이름의 파일 저장
				      Save_file.renameTo(Rename_file); //원본파일의 이름을 바꿔주는 부분
				     
			   		  filename = Rename_file.getName(); //원본파일 명과 변경한 파일명을 비교해주기 위함
				      System.out.println("변경된 이름 확인 : "+filename);
				     }
					i++;
			}
			/* ++++ ++++ ++++ ++++ ++++ ++++ ++++ ++++  ++++ ++++ ++++ */
			
			/* DB에 데이터 넣기  */
			Class.forName(driver).newInstance();						//
	    	  	conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
			stmt = conn.createStatement();	
	        
	    	String image_Name = R_ID;
			
			String get_school = "select school from register where phone = "+PhoneNum;
	        String get_passwd = "select passwd from register where phone = "+PhoneNum;
	        
	        sql = String.format("INSERT INTO book(R_ID,passwd,school,phone,subject,title,publisher,writer,quality,price,registration_date,relaycount,Path,filename,memo) "+
	                "values('%s',( %s ),( %s ),'%s','%s','%s','%s','%s','%s','%s',now(),'%d','%s','%s','%s')",R_ID,get_passwd,get_school,PhoneNum,Subject,Title,publisher,Writer,Quality,Price,0,folderPath,image_Name,Memo ); 
	
	        System.out.println("sql 문 : "+sql);
	        
	        stmt.executeUpdate(sql);// db에 업데이트 해주는 부분
	        System.out.println("추가 성공"); 
	        out.println("RecvOK");
       	}
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	
	
%>
