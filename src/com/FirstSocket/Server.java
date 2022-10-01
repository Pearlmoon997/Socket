package com.FirstSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket serverSocket = null;
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            serverSocket = new ServerSocket(1234);
        }catch (IOException e) {
            System.out.println("해당 포트가 열려있습니다,");
        } try {
            System.out.println("서버 오픈");
            socket = serverSocket.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            String str = null;
            str = br.readLine();
            System.out.println("Client의 메세지 : " + str);
            pw.write(str);
            pw.flush();
            socket.close();
        } catch (IOException e) {

        }
    }
}
