package com.ChatF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

//소켓 처리단
//서버의 중요 기능을 담당
//클라이언트로부터 정보를 전달받아 사용자의 서버에 메세지 내용을 뿌려주는 서버
//스레드의 역할
public class ChatHandler extends Thread{
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Socket socket;
    private List<ChatHandler> list; //멀티 스레드

    public ChatHandler(Socket socket, List<ChatHandler> list) throws IOException {
        this.socket = socket;
        this.list = list;
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());
    }

    public void run() {
        InfoDTO dto = null;
        String nickName;
        try {
            while (true) {
                dto = (InfoDTO)reader.readObject();
                nickName = dto.getNickName();

                //사용자가 접속을 종료했을 때 퇴장하였다는 메세지
                if (dto.getCommand()==Info.EXIT) {
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.EXIT);
                    writer.writeObject(sendDto);
                    writer.flush();

                    reader.close();
                    writer.close();
                    socket.close();

                    list.remove(this);  //퇴장한 유저를 삭제함

                    sendDto.setCommand(Info.SEND);  //퇴장 메세지 전달
                    sendDto.setMessage(nickName+"님 퇴장하였습니다");
                    broadcast(sendDto);
                    break;
                } else if (dto.getCommand()==Info.JOIN) {
                    //사용자가 접속하면 입장 메세지 전달
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.SEND);
                    sendDto.setMessage(nickName+"님 입장하였습니다");
                    broadcast(sendDto);
                } else if (dto.getCommand()==Info.SEND) {
                    //사용자가 메세지를 입력하면 전달하고 출력
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.SEND);
                    sendDto.setMessage("["+nickName+"]"+dto.getMessage());
                    broadcast(sendDto);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    //다른 클라이언트에게 전체 메세지 전달
    public void broadcast(InfoDTO sendDto) throws IOException {
        for (ChatHandler handler : list) {
            //핸들러의 writer 값 보냄
            handler.writer.writeObject(sendDto);
            //핸들러 안의 writer 값 비워줌 -> 모두 보냄
            handler.writer.flush();
        }
    }
}
