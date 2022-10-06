package com.TCP_UDP.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {
    public void start() throws IOException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress serverAddr = InetAddress.getByName("127.0.0.1");

        //데이터가 저장될 공간으로 임의로 지정한 크기의 byte 배열을 생성
        byte[] message = new byte[200];

        DatagramPacket outPacket = new DatagramPacket(message, 1, serverAddr, 6789);
        DatagramPacket inPacket = new DatagramPacket(message, message.length);

        //데이터그램패킷 송신
        datagramSocket.send(outPacket);
        //데이터그램패킷 수신
        datagramSocket.receive(inPacket);

        System.out.println("현재 시간 : " + new String(inPacket.getData()));

        datagramSocket.close();
    }

    public static void main(String[] args) {
        try {
            new Client().start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
