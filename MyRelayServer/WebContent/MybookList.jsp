<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.json.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	/*
	 * 사용자가 등록한 책 목록을 보내 준다.
	 */

			//현재 시간 출력
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a"); 
			System.out.println("\n"+sdf.format(dt).toString()+" 예약목록 보내기");


			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://172.27.144.195:3306/relaybook";
  		    String user_id = "relay";
    	    String password = "book";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String sql = null;
		
			String keyword = request.getParameter("keyword"); //전화번

			String option = request.getParameter("option"); // R(reservation) | M(Mybook)
			
			
			 try {
	                Class.forName(driver).newInstance();
	                conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
					stmt = conn.createStatement();
			
	                /*
	                 * client로 부터 get 방식으로 option이라는 파라미터의 값을 R/M을 받아 R(Reservation)일때는 사용자가 찜한 책 리스트를 보내주고 M(Mybook)일때는 사용자가 등록한 책 정보를 보내준다.
	                 */
					if(option.equals("R")){
						sql =  String.format("select b.title,b.subject,b.publisher,b.writer,b.price,b.quality,b.memo,b.path,b.filename from reservation v,book b where b.R_ID = v.R_ID and v.phone = '%s'",keyword );
					}else{
						sql =  String.format("select r.passwd,b.title,b.subject,b.publisher,b.writer,b.price,b.quality,b.memo,b.path,b.filename,b.active from register r,book b where b.phone = r.phone and b.phone='%s' ",keyword);
					}
		                
			        rs = stmt.executeQuery(sql); 				

					JSONArray arr = new JSONArray();
					JSONObject listData = new JSONObject();						

					try{
						while(rs.next()){
							
							JSONObject send_book = new JSONObject();
							
							send_book.put("subject", rs.getString("subject"));
							send_book.put("title", rs.getString("title"));
							send_book.put("publisher", rs.getString("publisher"));
							send_book.put("writer", rs.getString("writer"));
							send_book.put("price", rs.getString("price"));
							send_book.put("quality", rs.getFloat("quality"));
							send_book.put("path", rs.getString("path"));
							send_book.put("filename", rs.getString("filename"));
							send_book.put("memo", rs.getString("memo"));
							send_book.put("active", rs.getString("active"));
							
							if(option.equals("M")){
								send_book.put("passwd", rs.getString("passwd"));
								send_book.put("section", "1"); // section = 1 이면 Mybook list를 뜻한다.
							}else{
								//내가 올린 책과 찜한 책을 구별해 주기위한 파라메터
								send_book.put("section", "0");// section = 0 이면 Reservation list를 뜻한다.
								/* */
								send_book.put("passwd", "");
							}
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
