package com.FirstSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;       //Server 와 통신하기 위한 소켓
        BufferedReader br = null;       //Server 로 부터 데이터를 읽어들이기 위한 입력 스트림
        BufferedReader br2 = null;      //키보드로부터 읽어들이기 위한 입력 스트림
        PrintWriter pw = null;      //Server 로 내보내기 위한 출력 스트림
        InetAddress ia = null;

        try {
            ia = InetAddress.getLocalHost();        //Server 로 접속하기 위한 로컬호스트 주소 가져옴
            socket = new Socket(ia, 1234);      //서버의 포트 번호
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            br2 = new BufferedReader(new InputStreamReader(System.in));

            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            System.out.println(socket.toString());

        } catch (IOException e) {

        } try {
            System.out.print("서버로 보낼 메세지 : ");
            String data = br2.readLine();       //키보드로부터 입력
            pw.println(data);       //서버로 데이터 전송
            pw.flush();

            String str2 = br.readLine();        //서버로부터 되돌아오는 데이터 읽어옴
            System.out.print("서버의 응답 : " + str2);
            socket.close();
        } catch (IOException e) {

        }
    }
}
