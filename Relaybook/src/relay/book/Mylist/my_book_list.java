package relay.book.Mylist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import relay.book.Option.PhoneNum;
import relay.book.intentdemob2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.staggeredgridviewdemo.ImageItem;
import com.example.staggeredgridviewdemo.StaggeredAdapter;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class my_book_list extends Activity implements OnItemSelectedListener {
	ArrayList<String> arraylist;
	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/MybookList.jsp";
	static String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";

	private String urls[];

	/* 서버로 넘겨주는 값을 저장해줌 */

	public void onCreate(Bundle savedInstanceState) {
		super.onResume();

		setContentView(R.layout.my_book_list);

		final TextView top_title = (TextView) findViewById(R.id.textView1);

		final ToggleButton tb = (ToggleButton) this
				.findViewById(R.id.toggleButton1);
		tb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tb.isChecked()) {
					tb.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.blankstar));
					top_title.setText(getResources().getText(0, "찜 목록"));

					send_search(PhoneNum.getPhoneNum(), "R");// 내가 찜한 책의 목록을
																// 받아온다. | R =
																// Reservation_book

				} else {
					tb.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.fullstar));
					top_title.setText(getResources().getText(0, "내 책관리하기"));

					send_search(PhoneNum.getPhoneNum(), "M");// 내가 등록한 책의 목록을
																// 받아온다. | M =
																// My_book

				}
			}
		});

		send_search(PhoneNum.getPhoneNum(), "M");// 서버로 검색조건과 keyword를 보낸다.

	}

	// 서버로 부터 항목을 받아오는 부분
	void send_search(String keyword, final String option) {

		DefaultHttpClient client = new DefaultHttpClient();

		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			String keyword1 = URLEncoder.encode(keyword, "UTF-8");// 한글인코딩 처리를
																	// 위해 한번
																	// 변환해줌

			HttpPost post = new HttpPost(URL_book_inform + "?keyword="
					+ keyword1 + "&option=" + option);

			/* 지연시간 최대 5초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}

			System.out.println(result);

			JSONObject json = new JSONObject(result);
			final JSONArray rece = json.getJSONArray("Book_inform");

			if (rece.length() == 0) { // 검색항목이 없을시 다음 wait를 해주어 오류를 잡아준다.
				Toast T = Toast.makeText(getApplicationContext(),
						"등록된 항목이 없습니다.", Toast.LENGTH_SHORT);
				T.setGravity(Gravity.TOP, 0, 400);
				T.show();

			} else {

				/*
				 * 서버로 부터 받아온 데이터를 ImageItem(Array list) 에 저장한다.
				 */
				final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();

				urls = new String[rece.length()];

				System.out.println("사이즈 : " + rece.length());

				for (int i = 0; i < rece.length(); i++) {
					imageItems.add(new ImageItem(rece.getJSONObject(i)
							.getString("title").toString(), rece
							.getJSONObject(i).getString("writer").toString(),
							rece.getJSONObject(i).getString("price").toString()
									+ "원")); // 이부분이 커스텀 뷰의 텍스트항목에 어떤 값을 보내주는지
												// 알려준다.
					urls[i] = imageUrl
							+ rece.getJSONObject(i).getString("filename")
									.toString() + "_1.jpg";
					System.out.println("이미지 경로 : " + urls[i]);

				}

				/*
				 * 서버로 부터 받아온 데이터를 그리드뷰로 표시해주는 부분 시작
				 */
				/* Staggered Grid View */

				StaggeredGridView gridView = (StaggeredGridView) this
						.findViewById(R.id.staggeredGridView1);
				int margin = getResources().getDimensionPixelSize(
						R.dimen.margin);
				gridView.setItemMargin(margin);

				gridView.setPadding(margin, 0, margin, 0); // have the margin on
															// the sides as well

				/*
				 * 검색하면 서버로부터 검색 정보를 JSON으로 불러와 urls에 이미지 경로를 넣어주고 bookInform라는
				 * Map함수에는 책정보를 입력해준다.
				 */
				StaggeredAdapter adapter1 = new StaggeredAdapter(
						my_book_list.this, R.id.imageView1, urls, imageItems); // urls
																				// 의
																				// 크기를
																				// 구하여
																				// 몇개의
																				// view가
																				// 생성되는지
																				// 확인

				gridView.setAdapter(adapter1);

				gridView.setOnItemClickListener(new OnItemClickListener() {

					/*
					 * 각 View를 클릭해서 상세 정보를 볼수 있도록 다음 activity로 값을 넘겨준다
					 * 
					 * @see com.origamilabs.library.views.StaggeredGridView.
					 * OnItemClickListener
					 * #onItemClick(com.origamilabs.library.views
					 * .StaggeredGridView, android.view.View, int, long)
					 */
					@Override
					public void onItemClick(StaggeredGridView parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub

						Intent myIntent = new Intent(my_book_list.this,
								View_mylist.class);
						try {
							myIntent.putExtra(
									"title",
									rece.getJSONObject(position)
											.getString("title").toString());
							myIntent.putExtra(
									"writer",
									rece.getJSONObject(position)
											.getString("writer").toString());
							myIntent.putExtra(
									"price",
									rece.getJSONObject(position)
											.getString("price").toString());
							myIntent.putExtra(
									"subject",
									rece.getJSONObject(position)
											.getString("subject").toString());
							myIntent.putExtra(
									"memo",
									rece.getJSONObject(position)
											.getString("memo").toString());
							myIntent.putExtra(
									"publisher",
									rece.getJSONObject(position)
											.getString("publisher").toString());
							myIntent.putExtra(
									"filename",
									rece.getJSONObject(position)
											.getString("filename").toString());
							myIntent.putExtra(
									"quality",
									rece.getJSONObject(position)
											.getString("quality").toString());
							myIntent.putExtra(
									"section",
									rece.getJSONObject(position)
											.getString("section").toString());
							myIntent.putExtra(
									"active",
									rece.getJSONObject(position)
											.getString("active").toString());

							if (option.equals("M")) { // mybook 과
														// reservationbook에서
														// 사용하는 항목이 다르므로 구별해준다.
								myIntent.putExtra(
										"passwd",
										rece.getJSONObject(position)
												.getString("passwd").toString());
							} else {
								myIntent.putExtra(
										"relaycount",
										rece.getJSONObject(position)
												.getString("relaycount")
												.toString());
								myIntent.putExtra(
										"school",
										rece.getJSONObject(position)
												.getString("school").toString());
								myIntent.putExtra(
										"phone",
										rece.getJSONObject(position)
												.getString("phone").toString());
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						my_book_list.this.startActivity(myIntent); // 새로운 액티비티
																	// 이동

					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, arraylist.get(arg2),
		// Toast.LENGTH_LONG).show();//해당목차눌렸을때
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/* 종료묻기 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			String alertTitle = getResources().getString(R.string.app_name);
			String buttonMessage = getResources().getString(
					R.string.alert_msg_exit);
			String buttonYes = getResources().getString(R.string.button_yes);
			String buttonNo = getResources().getString(R.string.button_no);

			new AlertDialog.Builder(my_book_list.this)
					.setTitle(alertTitle)
					.setMessage(buttonMessage)
					.setPositiveButton(buttonYes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									moveTaskToBack(true);
									finish();
								}
							}).setNegativeButton(buttonNo, null).show();
		}
		return true;
	}

}
