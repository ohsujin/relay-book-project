<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%> 
<%@ page import="java.sql.*"%>
<%@ page import="org.json.*"%>
<%@ page import="org.json.simple.*"%>
<%
	//한글 깨짐 방지를 위하여 문서의 맨 상단에 적을것
	request.setCharacterEncoding("utf-8");
%>

<%

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/relay_db";
	String user_id = "root";
	String password = "";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = null;

														  //http://localhost:8080/MyServer/JSONServer.jsp?phone=[안드로이드 핸드폰 번호]
	String phoneNumber_Chk = request.getParameter("phone");  // get방식으로 전달된 phone이라는 매게변수를 phoneNumber_Chk에 저장
	String recvMessage1 = request.getParameter("regi_JSON");// post방식으로 전달된 JSON 형식의 문자열을 recvMessage1에 저장
	
	System.out.println("msg : "+recvMessage1);
	String resultStr = "";

		try {
	        Class.forName(driver).newInstance();						//
	        conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
			stmt = conn.createStatement();								//

	        if(request.getParameter("phone") == null)			//phone에 아무런 값도 입력받아 오지 않은경우(null일때) 
	        {
	        	JSONObject json = new JSONObject(recvMessage1);		//JSON 형식으로 받은 문자열을 JSON object에 넣어서 분해한다.
				JSONObject rece = json.getJSONObject("Register");
				
				
				System.out.println("phone : "+	rece.getString("phone"));  
		
                String school = rece.getString("school"); 		//key 값이 shool,phone,passwd인 항목의 value를 빼내어 변수에 저장한다.
                String phone = rece.getString("phone");
                String passwd = rece.getString("passwd");
                
                sql = String.format("INSERT INTO register values('%s', '%s','%s')", school, phone,passwd); //위에서 분해한 항목들을 db에 저장한다.
                System.out.println(sql); 
                stmt.executeUpdate(sql);// db에 업데이트 해주는 부분
                System.out.println("추가 성공");   
	        }
	        else{  //http://localhost:8080/MyServer/JSONServer.jsp?phone= 에서 phone 항목에 값이 전달된다면 null이 아니므로 전달된 전화번호와 db를 비교하여준다.
	        	sql =  String.format(" SELECT * FROM register where phone = '%s' ",phoneNumber_Chk);
	        	System.out.println("sql : " + sql);
	 	        rs = stmt.executeQuery(sql); // 쿼리문을 수행해주고 값이 true를 반환해주는것 같음.

	 	        /*
	 	        	select로 불러온 컬럼의 값을 불러오는 방법
	 	        */
	 	        String GCM = rs.getString("GCM_ID");
	 	        
	 	        
	 	        if(rs.next())             //전화번호가 db에 저장되있는경우 rs.next()의 값은 true이다.                                                                                                                  
	 	        {
	 	                out.println("join");      //안드로이드로 join이라는 문자열을 보내어 회원가입 유무를 판별해준다.
	 	                System.out.println("가입된 회원입니다.");      
	 	        }else{
	 	        	out.println("unjoin");		// 등록된 번호가 아닐경우 rs.next()는 false이므로 unjoin이란 문자열을 보내어 가입을 진행한다.
	 	        	System.out.println("가입해라");
	 	        }

	        }
	        
	       
	        } catch (Exception e) {
		        out.println("접속 실패");
		     	System.out.println("ERROR :  '" + e );
	       		e.printStackTrace();
	        }

	%>