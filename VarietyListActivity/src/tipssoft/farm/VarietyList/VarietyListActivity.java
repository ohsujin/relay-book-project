package tipssoft.farm.VarietyList;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class VarietyListActivity extends Activity implements OnClickListener {
	// 리스트뷰를 구성하는 리스트뷰와 어댑터 변수
	private ExamAdapter m_adapter = null;
	private ListView m_list = null;

	// 대화 상대방의 별명을 저장하는 변수
	//private String m_user_name = "판매자";

	// 시간출력시 사용할 포맷 객체 변수
	private SimpleDateFormat m_date_format = null;
	private SimpleDateFormat m_time_format = null;

	/**/
	private EditText ed_msg;

	private Socket socket; // 소켓 처리
	private PrintStream toServer; // 메시지 송신
	private BufferedReader fromServer; // 메시지 수신

	
	ExamData data = null;
	/**/
	static Boolean nick_name=true;
	static String my_id=null;

	final ArrayList<String> nickname = new ArrayList<String>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_main);
		
		
		Spinner spinner = (Spinner)findViewById(R.id.spinner);
      	
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nickname);
			spinner.setAdapter(adapter);
			spinner.setPrompt("선택"); // 스피너 제목
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setSelection(0);
		
		
		/* 서버 접속*/
		 try {
			   // 소켓 수신 스레드 시작
			   MyChatThread th = new MyChatThread();
			   th.start();
			  } catch (Exception e) {
			   e.printStackTrace();
			  }    
		/* */

		/**/
		ed_msg = (EditText) findViewById(R.id.ed_msg);

		/**/

		// 버튼에 리스너를 등록한다.
		Button btn = (Button) findViewById(R.id.send);
		btn.setOnClickListener(this);
		// btn = (Button) findViewById(R.id.btn3);
		// btn.setOnClickListener(this);

		// ExamData 객체를 관리하는 ArrayList 객체를 생성한다.
		ArrayList<ExamData> data_list = new ArrayList<ExamData>();
		// 사용자 정의 어댑터 객체를 생성한다.
		m_adapter = new ExamAdapter(data_list);

		// 리스트를 얻어서 어댑터를 설정한다.
		m_list = (ListView) findViewById(R.id.var_list);
		m_list.setAdapter(m_adapter);

		// 한국 기준의 시간으로 날짜의 출력 형태를 정의하여 객체를 생성한다.
		m_date_format = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
		m_time_format = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
	}
	
	// 채팅 메시지 수신 스레드 클래스 선언
	 class MyChatThread extends Thread {
	  public MyChatThread() {
	  }
	  
	  public void run() { // 서버 메시지 수신 (무한반복)
	   try {
	    socket = new Socket("192.168.0.73", 10001);
	    toServer = new PrintStream(socket.getOutputStream(),true,"UTF-8");
	    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
	    
	    String chat_msg = null;
	    String all_msg = "";
	    while ((chat_msg = fromServer.readLine()) != null) {
	     all_msg += chat_msg + "\n";
	     //txt_chat.setText(all_msg);
	                    String chat = chat_msg;
	                    Message msg = handler.obtainMessage(1, chat);
	                    handler.sendMessage(msg);
	                    System.out.println("from server : "+chat);
	  // **++++++++**++++++++**++++++++**++++++++**++++++++**++++++++
	                    
	                    
	    }      
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	  }
	 }
	 
	 Handler handler = new Handler() {
		  
		  public void handleMessage(Message msg) {
		   super.handleMessage(msg);
//		   txt_chat.setText((CharSequence)msg.obj);
		   
		   String MSG=(String)msg.obj;
		   
		   int start = MSG.indexOf("");
		   int end = MSG.indexOf(" ",start);
		   String to = MSG.substring(start,end);
		   
		   /*
		    * 서버에 저장된 id 리스트를 받아서 분리한다.
		    */
		   if(to.equals(my_id)){
			   
		   }else if(to.equals("list")){	   
			   int S = MSG.indexOf(" : ");
			   int E = MSG.indexOf("]");
			   String list = MSG.substring(8,E);
			   
			   StringTokenizer token = new StringTokenizer(list,",");
			   
			   while(token.hasMoreTokens()){
				   String user_id = token.nextToken().replace(" ", "");
				   System.out.println("리스트 : "+user_id);
				   nickname.add(user_id);
			   }
			   
			   
		   }else{
		   
			   ed_msg.setText("");
			   
			   data = new ExamData((byte) 0, (String)msg.obj,
						m_time_format.format(new Date()));
			   m_adapter.add(data);
				ed_msg.setText("");
				m_list.smoothScrollToPosition(m_adapter.getCount() - 1);
		   }
		  }
	}; 

	// 버튼이 눌렸을 때 호출되는 이벤트 핸들러
	public void onClick(View view) {
		ExamData data = null;

		switch (view.getId()) {
		
		case R.id.send:
			// 소켓으로 메시지 전송
			if (toServer != null) {
				toServer.println(ed_msg.getText().toString());
				toServer.flush();
			}
			
			// 두번째 버튼이 눌리면 내가 이야기하는 듯한 데이터를 구성한다.
			data = new ExamData((byte) 1, ed_msg.getText().toString(),
					m_time_format.format(new Date()));
			

			if(nick_name){ //최초 접속시 사용자의 닉네임을 기억해준다.
				my_id = ed_msg.getText().toString();
				nick_name=false;
			}
			ed_msg.setText("");

			break;
	
		}
		// 어댑터에 데이터를 추가한다.
		m_adapter.add(data);
		// 추가된 영역으로 스크롤을 이동시킨다.
		m_list.smoothScrollToPosition(m_adapter.getCount() - 1);
	}


	// BaseAdapter 를 상속하여 어댑터 클래스를 재정의한다.
	private class ExamAdapter extends BaseAdapter {
		private LayoutInflater m_inflater = null;
		// ExamData 객체를 관리하는 ArrayList
		private ArrayList<ExamData> m_data_list;

		public ExamAdapter(ArrayList<ExamData> items) {
			m_data_list = items;
			// 인플레이터를 얻는다.
			m_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		// ArrayList 에 ExamData 객체를 추가하는 메서드
		public void add(ExamData data) {
			m_data_list.add((ExamData) data);
			// 데이터가 변화됨을 알려준다.
			notifyDataSetChanged();
		}

		// 어댑터에서 참조하는 ArrayList 가 가진 데이터의 개수를 반환하는 함수
		@Override
		public int getCount() {
			return m_data_list.size();
		}

		// 인자로 넘어온 값에 해당하는 데이터를 반환하는 함수
		@Override
		public ExamData getItem(int position) {
			return m_data_list.get(position);
		}

		// 인자로 넘어온 값에 해당하는 행 ID 를 반환하는 메서드
		@Override
		public long getItemId(int position) {
			return position;
		}

		// 인자로 넘어온 값에 해당하는 뷰의 타입을 반환하는 메서드
		@Override
		public int getItemViewType(int position) {
			return m_data_list.get(position).type;
		}

		// getView 메서드로 생성될 수 있는 뷰의 수를 반환하는 메서드
		@Override
		public int getViewTypeCount() {
			return 3;
		}

		// 각 항목에 출력될 뷰를 구성하여 반환하는 메서드
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			// 해당 항목의 뷰 타입을 얻는다.
			int type = getItemViewType(position);

			// convertView 뷰는 어댑터가 현재 가지고 있는 해당 항목의 뷰객체이다.
			// null 이 넘어오는 경우에만 새로 생성하고, 그렇지않은 경우에는 그대로 사용한다.
			if (convertView == null) {
				// 타입에 따라 각기 다른 XML 리소스로 뷰를 생성한다.
				switch (type) {
				case 0:
					view = m_inflater.inflate(R.layout.list_item1, null);
					break;
				case 1:
					view = m_inflater.inflate(R.layout.list_item2, null);
					break;
				case 2:
					view = m_inflater.inflate(R.layout.list_item3, null);
					break;
				}
			} else {
				view = convertView;
			}

			// 요청하는 항목에 해당하는 데이터 객체를 얻는다.
			ExamData data = m_data_list.get(position);

			// 데이터가 존재하는 경우
			if (data != null) {
				// 뷰 타입에 따라 값을 설정한다.
				if (type == 0) {
					TextView user_tv = null, msg_tv = null, date_tv = null;
					msg_tv = (TextView) view.findViewById(R.id.message_view1);
					date_tv = (TextView) view.findViewById(R.id.date_view1);

					// 이름, 메세지, 시간정보를 텍스트뷰에 설정한다.
					//user_tv.setText(m_user_name);
					msg_tv.setText(data.data1);
					date_tv.setText(data.data2);
				} else if (type == 1) {
					TextView msg_tv = null, date_tv = null;
					msg_tv = (TextView) view.findViewById(R.id.message_view2);
					date_tv = (TextView) view.findViewById(R.id.date_view2);

					// 메세지, 시간정보를 텍스트뷰에 설정한다.
					msg_tv.setText(data.data1);
					date_tv.setText(data.data2);
				} else if (type == 2) {
					// 날짜정보를 텍스트뷰에 설정한다.
					((TextView) view).setText(data.data1);
				}
			}
			return view;
		}
	}
}