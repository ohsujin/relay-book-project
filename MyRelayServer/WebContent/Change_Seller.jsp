<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%> 
<%@ page import="java.sql.*"%>
<%@ page import="org.json.*"%>
<%@ page import="org.json.simple.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	//한글 깨짐 방지를 위하여 문서의 맨 상단에 적을것
	request.setCharacterEncoding("utf-8");
%>

<%
	 //현재 시간 출력
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
	System.out.println("\n-- Change_Seller.jsp --");
        System.out.println(sdf.format(dt).toString()+" 구매완료 하기");

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://172.27.144.195:3306/relaybook";
	String user_id = "relay";
	String password = "book";
	Connection conn = null;
	Statement stmt = null;
	Statement stmt_delete_reserV = null;
	ResultSet rs = null;
	String sql = null;
	String change_sql = null;

	String R_ID = request.getParameter("R_ID");
	String phone = request.getParameter("phone");
	
	System.out.println("전화번호 : "+phone);		

	String resultStr = "";

		try {
	        Class.forName(driver).newInstance();						//
	        conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
		stmt = conn.createStatement();
		stmt_delete_reserV = conn.createStatement();     

		String school = "select school from register where phone = '" +phone+"'";		
		String passwd = "select passwd from register where phone = '" +phone+"'";	
		
		//book table의 소유자를 변경한다.
		change_sql = String.format("update book set phone='%s',active=0,school=(%s),passwd=(%s) where R_ID = '%s'",phone,school,passwd,R_ID);
		//reservation table의 판매가 완료된 항목은 삭제 한다.
		sql = String.format("delete from reservation where R_ID = '%s'",R_ID);		


                System.out.println(sql+"\n"+change_sql); 
                stmt.executeUpdate(change_sql);// db에 업데이트 해주는 부분
		stmt_delete_reserV.executeUpdate(sql);
                System.out.println("구매 완료"); 
		out.println("complete");
	        
		} catch (Exception e) {
		        out.println("접속 실패");
		     	System.out.println("ERROR :  '" + e );
	       		e.printStackTrace();
	        }

	%>
