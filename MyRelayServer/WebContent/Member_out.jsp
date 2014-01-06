<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.*" %>
<%
		//현재 시간 출력
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
		System.out.println("\n"+sdf.format(dt).toString());
		
		String driver = "com.mysql.jdbc.Driver";
		//String url = "jdbc:mysql://172.27.144.195:3306/relaybook";
		//String user_id = "relay";
		//String password = "book";
		String url = "jdbc:mysql://localhost:3306/relay_db";
		String user_id = "root";
		String password = "";
		Connection conn = null;
		Statement stmt = null;
		Statement stmt_file = null;
		ResultSet rs = null;
		String sql = null;
		String sql_fileDelete = null;

		
		String phone = request.getParameter("Phone_Num");	

		try {
	        Class.forName(driver).newInstance();						//
	        conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
			stmt = conn.createStatement();		
			stmt_file = conn.createStatement(); // 파일을 삭제하기 위한 statement

			 sql = String.format("delete from register where phone = '%s'", phone); //파라미터로 받은 phone번호를 이용하여 register table에서 해당 회원을 삭제한다.
			 sql_fileDelete = String.format("select R_ID from book where phone =  '%s'", phone);
			 
			 rs = stmt_file.executeQuery(sql_fileDelete);
			 stmt.executeUpdate(sql);// db에 업데이트 해주는 부분
			 
			 while(rs.next()){
				 
				 File f1 = new File("/Users/osujin/Desktop/save/"+ rs.getString("R_ID") +"_1.jpg");
				 File f2 = new File("/Users/osujin/Desktop/save/"+ rs.getString("R_ID") +"_2.jpg");
				 File f3 = new File("/Users/osujin/Desktop/save/"+ rs.getString("R_ID") +"_3.jpg");
				 
				 f1.delete();
				 f2.delete();
				 f3.delete();
				 
				 System.out.println("파일삭제 성공");
				 
			 }
			 
             System.out.println("삭제 성공"); 
             out.print("delete");
	
		 } catch (Exception e) {
		        out.println("접속 실패");
		     	System.out.println("ERROR :  '" + e );
	       		e.printStackTrace();
	   	 }

%>
