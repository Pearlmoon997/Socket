package com.FirstSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader br = null;
        BufferedReader br2 = null;
        PrintWriter pw = null;
        InetAddress ia = null;

        try {
            ia = InetAddress.getLocalHost();
            socket = new Socket(ia, 1234);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            br2 = new BufferedReader(new InputStreamReader(System.in));

            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            System.out.println(socket.toString());

        } catch (IOException e) {

        } try {
            System.out.print("서버로 보낼 메세지 : ");
            String data = br2.readLine();
            pw.println(data);
            pw.flush();

            String str2 = br.readLine();
            System.out.print("서버의 응답 : " + str2);
            socket.close();
        } catch (IOException e) {

        }
    }
}
