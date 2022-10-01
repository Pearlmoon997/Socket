package com.FirstSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        Socket socket = null;       //Client 와 통신하기 위한 소켓
        ServerSocket serverSocket = null;       //서버 생성을 위한 서버소켓
        BufferedReader br = null;       //Client 로 부터 데이터를 읽어오기 위한 입력 스트림
        PrintWriter pw = null;      //Client 로 데이터를 보내기 위한 출력 스트림

        try {
            serverSocket = new ServerSocket(1234);
        }catch (IOException e) {
            System.out.println("해당 포트가 열려있습니다,");
        } try {
            System.out.println("서버 오픈");
            //서버를 생성, Client 접속 대기
            socket = serverSocket.accept();

            //입력 스트림 생성
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //출력 스트림 생성
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            String str = null;
            str = br.readLine();        //Client 로 부터 데이터 읽어옴
            System.out.println("Client의 메세지 : " + str);
            pw.write(str);
            pw.flush();     //버퍼링으로 인해 기록되지 않은 데이터를 출력스트림에 모두 출력
            socket.close();     //출력되지 않은 스트림 모두 출력하고 소켓 닫음
        } catch (IOException e) {

        }
    }
}
