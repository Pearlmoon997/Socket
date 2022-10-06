package com.TCP_UDP.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    public void start() throws IOException{
        // 6789번을 사용하는 소켓 생성
        DatagramSocket socket = new DatagramSocket(6789);
        DatagramPacket inPacket, outPacket;

        byte[] inMessage = new byte[10];
        byte[] outMessage;

        while(true) {
            //데이터 수신을 위한 패킷 생성
            inPacket = new DatagramPacket(inMessage, inMessage.length);
            socket.receive(inPacket); //패키을 통해 데이터 수신

            //수신한 패킷에서 클라이언트의 IP 주소와 Port 를 얻음
            InetAddress address = inPacket.getAddress();
            int port = inPacket.getPort();

            //서버의 현재 시간 포맷을 설정 및 반환
            SimpleDateFormat simple = new SimpleDateFormat("[hh:mm:ss]");
            String time = simple.format(new Date());
            outMessage = time.getBytes();

            //패킷 생성 후 클라이언트에게 송신
            outPacket = new DatagramPacket(outMessage, outMessage.length, address, port);
            socket.send(outPacket);
        }
    }

    public static void main(String[] args) {
        try {
            //UDP 서버 시작
            new Server().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
