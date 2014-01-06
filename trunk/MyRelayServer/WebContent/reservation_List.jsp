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
                System.out.println("\n"+sdf.format(dt).toString()+" 예약 추가");

                String driver = "com.mysql.jdbc.Driver";
                String url = "jdbc:mysql://172.27.144.195:3306/relaybook";
                String user_id = "relay";
                String password = "book";
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;
                String sql = null;
            
                
                String phone = request.getParameter("phone");
                String R_ID = request.getParameter("R_ID");
                
                try {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url, user_id, password); //  mysql 접속 부분
                    stmt = conn.createStatement();	
                    
                    sql = String.format("INSERT INTO reservation values('%s','%s')",phone,R_ID);
                   
                   stmt.executeUpdate(sql);// db에 업데이트 해주는 부분
                   
                   System.out.println("추가 성공");
                   out.print("reserV");
                   
                }catch(Exception e) {
    		        out.print("error");
    		     	System.out.println("ERROR :  '" + e );
    	       		e.printStackTrace();
    	   	 }
                
                
                
                
%>