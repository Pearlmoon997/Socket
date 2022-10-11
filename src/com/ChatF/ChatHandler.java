package com.ChatF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

//소켓 처리달
//서버의 중요 기능을 담당
//클라이언트로부터 정보를 전달받아 사용자의 서버에 메세지 내용을 뿌려주는 서버
public class ChatHandler extends Thread{
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Socket socket;
    private List<ChatHandler> list;

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

                if (dto.getCommand()==Info.EXIT) {
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.EXIT);
                    writer.writeObject(sendDto);
                    writer.flush();

                    reader.close();
                    writer.close();
                    socket.close();

                    list.remove(this);

                    sendDto.setCommand(Info.SEND);
                    sendDto.setMessage(nickName+"님 퇴장하였습니다");
                    broadcast(sendDto);
                    break;
                } else if (dto.getCommand()==Info.JOIN) {
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.SEND);
                    sendDto.setMessage(nickName+"님 입장하였습니다");
                    broadcast(sendDto);
                } else if (dto.getCommand()==Info.SEND) {
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

    public void broadcast(InfoDTO sendDto) throws IOException {
        for (ChatHandler handler : list) {
            handler.writer.writeObject(sendDto);
            handler.writer.flush();
        }
    }
}
