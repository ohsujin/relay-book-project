
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%> 
<%@ page import="java.sql.*"%>
<%@ page import="org.json.*"%>
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
			System.out.println("\n"+sdf.format(dt).toString()); 

			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/relay_db";
			String user_id = "root";
			String password = "";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String sql = null;
		
	 
		
		
			 //http://localhost:8080/MyServer/JSONServer.jsp?serch_option="검색항목"&keyword="키워드";
			String search_option = request.getParameter("serch_option");
			String keyword =  java.net.URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
		
			
			System.out.println("항목 : "+search_option+ "\n키워드 : "+keyword);
			
			if(keyword.equals(""))
				keyword = "ß˜Ωµœå";
				
			 try {
		         Class.forName(driver).newInstance();						//
		       	 conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
					stmt = conn.createStatement();		
		        
				/* sql =  String.format("select subject,title,publisher,writer,quality,price,registration_date,relaycount,Path,filename,memo from book where %s = '%s' ",search_option,keyword);
				System.out.println("sql : " + sql);
			    rs = stmt.executeQuery(sql); */
			    
			 	sql = "select R_ID,subject,title,publisher,writer,quality,price,registration_date,relaycount,Path,filename,memo from book where "+search_option+ " like "+"'%"+keyword+"%'";
	
				
			
				System.out.println("SQL : "+sql);

				PreparedStatement pstmt = null;
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				JSONArray arr = new JSONArray();
				JSONObject listData = new JSONObject();


					try{
						
						while(rs.next()){
							
							JSONObject send_book = new JSONObject();
							
							send_book.put("R_ID", rs.getString("R_ID"));
							send_book.put("subject", rs.getString("subject"));
							send_book.put("title", rs.getString("title"));
							send_book.put("publisher", rs.getString("publisher"));
							send_book.put("writer", rs.getString("writer"));
							send_book.put("price", rs.getString("price"));
							send_book.put("quality", rs.getFloat("quality"));
							send_book.put("registration_date", rs.getDate("registration_date"));
							send_book.put("relaycount", rs.getInt("relaycount"));
							send_book.put("path", rs.getString("path"));
							send_book.put("filename", rs.getString("filename"));
							send_book.put("memo", rs.getString("memo"));
							
							arr.put(send_book);
						}
						
						listData.put("Book_inform", arr);
						
						
						System.out.println(listData.toString());
						
						out.println(listData.toString());
						
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		        
			} catch (Exception e) {
		        
				out.println("접속실패");
		     	System.out.println("ERROR :  '" + e );
		   		e.printStackTrace();
		    }
 
			
	%>