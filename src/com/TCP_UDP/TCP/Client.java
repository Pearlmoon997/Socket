package com.TCP_UDP.TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception{
        //서버 접속
        Socket s = new Socket("localhost", 6789);
        System.out.println("[===클라이언트-서버 접속===]");

        //송신 코드
        OutputStream os = s.getOutputStream();
        PrintStream ps = new PrintStream(os);
        ps.print("서버 어서오고~\n");
        ps.flush();

        //수신 코드
        InputStream is = s.getInputStream();
        Scanner sc = new Scanner(is);
        System.out.println("[서버의 메세지 : " + sc.nextLine() + "]");

        //서버와 연결 해제
        s.close();
        System.out.println("[===서버-클라이언트 접속 해제===]");
    }
}
