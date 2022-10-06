package com.TCP_UDP.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            //서버 소켓 생성
            ServerSocket serverSocket = new ServerSocket(6789); //포트 번호 지정
            System.out.println("[====서버 6789포트에서 시작====]");

            while (true) {
                Socket s = serverSocket.accept();

                //수신 코드
                InputStream is = s.getInputStream();
                Scanner sc = new Scanner(is); //Scanner: 2Byte 처리
                System.out.println("[클라이언트 메세지 : " + sc.nextLine() + "]");

                //클라이언트로 응답 전송
                OutputStream os = s.getOutputStream();
                PrintStream ps = new PrintStream(os);
                ps.print("클라이언트 어서오고~\n"); //보낼 메세지
                ps.flush(); //버퍼의 데이터를 한번에 보냄

                //클라이언트 간 연결만 해제, 서버는 살아있음
                s.close();
                System.out.println("[===서버-클라이언트 연결 해제===]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
