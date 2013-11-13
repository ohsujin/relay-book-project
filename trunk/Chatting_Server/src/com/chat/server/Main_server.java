package com.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main_server {
	private final static int SERVER_PORT = 10001;

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(SERVER_PORT); 
			// port 10001를 인자로한 ServerSocket형 객체 server를 생성 

			HashMap hm = new HashMap(); //해시맵 생성
			System.out.println("waiting connection..");
			while (true) { // 클라이언트가 접속할때 마다 쓰레드를 생성해서 돌리는 구문
				
				Socket sock = server.accept(); // 대기하고 있다가 클라이언트가 접속하면 소켓 객체 반환
			
				ChatThread chatthread = new ChatThread(sock, hm); //소켓과 해시맵 가지고 쓰레드 생성
				chatthread.start(); //쓰레드 시작 / Thread클래스의 run()실행
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

//ChatThread는 클라이언트와 실질적으로 통신하는 객체이다. 
//여러 클라이언트를 동시에 처리하기 위해서 쓰레드 사용
class ChatThread extends Thread {
	private final Socket sock;
	private final HashMap hm;
	private String id;
	private BufferedReader br;
	private boolean initFlag = false;
    /*
	OutputStream is= null;
	OutputStreamWriter osw=null;
	BufferedWriter pw =null;
	*/
	public ChatThread(Socket sock, HashMap hm) { // 소켓과 해시맵을 받는 생성자
		//필드에 저장
		this.sock = sock; 
		this.hm = hm;

		
		try {
			/*
			is = sock.getOutputStream();
			 osw = new OutputStreamWriter(is);
			 pw = new BufferedWriter(osw);
			*/
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));	
			// Socket으로부터 outputStream을 얻어 PrintWriter형 객체 pw를 생성. (outputStream : 쓰기)
			// 클라이언트로의 전송을 위해 필요. 
					
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));  
					// Socket으로부터 InputStream을 얻어 BufferedReader형 객체 br를 생성. (InputStream : 읽기)
					// 클라이언트로부터의 입력을 위해 필요. 
			id = br.readLine(); //br을 한줄씩읽어 String형 id에 담는다
			broadcast(id + " is connected"); //접속된 전체 Client에게 id를 보낸다.
			System.out.println("connected user id is " + id);//서버창에 접속한 아이디를 찍는다

			synchronized (hm) { //hm을 동기화, 여러 쓰레드에서 접근을 방지 한 쓰레드만 접근 허용

				hm.put(this.id, pw); //HashMap에 id를 키값으로한 pw객체를 담는다

			}
			initFlag = true;

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void run() { //start하면 실행됨, 쓰레드가 실행되는 메소드
		try {
			String line = null; 
			while ((line = br.readLine()) != null) { //한줄을 읽어서 널값이 나올때까지 반복 
				if (line.equals("/quit"))
					break;
				if (line.indexOf("/to") == 0) //라인값을 인자로 갖는 귓속말 메소드 호출
					sendmsg(line);
				else
					broadcast(id + ":" + line); //전체 접속자에게 id와 line를 보낸다(전체 메세지 전송 메소드)

			}
		} catch (Exception ex) {
		} finally { //끝나면 무조건 실행
			synchronized (hm) { //해시맵 객체 동기화, 여러 쓰레드에서 접근을 방지, 한 쓰레드만 접근 허용
				hm.remove(id); //키값이 id인 값 삭제
			}
			broadcast(id + " is terminated"); //접속한 전체 클라이언트에게 보냄
			try {
				if (sock != null)
					sock.close(); //소켓 닫기
			} catch (Exception ex) {
			}
		}
	}

	public void sendmsg(String msg) { //귓속말 메세지
		int start = msg.indexOf(" ") + 1; //공백의 인덱스 값 +1, 공백다음의 인덱스 값, 시작인덱스
		int end = msg.indexOf(" ", start); //시작 값부터 다음 공백의 인덱스 값, 마지막 인덱스

		if (end != -1) { //참이면 아이디가 존재, 거짓이면 아이디 없음
			String to = msg.substring(start, end); //메시지의 처음과 끝의 스트링값 반환, 아이디
			String msg2 = msg.substring(end + 1); //끝의 다음 문자열 반환, 내용
			Object obj = hm.get(to);
			if (obj != null) {
				PrintWriter pw = (PrintWriter) obj; //obj를 PrintWriter형으로 변화하여 pw에 치환
				pw.println(id + " tells : " + msg2); //pw에 집어넣어 클라이언트에게 넘긴다
				pw.flush(); //밀어넣기
			}
		}
	}

	public void broadcast(String msg) { //접속된 클라이언트 모두에게 보내는 메소드
		synchronized (hm) { //hm 동기화
			Collection collection = hm.values(); //hm에 모든 value값을 collection에 담는다
			Iterator iter = collection.iterator(); //collection을 정렬해서 iter에 담는다, 하나씩 뽑아오기 위해
			while (iter.hasNext()) { //데이터가 잇을때 까지 반복
				PrintWriter pw = (PrintWriter) iter.next(); //iter에 담겨진 값을 하나씩 PrintWriter형으로 pw 에 담는다
				pw.println(msg); //pw에 msg를담아서 클라이이언트에게 넘긴다(메세지 전송)
				pw.flush();
			}
		}
	}
}