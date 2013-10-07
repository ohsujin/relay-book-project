package com.example.SyncCapture;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import java.net.*;
import java.io.*;
import java.lang.*;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.content.Context;

import android.widget.Button;   
import android.widget.ImageView;   
import android.widget.TextView;
import android.widget.EditText;

import android.os.Environment;
import java.lang.System;

public class SyncCaptureActivity extends Activity implements Runnable, OnClickListener
{
    final static int TIME_OUT = 5000;
    final static byte NM_SEND_IMAGE_SIZE = 1;
    final static byte NM_SEND_CAPTURE_IMAGE = 2;
    final static byte NM_FINISH_IMAGE_DATA = 3;
    final static byte NM_SUCCESS_RECV_DATA = 5;
 
    // 한번에 보낼 패킷 크기
    final static short MAX_SEND_DATA_SIZE = 4096;

    // 소켓과 스트림 변수들
    private Socket m_client_socket = null;
    private BufferedOutputStream m_sock_out_stream = null;
    private InputStream m_sock_in_stream = null;
 
    // ID 및 port 번호를 저장하는 멤버변수
    private String m_ip_address;
    private int m_port_number = 9537;

    // 쓰레드 변수
    private Thread m_client_thread;

    // 추가 쓰레드에서 출력할 문자열을 저장하는 변수
    private String m_display_string;
    private TextView m_text_view;
 
