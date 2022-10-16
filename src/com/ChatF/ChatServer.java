package com.ChatF;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//서버와 포트번호만을 준비하는 클래스
//클라이언트가 접속하게되면 그 클라이언트의 정보를 ChatHandler 클래스에 전달
public class ChatServer {
    private ServerSocket serverSocket; //서버 소켓 생성
    private List<ChatHandler> list;
    public ChatServer() {
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("서버 실행");
            list = new ArrayList<ChatHandler>();
            while (true) {
                Socket socket = serverSocket.accept();
                ChatHandler handler = new ChatHandler(socket, list);
                //스레드 시작
                handler.start();
                // 핸들러를 담음 (클라이언트의 갯수)
                list.add(handler);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
