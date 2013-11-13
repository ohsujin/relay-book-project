package com.example.chatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private EditText ed_msg;
	private Button btn_send;
	private TextView txt_chat;
	// 소켓 처리
	private Socket socket;
	private PrintStream toServer; // 메시지 송신
	private BufferedReader fromServer; // 메시지 수신

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_chat);
		ed_msg = (EditText) findViewById(R.id.ed_msg);
		btn_send = (Button) findViewById(R.id.btn_send);
		txt_chat = (TextView) findViewById(R.id.txt_chat);
		// 소켓 서버 접속
		try {
			// 소켓 수신 스레드 시작
			MyChatThread th = new MyChatThread();
			th.start();
			// 서버 메시지 수신 (무한반복)
			// String chat_msg = null;
			// String all_msg = "";
			// while ((chat_msg = fromServer.readLine()) != null) {
			// all_msg += chat_msg + "\n";
			// txt_chat.setText(all_msg);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		btn_send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 채팅 메시지 보내기
				sendMsg(ed_msg.getText().toString());
				ed_msg.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ed_msg.getWindowToken(), 0); //키보드숨김
			}
		});
	} // onCreate

	private void sendMsg(String msg) {
		// 소켓으로 메시지 전송
		if (toServer != null) {
			toServer.println(msg);
			toServer.flush();
		}
	}

	// 채팅 메시지 수신 스레드 클래스 선언
	class MyChatThread extends Thread {
		public MyChatThread() {
		}

		public void run() { // 서버 메시지 수신 (무한반복)
			try {

				socket = new Socket("192.168.0.11", 10001);
				toServer = new PrintStream(socket.getOutputStream(), true,"UTF-8");
				fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String chat_msg = null;
				String all_msg = "";
				while ((chat_msg = fromServer.readLine()) != null) {
					all_msg += chat_msg + "\n";
					// txt_chat.setText(all_msg);
					String chat = "[My Thread Msg] : " + all_msg;
					Message msg = handler.obtainMessage(1, chat);
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 스레드의 메시지를 수신하여 UI를 업데이트하는 핸들러 선언
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			txt_chat.setText((CharSequence) msg.obj);
		}
	};
}