    // 출력 사항이 발생하면 해당 사항을 출력하는 루틴을 정의
    Runnable m_display_run = new Runnable() {
        public void run()
        {
            m_text_view.setText(m_display_string);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        // 리소스 파일에 정의된 id_tv 라는 ID의 텍스트뷰를 얻어온다.
        m_text_view = (TextView) findViewById(R.id.id_tv);
        // 텍스트뷰에 문자열을 설정한다.
        m_text_view.setText("input server IP address");

        EditText ip_edit = (EditText) findViewById(R.id.id_ip_edit);
        ip_edit.setText("0.0.0.0");

        // 리소스 파일에 정의된 ID의 버튼을 얻어온다.
        Button capture_btn = (Button) findViewById(R.id.id_capture_btn);
        // 버튼에 리스너를 등록한다.
        capture_btn.setOnClickListener(this);

        try {
            // 고유영역에 test_picture.jpg 라는 쓰기용 파일을 생성한다.
            // 이 때, 다른 어플리케이션이 이 파일에 데이터를 쓸 수 있도록 속성을 부여한다.
            FileOutputStream fos = openFileOutput("test_picture.jpg", Context.MODE_WORLD_WRITEABLE);
            // 파일 출력 스트림을 닫는다.
            fos.close();
         } catch (IOException ie) {
            m_text_view.setText(ie.toString());
         }

    }

    public void onClick(View view) 
    {
        int id = view.getId();
        // capture 버튼을 클릭한 경우
        if(id == R.id.id_capture_btn) {
            // 소켓이 연결되어 있지 않은 경우, 카메라 액티비티를 호출한다.
            if(m_client_socket == null) callCaptureActivity();
        }
    }

    // 카메라 액티비티를 호출하는 메소드
    public void callCaptureActivity()
    {
        // 카메라 촬영을 할 수 있는 액티비티를 실행할 수 있도록 인텐트 객체를 생성한다.
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try { 
            // 어플리케이션의 고유영역 경로를 File 객체로 얻는다.
            File path = getFilesDir();
            // 고유영역에 있는 test_picture.jpg 파일의 객체를 얻는다.
            File file = new File(path, "test_picture.jpg");

            // File 객체의 URI 를 얻는다.
            Uri uri = Uri.fromFile(file);
            // 인텐트에 URI 정보를 저장한다.
            // 카메라 액티비티는 이 URI 에 입력된 경로에 촬영한 이미지를 저장한다.
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
           
            // 인텐트 정보에 맞는 액티비티를 실행한다.
            startActivityForResult(intent, 0);       
        } catch (Exception ie) {
            // 파일 입출력에 관한 예외 발생시 예외사항을 출력한다.
            m_text_view.setText(ie.toString());
        } 
    }

    public void onActivityResult(int request_code, int result_code, Intent data)
    {
        // 결과 값이 있고, 액티비티를 시작할 때 0로 설정해둔 결과값인 경우
        if(request_code == 0 && result_code == RESULT_OK) {
            try {
                // 고유영역에서 파일명이 test_picture.jpg 인 파일의 입력용 파일 스트림 객체를 얻는다.
                FileInputStream input_stream = openFileInput("test_picture.jpg");
 
                // id_image 라는 ID 의 이미지뷰를 얻는다.
                ImageView img_view = (ImageView) findViewById(R.id.id_image);

                // 기존에 이미지가 설정되어 있었다면 설정된 이미지를 해제한다.
                img_view.setImageDrawable(null);
                // 입력 스트림을 이용하여 Drawable 객체를 생성하고,
                // 해당 객체를 이미지뷰에 출력시킨다.
                img_view.setImageDrawable(Drawable.createFromStream(input_stream, "image.jpg"));
                // 파일 입력 스트림을 닫는다.
                input_stream.close();

                // 서버에 이미지를 전송하기 위하여 소켓 연결을 한다.
                onConnectServer();

                // 가비지 컬렉션을 수행한다.
                // 메모리 해제가 수동으로 되지 않기때문에 직접 수행시켜야
                // 메모리 부족으로 프로그램이 중지되지 않는다.
                // 단, 이 작업이 부하가 크기때문에 자주 사용하는 것을 권장하지 않는다.
                // 나중에 이것을 보완할 다른 방법에 대하여 알아보도록 하겠다.
                System.gc();

            } catch (IOException ie) {
                // 파일 입출력에 관한 예외 발생시 예외사항을 출력한다.
                m_text_view.setText(ie.toString());
            }
        }
    }


    // 서버와의 소켓 연결을 수행하는 메소드
    public void onConnectServer()
    {
        // 소켓이 연결되어 있지 않다면
        if(m_client_socket == null) {
            EditText ip_edit = (EditText) findViewById(R.id.id_ip_edit);
            // 에디트텍스트에 입력된 IP 주소를 얻는다.
            m_ip_address = ip_edit.getText().toString();

            // 쓰레드를 생성한다.
            m_client_thread = new Thread(this);
            // 쓰레드를 시작한다.
            m_client_thread.start();
        } 
    }

    // 서버와의 연결을 종료하는 메소드
    public void onDisconnectServer()
    {
        // 소켓이 제거되지 않은 경우
        if(m_client_socket != null){
            try {
                // 입출력 스트림의 무효화 작업을 수행한다. 추가된 쓰레드에서 입력 스트림이 read 메소드에
                // 대기 상태가 걸려있기때문에 이 함수를 호출하여 스트림을 무효화한다.
                if(m_sock_in_stream != null) m_client_socket.shutdownInput();
                if(m_sock_out_stream != null) m_client_socket.shutdownOutput();
 
                // 쓰레드가 수행중인 경우
                if(m_client_thread.isAlive()){
                    // 추가된 쓰레드에 인터럽트를 건다.
                    m_client_thread.interrupt();
                    // 쓰레드가 인터럽트를 감지하여 종료할때까지 기다린다.
                    m_client_thread.join();
                }
            } catch (Exception e) {
                m_text_view.setText(e.toString());
            }
        } 
    }

    // 소켓 연결을 수행하는 별도의 쓰레드가 수행하는 메소드
    public void run() 
    {
        try {
            // IP 주소와 포트 번호를 관리하는 객체를 생성한다.
            SocketAddress sock_addr = new InetSocketAddress(m_ip_address, m_port_number);
            // 소켓을 생성한다.
            m_client_socket = new Socket();
            // 수신 버퍼 크기를 1024 바이트로 설정한다.
            m_client_socket.setReceiveBufferSize(1024); 
            // 송신 버퍼 크기를 8192 바이트로 설정한다.
            m_client_socket.setSendBufferSize(8192);
            // 소켓을 닫을 때 TIME_OUT 에 저장해둔 밀리세컨만큼 대기한 후 닫는다.
            m_client_socket.setSoLinger(true, TIME_OUT); 
            
            // 서버와 연결을 시도한다. TIME_OUT 시간 내에 응답이 오지 않으면 연결을 포기한다.
            m_client_socket.connect(sock_addr, TIME_OUT);
            
            // 연결된 경우
            if(m_client_socket != null && m_client_socket.isConnected()){
                // 연결 성공 메세지가 텍스트뷰에 출력되도록 m_display_run 인터페이스를 메인 쓰레드에 전달한다.
                m_display_string = "Try Reflesh!!";
                m_text_view.post(m_display_run);
                
                // 소켓의 입력 스트림과 출력 스트림을 얻는다.
                m_sock_out_stream = new BufferedOutputStream( m_client_socket.getOutputStream());
                m_sock_in_stream = m_client_socket.getInputStream();
                
                // 이미지 데이터를 전송한다.
                onSendPicture();
                // 서버에서 보내는 데이터를 수신한다.
                onReadStream();
            }
        } catch (Exception e) { 
            // 최상위 예외 클래스로 쓰레드에서 발생하는 모든 예외를 동일하게 처리한다.
            m_display_string = e.toString();
            m_text_view.post(m_display_run);
        } finally {
            try {
                if(m_client_socket != null) {
                    if(m_sock_out_stream != null) { 
                        // 출력스트림을 닫는다.
                        m_sock_out_stream.close();
                        m_sock_out_stream = null;
                    }
                    if(m_sock_in_stream != null) { 
                        // 출력스트림을 닫는다.
                        m_sock_in_stream.close();
                        m_sock_in_stream = null;
                    }
                
                    // 소켓을 닫는다.
                    m_client_socket.close();
                    m_client_socket = null;
                }
            } catch (IOException e) {
                m_display_string = e.toString();
                m_text_view.post(m_display_run);
            }

        }
    }

    // 추가 thread 가 서버에서 데이터를 수신하기위해 사용하는 메소드
    public void onReadStream() throws IOException
    {
        byte msg_id;
        byte[] size = new byte[2];
        
        // 쓰레드에 인터럽트가 발생하지 않으면 반복한다.
        while (!m_client_thread.isInterrupted()) {
            // 메세지 번호를 읽는다.
            msg_id = (byte)m_sock_in_stream.read();
            // 수신완료 메세지가 맞다면
            if(msg_id == NM_SUCCESS_RECV_DATA){
                // 크기 정보가 저장된 2바이트를 읽는다. 
                if(m_sock_in_stream.read(size) == 2){
                    // 반복문을 나와서 메세지 수신 대기를 종료한다.
                    break;
                }
            }
        }
    }

    // 이미지 데이터를 전송하는 메소드
    public void onSendPicture()
    {
        // 소켓이 존재하는 경우
        if(m_client_socket != null){
            FileInputStream input_stream = null;
            try {
                // 전체 파일 크기와 프레임 body 사이즈를 저장할 바이트 배열을 생성한다.
                byte[] file_size = new byte[4];
                byte[] body_size = new byte[2];                
                // 고유영역에서 파일명이 test_picture.jpg 인 파일의 입력용 파일 스트림 객체를 얻는다.
                input_stream = openFileInput("test_picture.jpg");

                // Byte Ordering 을 고려하여 바이트 값을 int 값으로 변환한다.
                // 0x12345678 을 윈도우즈 시스템에서 전송했다면 수신시 0x78563412 로 저장되므로
                // 순서를 역으로 변환해야한다.
                int full_size = input_stream.available();
                file_size[0] = (byte)(full_size & 0x000000FF);
                file_size[1] = (byte)((full_size & 0x0000FF00) >> 8);
                file_size[2] = (byte)((full_size & 0x00FF0000) >> 16);
                file_size[3] = (byte)((full_size & 0xFF000000) >> 24);

                // 파일의 전체 크기를 텍스트뷰에 출력해준다.
                m_display_string = "send size : " + full_size;
                m_text_view.post(m_display_run);

                // 바이트의 사이즈를 저장한다.
                // 안드로이드의 기반인 리눅스와 윈도우즈는 Byte Ordering 이 다르기때문에
                // 2바이트의 데이터를 송수신할 때 1바이트씩 값을 바꿔주어야 한다.
                int data_size = file_size.length;
                body_size[0] = (byte)data_size;
                body_size[1] = (byte)(data_size >>> 8);                

                // 데이터 전송 시작의 의미로 파일사이즈를 스트림에 쓴다.
                m_sock_out_stream.write(NM_SEND_IMAGE_SIZE);
                // 전송할 프레임의 body 사이즈를 스트림에 쓴다.
                m_sock_out_stream.write(body_size);
                // 파일 크기를 스트림에 쓴다.
                m_sock_out_stream.write(file_size);
                // 스트림에 쓰여진 정보를 서버로 전송한다.
                m_sock_out_stream.flush();

                // 이미지 데이터를 저장할 바이트 배열을 생성한다.
                byte[] send_data = new byte[MAX_SEND_DATA_SIZE];
                int remain_file_size = full_size, read_size;

                // 전송할 데이터 크기가 남았다면 계속 반복한다.
                while(remain_file_size > 0) {
                    // 50 ms 를 대기한다.
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ie) {
                        m_display_string = ie.toString();
                        m_text_view.post(m_display_run);
                    }
 
                    // 남은 데이터 크기가 최대 전송가능 데이터 크기보다 크면
                    // 전송할 크기를 최대치로 하고, 아니면 남은 크기로 한다.
                    if(remain_file_size > MAX_SEND_DATA_SIZE) data_size = MAX_SEND_DATA_SIZE;
                    else data_size = remain_file_size;

                    // 파일 입력 스트림에서 데이터 크기만큼 읽고, 실제 읽은 크기를 반환받는다.
                    read_size = input_stream.read(send_data, 0, data_size);
 
                    if(read_size < 0) {
                        // 읽은 데이터 크기가 0보다 작으면 문제가 발생한 것이므로
                        // 스트림을 닫고, 예외를 발생시킨다.
                        input_stream.close();
                        throw new IOException("End of Stream");
                    } else if(read_size < data_size) {
                        // 읽을 크기보다 실제 읽은 크기가 작으면..
                        int temp;
                        // 읽어야할 크기와 읽은 크기가 같아질 때까지 반복한다.
                        while(read_size < data_size){
                            // 1바이트를 읽는다.
                            temp = input_stream.read();
 
                            if(temp == -1) {
                                // -1이면 소켓을 닫고 예외를 발생시킨다.
                                input_stream.close();
                                throw new IOException("End of Stream");
                            } else {
                                // -1이 아니면 읽은 값을 배열에 저장한다.
                                send_data[read_size] = (byte)temp;
                                // 실제 읽은 크기 값을 증가시킨다.
                                read_size++;
                            }
                        }
                    }
                    // 전송할 데이터 크기를 바이트 배열에 알맞게 저장한다.
                    body_size[0] = (byte)data_size;
                    body_size[1] = (byte)(data_size >>> 8);                

                    // 이미지 데이터의 전송을 의미하는 메세지를 스트림에 쓴다.
                    m_sock_out_stream.write(NM_SEND_CAPTURE_IMAGE);
                    // 이미지 데이터의 크기를 스트림에 쓴다.
                    m_sock_out_stream.write(body_size);
                    // 이미지 데이터를 스트림에 쓴다.
                    m_sock_out_stream.write(send_data, 0, data_size);
                    // 스트림에 쓰여진 정보를 서버로 전송한다.
                    m_sock_out_stream.flush();

                    // 남은 데이터 크기를 조정한다.
                    remain_file_size -= data_size;

                    // 현재까지 얼마나 데이터를 전송하였는지 텍스트뷰에 출력해준다.
                    m_display_string = "send_size : " + full_size + " / " + (full_size - remain_file_size);
                    m_text_view.post(m_display_run);
                }

                // 전송이 완료되었음을 서버에 알린다.
                body_size[0] = 0;
                body_size[1] = 0;
                m_sock_out_stream.write(NM_FINISH_IMAGE_DATA);
                m_sock_out_stream.write(body_size);
                m_sock_out_stream.flush();   

                // 전송이 완료되었음을 텍스트뷰에 출력해준다.
                m_display_string = "Success send Picture!!";
                m_text_view.post(m_display_run);
            } catch (IOException ie) {
                // 예외가 발생하면 텍스트뷰에 예외사항을 출력한다.
                m_display_string = ie.toString();
                m_text_view.post(m_display_run);
            } finally {
                // 파일 입력 스트림이 null 이 아니면 스트림을 닫는다.
                if(input_stream != null) {
                    try {
                        input_stream.close();
                    } catch(IOException e) {
                        m_display_string = e.toString();
                        m_text_view.post(m_display_run);
                    }
                }
            }
        } 
    }

    protected void onStop()
    {
        // 소켓의 연결을 종료한다.
        onDisconnectServer();
        // 상위 클래스의 onStop 메소드를 수행시킨다.
        super.onStop();
    }
}
